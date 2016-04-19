package com.jiqu.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetReceiver extends BroadcastReceiver {
	private ConnectivityManager cm;
	private NetworkInfo ni;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			ni = cm.getActiveNetworkInfo();
			if (ni != null && ni.isAvailable()) {
				String name = ni.getTypeName();
				Log.i("TAG", name);
				if (ni.getType() == ConnectivityManager.TYPE_MOBILE) {
					ni.getSubtype();
					Log.i("TAG", ni.getSubtypeName());
				}else if (ni.getType() == ConnectivityManager.TYPE_WIFI) {
					
				}
			}
		}
	}

}
