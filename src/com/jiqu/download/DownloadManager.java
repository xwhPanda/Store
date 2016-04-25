package com.jiqu.download;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.progress.ProgressMonitor;

import com.jiqu.application.StoreApplication;
import com.jiqu.database.DaoSession;
import com.jiqu.database.DownloadAppinfo;
import com.jiqu.database.DownloadAppinfoDao.Properties;
import com.jiqu.tools.Constant;

import de.greenrobot.dao.query.QueryBuilder;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.renderscript.Light;
import android.util.Log;
import android.widget.Toast;

public class DownloadManager {
	public static final int STATE_NONE = 0;
	/** 等待中 */
	public static final int STATE_WAITING = 1;
	/** 下载中 */
	public static final int STATE_DOWNLOADING = 2;
	/** 暂停 */
	public static final int STATE_PAUSED = 3;
	/** 下载完毕 */
	public static final int STATE_DOWNLOADED = 4;
	/** 下载失败 */
	public static final int STATE_ERROR = 5;
	/** 等待解压 */
	public static final int WAITING_UNZIP = 6;
	/** 正在解压 */
	public static final int STATE_UNZIPING = 7;
	/** 解压完成 */
	public static final int STATE_UNZIPED = 8;
	/** 已安装 */
	public static final int STATE_INSTALLED = 9;
	/** 升级  */
	public static final int STATE_NEED_UPDATE = 10;
	/** 解压失败 */
	public static final int STATE_UNZIP_FAILED = 11;

	// public static final int STATE_READ = 6;

	private static DownloadManager instance;

	public static DaoSession DBManager;

	private static final int UPDATE_DB_PER_SIZE = 2 * 1024;

	private static final int BUFFER_SIZE = 4 * 1024;

	private DownloadManager() {
		DBManager = StoreApplication.daoSession;
	}

	/** 用于记录下载信息，如果是正式项目，需要持久化保存 */
	// private Map<Long, DownloadInfo> mDownloadMap = new
	// ConcurrentHashMap<Long, DownloadInfo>();
	/** 用于记录观察者，当信息发送了改变，需要通知他们 */
	private List<DownloadObserver> mObservers = new ArrayList<DownloadObserver>();
	/** 用于记录所有下载的任务，方便在取消下载时，通过id能找到该任务进行删除 */
	private Map<Long, DownloadTask> mTaskMap = new ConcurrentHashMap<Long, DownloadTask>();

	public static synchronized DownloadManager getInstance() {
		if (instance == null) {
			instance = new DownloadManager();
		}
		return instance;
	}

	public boolean isDownloading(Long id){
		return mTaskMap.get(id) == null?false:true;
	}
	
	/** 注册观察者 */
	public void registerObserver(DownloadObserver observer) {
		synchronized (mObservers) {
			if (!mObservers.contains(observer)) {
				mObservers.add(observer);
			}
		}
	}

	/** 反注册观察者 */
	public void unRegisterObserver(DownloadObserver observer) {
		synchronized (mObservers) {
			if (mObservers.contains(observer)) {
				mObservers.remove(observer);
			}
		}
	}

	/** 当下载状态发送改变的时候回调 */
	public void notifyDownloadStateChanged(DownloadAppinfo info) {
		synchronized (mObservers) {
			for (DownloadObserver observer : mObservers) {
				observer.onDownloadStateChanged(info);
			}
		}
	}

	/** 当下载进度发送改变的时候回调 */
	public void notifyDownloadProgressed(DownloadAppinfo info) {
		synchronized (mObservers) {
			for (DownloadObserver observer : mObservers) {
				observer.onDownloadProgressed(info);
			}
		}
	}

	/** 下载，需要传入一个appInfo对象 */
	public synchronized void download(DownloadAppinfo appInfo) {
		// 先判断是否有这个app的下载信息
		Log.i("TAG", appInfo.getUrl());
		// DownloadInfo info = mDownloadMap.get(appInfo.getId());
		DownloadAppinfo info = DBManager.getDownloadAppinfoDao().queryBuilder().where(Properties.Id.eq(appInfo.getId())).unique();
		if (info == null) {// 如果没有，则根据appInfo创建一个新的下载信息
			info = appInfo;
			DBManager.getDownloadAppinfoDao().insertOrReplace(info);
			// mDownloadMap.put(appInfo.getId(), info);
		} else {
			String path = "";
			if (appInfo.getIsZip()) {
				path = appInfo.getZipPath();
			} else {
				path = info.getApkPath();
			}
			File file = new File(path);
			if (file.exists()) {
				long fileSize = FileUtil.getFileSize(path);
				if (info.getCurrentSize() > fileSize) {
					file.delete();
					info.setCurrentSize(0l);
					info.setHasFinished(false);
					info.setThread1(0l);
					info.setThread2(0l);
					info.setThread3(0l);
					info.setThread4(0l);
					info.setThread5(0l);
					info.setProgress(0.0f);
					info.setDownloadState(DownloadManager.STATE_NONE);
				}else if (info.getCurrentSize() == fileSize) {
					info.setHasFinished(true);
					info.setProgress((fileSize + 0.0f) / Float.parseFloat(info.getAppSize()));
					info.setDownloadState(DownloadManager.STATE_DOWNLOADED);
				}
			} else {
				info.setCurrentSize(0l);
				info.setHasFinished(false);
				info.setThread1(0l);
				info.setThread2(0l);
				info.setThread3(0l);
				info.setThread4(0l);
				info.setThread5(0l);
				info.setProgress(0.0f);
				info.setDownloadState(DownloadManager.STATE_NONE);
			}
			setDownloadInfo(info.getId(), info);
		}
		// 判断状态是否为STATE_NONE、STATE_PAUSED、STATE_ERROR。只有这3种状态才能进行下载，其他状态不予处理
		if (info.getDownloadState() == STATE_NONE || info.getDownloadState() == STATE_PAUSED || info.getDownloadState() == STATE_ERROR) {
			// 下载之前，把状态设置为STATE_WAITING，因为此时并没有产开始下载，只是把任务放入了线程池中，当任务真正开始执行时，才会改为STATE_DOWNLOADING
			info.setDownloadState(STATE_WAITING);
			notifyDownloadStateChanged(info);// 每次状态发生改变，都需要回调该方法通知所有观察者
			DownloadTask task = new DownloadTask(info);// 创建一个下载任务，放入线程池
			mTaskMap.put(info.getId(), task);
			ThreadManager.getDownloadPool().execute(task);
		}
	}

	/** 暂停下载 */
	public synchronized void pause(DownloadAppinfo appInfo) {
		stopDownload(appInfo);
		DownloadAppinfo info = DBManager.getDownloadAppinfoDao().queryBuilder().where(Properties.Id.eq(appInfo.getId())).unique();
		if (info != null) {// 修改下载状态
			info.setDownloadState(STATE_PAUSED);
			notifyDownloadStateChanged(info);
		}
	}

	/** 退出应用时调用 */
	public synchronized void pauseExit() {
		for (Map.Entry<Long, DownloadTask> entry : mTaskMap.entrySet()) {
			DownloadAppinfo info = DBManager.getDownloadAppinfoDao().queryBuilder().where(Properties.Id.eq(entry.getKey())).unique();
			if (info != null) {
				stopDownload(info);
				info.setDownloadState(STATE_PAUSED);
				DBManager.getDownloadAppinfoDao().insertOrReplace(info);
			}
		}
	}
	
	/** 网络变化时调用 */
	public synchronized void pauseAllExit() {
		for (Map.Entry<Long, DownloadTask> entry : mTaskMap.entrySet()) {
			DownloadAppinfo info = DBManager.getDownloadAppinfoDao().queryBuilder().where(Properties.Id.eq(entry.getKey())).unique();
			if (info != null) {
				pauseDownload(info);
				info.setDownloadState(STATE_PAUSED);
				notifyDownloadStateChanged(info);
				DBManager.getDownloadAppinfoDao().insertOrReplace(info);
			}
		}
	}
	
	/** 网络变化时调用 */
	public void startAll(){
		for (Map.Entry<Long, DownloadTask> entry : mTaskMap.entrySet()) {
			DownloadTask task = mTaskMap.get(entry.getKey());
			if (task != null) {
				DownloadAppinfo info = DBManager.getDownloadAppinfoDao().queryBuilder().where(Properties.Id.eq(entry.getKey())).unique();
				if (info != null) {
					download(info);
				}
			}
		}
	}

	/** 取消下载，逻辑和暂停类似，只是需要删除已下载的文件 */
	public synchronized void cancel(DownloadAppinfo appInfo) {
		stopDownload(appInfo);
		// DownloadInfo info = mDownloadMap.get(appInfo.getId());// 找出下载信息
		DownloadAppinfo info = DBManager.getDownloadAppinfoDao().queryBuilder().where(Properties.Id.eq(appInfo.getId())).unique();
		if (info != null) {// 修改下载状态并删除文件
			info.setDownloadState(STATE_NONE);
			notifyDownloadStateChanged(info);
			info.setCurrentSize((long) 0);
			File file = null;
			if (appInfo.getIsZip()) {
				file = new File(info.getZipPath());
			} else {
				file = new File(info.getApkPath());
			}
			file.delete();
		}
	}

	/** 安装应用 */
	public synchronized void install(DownloadAppinfo appInfo) {
		stopDownload(appInfo);
		// DownloadInfo info = mDownloadMap.get(appInfo.getId());// 找出下载信息
		DownloadAppinfo info = DBManager.getDownloadAppinfoDao().queryBuilder().where(Properties.Id.eq(appInfo.getId())).unique();
		if (info != null) {// 发送安装的意图
			Intent installIntent = new Intent(Intent.ACTION_VIEW);
			installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			String path = "";
			if (info.getIsZip()) {
				path = info.getUnzipPath() + File.separator + ".apk" + File.separator + info.getPackageName() + ".apk";
			} else {
				path = info.getApkPath();
			}
			installIntent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
			AppUtil.getContext().startActivity(installIntent);
		}
		// notifyDownloadStateChanged(info);
	}

	/** 使用ADB命令进行安装 需放到线程里面执行 */
	public synchronized void installByADB(DownloadInfo appInfo) {
		Process install = null;
		try {
			// 安装apk命令
			install = Runtime.getRuntime().exec("adb install " + appInfo.getPath());
			// install =
			// Runtime.getRuntime().exec("pm install -r   +  /storage/sdcard0/cccfile/ccc.apk");

			BufferedReader reader = new BufferedReader(new InputStreamReader(install.getInputStream()));
			int read;
			char[] buffer = new char[4096];
			StringBuffer output = new StringBuffer();
			while ((read = reader.read(buffer)) > 0) {
				output.append(buffer, 0, read);
			}
			reader.close();

			install.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (install != null) {
				install.destroy();

			}

		}
	}

	public synchronized void installByPM(DownloadInfo appInfo) {
		String result = execCommand("pm", "install", "-f", appInfo.getPath());
		Log.i("installByPM", result + " ： result");
	}

	public static String execCommand(String... command) {
		Process process = null;
		InputStream errIs = null;
		InputStream inIs = null;
		String result = "a";

		try {
			process = new ProcessBuilder().command(command).start();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int read = -1;
			errIs = process.getErrorStream();
			while ((read = errIs.read()) != -1) {
				baos.write(read);
			}
			inIs = process.getInputStream();
			while ((read = inIs.read()) != -1) {
				baos.write(read);
			}
			result = new String(baos.toByteArray());
			if (inIs != null)
				inIs.close();
			if (errIs != null)
				errIs.close();
			process.destroy();
		} catch (IOException e) {
			result = e.getMessage();
		}
		return result;
	}

	/** 启动应用，启动应用是最后一个 */
	public synchronized void open(String pkg) {
		try {
			Context context = AppUtil.getContext();
			// 获取启动Intent
			Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkg);
			context.startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(StoreApplication.context, "应用未安装", Toast.LENGTH_SHORT).show();
		}
	}

	/** 如果该下载任务还处于线程池中，且没有执行，先从线程池中移除 */
	private void stopDownload(DownloadAppinfo appInfo) {
		DownloadTask task = mTaskMap.remove(appInfo.getId());// 先从集合中找出下载任务
		if (task != null) {
			task.setPause();
			ThreadManager.getDownloadPool().cancel(task);// 然后从线程池中移除
		}
	}
	
	/** 如果该下载任务还处于线程池中，且没有执行，先从线程池中移除 */
	private void pauseDownload(DownloadAppinfo appInfo) {
		DownloadTask task = mTaskMap.get(appInfo.getId());// 先从集合中找出下载任务
		if (task != null) {
			task.setPause();
			ThreadManager.getDownloadPool().cancel(task);// 然后从线程池中移除
		}
	}
	
	
	/** 获取下载信息 */
	public synchronized DownloadAppinfo getDownloadInfo(long id) {
		// return mDownloadMap.get(id);
		QueryBuilder qb = DBManager.getDownloadAppinfoDao().queryBuilder();
		DownloadAppinfo appinfo = (DownloadAppinfo) qb.where(Properties.Id.eq(id)).unique();
		return appinfo;
	}

	public synchronized void setDownloadInfo(long id, DownloadAppinfo info) {
		// mDownloadMap.put(id, info);
		DBManager.getDownloadAppinfoDao().insertOrReplace(info);
	}

	/** 下载任务 */
	public class DownloadTask implements Runnable {
		private DownloadAppinfo info;
		private DownloadThread[] threads = new DownloadThread[5];
		private int code;

		public DownloadTask(DownloadAppinfo info) {
			this.info = info;
		}
		
		public void setPause(){
			info.setDownloadState(STATE_PAUSED);
		}
		
		Handler handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				if (msg.what == 1) {
					notifyDownloadStateChanged(info);
				}
			};
		};

		@Override
		public void run() {
			info.setDownloadState(STATE_DOWNLOADING);// 先改变下载状态
			notifyDownloadStateChanged(info);
			String path = "";
			if (info.getIsZip()) {
				path = info.getZipPath();
			} else {
				path = info.getApkPath();
			}
			File file = new File(path);
			InputStream stream = null;
//			if (info.getCurrentSize() == 0 || !file.exists()) {
//				// 如果文件不存在，或者进度为0，或者进度和文件长度不相符，就需要重新下载
//
//				info.setCurrentSize((long) 0);
//				file.delete();
//				
//			}
			if (file.length() > Long.parseLong(info.getAppSize())) {
				info.setCurrentSize(0l);
				file.delete();
			}
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
			
			HttpURLConnection connection = null;
			long length = 0;
			long complete = 0;
			try {
				long apkSize = Long.parseLong(info.getAppSize());
				if (apkSize <= 0l) {
					URL url = new URL(info.getUrl());
					connection = (HttpURLConnection) url.openConnection();
					connection.setConnectTimeout(30000);
					connection.setReadTimeout(1000 * 60 * 2);
					connection.setRequestProperty("Accept-Encoding", "identity");
					connection.setRequestMethod("GET");
					
					int stateCode = connection.getResponseCode();
					if (stateCode == 200) {
						apkSize = connection.getContentLength();
						RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rwd");
						randomAccessFile.setLength(apkSize);
						randomAccessFile.close();
						connection.disconnect();
					}else {
						return;
					}
				}else {
					int threadCount = threads.length;
					long sectionSize = apkSize / threadCount;
					for (int i = 0; i < threadCount - 1; i++) {
						threads[i] = new DownloadThread(i, i * sectionSize, getCompelete(i), (i + 1) * sectionSize -1);
						threads[i].start();
					}
					threads[threadCount -1] = new DownloadThread(threadCount - 1, (threadCount - 1) * sectionSize, getCompelete(threadCount - 1), apkSize - 1);
					threads[threadCount -1].start();
				}
				
				
//				RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rwd");
//				randomAccessFile.seek(file.length());
//				complete = file.length();
//				int count = -1;
//				byte[] buffer = new byte[4 * 1024];
//				while (((count = stream.read(buffer)) != -1) && info.getDownloadState() == STATE_DOWNLOADING) {
//					// 每次读取到数据后，都需要判断是否为下载状态，如果不是，下载需要终止，如果是，则刷新进度
//					randomAccessFile.write(buffer, 0, count);
//					complete += count;
//					info.setCurrentSize(complete);
//					if (complete - length > UPDATE_DB_PER_SIZE) {
//						// 写入数据库
//						length = complete;
//						info.setProgress((info.getCurrentSize() + 0.0f) / Float.parseFloat(info.getAppSize()));
//						DBManager.getDownloadAppinfoDao().insertOrReplace(info);
//					}
//
//					notifyDownloadProgressed(info);// 刷新进度
//				}
//				Log.i("TAG", info.getCurrentSize() + "/" + info.getAppSize() + "/" + complete);
			} catch (Exception e) {
				e.printStackTrace();
//				info.setDownloadState(STATE_ERROR);
//				notifyDownloadStateChanged(info);
			} finally {
				try {
					if (stream != null) {
						stream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (connection != null) {
					connection.disconnect();
				}
			}
			
			new Thread(){
				public void run() {
					while (info.getDownloadState() == STATE_DOWNLOADING) {
						synchronized (info) {
							try {
								sleep(100);
//							if (code == 416) {
//								info.setDownloadState(DownloadManager.STATE_ERROR);
//								if (info != null) {
//									DBManager.getDownloadAppinfoDao().delete(info);
//									String path;
//									if (info.getIsZip()) {
//										path = info.getZipPath();
//									}else {
//										path = info.getApkPath();
//									}
//									File file = new File(path);
//									if (file.exists()) {
//										file.delete();
//									}
//								}
//							}
								if (info.getCurrentSize() >= Long.parseLong(info.getAppSize())) {
									info.setProgress(1.0f);
									info.setHasFinished(true);
//									notifyDownloadProgressed(info);
									mTaskMap.remove(info.getId());
									if (!info.getIsZip()) {
										info.setDownloadState(STATE_DOWNLOADED);
										install(info);
									}else {
										info.setDownloadState(STATE_UNZIPING);
										UnZipManager.getInstance().unzip(info, Constant.PASSWORD,handler);
									}
									DBManager.getDownloadAppinfoDao().insertOrReplace(info);
									notifyDownloadStateChanged(info);
									return;
								}
								info.setCurrentSize(info.getThread1() + info.getThread2() + info.getThread3() + info.getThread4() + info.getThread5());
								info.setProgress((info.getCurrentSize() + 0.0f) / Float.parseFloat(info.getAppSize()));
								DBManager.getDownloadAppinfoDao().insertOrReplace(info);
								notifyDownloadProgressed(info);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				};
			}.start();

//			if (info.getCurrentSize() == Long.parseLong(info.getAppSize())) {
//				info.setDownloadState(STATE_DOWNLOADED);
//				info.setProgress(1.0f);
//				DBManager.getDownloadAppinfoDao().insertOrReplace(info);
//				notifyDownloadStateChanged(info);
//			} else if (info.getDownloadState() == STATE_PAUSED) {// 判断状态
//				DBManager.getDownloadAppinfoDao().insertOrReplace(info);
//				notifyDownloadStateChanged(info);
//			} else {
//				info.setDownloadState(STATE_ERROR);
//				// DBManager.getDownloadAppinfoDao().delete(info);
//				notifyDownloadStateChanged(info);
//				// info.setCurrentSize((long) 0);// 错误状态需要删除文件
//				// file.delete();
//			}
//
//			mTaskMap.remove(info.getId());
		}
		
		private long getCompelete(int i){
			long size = 0;
			switch (i) {
			case 0:
				size = info.getThread1();
				break;
			case 1:
				size = info.getThread2();
				break;
			case 2:
				size = info.getThread3();
				break;
			case 3:
				size = info.getThread4();
				break;
			case 4:
				size = info.getThread5();
				break;
			}
			
			return size;
		}
		
		
		private class DownloadThread extends Thread{
			private int threadId;
			private long startPos;
			private long endPos;
			private long compeleteSize;
			
			public DownloadThread(int threadId,long startPos,long compeleteSize,long endPos){
				this.threadId = threadId;
				this.startPos = startPos;
				this.endPos = endPos;
				this.compeleteSize = compeleteSize;
			}
			
			private synchronized void setComplete(){
				if (threadId == 0) {
					info.setThread1(compeleteSize);
				}else if (threadId == 1) {
					info.setThread2(compeleteSize);
				}else if (threadId == 2) {
					info.setThread3(compeleteSize);
				}else if (threadId == 3) {
					info.setThread4(compeleteSize);
				}else if (threadId == 4) {
					info.setThread5(compeleteSize);
				}
			}
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String path = "";
				if (info.getIsZip()) {
					path = info.getZipPath();
				}else {
					path = info.getApkPath();
				}
				HttpURLConnection connection = null;
				InputStream is = null;
				byte[] bf = new byte[8 * 1024];
				RandomAccessFile randomAccessFile = null;
				try {
					randomAccessFile = new RandomAccessFile(path, "rwd");
					randomAccessFile.seek(startPos + compeleteSize);
					connection = (HttpURLConnection) new URL(info.getUrl()).openConnection();
					connection.setConnectTimeout(60 * 1000);
					connection.setReadTimeout(60 * 1000);
					connection.setRequestMethod("GET");
					connection.setRequestProperty("Accept-Encoding", "identity");
					connection.setRequestProperty("Range", "bytes=" + (compeleteSize + startPos) + "-" + endPos);
					Log.i("TAG", startPos + "/" + (compeleteSize + startPos) + "/" + endPos + "/" + info.getAppSize());
					Log.i("TAG", "code : " + connection.getResponseCode());
					code = connection.getResponseCode();
					is = connection.getInputStream();
					int length = 0;
					while ((length = is.read(bf)) != -1 && info.getDownloadState() == STATE_DOWNLOADING) {
						randomAccessFile.write(bf, 0, length);
						compeleteSize += length;
						synchronized (this) {
							setComplete();
//							DBManager.getDownloadAppinfoDao().insertOrReplace(info);
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					info.setDownloadState(STATE_ERROR);
					mTaskMap.remove(info.getId());
				} finally{
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (connection != null) {
						connection.disconnect();
					}
					try {
						if (randomAccessFile != null) {
							randomAccessFile.close();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					synchronized (this) {
						setComplete();
						DBManager.getDownloadAppinfoDao().insertOrReplace(info);
					}
				}
				
			}
		}
	}
	
	public interface DownloadObserver {

		public void onDownloadStateChanged(DownloadAppinfo info);

		public void onDownloadProgressed(DownloadAppinfo info);
	}

	/* 重写了Inpustream 中的skip(long n) 方法，将数据流中起始的n 个字节跳过 */
	private long skipBytesFromStream(InputStream inputStream, long n) {
		long remaining = n;
		// SKIP_BUFFER_SIZE is used to determine the size of skipBuffer
		int SKIP_BUFFER_SIZE = 10000;
		// skipBuffer is initialized in skip(long), if needed.
		byte[] skipBuffer = null;
		int nr = 0;
		if (skipBuffer == null) {
			skipBuffer = new byte[SKIP_BUFFER_SIZE];
		}
		byte[] localSkipBuffer = skipBuffer;
		if (n <= 0) {
			return 0;
		}
		while (remaining > 0) {
			try {
				long skip = inputStream.skip(10000);
				nr = inputStream.read(localSkipBuffer, 0, (int) Math.min(SKIP_BUFFER_SIZE, remaining));
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (nr < 0) {
				break;
			}
			remaining -= nr;
		}
		return n - remaining;
	}
}
