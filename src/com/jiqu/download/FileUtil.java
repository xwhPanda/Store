package com.jiqu.download;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

/**
 * 文件工具类
 */
public class FileUtil {
	private static String TAG = "FileUtil";

	/**
	 * 获取目录名称
	 * @param url
	 * @return FileName
	 */

	public static String getFileName(String url) {
		int lastIndexStart = url.lastIndexOf("/");
		if (lastIndexStart != -1) {
			return url.substring(lastIndexStart + 1, url.length());
		} else {
			return new Timestamp(System.currentTimeMillis()).toString();
		}
	}

	/**
	 * 判断SD卡是否存在
	 * 
	 * @return boolean
	 */
	public static boolean checkSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public static String getApkDownloadDir(Context context) {
		String path = getPhoneMemoryPath(context) + File.separator
				+ "mdownload" + File.separator + "apkDownload";
		createNewDir(path);
		return path;
	}
	
	public static String getZipDownloadDir(Context context) {
		String path = getPhoneMemoryPath(context) + File.separator
				+ "mdownload" + File.separator + "zipDownload";
		createNewDir(path);
		return path;
	}
	
	public static String getUpgradeDownloadDir(Context context){
		String path = getPhoneMemoryPath(context) + File.separator
				+ "mdownload" + File.separator + "upgradeDownload";
		createNewDir(path);
		return path;
	}
	
	public static void createNewDir(String dir) {
		if (!checkSDCard()) {
			return;
		}
		if (null == dir) {
			return;
		}
		File f = new File(dir);
		if (!f.exists()) {
			String[] pathSeg = dir.split(File.separator);
			String path = "";
			for (String temp : pathSeg) {
				if (TextUtils.isEmpty(temp)) {
					path += File.separator;
					continue;
				} else {
					path += temp + File.separator;
				}
				File tempPath = new File(path);
				if (tempPath.exists() && !tempPath.isDirectory()) {
					tempPath.delete();
				}
				tempPath.mkdirs();
			}
		} else {
			if (!f.isDirectory()) {
				f.delete();
				f.mkdirs();
			}
		}
	}

	/**
	 * 获取手机可用的存储路径
	 */
	public static String getPhoneMemoryPath(Context c) {
		String sdStatus = Environment.getExternalStorageState();
		boolean sdCardExist = sdStatus.equals(android.os.Environment.MEDIA_MOUNTED);

		if (TextUtils.isEmpty(sdStatus)) {
			return c.getFilesDir().getAbsolutePath();
		}

		if (!sdCardExist) {
			return c.getFilesDir().getAbsolutePath();
		}

		try {
			long sdcardSpace = 0;
			try {
				sdcardSpace = getSDcardAvailableSpace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (sdcardSpace >= 5) {
				return getSDCardPath(c);
			}

			long phoneSpace = getDataStorageAvailableSpace();
			if (phoneSpace >= 5) {
				return c.getFilesDir().getAbsolutePath();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return c.getFilesDir().getAbsolutePath();
	}

	/**
	 * 获取data可用空间大小
	 * 
	 * @return
	 */
	public static long getDataStorageAvailableSpace() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * 获取手机内置SD卡可用空间大小
	 * 
	 */
	public static long getSDcardAvailableSpace() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File path = Environment.getExternalStorageDirectory();
			if (path == null) {
				return 0;
			}
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return availableBlocks * blockSize; // "Byte"
		} else {
			return 0;
		}
	}

	/**
	 * 获取手机内置SD卡路径
	 * 
	 */
	public static String getSDCardPath(Context c) {
		File sdDir = null;
		String sdStatus = Environment.getExternalStorageState();
		boolean sdCardExist = sdStatus
				.equals(android.os.Environment.MEDIA_MOUNTED);

		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();
			return sdDir.toString();
		}
		return "";
	}

	/**
	 * 获取文件的大小
	 * 
	 * @param fileSize
	 *            文件的大小
	 * @return
	 */
	public static String FormetFileSize(long fileSize) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileSize < 1024) {
			fileSizeString = df.format((double) fileSize) + "B";
		} else if (fileSize < 1048576) {
			fileSizeString = df.format((double) fileSize / 1024) + "K";
		} else if (fileSize < 1073741824) {
			fileSizeString = df.format((double) fileSize / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileSize / 1073741824) + "G";
		}
		return fileSizeString;
	}

	public static boolean isValidAttach(String path, boolean inspectSize) {
		if (!isExistsFile(path) || getFileSize(path) == 0) {
			Log.d(TAG, "isValidAttach: file is not exist, or size is 0");
			return false;
		}
		if (inspectSize && getFileSize(path) > 2 * 1024 * 1024) {
			Log.d(TAG, "file size is too large");
			return false;
		}
		return true;
	}

	public static boolean isExistsFile(String filepath) {
		try {
			if (TextUtils.isEmpty(filepath)) {
				return false;
			}
			File file = new File(filepath);
			return file.exists();
		} catch (Exception e) {
			// e.printStackTrace();
			Log.d(TAG, "the file is not exists file path is: " + filepath);
			return false;
		}
	}

	public static int getFileSize(String filepath) {
		try {
			if (TextUtils.isEmpty(filepath)) {
				return -1;
			}
			File file = new File(filepath);
			return (int) file.length();
		} catch (Exception e) {
			return -1;
		}
	}
	
	
	public static void copy(String src, String dest) {
		if (TextUtils.isEmpty(src) || TextUtils.isEmpty(dest)) {
			return;
		}
		InputStream is = null;
		OutputStream os = null;
		File out = new File(dest);
		if (!out.getParentFile().exists()) {
			out.getParentFile().mkdirs();
		}
		try {
			is = new BufferedInputStream(new FileInputStream(src));
			os = new BufferedOutputStream(new FileOutputStream(dest));
			byte[] b = new byte[256];
			int len = 0;
			try {
				while ((len = is.read(b)) != -1) {
					os.write(b, 0, len);
				}
				os.flush();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						Log.e(TAG, e.getMessage());
					}
				}
			}
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
				}
			}
		}
	}

	public static boolean isPic(String name) {
		if (TextUtils.isEmpty(name)) {
			return false;
		}
		String path = name.toLowerCase();
		if (path.endsWith(".png") || path.endsWith(".jpg")
				|| path.endsWith(".jpeg") || path.endsWith(".bmp")
				|| path.endsWith(".gif")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isVideo(String name) {
		if (TextUtils.isEmpty(name)) {
			return false;
		}
		String path = name.toLowerCase();
		if (path.endsWith(".mp4") || path.endsWith(".3gp")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isAudio(String name) {
		if (TextUtils.isEmpty(name)) {
			return false;
		}
		String extArrayString[] = { ".amr", ".ogg", ".mp3", ".aac", ".ape",
				".flac", ".wma", ".wav", ".mp2", ".mid", ".3gpp" };
		String path = name.toLowerCase();
		for (String ext : extArrayString) {
			if (path.endsWith(ext))
				return true;
		}

		return false;
	}
	
	public static String getFileSize(long size){
		if (size >= 0) {
			if(size < 1024){
				return size+"B";
			}else if(size<1024*1024){
				float kbsize = size/1024f;  
				return new BigDecimal(kbsize).setScale(1, BigDecimal.ROUND_UP).toString() + "KB";
			}else if(size<1024*1024*1024){
				float mbsize = size/1024f/1024f;  
				return new BigDecimal(mbsize).setScale(1, BigDecimal.ROUND_UP).toString() + "M";
			}else if(size<1024*1024*1024*1024){
				float gbsize = size/1024f/1024f/1024f;  
				return new BigDecimal(gbsize).setScale(1, BigDecimal.ROUND_UP).toString() + "G";
			}else{
				return 0 + "KB";
			}
		}else {
			return 0 + "KB";
		}
	}
	
	public static String getSize(long size){
		String str = "0 M";
		if ((size / 1024) == 0) {
			return str;
		}else {
			if (size / 1024 / 1024 == 0) {
				return size / 1024 + " KB";
			}else {
				if (size / 1024 / 1024 / 1024 == 0) {
					return size / 1024 / 1024 + "M";
				}else {
					if(size / 1024 / 1024 / 1024 / 1024 == 0){
						return size / 1024 / 1024 / 1024 + "G";
					}
				}
			}
		}
		return str;
	}
}
