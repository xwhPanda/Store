package com.jiqu.tools;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SharePreferenceTool{
	
	public static void setValuePreferences(SharedPreferences preferences,String key,Object value){
		Editor editor = preferences.edit();
		if (value instanceof Boolean) {
			editor.putBoolean(key, (Boolean) value);
			editor.commit();
		}else if (value instanceof Integer) {
			editor.putInt(key, (Integer) value);
			editor.commit();
		}
	}
	
	public static boolean getBooleanFromPreferences(SharedPreferences preferences,String key,boolean defaultValue){
		return preferences.getBoolean(key, defaultValue);
	}
	
	public static int getIntFromPreferences(SharedPreferences preferences,String key,int defaultValue){
		return preferences.getInt(key, defaultValue);
	}
}
