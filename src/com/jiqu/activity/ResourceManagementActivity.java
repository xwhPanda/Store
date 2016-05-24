package com.jiqu.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.widget.ListView;

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
import com.jiqu.view.PullableListView;
import com.jiqu.view.TitleView;

public class ResourceManagementActivity extends BaseActivity {
	private final String RESOURECE_REQUEST = "resourceRequest";
	private TitleView titleView;
	private PullToRefreshLayout refreshView;
	private ListView resourceListView;
	private GameAdapter adapter;
	private List<GameInfo> upgradeGameInfos = new ArrayList<GameInfo>();
	private RequestTool requestTool;
	private HashMap<String, Object> map = new HashMap<String, Object>();
	private String json = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestTool = RequestTool.getInstance();
		json = initInstallAppJson();
		initView();
		loadData(RequestTool.OTHER_UPGRADE_URL,json);
	}
	
	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.resource_management_layout;
	}

	private void initView(){
		titleView = (TitleView) findViewById(R.id.titleView);
		refreshView = (PullToRefreshLayout) findViewById(R.id.refreshView);
		resourceListView = (PullableListView) findViewById(R.id.resourceListView);
		
		titleView.setActivity(this);
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.tip.setText(R.string.resourceManagement);
		
		adapter = new GameAdapter(this, upgradeGameInfos,false,false);
		resourceListView.setAdapter(adapter);
		
	}
	
	private String initInstallAppJson(){
		List<InstalledApp> apps = InstalledAppTool.getPersonalApp(this);
		JSONArray array = new JSONArray();
		for(InstalledApp app : apps){
			JSONObject object = new JSONObject();
			object.put("package_name", app.packageName);
			object.put("version", 1);
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
						upgradeGameInfos.addAll(resourceInfo.getData());
						adapter.notifyDataSetChanged();
					}else if (resourceInfo.getStatus() == 0) {
						
					}
				}
			}
		}, url, new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
			}
			
		}, map, RESOURECE_REQUEST);
	}
}
