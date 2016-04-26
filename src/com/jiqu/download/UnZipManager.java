package com.jiqu.download;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.progress.ProgressMonitor;

import android.os.Handler;
import android.util.Log;

import com.jiqu.database.DownloadAppinfo;

public class UnZipManager {
	private static final String UNZIP_THREADPOOL_NAME = "SINGLE_UNZIP_THREADPOOL_NAME";
	public static final int UNZIP_SUCCESS = 0;
	public static final int UNZIP_FAILE = 1;

	private static UnZipManager instance;
	int percent = 0;
	private Handler handler;
	
	private Map<String, UnZipRunnable> map = new ConcurrentHashMap<String, UnZipManager.UnZipRunnable>();
	
	
	public static synchronized UnZipManager getInstance(){
		if (instance == null) {
			instance = new UnZipManager();
		}
		return instance;
	}
	
	public void unzip(DownloadAppinfo downloadAppinfo ,String password,Handler handler){
		this.handler = handler;
		UnZipRunnable runnable = map.get(downloadAppinfo.getPackageName());
		if (runnable == null) {
			runnable = new UnZipRunnable(downloadAppinfo, password);
			map.put(downloadAppinfo.getPackageName(), runnable);
		}
		ThreadManager.getSinglePool(UNZIP_THREADPOOL_NAME).execute(runnable);
	}
	
	public boolean isUnZiping(String packageName){
		return map.get(packageName) == null?false:true;
	}
	
	private class UnZipRunnable implements Runnable{
		private DownloadAppinfo downloadAppinfo;
		private String password;
		private boolean unziping = true;
		ProgressMonitor monitor;
		
		public UnZipRunnable(DownloadAppinfo downloadAppinfo,String password){
			this.downloadAppinfo = downloadAppinfo;
			this.password = password;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				File file = new File(downloadAppinfo.getZipPath());
				if (file.exists()) {
					String path = file.getParent() + File.separator + downloadAppinfo.getAppName();
					File folder = new File(path);
					if (folder.exists()) {
						try {
							deleteFolder(folder);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				ZipFile zFile = new ZipFile(downloadAppinfo.getZipPath());
				zFile.setFileNameCharset("GBK");
				  
		        if (!zFile.isValidZipFile()){
		        	if (handler != null) {
		        		downloadAppinfo.setDownloadState(DownloadManager.STATE_UNZIP_FAILED);
						handler.sendEmptyMessage(1);
					}
		            return;
		        }  
		        File destDir = new File(downloadAppinfo.getUnzipPath());
		        if (destDir.isDirectory() && !destDir.exists()){
		            destDir.mkdir();  
		        }  
		        if (zFile.isEncrypted()){
		            zFile.setPassword(password); // 设置解压密码  
		        }
		        monitor = zFile.getProgressMonitor();
		        
		        new Thread(){
		        	public void run() {
		        		try {
							 while (unziping) {
								 sleep(1000);
						        	percent = monitor.getPercentDone();
						        	Log.i("TAG", "解压进度 ： " + percent);
						        	if (percent >= 100) {
						        		unziping = false;
						        		map.remove(downloadAppinfo.getPackageName());
						        		reName(downloadAppinfo.getUnzipPath() + "/.apk/",  
						        		downloadAppinfo.getPackageName() + ".apk");
						        		DownloadManager.getInstance().install(downloadAppinfo);
						        		downloadAppinfo.setDownloadState(DownloadManager.STATE_UNZIPED);
						        		DownloadManager.DBManager.insertOrReplace(downloadAppinfo);
						        		handler.sendEmptyMessage(1);
						        		break;
						        	}
								}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
		        	};
		        }.start();

		        zFile.setRunInThread(false); //true 在子线程中进行解压 , false主线程中解压  
		        zFile.extractAll(downloadAppinfo.getUnzipPath());
		        
			} catch (ZipException e) {
				map.remove(downloadAppinfo.getPackageName());
				unziping = false;
				downloadAppinfo.setDownloadState(DownloadManager.STATE_UNZIP_FAILED);
        		DownloadManager.DBManager.insertOrReplace(downloadAppinfo);
        		handler.sendEmptyMessage(1);
				e.printStackTrace();
			}
		}
	}
	
	private void reName(String path,String newName){
		File file = new File(path);
		if (file.exists() && file.isDirectory()) {
			File[] files = file.listFiles();
			if (file != null) {
				for(int i = 0; i<files.length;i++){
					Log.i("TAG", files[i].getAbsolutePath());
				}
				if (files.length == 1) {
					files[0].renameTo(new File(files[0].getParent() + File.separator + newName));
				}else {
					try {
						throw new Exception("file number is not one !");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}else {
			try {
				throw new Exception("file not exists or file is not directory !");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	private void deleteFolder(File file) throws Exception{
		java.io.File[] fileList = file.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isDirectory()) {
				deleteFolder(fileList[i]);
			} else {
				fileList[i].delete();
			}
		}
	}
}
