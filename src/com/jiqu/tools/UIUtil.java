package com.jiqu.tools;

import com.jiqu.application.StoreApplication;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;

public class UIUtil {
	
	public static void setViewSize(View view,float width,float height){
		LayoutParams lp = view.getLayoutParams();
		lp.width = (int)width;
		lp.height = (int)height;
		view.setLayoutParams(lp);
	}
	
	public static void setViewWidth(View view,float width){
		LayoutParams lp = view.getLayoutParams();
		lp.width = (int)width;
		view.setLayoutParams(lp);
	}
	
	public static void setViewHeight(View view,float height){
		LayoutParams lp = view.getLayoutParams();
		lp.height = (int)height;
		view.setLayoutParams(lp);
	}
	
	public static void setViewSizeMargin(View view,float left,float top,float right,float bottom) throws Exception{
		ViewGroup.LayoutParams lp = view.getLayoutParams();
		if (lp instanceof ViewGroup.MarginLayoutParams) {
			ViewGroup.MarginLayoutParams mlp = (MarginLayoutParams) lp;
			mlp.leftMargin = (int) left;
			mlp.topMargin = (int) top;
			mlp.rightMargin = (int) right;
			mlp.bottomMargin = (int) bottom;
			view.setLayoutParams(mlp);
		}else {
			Log.w("TAG", "View's LayoutParams is not MarginLayoutParams !");
		}
	}
	
	public static void setTextSize(TextView view, float size){
		view.setTextSize(TypedValue.COMPLEX_UNIT_PX, size * MetricsTool.Rx);
	}
	
	public static void setViewPadding(View view,int left,int top,int right,int bottom){
		view.setPadding(left, top, right, bottom);
	}
	
	public static String getVersionName(Context context){
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
		
	}
}
