package com.jiqu.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.jiqu.adapter.ThematicAdapter;
import com.jiqu.adapter.ViewPagerAdapter;
import com.jiqu.application.StoreApplication;
import com.jiqu.object.ThematicInfo;
import com.jiqu.object.ThematicItem;
import com.jiqu.store.BaseActivity;
import com.vr.store.R;
import com.jiqu.tools.CountDownTimer;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.LoadStateView;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.PullableListView;
import com.jiqu.view.TitleView;
import com.jiqu.view.ViewPagerLinView;

public class ThematicActivity extends BaseActivity implements Listener<JSONObject> ,ErrorListener,OnPageChangeListener{
	private final String THEMATIC_REQUEST = "thematicRequest";
	private TitleView titleView;
	private PullToRefreshLayout refreshView;
	private ListView thematicListView;
	private LoadStateView loadView;
	
	private View headView;
	private RelativeLayout parent;
	private ViewPager viewPager;
	private RelativeLayout titleRel;
	private TextView thematicTitle;
	private LinearLayout radioGroup;
	
	private List<ThematicInfo> thematicInfos = new ArrayList<ThematicInfo>();
	private ThematicAdapter adapter;
	
	private ImageView[] imageViews;
	private ImageView[] topImgs;
	private RequestTool requestTool;
	
	private CountDownTimer timer;
	private int currentIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestTool = RequestTool.getInstance();
		initView();
		
		loadThematicData(RequestTool.SPECIAL_URL);
	}

	private void loadThematicData(String url){
		requestTool.getMap().clear();
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				loadView.loadDataSuccess();
				loadView.setVisibility(View.GONE);
				refreshView.setVisibility(View.VISIBLE);
				ThematicInfo thematicInfo = JSON.parseObject(arg0, ThematicInfo.class);
				if (thematicInfo != null) {
				}
			}
		}, url, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				
			}
		}, requestTool.getMap(), THEMATIC_REQUEST);
	}
	
	private void request(){
		requestTool.getMap().clear();
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				ThematicInfo thematicInfo = JSON.parseObject(arg0, ThematicInfo.class);
			}
		}, RequestTool.thematicListUrl, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				Log.i("TAG", arg0.toString());
			}
		}, requestTool.getMap(), "thematicList");
	}
	
	
	private void setData(ThematicInfo info){
		thematicInfos.add(info);
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.thematic_layout;
	}

	private void initView(){
		initHeadView();
		titleView = (TitleView) findViewById(R.id.titleView);
		refreshView = (PullToRefreshLayout) findViewById(R.id.refreshView);
		thematicListView = (PullableListView) findViewById(R.id.thematicListView);
		loadView = (LoadStateView) findViewById(R.id.loadView);
		
		thematicListView.addHeaderView(headView);
		
		titleView.tip.setText(getResources().getString(R.string.thematic));
		titleView.setActivity(this);
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.editBtn.setBackgroundResource(R.drawable.fenxiang_white);
		titleView.editBtn.setVisibility(View.VISIBLE);
		titleView.parentView.setBackgroundColor(getResources().getColor(R.color.bottomBgColor));
		
		adapter = new ThematicAdapter(this, thematicInfos);
		thematicListView.setAdapter(adapter);
		
		initViewSize();
		
	}
	
	private void initHeadView(){
		LayoutInflater inflater = LayoutInflater.from(this);
		headView = inflater.inflate(R.layout.thematic_head_layout, null);
		
		parent = (RelativeLayout) headView.findViewById(R.id.parent);
		viewPager = (ViewPager) headView.findViewById(R.id.viewPager);
		titleRel = (RelativeLayout) headView.findViewById(R.id.titleRel);
		thematicTitle = (TextView) headView.findViewById(R.id.thematicTitle);
		radioGroup = (LinearLayout) headView.findViewById(R.id.radioGroup);
	}
	
	private void initViewSize(){
		UIUtil.setViewHeight(parent, 460 * Ry);
		
		thematicListView.setDividerHeight((int)(40 * Ry));
	}

	@Override
	public void onErrorResponse(VolleyError arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResponse(JSONObject arg0) {
		// TODO Auto-generated method stub
		Log.i("TAG", arg0.toString());
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		for(int i = 0;i<imageViews.length;i++){
			if (arg0 == i) {
				imageViews[i].setBackgroundResource(R.drawable.dian_blue);
			}else {
				imageViews[i].setBackgroundResource(R.drawable.dian_white);
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
		}
	}
}
