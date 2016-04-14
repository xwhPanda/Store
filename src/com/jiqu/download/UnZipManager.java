package com.jiqu.download;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.progress.ProgressMonitor;

import android.util.Log;

import com.jiqu.database.DownloadAppinfo;

public class UnZipManager {
	private static final String UNZIP_THREADPOOL_NAME = "SINGLE_UNZIP_THREADPOOL_NAME";

	private static UnZipManager instance;
	
	private Map<String, UnZipRunnable> map = new ConcurrentHashMap<String, UnZipManager.UnZipRunnable>();
	
	
	public static synchronized UnZipManager getInstance(){
		if (instance == null) {
			instance = new UnZipManager();
		}
		return instance;
	}
	
	public void unzip(DownloadAppinfo downloadAppinfo ,String password){
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
		            throw new ZipException("exception!");  
		        }  
		        File destDir = new File(downloadAppinfo.getUnzipPath());
		        if (destDir.isDirectory() && !destDir.exists()){
		            destDir.mkdir();  
		        }  
		        if (zFile.isEncrypted()){
		            zFile.setPassword(password); // 设置解压密码  
		        }  
		        zFile.setRunInThread(true); //true 在子线程中进行解压 , false主线程中解压  
		        zFile.extractAll(downloadAppinfo.getUnzipPath());
		        ProgressMonitor monitor = zFile.getProgressMonitor();
		        while (unziping) {
//		        	Log.i("TAG", "解压进度 ： " + monitor.getPercentDone());
		        	if (monitor.getPercentDone() >= 100) {
		        		map.remove(downloadAppinfo.getPackageName());
		        		reName(downloadAppinfo.getUnzipPath() + "/.apk/" 
		        		+ downloadAppinfo.getPackageName() + ".txt",  
		        		downloadAppinfo.getPackageName() + ".apk");
		        		unziping = false;
		        	}
				}
		        
			} catch (ZipException e) {
				map.remove(downloadAppinfo.getPackageName());
				unziping = false;
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
