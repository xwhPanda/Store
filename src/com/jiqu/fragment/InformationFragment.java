package com.jiqu.fragment;

import java.util.ArrayList;
import java.util.List;

import com.jiqu.activity.HeadlineActivity;
import com.jiqu.adapter.InformationAdapter;
import com.jiqu.object.GameInformation;
import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.MyImageView;
import com.jiqu.view.PullToRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class InformationFragment extends Fragment implements OnClickListener{
	private float Rx,Ry;
	private View view;
	private LinearLayout explosiveHeadlinesLin,allHeadlinesLin;
	private ViewPager informationImgViewPager;
	private RelativeLayout informationImgRel;
	private LinearLayout headlineLin;
	private ImageView explosiveHeadlinesImg,allHeadlinesImg;
	private TextView explosiveHeadlinesTx,allHeadlinesTx;
	private List<MyImageView> viewList = new ArrayList<MyImageView>();
	
	private ListView informationListView;
	private PullToRefreshLayout ptrl;
	//头部view
	private View headView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.information, container, false);
		headView = inflater.inflate(R.layout.information_head, null);
		
		initView();
		return view;
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
		
		
		initHeadView();
		setOnClick();
		initViewPager();
		
		
		//必须在设置adapter之前执行
		informationListView.addHeaderView(headView);
		
		
		List<GameInformation> gameInformations = new ArrayList<GameInformation>();
		
		for (int i = 0; i < 30; i++)
		{
			GameInformation game = new GameInformation();
			gameInformations.add(game);
		}
		InformationAdapter adapter = new InformationAdapter(getActivity(), gameInformations,0);
		ptrl = ((PullToRefreshLayout) view.findViewById(R.id.refresh_view));
		informationListView.setAdapter(adapter);
		
		initViewSize();
		
		informationListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), HeadlineActivity.class));
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
		
		explosiveHeadlinesLin.setBackgroundColor(getResources().getColor(R.color.blue));
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化viewpager数据
	 */
	private void initViewPager(){
		for(int i = 0;i < 3; i++){
			MyImageView view = new MyImageView(getContext());
			view.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_launcher));
			viewList.add(view);
		}
		
		informationImgViewPager.setAdapter(new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return viewList.size();
			}
			
			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				// TODO Auto-generated method stub
				//不能少，否则没数据
				container.addView(viewList.get(position), 0);
				return viewList.get(position);
			}
			
			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				// TODO Auto-generated method stub
				container.removeView(viewList.get(position));
			}
		});
		
		informationImgViewPager.setCurrentItem(0);
	}
}
