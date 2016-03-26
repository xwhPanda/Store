package com.jiqu.tools;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class MetricsTool {
	public static int height,width;
	public static int densityDpi;
	public static float density;
	
	public static float Rx;
	public static float Ry;
	
	public static void initMetrics(WindowManager wm){
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);
		
		height = metrics.heightPixels;
		width = metrics.widthPixels;
		densityDpi = metrics.densityDpi;
		density = metrics.density;
		
		Rx = (float) (width / 1080.0);
		Ry = (float) (height / 1920.0);
	}
	
	
}
