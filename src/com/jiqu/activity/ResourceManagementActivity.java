package com.jiqu.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.share.sdk.openapi.SendMessageToZFB.Req;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.jiqu.adapter.GameAdapter;
import com.jiqu.adapter.ResourcesManagmentAdapter;
import com.jiqu.application.StoreApplication;
import com.jiqu.download.DownloadManager;
import com.jiqu.object.GameInfo;
import com.jiqu.object.InstalledApp;
import com.jiqu.object.ResourceInfo;
import com.jiqu.object.ResourcesItem;
import com.jiqu.store.BaseActivity;
import com.umeng.socialize.utils.Log;
import com.vr.store.R;
import com.jiqu.tools.InstalledAppTool;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.PullToRefreshLayout.OnRefreshListener;
import com.jiqu.view.PullableListView;
import com.jiqu.view.TitleView;

public class ResourceManagementActivity extends BaseActivity{
	private final String RESOURECE_REQUEST = "resourceRequest";
	private RelativeLayout parent;
	private TitleView titleView;
	private PullToRefreshLayout refreshView;
	private PullableListView resourceListView;
	private GameAdapter adapter;
	private List<GameInfo> upgradeGameInfos = new ArrayList<GameInfo>();
	private RequestTool requestTool;
	private HashMap<String, Object> map = new HashMap<String, Object>();
	private String json = "";
	private boolean isShowing = false;
	private InstalledAppTool installedAppTool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestTool = RequestTool.getInstance();
		installedAppTool = new InstalledAppTool();
		json = initInstallAppJson();
		initView();
		loadData(RequestTool.OTHER_UPGRADE_URL,json);
		
		adapter.startObserver();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		adapter.stopObserver();
		requestTool.stopRequest(RESOURECE_REQUEST);
	}
	
	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.resource_management_layout;
	}

	private void initView(){
		parent = (RelativeLayout) findViewById(R.id.parent);
		parent.setBackgroundDrawable(StoreApplication.BG_IMG);
		titleView = (TitleView) findViewById(R.id.titleView);
		refreshView = (PullToRefreshLayout) findViewById(R.id.refreshView);
		resourceListView = (PullableListView) findViewById(R.id.resourceListView);
		
		resourceListView.setCanPullUp(false);
		
		titleView.setActivity(this);
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.tip.setText(R.string.resourceManagement);
		
		adapter = new GameAdapter(this, upgradeGameInfos,false,false);
		resourceListView.setAdapter(adapter);
		
		resourceListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				startActivity(new Intent(ResourceManagementActivity.this, DetailActivity.class)
				.putExtra("id", upgradeGameInfos.get(position).getId()));
			}
		});
	}
	
	private String initInstallAppJson(){
		List<InstalledApp> apps = installedAppTool.getPersonalApp(ResourceManagementActivity.this);
		JSONArray array = new JSONArray();
		for(InstalledApp app : apps){
			JSONObject object = new JSONObject();
			object.put("package_name", app.packageName);
			object.put("version", app.versionCode);
			array.add(object);
		}
		return array.toString();
	}
	
	private void loadData(String url,String json){
		map.clear();
		map.put("package", json);
		requestTool.startStringRequest(Method.POST, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				ResourceInfo resourceInfo = JSON.parseObject(arg0, ResourceInfo.class);
				if (resourceInfo != null) {
					if (resourceInfo.getStatus() == 1 && resourceInfo.getData() != null) {
						for (GameInfo info : resourceInfo.getData()) {
							info.setState(DownloadManager.STATE_NEED_UPDATE);
							upgradeGameInfos.add(info);
						}
						adapter.notifyDataSetChanged();
					}else if (resourceInfo.getStatus() == 0) {
						Toast.makeText(ResourceManagementActivity.this, "没有可更新的应用", Toast.LENGTH_SHORT).show();
					}
				}
				
				if (isShowing) {
					isShowing = false;
					refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
			}
		}, url, new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				if (isShowing) {
					isShowing = false;
					refreshView.refreshFinish(PullToRefreshLayout.FAIL);
				}
			}
			
		}, map, RESOURECE_REQUEST);
	}
}
