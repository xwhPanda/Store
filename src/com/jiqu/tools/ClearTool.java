package com.jiqu.tools;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jiqu.application.StoreApplication;
import com.jiqu.download.ThreadManager;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

public class ClearTool {
	private static final String CLEAR_SINGLE_POOL_NAME= "CLEAR_TOOL";
	private Map<String, GetAppSizeRunnable> tasks = new ConcurrentHashMap<String, ClearTool.GetAppSizeRunnable>();
	private long cachesize;
	private long externalCacheSize;
	private Handler handler;
	private static ClearTool instance;
	private int count = 0;
	private int size = 0;
	
	public static ClearTool getInstance(){
		if (instance == null) {
			instance = new ClearTool();
		}
		return instance;
	}
	
	public void setHandler(Handler handler){
		this.handler = handler;
	}

	/** 
	 *获取所有应用的缓存大小
	 *所有/data/data/应用包名/cache相加
	 **/
	public void startClearCacheSize(Context context){
		cachesize = 0;
		externalCacheSize = 0;
		count = 0;
		size = 0;
		PackageManager pm = context.getPackageManager();
		List<String> pkgs = getAllInstallAppPkg(context); 
		size = pkgs.size();
		for(String pkg : pkgs){
			GetAppSizeRunnable runnable = new GetAppSizeRunnable(context,pm,pkg);
			if (tasks.get(pkg) == null) {
				tasks.put(pkg, runnable);
			}
			ThreadManager.getSinglePool(CLEAR_SINGLE_POOL_NAME).execute(runnable);
		}
	}
	
	public void stopClear(){
		tasks.clear();
		ThreadManager.getSinglePool(CLEAR_SINGLE_POOL_NAME).stop();
	}
	
	/**
	 * 获取所有安装应用的包名
	 * @return
	 */
	private List<String> getAllInstallAppPkg(Context context){
		List<String> pgkList = new ArrayList<String>();
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packageInfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for(PackageInfo info : packageInfos){
			pgkList.add(info.packageName);
		}
		
		return pgkList;
	}
	
	public class GetAppSizeRunnable implements Runnable{
		private PackageManager pm;
		private String packageName;
		private Context context;
		
		public GetAppSizeRunnable(Context context,PackageManager pm,String packname){
			this.pm = pm;
			this.packageName = packname;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Method method = PackageManager.class.getMethod(
						"getPackageSizeInfo", new Class[] { String.class,IPackageStatsObserver.class });

				method.invoke(pm, new Object[] { packageName,new IPackageStatsObserver.Stub() {

							public void onGetStatsCompleted(PackageStats pStats,
									boolean succeeded) throws RemoteException {
								// 注意这个操作是一个异步的操作
//								externalCacheSize += pStats.externalCacheSize + pStats.externalMediaSize;
								tasks.remove(packageName);
								++count;
								if (StoreApplication.PACKAGE_NAME.equals(packageName)) {
									cachesize = pStats.cacheSize;
									deleteFolderFile(StoreApplication.DATA_CACHE_PATH, false);
								}
								if (count == size) {
									Message msg = handler.obtainMessage();
									msg.what = 1;
									msg.obj = getDataSize(cachesize)+ "@" + getDataSize(externalCacheSize);
									handler.sendMessage(msg);
								}
							}
						} });

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void deleteFolderFile(String filePath, boolean deleteThisPath) {  
        if (!TextUtils.isEmpty(filePath)) {  
            try {  
                File file = new File(filePath);  
                if (file.isDirectory()) {// 如果下面还有文件  
                    File files[] = file.listFiles();  
                    for (int i = 0; i < files.length; i++) {  
                        deleteFolderFile(files[i].getAbsolutePath(), true);  
                    }  
                }  
                if (deleteThisPath) {  
                    if (!file.isDirectory()) {// 如果是文件，删除  
                        file.delete();  
                    } else {// 目录  
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除  
                            file.delete();  
                        }  
                    }  
                }  
            } catch (Exception e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        }  
    }
	
	private long getEnvironmentSize() {
        File localFile = Environment.getDataDirectory();  
        long l1;  
        if (localFile == null)  
            l1 = 0L;  
        while (true) {  
            String str = localFile.getPath();  
            StatFs localStatFs = new StatFs(str);  
            long l2 = localStatFs.getBlockSize();  
            l1 = localStatFs.getBlockCount() * l2;  
            return l1;  
        }  
  
    }
	
	/**
	 * 清理缓存
	 * @param context
	 */
	private void clearCache(Context context,String packageName) {  
		  
        try {  
            PackageManager packageManager = context.getPackageManager();  
            Method localMethod = packageManager.getClass().getMethod("deleteApplicationCacheFiles", String.class,  
                    IPackageDataObserver.class);  
            Long localLong = Long.valueOf(getEnvironmentSize() - 1L);  
            Object[] arrayOfObject = new Object[2];  
            arrayOfObject[0] = localLong;  
            localMethod.invoke(packageManager, new Object[] { packageName,new IPackageDataObserver.Stub() {  
  
                @Override  
                public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {  
                } 
            }});  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
	
	/**
	 * 返回byte的数据大小对应的文本
	 * @param size
	 * @return
	 */
	public static String getDataSize(long size){
		DecimalFormat formater = new DecimalFormat("####.00");
		if(size<1024){
			return size+"B";
		}else if(size<1024*1024){
			float kbsize = size/1024f;  
			return formater.format(kbsize)+"KB";
		}else if(size<1024*1024*1024){
			float mbsize = size/1024f/1024f;  
			return formater.format(mbsize)+"M";
		}else if(size<1024*1024*1024*1024){
			float gbsize = size/1024f/1024f/1024f;  
			return formater.format(gbsize)+"G";
		}else{
			return "size: error";
		}
	}
	
	public void killProcess(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> infoList = am.getRunningAppProcesses();
        List<ActivityManager.RunningServiceInfo> serviceInfos = am.getRunningServices(100);

        long beforeMem = getAvailMemory(context);
        Log.d("TAG", "-----------before memory info : " + beforeMem);
        int count = 0;
        if (infoList != null) {
            for (int i = 0; i < infoList.size(); ++i) {
                RunningAppProcessInfo appProcessInfo = infoList.get(i);
                //importance 该进程的重要程度  分为几个级别，数值越低就越重要。
                Log.d("TAG", "importance : " + appProcessInfo.importance);

                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_SERVICE的进程都长时间没用或者空进程了
                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_VISIBLE的进程都是非可见进程，也就是在后台运行着
                if (appProcessInfo.importance > RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    String[] pkgList = appProcessInfo.pkgList;
                    for (int j = 0; j < pkgList.length; ++j) {//pkgList 得到该进程下运行的包名
                        Log.d("TAG", "It will be killed, package name : " + pkgList[j]);
                        am.killBackgroundProcesses(pkgList[j]);
                        count++;
                    }
                }

            }
        }

        long afterMem = getAvailMemory(context);
        Log.d("TAG", "----------- after memory info : " + afterMem);
    }
	
	   //获取可用内存大小
    private long getAvailMemory(Context context) {
        // 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        //return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
        return mi.availMem / (1024 * 1024);
    }
}
