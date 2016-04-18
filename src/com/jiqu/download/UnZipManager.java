package com.jiqu.download;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.progress.ProgressMonitor;

import android.os.Handler;
import android.util.Log;

import com.jiqu.database.DownloadAppinfo;

public class UnZipManager {
	private static final String UNZIP_THREADPOOL_NAME = "SINGLE_UNZIP_THREADPOOL_NAME";
	public static final int UNZIP_SUCCESS = 0;
	public static final int UNZIP_FAILE = 1;

	private static UnZipManager instance;
	private Handler handler;
	int percent = 0;
	
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
	
	private class UnZipRunnable implements Runnable{
		private DownloadAppinfo downloadAppinfo;
		private String password;
		private boolean unziping = true;
		
		public UnZipRunnable(DownloadAppinfo downloadAppinfo,String password){
			this.downloadAppinfo = downloadAppinfo;
			this.password = password;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				ZipFile zFile = new ZipFile(downloadAppinfo.getZipPath());
				zFile.setFileNameCharset("GBK");
				  
		        if (!zFile.isValidZipFile()){
		        	handler.sendEmptyMessage(UNZIP_FAILE);
		            return;
		        }  
		        File destDir = new File(downloadAppinfo.getUnzipPath());
		        if (destDir.isDirectory() && !destDir.exists()){
		            destDir.mkdir();  
		        }  
		        if (zFile.isEncrypted()){
		            zFile.setPassword(password); // 设置解压密码  
		        }
		        final ProgressMonitor monitor = zFile.getProgressMonitor();
		        
		        new Thread(){
		        	public void run() {
		        		try {
							 while (true) {
								 sleep(1000);
						        	percent = monitor.getPercentDone();
						        	Log.i("TAG", "解压进度 ： " + percent);
						        	if (percent >= 100) {
						        		map.remove(downloadAppinfo.getPackageName());
						        		reName(downloadAppinfo.getUnzipPath() + "/.apk/" 
						        		+ downloadAppinfo.getPackageName() + ".txt",  
						        		downloadAppinfo.getPackageName() + ".apk");
						        		handler.sendEmptyMessage(UNZIP_SUCCESS);
						        		break;
						        	}
								}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
		        	};
		        }.start();
//		       
//		        zFile.setRunInThread(true); //true 在子线程中进行解压 , false主线程中解压  
//		        zFile.extractAll(downloadAppinfo.getUnzipPath());
		        
		        List fileHeaderList = zFile.getFileHeaders();
		        for(int i = 0 ; i<fileHeaderList.size() ; i++){
		        	Log.i("TAG", ((FileHeader)fileHeaderList.get(i)).getFileName());
		        	String name = ((FileHeader)fileHeaderList.get(i)).getFileName();
		        	if (name.endsWith(".txt") || name.endsWith(".obb")) {
						zFile.extractFile(name, downloadAppinfo.getUnzipPath());
					}
		        }
			} catch (ZipException e) {
				map.remove(downloadAppinfo.getPackageName());
				unziping = false;
				handler.sendEmptyMessage(UNZIP_FAILE);
				e.printStackTrace();
			}  
	        
		}
		
	}
	
	private void reName(String path,String newName){
		File file = new File(path);
		if (file.exists()) {
			file.renameTo(new File(file.getParent() + File.separator + "com.beenoculus.timecoaster.apk"));
		}else {
			Log.i("UnZipManager", "file not exists !");
		}
	}
}
