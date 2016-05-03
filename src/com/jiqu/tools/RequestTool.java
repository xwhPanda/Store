package com.jiqu.tools;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.R.integer;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.jiqu.application.StoreApplication;

public class RequestTool {
	private static RequestTool instance;
	private static final String url = "http://xu8api.91xuxu.com/api/1.0/getHomeRecommend";
	private static final String topUrl = "http://xu8api.91xuxu.com/api/1.0/getTopRecommend";
	private static final String recommedAppsUrl = "http://xu8api.91xuxu.com/api/1.0/recommendApps";
	private static final String detaileUrl = "http://xu8api.91xuxu.com/api/1.0/getProductDetail";
	private static final String searchUrl = "http://xu8api.91xuxu.com/api/1.0/getSearchList";
	private static final String rankUrl = "http://xu8api.91xuxu.com/api/1.0/getProducts";
	private static final String categoryUrl = "http://xu8api.91xuxu.com/api/1.0/getCategoryList";
	private static final String categoryAppsUrl = "http://xu8api.91xuxu.com/api/1.0/getCategoryApps";
	
	public static final String specialsUrl = "http://115.28.169.193:81/api/market/apps/app-subject/?page=1&page_size=18&essence=1&ordering=-top%2C-updated&token=f78543cc344a027ffaa27963533510c8&nonce=1462238714277";
	public static final String thematicListUrl = "http://xml.mumayi.com/v18/special/list.php?type=p&page=1";
	public static final String thematicUrl = "http://xml.mumayi.com/v18/special/list.php?type=app&page=1&id=";

	public static final String PRIKEY = "*7&SKJuas";
	public static final String REGISTER_URL = "http://ht.163zs.com/index.php/Api/User/register";
	public static final String LOGIN_URL = "http://ht.163zs.com/index.php/Api/User/login";
	public static final String MODIFY_URL = "http://ht.163zs.com/index.php/Api/User/modifyInfo";
	public static final String GET_RESET_CODE_URL = "http://ht.163zs.com/index.php/Api/User/getResetCode";
	public static final String RESETPASSWORD_URL = "http://ht.163zs.com/index.php/Api/User/resetPasswd";

	private Map<String, Object> paramMap = new HashMap<String, Object>();

	public static RequestTool getInstance() {
		if (instance == null) {
			instance = new RequestTool();
		}
		return instance;
	}

	public void initParam() {
		paramMap.clear();
		paramMap.put("android_id", "a9f7234301030848");
		paramMap.put("imei", "000000000000000");
		paramMap.put("versionCode", "127");
		paramMap.put("versionName", "0.1.27");
		paramMap.put("device_id", "000000000000000");
		paramMap.put("mac", "08:00:27:d3:53:ef");
	}

	public void setParam(String key, String value) {
		paramMap.put(key, value);
	}

	public void setParam(String key, int value) {
		paramMap.put(key, value);
	}

	public Map<String, Object> getMap() {
		return paramMap;
	}

	/**
	 * 推荐页游戏列表
	 * 
	 * @param listener
	 * @param errorListener
	 * @param retry
	 *            是否重试
	 */
	public void startHomeRecommendRequest(Listener<JSONObject> listener, ErrorListener errorListener, boolean retry) {
		JSONObject object = new JSONObject(paramMap);
		JsonObjectRequest objectRequest = new JsonObjectRequest(Method.POST, url, object, listener, errorListener);
		if (retry) {
			objectRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 2, 2));
		}
		StoreApplication.getInstance().addToRequestQueue(objectRequest, "homeRecommend");
		StoreApplication.getInstance().getRequestQueue().start();
	}

	/**
	 * 推荐页顶部轮换图片
	 * 
	 * @param listener
	 * @param errorListener
	 */
	public void startTopRecommendRequest(Listener<JSONObject> listener, ErrorListener errorListener) {
		JSONObject object = new JSONObject(paramMap);
		JsonObjectRequest objectRequest = new JsonObjectRequest(Method.POST, topUrl, object, listener, errorListener);
		StoreApplication.getInstance().addToRequestQueue(objectRequest, "topRecommend");
		StoreApplication.getInstance().getRequestQueue().start();
	}

	/**
	 * 推荐页推荐游戏
	 * 
	 * @param listener
	 * @param errorListener
	 */
	public void startRecommendAppsRequest(Listener<JSONObject> listener, ErrorListener errorListener) {
		JSONObject object = new JSONObject(paramMap);
		JsonObjectRequest objectRequest = new JsonObjectRequest(Method.POST, recommedAppsUrl, object, listener, errorListener);
		StoreApplication.getInstance().addToRequestQueue(objectRequest, "recommendApps");
		StoreApplication.getInstance().getRequestQueue().start();
	}

	/**
	 * 游戏详情
	 * 
	 * @param listener
	 * @param errorListener
	 */
	public void startGameDetailRequest(Listener<JSONObject> listener, ErrorListener errorListener) {
		JSONObject object = new JSONObject(paramMap);
		JsonObjectRequest objectRequest = new JsonObjectRequest(Method.POST, detaileUrl, object, listener, errorListener);
		StoreApplication.getInstance().addToRequestQueue(objectRequest, "gameDetail");
		StoreApplication.getInstance().getRequestQueue().start();
	}

	/**
	 * 取消详情页请求
	 */
	public void stopGameDetailRequest() {
		cancleRequest("gameDetail");
	}

	/**
	 * 搜索请求
	 * 
	 * @param listener
	 * @param errorListener
	 */
	public void startSearchRequest(Listener<JSONObject> listener, ErrorListener errorListener) {
		JSONObject object = new JSONObject(paramMap);
		JsonObjectRequest objectRequest = new JsonObjectRequest(Method.POST, searchUrl, object, listener, errorListener);
		StoreApplication.getInstance().addToRequestQueue(objectRequest, "search");
		StoreApplication.getInstance().getRequestQueue().start();
	}

	/**
	 * 取消搜索请求
	 */
	public void stopSearchRequest() {
		cancleRequest("search");
	}

	/**
	 * 排行请求
	 * 
	 * @param listener
	 * @param errorListener
	 */
	public void startRankRequest(Listener<JSONObject> listener, ErrorListener errorListener) {
		JSONObject object = new JSONObject(paramMap);
		JsonObjectRequest objectRequest = new JsonObjectRequest(Method.POST, rankUrl, object, listener, errorListener);
		StoreApplication.getInstance().addToRequestQueue(objectRequest, "rank");
		StoreApplication.getInstance().getRequestQueue().start();
	}

	/**
	 * 取消排行请求
	 */
	public void stopRankRequest() {
		cancleRequest("rank");
	}

	/**
	 * 分类请求
	 * 
	 * @param listener
	 * @param errorListener
	 */
	public void startCategoryRequest(Listener<JSONObject> listener, ErrorListener errorListener) {
		JSONObject object = new JSONObject(paramMap);
		JsonObjectRequest objectRequest = new JsonObjectRequest(Method.POST, categoryUrl, object, listener, errorListener);
		StoreApplication.getInstance().addToRequestQueue(objectRequest, "category");
		StoreApplication.getInstance().getRequestQueue().start();
	}

	/**
	 * 取消分类请求
	 */
	public void stopCategoryRequest() {
		cancleRequest("category");
	}

	/**
	 * 请求指定分类
	 * 
	 * @param listener
	 * @param errorListener
	 */
	public void startCategoryAppsRequest(Listener<JSONObject> listener, ErrorListener errorListener) {
		JSONObject object = new JSONObject(paramMap);
		JsonObjectRequest objectRequest = new JsonObjectRequest(Method.POST, categoryAppsUrl, object, listener, errorListener);
		StoreApplication.getInstance().addToRequestQueue(objectRequest, "categoryApps");
		StoreApplication.getInstance().getRequestQueue().start();
	}

	/**
	 * 取消分类内容请求
	 */
	public void stopCategoryAppsRequest() {
		cancleRequest("categoryApps");
	}

	/**
	 * 请求专题
	 * 
	 * @param listener
	 * @param errorListener
	 */
	public void startSpecialsRequest(Listener<JSONObject> listener, ErrorListener errorListener) {
		JsonObjectRequest objectRequest = new JsonObjectRequest(Method.GET, specialsUrl, listener, errorListener);
		StoreApplication.getInstance().addToRequestQueue(objectRequest, "specials");
		StoreApplication.getInstance().getRequestQueue().start();
	}

	/**
	 * 取消专题请求
	 */
	public void stopSpecialsRequest() {
		cancleRequest("specials");
	}

	/**
	 * 开始请求
	 * 
	 * @param listener
	 * @param url
	 * @param errorListener
	 * @param map
	 * @param tag
	 */
	public void startStringRequest(int method, Listener<String> listener, String url, ErrorListener errorListener, final Map<String, Object> map, String tag) {
		if (method == Method.POST) {
			stringPostRequest(method, listener, url, errorListener, map, tag);
		} else if (method == Method.GET) {
			stringGetRequest(method, listener, url, errorListener, map, tag);
		}
	}

	/**
	 * 取消请求
	 * 
	 * @param tag
	 */
	public void stopRequest(String tag) {
		cancleRequest(tag);
	}

	/**
	 * StringRequest post请求
	 * 
	 * @param method
	 * @param listener
	 * @param url
	 * @param errorListener
	 * @param map
	 * @param tag
	 */
	private void stringPostRequest(int method, Listener<String> listener, String url, ErrorListener errorListener, final Map<String, Object> map, String tag) {
		StringRequest stringRequest = new StringRequest(method, url, listener, errorListener) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> paramMap = new HashMap<String, String>();
				for (String entry : map.keySet()) {
					paramMap.put(entry, (String) map.get(entry));
				}
				return paramMap;
			}
		};
		StoreApplication.getInstance().addToRequestQueue(stringRequest, tag);
		StoreApplication.getInstance().getRequestQueue().start();
	}

	/**
	 * StringRequest get请求
	 * 
	 * @param method
	 * @param listener
	 * @param url
	 * @param errorListener
	 * @param map
	 * @param tag
	 */
	private void stringGetRequest(int method, Listener<String> listener, String url, ErrorListener errorListener, final Map<String, Object> map, String tag) {
		StringRequest stringRequest = new StringRequest(method, url, listener, errorListener) {
		};
		StoreApplication.getInstance().addToRequestQueue(stringRequest, tag);
		StoreApplication.getInstance().getRequestQueue().start();
	}

	/**
	 * 根据tag取消请求
	 * 
	 * @param tag
	 */
	private void cancleRequest(String tag) {
		StoreApplication.getInstance().getRequestQueue().cancelAll(tag);
	}
}
