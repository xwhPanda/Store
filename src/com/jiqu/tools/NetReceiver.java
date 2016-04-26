package com.jiqu.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetReceiver extends BroadcastReceiver {
	public static final int NET_WIFI = 1;
	public static final int NET_NOBILE = 0;
	public static final int NET_NONE = -1;
	public static int NET_TYPE = -1;
	private ConnectivityManager cm;
	private NetworkInfo ni;
	private OnNetChangeListener onNetChangeListener;
	public static NetReceiver instance;
	
	private int currentType = -1;

	public static NetReceiver getInstance(){
		if (instance == null) {
			instance = new NetReceiver();
		}
		
		return instance;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			ni = cm.getActiveNetworkInfo();
			if (ni != null && ni.isConnected()) {
				int type = ni.getType();
				if (type == 1 && currentType != type) {
					currentType = type;
					NET_TYPE = currentType;
					if (onNetChangeListener != null) {
						onNetChangeListener.onNetChange(NET_WIFI);
					}
					
				}else if (type == 0 && currentType != type) {
					currentType = type;
					NET_TYPE = currentType;
					if (onNetChangeListener != null) {
						onNetChangeListener.onNetChange(NET_NOBILE);
					}
				}
			}else {
				if (currentType != NET_NONE) {
					currentType = NET_NONE;
					NET_TYPE = currentType;
					if (onNetChangeListener != null) {
						onNetChangeListener.onNetChange(NET_NONE);
					}
				}
			}
		}
	}
	
	public void registerReceive(Context context){
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		context.registerReceiver(this, filter);
	}
	
	public void unregisterReceive(Context context){
		context.unregisterReceiver(this);
	}
	
	public void setNetChangeListener(OnNetChangeListener onNetChangeListener){
		Log.i("TAG", onNetChangeListener.toString());
		this.onNetChangeListener = onNetChangeListener;
	}
	
	public interface OnNetChangeListener{
		public void onNetChange(int netType);
	}
}
