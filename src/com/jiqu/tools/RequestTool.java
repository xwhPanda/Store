package com.jiqu.tools;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.R.integer;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.jiqu.application.StoreApplication;

public class RequestTool {
	private static RequestTool instance;
	
	public static final String PRIKEY = "*7&SKJuas";
	/** 推荐页数据URL **/
	public static final String RECOMMEND_URL = "http://ht.163zs.com/index.php/Api/Recommend/getRecommend";
	/**活跃统计URL**/
	public static final String ACTIVE_URL = "http://ht.163zs.com/index.php/Api/Channel/active";
	/**注册URL**/
	public static final String REGISTER_URL = "http://ht.163zs.com/index.php/Api/User/register";
	/**登录URL**/
	public static final String LOGIN_URL = "http://ht.163zs.com/index.php/Api/User/login";
	/**修改用户信息URL**/
	public static final String MODIFY_URL = "http://ht.163zs.com/index.php/Api/User/modifyInfo";
	/**获取验证码URL**/
	public static final String GET_RESET_CODE_URL = "http://ht.163zs.com/index.php/Api/User/getResetCode";
	/**重置密码URL**/
	public static final String RESETPASSWORD_URL = "http://ht.163zs.com/index.php/Api/User/resetPasswd";
	/**最新游戏URL**/
	public static final String LATEST_GAME_URL = "http://ht.163zs.com/index.php/Api/Game/latestGame";
	/**最热游戏URL**/
	public static final String HOT_GAME_URL = "http://ht.163zs.com/index.php/Api/Game/hotGame";
	/**安装统计URL（第一次使用）**/
	public static final String INSTALL_URL = "http://ht.163zs.com/index.php/Api/Channel/install";
	/** 精品URL **/
	public static final String BOUTIQUE_URL = "http://ht.163zs.com/index.php/Api/Recommend/getChoice";
	/** 专题URL**/
	public static final String SPECIAL_URL = "http://ht.163zs.com/index.php/Api/Recommend/getTopics";
	/** 专题详情URL **/
	public static final String SPECIAL_DETAIL_URL = "http://ht.163zs.com/index.php/Api/Recommend/getTopicsDetail";
	/** 好评榜URL**/
	public static final String PRAISE_URL = "http://ht.163zs.com/index.php/Api/Recommend/getPraiseGold";
	/** 最热榜 URL**/
	public static final String HOT_URL = "http://ht.163zs.com/index.php/Api/Recommend/getHotGold";
	/** 分类URL **/
	public static final String CATEGORY_URL = "http://ht.163zs.com/index.php/Api/Recommend/getClassify";
	/** 游戏详情URL **/
	public static final String GAME_DETAIL_URL = "http://ht.163zs.com/index.php/Api/InfoDetail/getGame";
	/** 分类详情URL **/
	public static final String CATEGORY_APPS_URL = "http://ht.163zs.com/index.php/Api/recommend/getGameClass";
	/** 新游榜URL **/
	public static final String NEW_GAME_LIST_URL = "http://ht.163zs.com/index.php/Api/Recommend/newGameList";
	/** 热游榜URL **/
	public static final String POPULAR_GAME_LIST_URL = "http://ht.163zs.com/index.php/Api/Recommend/popularGameList";
	/** 必玩榜URL **/
	public static final String WILL_GAME_LIST_URL = "http://ht.163zs.com/index.php/Api/Recommend/willGameScroll";
	/** 游戏推广URL **/
	public static final String SPREAD_URL = "http://ht.163zs.com/index.php/Api/Game/getSpread";
	/** 资讯页顶部URL **/
	public static final String HEADLINE_TOP_URL = "http://ht.163zs.com/index.php/Api/News/getSpread";
	/** 资讯头条URL **/
	public static final String HOT_HEADLINE_URL = "http://ht.163zs.com/index.php/Api/News/headlinesNews";
	/** 全部资讯URL **/
	public static final String ALL_HEADLINE_URL = "http://ht.163zs.com/index.php/Api/News/getAllNews";
	/** 评测URL **/
	public static final String EVALUATION_URL = "http://ht.163zs.com/index.php/Api/Reviews/getReviewsList";
	/** 搜索首页URL **/
	public static final String SEARCHE_INDEX_URL = "http://ht.163zs.com/index.php/Api/Search/index";
	/** 搜索URL **/
	public static final String SEARCH_URL = "http://ht.163zs.com/index.php/Api/Search/getGameList";
	
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
	 * 开始请求，默认不重试
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
			stringGetRequest(method, listener, url, errorListener, map, false,tag);
		}
	}
	
	/**
	 * 开始请求,设置重试
	 * 
	 * @param listener
	 * @param url
	 * @param errorListener
	 * @param map
	 * @param tag
	 */
	public void startStringRequest(int method, Listener<String> listener, String url, ErrorListener errorListener, final Map<String, Object> map, boolean retry,String tag) {
		if (method == Method.POST) {
			stringPostRequest(method, listener, url, errorListener, map, tag);
		} else if (method == Method.GET) {
			stringGetRequest(method, listener, url, errorListener, map, retry,tag);
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
					paramMap.put(entry, String.valueOf(map.get(entry)) );
				}
				return paramMap;
			}
		};
		StoreApplication.getInstance().addToRequestQueue(stringRequest, tag);
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
	private void stringGetRequest(int method, Listener<String> listener, String url, ErrorListener errorListener, final Map<String, Object> map, boolean retry,String tag) {
		StringRequest stringRequest = new StringRequest(method, url, listener, errorListener) {
		};
		if (retry) {
			stringRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 2, 1.0f));
		}
		StoreApplication.getInstance().addToRequestQueue(stringRequest, tag);
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
