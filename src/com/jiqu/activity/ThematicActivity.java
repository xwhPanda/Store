package com.jiqu.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.jiqu.adapter.ThematicAdapter;
import com.jiqu.adapter.ThematicItemAdapter;
import com.jiqu.object.ThematicInfo;
import com.jiqu.object.ThematicItemInfo;
import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.HeaderGridView;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.PullableGridView;
import com.jiqu.view.PullableListView;
import com.jiqu.view.TitleView;

public class ThematicActivity extends BaseActivity implements Listener<JSONObject> ,ErrorListener{
	private TitleView titleView;
	private PullToRefreshLayout refreshView;
	private ListView thematicListView;
	
	private View headView;
	private ViewPager viewPager;
	private RelativeLayout groupRel;
	private TextView title;
	private LinearLayout viewGroup;
	
	private List<ThematicItemInfo> thematicInfos = new ArrayList<ThematicItemInfo>();
	private ThematicAdapter adapter;
	
	private ImageView[] imageViews;
	private RequestTool requestTool;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestTool = RequestTool.getInstance();
		initView();
		
		requestTool.initParam();
		requestTool.startSpecialsRequest(this, this);
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
		
		thematicListView.addHeaderView(headView);
		
		titleView.tip.setText(getResources().getString(R.string.thematic));
		titleView.setActivity(this);
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.editBtn.setBackgroundResource(R.drawable.fenxiang_white);
		titleView.editBtn.setVisibility(View.VISIBLE);
		titleView.parentView.setBackgroundColor(getResources().getColor(R.color.bottomBgColor));
		
		for(int i = 0 ; i < 20 ; i++){
			List<ThematicInfo> infos = new ArrayList<ThematicInfo>();
			int count = (int) (Math.random() * 5 +1);
			for(int j = 0 ;j < count;j++){
				ThematicInfo thematicInfo = new ThematicInfo();
				infos.add(thematicInfo);
			}
			ThematicItemInfo thematicItemInfo = new ThematicItemInfo();
			thematicItemInfo.setThematicInfos(infos);
			thematicItemInfo.setTitle("今日热门");
			
			thematicInfos.add(thematicItemInfo);
		}
		adapter = new ThematicAdapter(this, thematicInfos);
		thematicListView.setAdapter(adapter);
		
		initViewSize();
		
		initData();
	}
	
	private void initHeadView(){
		LayoutInflater inflater = LayoutInflater.from(this);
		headView = inflater.inflate(R.layout.thematic_head_layout, null);
		
		viewPager = (ViewPager) headView.findViewById(R.id.viewPager);
		groupRel = (RelativeLayout) headView.findViewById(R.id.groupRel);
		title = (TextView) headView.findViewById(R.id.title);
		viewGroup = (LinearLayout) headView.findViewById(R.id.viewGroup);
	}
	
	private void initData(){
		imageViews = new ImageView[4];
		for(int i = 0; i < 4; i++){
			ImageView img = new ImageView(this);
			LayoutParams lp = new LayoutParams((int)(22 * Rx), (int) (22 * Rx));
			img.setLayoutParams(lp);
			img.setBackgroundResource(R.drawable.dian_white);
			imageViews[i] = img;
			viewGroup.addView(img);
			
		}
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(groupRel, MetricsTool.width, 85 * Ry);
		UIUtil.setViewHeight(viewPager, 460 * Ry);
		
		
		UIUtil.setTextSize(title, 35);
		
		thematicListView.setDividerHeight((int)(40 * Ry));
		
		try {
			UIUtil.setViewSizeMargin(title, 40 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(viewGroup, 0, 0, 40 * Rx, 0);
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
		Log.i("TAG", arg0.toString());
	}
}
