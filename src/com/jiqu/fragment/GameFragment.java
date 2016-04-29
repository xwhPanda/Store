package com.jiqu.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.jiqu.activity.RankingActivity;
import com.jiqu.adapter.GameAdapter;
import com.jiqu.adapter.InformationAdapter;
import com.jiqu.database.DownloadAppinfo;
import com.jiqu.download.DownloadManager;
import com.jiqu.object.GameInfo;
import com.jiqu.object.GameInformation;
import com.jiqu.object.InstalledApp;
import com.jiqu.object.RankInfo;
import com.jiqu.store.R;
import com.jiqu.tools.InstalledAppTool;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.PullToRefreshLayout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameFragment extends Fragment implements OnClickListener{
	private static final int DEFAULT_PAGE_SIZE = 20;
	private ListView gameListView;
	private View view,headView;
	private ViewPager informationImgViewPager;
	private RelativeLayout informationImgRel;
	private LinearLayout imgList;
	private LinearLayout explosiveHeadlinesLin;
	private LinearLayout allHeadlinesLin;
	private LinearLayout headlineLin;
	private PullToRefreshLayout ptrl;
	private ImageView explosiveHeadlinesImg,allHeadlinesImg;
	private TextView explosiveHeadlinesTx,allHeadlinesTx;
	
	private List<GameInfo> newGameList = new ArrayList<GameInfo>();
	private List<GameInfo> hotGameList = new ArrayList<GameInfo>();
	private GameAdapter newGameAdapter;
	
	private RequestTool requestTool;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.game, null);
		headView = inflater.inflate(R.layout.information_head, null);
		requestTool = RequestTool.getInstance();
		initView();
		
		loadNew(0,DEFAULT_PAGE_SIZE);
		loadHot(0,DEFAULT_PAGE_SIZE);
		return view;
	}
	
	private void loadNew(int start,int end){
		requestTool.initParam();
		requestTool.setParam("start_position", start);
		requestTool.setParam("size", end);
		requestTool.setParam("orderby", "3");
		requestTool.startRankRequest(new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject arg0) {
				Log.i("TAG", arg0.toString());
				// TODO Auto-generated method stub
				RankInfo rankInfo = JSON.parseObject(arg0.toString(), RankInfo.class);
				if (rankInfo != null) {
					Collections.addAll(newGameList, rankInfo.getItem());
					int count = DEFAULT_PAGE_SIZE;
					if (newGameList.size() < DEFAULT_PAGE_SIZE) {
						count = newGameList.size();
					}
					List<InstalledApp> apps = InstalledAppTool.getPersonalApp(getActivity());
					for(int i = newGameList.size() - count;i<newGameList.size();i++){
						GameInfo gameInfo = newGameList.get(i);
						DownloadAppinfo info = DownloadManager.getInstance().getDownloadInfo(Long.parseLong(gameInfo.getP_id()));
						gameInfo.setAdapterType(1);
						int state = InstalledAppTool.contain(apps,gameInfo.getPackagename(), Integer.parseInt(gameInfo.getVersion_code()));
						if (state != -1) {
							newGameList.get(i).setState(state);
						}else {
							if (info != null 
									&& (info.getDownloadState() == DownloadManager.STATE_INSTALLED
									|| info.getDownloadState() == DownloadManager.STATE_NEED_UPDATE)) {
								DownloadManager.DBManager.delete(info);
							}
						}
					}
//					if (favorableRefreshViewShowing) {
//						favorableRefreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
//						favorableRefreshViewShowing = false;
//					}
					newGameAdapter.notifyDataSetChanged();
				}
			}
		}, new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				Log.i("TAG", arg0.toString());
			}});
	}
	
	private void loadHot(int start,int end){
		requestTool.initParam();
		requestTool.setParam("start_position", start);
		requestTool.setParam("size", end);
		requestTool.setParam("orderby", "4");
		requestTool.startRankRequest(new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject arg0) {
				// TODO Auto-generated method stub
				RankInfo rankInfo = JSON.parseObject(arg0.toString(), RankInfo.class);
				if (rankInfo != null) {
					Collections.addAll(hotGameList, rankInfo.getItem());
					int count = DEFAULT_PAGE_SIZE;
					if (hotGameList.size() < DEFAULT_PAGE_SIZE) {
						count = hotGameList.size();
					}
					List<InstalledApp> apps = InstalledAppTool.getPersonalApp(getActivity());
					for(int i = hotGameList.size() - count;i<hotGameList.size();i++){
						GameInfo gameInfo = hotGameList.get(i);
						DownloadAppinfo info = DownloadManager.getInstance().getDownloadInfo(Long.parseLong(gameInfo.getP_id()));
						gameInfo.setAdapterType(1);
						int state = InstalledAppTool.contain(apps,gameInfo.getPackagename(), Integer.parseInt(gameInfo.getVersion_code()));
						if (state != -1) {
							hotGameList.get(i).setState(state);
						}else {
							if (info != null 
									&& (info.getDownloadState() == DownloadManager.STATE_INSTALLED
									|| info.getDownloadState() == DownloadManager.STATE_NEED_UPDATE)) {
								DownloadManager.DBManager.delete(info);
							}
						}
					}
				}
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	private void initView(){
		gameListView = (ListView) view.findViewById(R.id.gameListView);
		informationImgRel = (RelativeLayout) headView.findViewById(R.id.informationImgRel);
		informationImgViewPager = (ViewPager) headView.findViewById(R.id.informationImgViewPager);
		imgList = (LinearLayout) headView.findViewById(R.id.imgList);
		explosiveHeadlinesLin = (LinearLayout) headView.findViewById(R.id.explosiveHeadlinesLin);
		allHeadlinesLin = (LinearLayout) headView.findViewById(R.id.allHeadlinesLin);
		headlineLin = (LinearLayout) headView.findViewById(R.id.headlineLin);
		explosiveHeadlinesImg = (ImageView) headView.findViewById(R.id.explosiveHeadlinesImg);
		allHeadlinesImg = (ImageView) headView.findViewById(R.id.allHeadlinesImg);
		explosiveHeadlinesTx = (TextView) headView.findViewById(R.id.explosiveHeadlinesTx);
		allHeadlinesTx = (TextView) headView.findViewById(R.id.allHeadlinesTx);
		
		allHeadlinesImg.setBackgroundResource(R.drawable.zuire);
		explosiveHeadlinesImg.setBackgroundResource(R.drawable.zuibao);
		
		gameListView.addHeaderView(headView);
		
		
		explosiveHeadlinesLin.setOnClickListener(this);
		allHeadlinesLin.setOnClickListener(this);
		
		List<GameInfo> gameInformations = new ArrayList<GameInfo>();
		
//		for (int i = 0; i < 30; i++)
//		{
//			GameInfo game = new GameInfo();
//			game.setP_id("0");
//			game.setGrade_difficulty("1");
//			game.setGrade_frames("1");
//			game.setGrade_gameplay("1");
//			game.setGrade_immersive("1");
//			game.setGrade_vertigo("1");
//			game.setUrl("sdfsfs.apk");
//			game.setApp_size("10");
//			game.setLdpi_icon_url("fsfsfsfs");
//			
//			if (i % 4 == 1) {
//				game.setAdapterType(0);
//			}else {
//				game.setAdapterType(1);
//			}
//			gameInformations.add(game);
//		}
//		GameAdapter adapter = new GameAdapter(getActivity(), gameInformations,false,false);
		newGameAdapter = new GameAdapter(getActivity(), newGameList,false,false);
		ptrl = ((PullToRefreshLayout) view.findViewById(R.id.refresh_view));
		gameListView.setAdapter(newGameAdapter);
		
		UIUtil.setViewSize(informationImgRel, MetricsTool.width, 455 * MetricsTool.Ry);
		UIUtil.setViewSize(headlineLin, MetricsTool.width, 75 * MetricsTool.Ry);
		UIUtil.setViewSize(explosiveHeadlinesImg, 56 * MetricsTool.Rx, 56 * MetricsTool.Rx);
		UIUtil.setViewSize(allHeadlinesImg, 56 * MetricsTool.Rx, 56 * MetricsTool.Rx);
		
		UIUtil.setTextSize(explosiveHeadlinesTx, 40);
		UIUtil.setTextSize(allHeadlinesTx, 40);
		
		try {
			UIUtil.setViewSizeMargin(headlineLin, 0, 20 * MetricsTool.Ry, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.explosiveHeadlinesLin) {
			Log.i("TAG", newGameList.size() + "");
			explosiveHeadlinesLin.setBackgroundColor(getResources().getColor(R.color.sortTitleColor));
			allHeadlinesLin.setBackgroundColor(getResources().getColor(R.color.itemDesColor));
			newGameAdapter.setList(newGameList);
			newGameAdapter.notifyDataSetChanged();
		}else if (v.getId() == R.id.allHeadlinesLin) {
			explosiveHeadlinesLin.setBackgroundColor(getResources().getColor(R.color.itemDesColor));
			allHeadlinesLin.setBackgroundColor(getResources().getColor(R.color.sortTitleColor));
			newGameAdapter.setList(hotGameList);
			newGameAdapter.notifyDataSetChanged();
		}
	}
	
}
