package com.jiqu.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.umeng.analytics.MobclickAgent;
import com.vr.store.R;
import com.jiqu.tools.Constants;
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
	private String[] titles;
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
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("ThematicActivity");
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("ThematicActivity");
		MobclickAgent.onPause(this);
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
					if (thematicInfo.getData1() != null) {
						setPagerData(thematicInfo.getData1());
					}
					if (thematicInfo.getData2() != null) {
						thematicInfos.add(thematicInfo);
						adapter.notifyDataSetChanged();
					}
				}
					
			}
		}, url, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				
			}
		}, requestTool.getMap(), THEMATIC_REQUEST);
	}
	
	private void setPagerData(final ThematicItem[] items){
		int length = items.length;
		topImgs = new ImageView[length];
		imageViews = new ImageView[length];
		titles = new String[length];
		for(int i = 0;i < length;i++){
			final ThematicItem thematicItem = items[i];
			ImageView view = new ImageView(this);
			LayoutParams lp = new LayoutParams((int)(20 * Rx), (int) (20 * Rx));
			if (i != 0) {
				lp.leftMargin = (int) (10 * Rx);
			}
			view.setLayoutParams(lp);
			if (i== 0) {
				view.setBackgroundResource(R.drawable.dian_blue);
				thematicTitle.setText(thematicItem.getTitle());
			}else {
				view.setBackgroundResource(R.drawable.dian_white);
			}
			titles[i] = thematicItem.getTitle();
			imageViews[i] = view;
			radioGroup.addView(view);
			
			ImageView img = new ImageView(this);
			img.setClickable(true);
			img.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(ThematicActivity.this, SortInfoActivity.class)
					.putExtra("thematicItem", thematicItem)
					.putExtra("fromWhere", 1));
				}
			});
			img.setScaleType(ScaleType.FIT_XY);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			img.setLayoutParams(params);
			ImageListener listener = ImageLoader.getImageListener(img, R.drawable.recommend_viewpager_default, R.drawable.recommend_viewpager_default);
			StoreApplication.getInstance().getImageLoader().get(thematicItem.getPic(), listener);
			topImgs[i] = img;
		}
		ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, topImgs);
		viewPager.setAdapter(viewPagerAdapter);
		
		timer = new CountDownTimer(Integer.MAX_VALUE,5000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				viewPager.setCurrentItem(currentIndex % topImgs.length);
				currentIndex++;
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				
			}
		};
		timer.start();
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
		
		viewPager.setOnPageChangeListener(this);
	}
	
	private void initViewSize(){
		UIUtil.setViewHeight(parent, 460 * Ry);
		UIUtil.setViewSize(titleRel, MetricsTool.width, 85 * Ry);
		UIUtil.setViewHeight(viewPager, 460 * Ry);
		
		UIUtil.setTextSize(thematicTitle, 35);
		thematicListView.setDividerHeight((int)(40 * Ry));
		
		try {
			UIUtil.setViewSizeMargin(thematicTitle, 40 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(radioGroup, 0, 0, 40 * Rx, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onErrorResponse(VolleyError arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResponse(JSONObject arg0) {
		// TODO Auto-generated method stub
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
				thematicTitle.setText(titles[i]);
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
	
	@Override
	public void removeFromActivityList() {
		// TODO Auto-generated method stub
		Constants.ACTIVITY_LIST.remove(this);
	}

	@Override
	public void addToActivityList() {
		// TODO Auto-generated method stub
		Constants.ACTIVITY_LIST.add(this);
	}
}
