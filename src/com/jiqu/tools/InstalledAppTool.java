package com.jiqu.tools;

import java.io.DataOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jiqu.application.StoreApplication;
import com.jiqu.download.DownloadManager;
import com.jiqu.download.ThreadManager;
import com.jiqu.interfaces.UninstallStateObserver;
import com.jiqu.object.InstalledApp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

public class InstalledAppTool {
	private static InstalledAppTool instance;
	private Map<String, InstalledApp> uninstallMap = new ConcurrentHashMap<String, InstalledApp>();
	private List<UninstallStateObserver> mObservers = new ArrayList<UninstallStateObserver>();
	private Map<String, UninstallTask> taskMap = new ConcurrentHashMap<String, InstalledAppTool.UninstallTask>();

	public static synchronized InstalledAppTool getInstance() {
		if (instance == null) {
			instance = new InstalledAppTool();
		}
		return instance;
	}

	public void registerObsverver(UninstallStateObserver observer) {
		synchronized (mObservers) {
			if (!mObservers.contains(observer)) {
				mObservers.add(observer);
			}
		}
	}

	public void unRegisterObsverver(UninstallStateObserver observer) {
		synchronized (mObservers) {
			if (mObservers.contains(observer)) {
				mObservers.remove(observer);
			}
		}
	}

	private static List<PackageInfo> getInstallAppsPkg(Context context){
		return context.getPackageManager().getInstalledPackages(0);
	}

	public static List<InstalledApp> getPersonalApp(Context context) {
		List<InstalledApp> apps = new ArrayList<InstalledApp>();
		List<PackageInfo> packageInfos = getInstallAppsPkg(context);
		PackageManager pm = context.getPackageManager();
		
		for (PackageInfo packageInfo:packageInfos) {
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
				// 第三方应用
				InstalledApp app = new InstalledApp();
				app.name = (String) packageInfo.applicationInfo.loadLabel(pm);
				app.packageName = packageInfo.packageName;
				app.appIcon = packageInfo.applicationInfo.loadIcon(pm);
				app.versionCode = packageInfo.versionCode;
				app.versionName = packageInfo.versionName;
				app.filePath = packageInfo.applicationInfo.publicSourceDir;
				app.isSystem = false;
				apps.add(app);
			}
		}
		return apps;
	}

	public static List<InstalledApp> getSystemApp(Context context) {
		List<InstalledApp> apps = new ArrayList<InstalledApp>();
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packageInfos = getInstallAppsPkg(context);
		for (PackageInfo packageInfo:packageInfos) {
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
				// 系统应用
				InstalledApp app = new InstalledApp();
				app.name = (String) packageInfo.applicationInfo.loadLabel(pm);
				app.packageName = packageInfo.packageName;
				app.appIcon = packageInfo.applicationInfo.loadIcon(pm);
				app.versionCode = packageInfo.versionCode;
				app.versionName = packageInfo.versionName;
				app.filePath = packageInfo.applicationInfo.publicSourceDir;
				app.isSystem = false;
				apps.add(app);
			}
		}
		return apps;
	}
	
	public static InstalledApp getInstallApp(Context context,String pkgName) {
		List<PackageInfo> packageInfos = getInstallAppsPkg(context);
		InstalledApp app = new InstalledApp();
		PackageManager pm = context.getPackageManager();
		for (PackageInfo packageInfo:packageInfos) {
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
				app.name = (String) packageInfo.applicationInfo.loadLabel(pm);
				app.packageName = packageInfo.packageName;
				app.appIcon = packageInfo.applicationInfo.loadIcon(pm);
				app.versionCode = packageInfo.versionCode;
				app.versionName = packageInfo.versionName;
				app.filePath = packageInfo.applicationInfo.publicSourceDir;
				app.isSystem = false;
			}
		}
		return app;
	}
	
	public static int contain(List<InstalledApp> personalApps ,String pkg,int versionCode){
		int state = -1;
		for (InstalledApp app : personalApps) {
			if (pkg.equals(app.packageName)) {
				if (app.versionCode < versionCode) {
					state = DownloadManager.STATE_NEED_UPDATE;
				}
				state = DownloadManager.STATE_INSTALLED;
			}
		}
		
		return state;
	}
	
	public static int contain(List<InstalledApp> personalApps ,String pkg){
		int state = -1;
		for (InstalledApp app : personalApps) {
			if (pkg.equals(app.packageName)) {
				state = DownloadManager.STATE_INSTALLED;
			}
		}
		return state;
	}

	public static boolean RootCommand(String command) {
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e) {
			}
		}
		return true;
	}

	public static void uninstallApp(Context context, String pkgName) {
		String uriStr = "package:" + pkgName;
		Uri packageURI = Uri.parse(uriStr);
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		context.startActivity(uninstallIntent);
	}
	
	public synchronized void addUninstall(InstalledApp app){
		InstalledApp installedApp = uninstallMap.get(app.packageName);
		if (installedApp == null) {
			uninstallMap.put(app.packageName, app);
			Log.i("TAG", "addUninstall : " + app.packageName + "/" + app.name);
		}
	}
	
	public synchronized void removeUninstall(InstalledApp app){
		InstalledApp installedApp = uninstallMap.get(app.packageName);
		if (installedApp != null) {
			uninstallMap.remove(app.packageName);
			Log.i("TAG", "removeUninstall : " + app.packageName + "/" + app.name);
		}
	}
	
	public synchronized void removeUninstallTask(InstalledApp app){
		removeUninstallTask(app.packageName);
	}
	
	public synchronized void removeUninstallTask(String pkg){
		InstalledApp installedApp = uninstallMap.get(pkg);
		if (installedApp != null) {
			uninstallMap.remove(pkg);
		}
		UninstallTask task = taskMap.get(pkg);
		if (task != null) {
			taskMap.remove(pkg);
			Log.i("TAG", "removeUninstallTask : " + pkg);
		}
	}
	
	public synchronized void uninstallAll(Context context){
		for(String key:uninstallMap.keySet()){
			InstalledApp app = uninstallMap.get(key);
			Log.i("TAG", "应用名 ：" + app.name);
			app.state = InstalledApp.UNINSTALLING;
			notifyUninstallStateChanged(app);
			UninstallTask appTask = taskMap.get(app.packageName);
			if (appTask == null) {
				UninstallTask uninstallTask = new UninstallTask(context, app);
				taskMap.put(app.packageName, uninstallTask);
				ThreadManager.getSinglePool().execute(uninstallTask);
			}else {
				ThreadManager.getSinglePool().execute(appTask);
			}
		}
	}
	
	public synchronized void uninstall(Context context ,InstalledApp app){
//		InstalledApp installedApp = uninstallMap.get(app.packageName);
//		if (installedApp == null) {
//			uninstallMap.put(app.packageName, app);
//		}
		if (app.state == InstalledApp.INSTALLED) {
			app.state = InstalledApp.UNINSTALLING;
			notifyUninstallStateChanged(app);
			UninstallTask uninstallTask = new UninstallTask(context, app);
			taskMap.put(app.packageName, uninstallTask);
			ThreadManager.getSinglePool().execute(uninstallTask);
		}
	}

	public class UninstallTask implements Runnable {
		private Context context;
		private InstalledApp app;

		public UninstallTask(Context context, InstalledApp app) {
			this.context = context;
			this.app = app;
		}

		@Override
		public void run() {
			app.state = InstalledApp.UNINSTALLING;
			InstalledAppTool.uninstallApp(context, app.packageName);
		}

	}
	
	public void notifyUninstallStateChanged(InstalledApp app){
		synchronized (mObservers) {
			for (UninstallStateObserver observer : mObservers) {
				app.state = InstalledApp.UNINSTALLING;
				observer.onUninstallStateChanged(app);
				Log.i("TAG", "正在卸载 ： " + app.name);
			}
		}
	}

}
