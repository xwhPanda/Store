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
		}else if (value instanceof Long) {
			editor.putLong(key, (Long) value);
			editor.commit();
		}else if (value instanceof String) {
			editor.putString(key, (String)value);
			editor.commit();
		}
	}
	
	public static boolean getBooleanFromPreferences(SharedPreferences preferences,String key,boolean defaultValue){
		return preferences.getBoolean(key, defaultValue);
	}
	
	public static int getIntFromPreferences(SharedPreferences preferences,String key,int defaultValue){
		return preferences.getInt(key, defaultValue);
	}
	
	public static long getLongFromPreferences(SharedPreferences preferences,String key,long defaultValue){
		return preferences.getLong(key, defaultValue);
	}
	
	public static String getStringFromPreferences(SharedPreferences preferences,String key,String defaultValue){
		return preferences.getString(key, defaultValue);
	}
}
