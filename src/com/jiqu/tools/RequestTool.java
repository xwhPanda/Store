package com.jiqu.tools;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jiqu.application.StoreApplication;

public class RequestTool {
	private static RequestTool instance;
	private static final String url = "http://xu8api.91xuxu.com/api/1.0/getHomeRecommend";
	private static final String topUrl = "http://xu8api.91xuxu.com/api/1.0/getTopRecommend";
	private static final String recommedAppsUrl = "http://xu8api.91xuxu.com/api/1.0/recommendApps";
	private static final String detaileUrl = "http://xu8api.91xuxu.com/api/1.0/getProductDetail";
	
	private Map<String, String> paramMap = new HashMap<String, String>();

	public static RequestTool getInstance(){
		if (instance == null) {
			instance = new RequestTool();
		}
		return instance;
	}

	public void initParam(){
		paramMap.clear();
		paramMap.put("android_id", "a9f7234301030848");
		paramMap.put("imei", "000000000000000");
		paramMap.put("versionCode", "127");
		paramMap.put("versionName", "0.1.27");
		paramMap.put("device_id", "000000000000000");
		paramMap.put("mac", "08:00:27:d3:53:ef");
	}
	
	public void setParam(String key,String value){
		paramMap.put(key, value);
	}
	
	public Map<String, String> getMap(){
		return paramMap;
	}
	
	/**
	 * 推荐页游戏列表
	 * @param listener
	 * @param errorListener
	 * @param retry 是否重试
	 */
	public void startHomeRecommendRequest(Listener<JSONObject> listener, ErrorListener errorListener,boolean retry){
		JSONObject object = new JSONObject(paramMap);
		JsonObjectRequest objectRequest = new JsonObjectRequest(Method.POST, url, object, listener, errorListener);
		if (retry) {
			objectRequest.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 2, 2));
		}
		StoreApplication.getInstance().addToRequestQueue(objectRequest, "homeRecommend");
		StoreApplication.getInstance().getRequestQueue().start();
	}
	
	/**
	 * 推荐页顶部轮换图片
	 * @param listener
	 * @param errorListener
	 */
	public void startTopRecommendRequest(Listener<JSONObject> listener,ErrorListener errorListener){
		JSONObject object = new JSONObject(paramMap);
		JsonObjectRequest objectRequest = new JsonObjectRequest(Method.POST, topUrl, object, listener, errorListener);
		StoreApplication.getInstance().addToRequestQueue(objectRequest, "topRecommend");
		StoreApplication.getInstance().getRequestQueue().start();
	}
	
	/**
	 * 推荐页推荐游戏
	 * @param listener
	 * @param errorListener
	 */
	public void startRecommendAppsRequest(Listener<JSONObject> listener,ErrorListener errorListener){
		JSONObject object = new JSONObject(paramMap);
		JsonObjectRequest objectRequest = new JsonObjectRequest(Method.POST, recommedAppsUrl, object, listener, errorListener);
		StoreApplication.getInstance().addToRequestQueue(objectRequest, "recommendApps");
		StoreApplication.getInstance().getRequestQueue().start();
	}
	
	/**
	 * 游戏详情
	 * @param listener
	 * @param errorListener
	 */
	public void startGameDetailRequest(Listener<JSONObject> listener,ErrorListener errorListener){
		JSONObject object = new JSONObject(paramMap);
		JsonObjectRequest objectRequest = new JsonObjectRequest(Method.POST, detaileUrl, object, listener, errorListener);
		StoreApplication.getInstance().addToRequestQueue(objectRequest, "gameDetail");
		StoreApplication.getInstance().getRequestQueue().start();
	}
}
