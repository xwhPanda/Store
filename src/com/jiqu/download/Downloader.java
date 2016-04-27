package com.jiqu.download;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jiqu.database.DownloadAppinfo;
import com.jiqu.tools.Constant;

public class Downloader implements Runnable {
	public static final int REFRESH_STATE_CHANGED = 1;
	public static final int REFRESH_PROGRESS_CHANGED = 2;
	private DownloadAppinfo info;
	private DownloadThread[] threads = new DownloadThread[5];
	private ChangeObserver observer;
	private boolean downloading = true;

	public Downloader(DownloadAppinfo info) {
		this.info = info;
	}

	public void registerObserver(ChangeObserver observer) {
		this.observer = observer;
	}

	public void setPause() {
		info.setDownloadState(DownloadManager.STATE_PAUSED);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		/** 先改变下载状态 **/
		info.setDownloadState(DownloadManager.STATE_DOWNLOADING);
		observer.onStateChanged(info);
		String path = "";
		/** 根据是zip包或apk选择下载文件存放路径 **/
		if (info.getIsZip()) {
			path = info.getZipPath();
		} else {
			path = info.getApkPath();
		}
		File file = new File(path);
		long apkSize = Long.parseLong(info.getAppSize());
		/** 如果本地文件大小大于数据库所记载的，则删除 **/
		if (file.length() > apkSize) {
			info.setCurrentSize(0l);
			file.delete();
		}
		/** 创建文件 **/
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				downloading = false;
			}
		}

		HttpURLConnection connection = null;
		InputStream in = null;
		try {
			if (apkSize <= 0l) {
				URL url = new URL(info.getUrl());
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(30 * 1000);
				connection.setReadTimeout(2 * 60 * 1000);
				connection.setRequestProperty("Accept-Encoding", "identity");
				connection.setRequestMethod("GET");
				int stateCode = connection.getResponseCode();
				if (stateCode == 200) {
					apkSize = connection.getContentLength();
					info.setAppSize(String.valueOf(apkSize));
					RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rwd");
					randomAccessFile.setLength(apkSize);
					randomAccessFile.close();
					in = connection.getInputStream();
					connection.disconnect();
				} else {
					/** 下载失败 **/
					info.setDownloadState(DownloadManager.STATE_ERROR);
					observer.onStateChanged(info);
					downloading = false;
					return;
				}
			} else {
				int threadCount = threads.length;
				long specSize = apkSize / threadCount;
				for (int i = 0; i < threadCount - 1; i++) {
					threads[i] = new DownloadThread(i, i * specSize, getCompelete(i), (i + 1) * specSize - 1);
					threads[i].start();
				}
				threads[threadCount - 1] = new DownloadThread(threadCount - 1, (threadCount - 1) * specSize, 
						getCompelete(threadCount - 1), apkSize - 1);
				threads[threadCount - 1].start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			downloading = false;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				connection.disconnect();
			}
		}
		
		new Thread(){
			public void run() {
				while (info.getDownloadState() == DownloadManager.STATE_DOWNLOADING) {
					try {
						sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				synchronized (info) {
					if (info.getCurrentSize() >= Long.parseLong(info.getAppSize())) {
						info.setProgress(1.0f);
						info.setHasFinished(true);
						if (!info.getIsZip()) {
							info.setDownloadState(DownloadManager.STATE_DOWNLOADED);
							observer.onRemoveFromTask(info);
							downloading = false;
							DownloadManager.getInstance().install(info);
						} else {
							info.setDownloadState(DownloadManager.STATE_UNZIPING);
							UnZipManager.getInstance().unzip(info, Constant.PASSWORD, unzipHandler);
						}
						DownloadManager.DBManager.getDownloadAppinfoDao().insertOrReplace(info);
						observer.onStateChanged(info);
						return;
					}
					info.setCurrentSize(info.getThread1() + info.getThread2() + info.getThread3() + info.getThread4() + info.getThread5());
					info.setProgress((info.getCurrentSize() + 0.0f) / Float.parseFloat(info.getAppSize()));
					DownloadManager.DBManager.getDownloadAppinfoDao().insertOrReplace(info);
					observer.onProgressChanged(info);
				}
			}
				downloading = false;
			};
		}.start();
		
		while (downloading) {
			
		}
	}
	
	private long getCompelete(int i) {
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

	Handler unzipHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				observer.onRemoveFromTask(info);
				observer.onStateChanged(info);
				downloading = false;
			}
		}
	};

	private class DownloadThread extends Thread {
		private int threadId;
		private long startPos;
		private long endPos;
		private long compeleteSize;

		public DownloadThread(int threadId, long startPos, long compeleteSize, long endPos) {
			this.threadId = threadId;
			this.startPos = startPos;
			this.endPos = endPos;
			this.compeleteSize = compeleteSize;
		}

		private synchronized void setComplete() {
			if (threadId == 0) {
				info.setThread1(compeleteSize);
			} else if (threadId == 1) {
				info.setThread2(compeleteSize);
			} else if (threadId == 2) {
				info.setThread3(compeleteSize);
			} else if (threadId == 3) {
				info.setThread4(compeleteSize);
			} else if (threadId == 4) {
				info.setThread5(compeleteSize);
			}
		}

		@Override
		public void run() {
			if ((compeleteSize + startPos) < endPos) {
				String path = "";
				if (info.getIsZip()) {
					path = info.getZipPath();
				} else {
					path = info.getApkPath();
				}
				HttpURLConnection connection = null;
				InputStream is = null;
				byte[] buffer = new byte[8 * 1024];
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
					is = connection.getInputStream();
					int length = 0;
					while ((length = is.read(buffer)) != -1 
							&& info.getDownloadState() == DownloadManager.STATE_DOWNLOADING) {
						randomAccessFile.write(buffer, 0, length);
						compeleteSize += length;
						setComplete();
					}
				} catch (Exception e) {
					e.printStackTrace();
					info.setDownloadState(DownloadManager.STATE_ERROR);
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (connection != null) {
						connection.disconnect();
					}
					if (randomAccessFile != null) {
						try {
							randomAccessFile.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					synchronized (this) {
						setComplete();
//						observer.onStateChanged(info);
						observer.onRemoveFromTask(info);
						DownloadManager.DBManager.getDownloadAppinfoDao().insertOrReplace(info);
					}
				}
			}
		}
	}

	public interface ChangeObserver {
		public void onStateChanged(DownloadAppinfo info);

		public void onProgressChanged(DownloadAppinfo info);

		public void onRemoveFromTask(DownloadAppinfo info);
	}
}
