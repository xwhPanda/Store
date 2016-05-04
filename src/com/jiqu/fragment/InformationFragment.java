package com.jiqu.fragment;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.jiqu.activity.HeadlineActivity;
import com.jiqu.adapter.InformationAdapter;
import com.jiqu.adapter.ViewPagerAdapter;
import com.jiqu.application.StoreApplication;
import com.jiqu.object.GameInformation;
import com.jiqu.object.InformationGallaryItem;
import com.jiqu.object.InformationInfo;
import com.jiqu.object.InformationResult;
import com.jiqu.store.R;
import com.jiqu.tools.CountDownTimer;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.MyImageView;
import com.jiqu.view.PullToRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.style.LeadingMarginSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class InformationFragment extends Fragment implements OnClickListener,OnPageChangeListener{
	private float Rx,Ry;
	private View view;
	private LinearLayout explosiveHeadlinesLin,allHeadlinesLin;
	private ViewPager informationImgViewPager;
	private RelativeLayout informationImgRel;
	private LinearLayout imgList;
	private LinearLayout headlineLin;
	private ImageView explosiveHeadlinesImg,allHeadlinesImg;
	private TextView explosiveHeadlinesTx,allHeadlinesTx;
	
	private ListView informationListView;
	private PullToRefreshLayout ptrl;
	//头部view
	private View headView;
	
	private List<InformationGallaryItem> informationGallaryItems = new ArrayList<InformationGallaryItem>();
	private ImageView[] contentViews;
	private ImageView[] radioViews;
	private InformationAdapter adapter;
	private RequestTool requestTool;
	private int currentIndex = 0;
	private CountDownTimer timer;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.information, container, false);
		headView = inflater.inflate(R.layout.information_head, null);
		
		requestTool = RequestTool.getInstance();
		
		initView();
		loadData(RequestTool.informationUrl + "1.html");
		
		return view;
	}

	private void loadData(String url){
		requestTool.getMap().clear();
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				InformationInfo info = JSON.parseObject(arg0, InformationInfo.class);
				
				setData(info.getResult().getData());
				setTopData(info.getResult().getGallary());
			}
		}, url, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				Log.i("TAG", arg0.toString());
			}
		}, requestTool.getMap(), "infortmation");
	}
	
	private void setTopData(InformationGallaryItem[] items){
		if (items != null && items.length > 0) {
			final int length = items.length;
			contentViews = new ImageView[length];
			radioViews = new ImageView[length];
			for(int i = 0;i < length;i++){
				ImageView content = new ImageView(getActivity());
				content.setScaleType(ScaleType.FIT_XY);
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				content.setLayoutParams(lp);
				ImageListener listener = ImageLoader.getImageListener(content, R.drawable.ic_launcher, R.drawable.ic_launcher);
				StoreApplication.getInstance().getImageLoader().get(items[i].getGallaryImg(), listener);
				contentViews[i] = content;
				
				ImageView img = new ImageView(getActivity());
				LayoutParams params = new LayoutParams((int)(20 * Rx), (int) (20 * Rx));
				if (i != 0) {
					params.leftMargin = (int) (10 * Rx);
				}
					img.setLayoutParams(params);
				if (i== 0) {
					img.setBackgroundResource(R.drawable.dian_blue);
				}else {
					img.setBackgroundResource(R.drawable.dian_white);
				}
				radioViews[i] = img;
				imgList.addView(img);
			}
			
			ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity(), contentViews);
			informationImgViewPager.setAdapter(adapter);
			
			timer = new CountDownTimer(Integer.MAX_VALUE, 5000) {
				
				@Override
				public void onTick(long millisUntilFinished) {
					// TODO Auto-generated method stub
					if (informationImgViewPager != null && length != 0) {
						informationImgViewPager.setCurrentItem(currentIndex % length);
						currentIndex++;
					}
				}
				
				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					
				}
			};
			timer.start();
		}
	}
	
	private void setData(InformationGallaryItem[] items){
		if (items != null && items.length > 0) {
			int length = items.length;
			for (int i = 0; i < length; i++) {
				informationGallaryItems.add(items[i]);
			}
			adapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.explosiveHeadlinesLin:
			explosiveHeadlinesLin.setBackgroundColor(getResources().getColor(R.color.blue));
			allHeadlinesLin.setBackgroundColor(getResources().getColor(R.color.itemDesColor));
			break;

		case R.id.allHeadlinesLin:
			explosiveHeadlinesLin.setBackgroundColor(getResources().getColor(R.color.itemDesColor));
			allHeadlinesLin.setBackgroundColor(getResources().getColor(R.color.blue));
			break;
		}
	}
	
	private void setOnClick(){
		explosiveHeadlinesLin.setOnClickListener(this);
		allHeadlinesLin.setOnClickListener(this);
	}
	
	private void initView (){
		Rx = MetricsTool.Rx;
		Ry = MetricsTool.Ry;
		informationListView = (ListView) view.findViewById(R.id.informationListView);
		ptrl = ((PullToRefreshLayout) view.findViewById(R.id.refresh_view));
		
		initHeadView();
		setOnClick();
		
		//必须在设置adapter之前执行
		informationListView.addHeaderView(headView);
		
		
		adapter = new InformationAdapter(getActivity(), informationGallaryItems,0);
		informationListView.setAdapter(adapter);
		
		initViewSize();
		
		informationListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), HeadlineActivity.class)
				.putExtra("data", informationGallaryItems.get(position - 1)));
			}
		});
	}
	
	private void initHeadView(){
		informationImgViewPager = (ViewPager) headView.findViewById(R.id.informationImgViewPager);
		explosiveHeadlinesLin = (LinearLayout) headView.findViewById(R.id.explosiveHeadlinesLin);
		allHeadlinesLin = (LinearLayout) headView.findViewById(R.id.allHeadlinesLin);
		informationImgRel = (RelativeLayout) headView.findViewById(R.id.informationImgRel);
		headlineLin = (LinearLayout) headView.findViewById(R.id.headlineLin);
		explosiveHeadlinesImg = (ImageView) headView.findViewById(R.id.explosiveHeadlinesImg);
		allHeadlinesImg = (ImageView) headView.findViewById(R.id.allHeadlinesImg);
		explosiveHeadlinesTx = (TextView) headView.findViewById(R.id.explosiveHeadlinesTx);
		allHeadlinesTx = (TextView) headView.findViewById(R.id.allHeadlinesTx);
		imgList = (LinearLayout) headView.findViewById(R.id.imgList);
		
		explosiveHeadlinesLin.setBackgroundColor(getResources().getColor(R.color.blue));
		informationImgViewPager.setOnPageChangeListener(this);
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(informationImgRel, MetricsTool.width, 455 * Ry);
		UIUtil.setViewSize(headlineLin, MetricsTool.width, 75 * Ry);
		UIUtil.setViewSize(explosiveHeadlinesImg, 56 * Rx, 56 * Rx);
		UIUtil.setViewSize(allHeadlinesImg, 56 * Rx, 56 * Rx);
		
		UIUtil.setTextSize(explosiveHeadlinesTx, 40);
		UIUtil.setTextSize(allHeadlinesTx, 40);
		
		try {
			UIUtil.setViewSizeMargin(headlineLin, 0, 20 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(imgList, 0, 0, 0, 30 * Ry);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		for(int i = 0;i<radioViews.length;i++){
			if (arg0 == i) {
				radioViews[i].setBackgroundResource(R.drawable.dian_blue);
			}else {
				radioViews[i].setBackgroundResource(R.drawable.dian_white);
			}
		}
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		timer.cancel();
	}
}
