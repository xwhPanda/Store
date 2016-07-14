package com.jiqu.tools;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.jiqu.application.StoreApplication;
import com.jiqu.download.Upgrade;
import com.jiqu.object.UpgradeVersionInfo;
import com.jiqu.object.UpgradeVersionInfo.VersionInfo;
import com.jiqu.view.NetChangeDialog;

/**
 * @author xiongweihua
 * @version 2016-7-2
 */
public class CheckNewVersion {
	private final String CHECKNE_WVERSION_REQUEST = "checkNewversionRequest";
	private Context context;
	private NetChangeDialog upgradeDialog;
	private VersionInfo versionInfo;

	public CheckNewVersion(Context context) {
		this.context = context;
		upgradeDialog = new NetChangeDialog(context);

		setDialogListener();
	}

	/** 设置监听 **/
	private void setDialogListener() {
		upgradeDialog.setNegativeListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				upgradeDialog.dismiss();
			}
		});

		upgradeDialog.setPositiveListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Upgrade(versionInfo.getDown_url(), StoreApplication.UPGRADE_DOWNLOAD_PATH, StoreApplication.PACKAGE_NAME + ".apk").startDownload();
				upgradeDialog.dismiss();
			}
		});
	}

	/** 升级检测 
	 * showToast 是否弹出toast**/
	public void checkNewVersion(final boolean showToast) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("version", Constants.VERSION_CODE);
		map.put("package", Constants.PACKAGENAME);
		map.put("channel", StoreApplication.CHANNEL);
		RequestTool.getInstance().startStringRequest(Method.POST, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				UpgradeVersionInfo upgradeVersionInfo = JSON.parseObject(arg0, UpgradeVersionInfo.class);
				if (upgradeVersionInfo != null) {
					if (upgradeVersionInfo.getStatus() == 1) {
						versionInfo = upgradeVersionInfo.getData();
						upgradeDialog.setContent("发现新版本 ： " + versionInfo.getVersion_name());
						upgradeDialog.setNegativeText("下次再说");
						upgradeDialog.setPositiveText("立即更新");
						upgradeDialog.show();
					} else if (upgradeVersionInfo.getStatus() == 0) {
						if (showToast) {
							Toast.makeText(context, "没有新版本", Toast.LENGTH_SHORT).show();
						}
						Log.i("UpgradeVersion", "latest version !");
					}
				}
			}
		}, RequestTool.VR_HELPER_UPGRADE_URL, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
			}
		}, map, CHECKNE_WVERSION_REQUEST);
	}
	
	public void stopRequest(){
		RequestTool.getInstance().stopRequest(CHECKNE_WVERSION_REQUEST);
	}
}
