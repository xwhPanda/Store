package com.jiqu.store;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jiqu.activity.DownloadManagerActivity;
import com.jiqu.activity.MemberLoginActivity;
import com.jiqu.activity.SearchActivity;
import com.jiqu.activity.ShowAccountInformatiomActivity;
import com.jiqu.application.StoreApplication;
import com.jiqu.database.Account;
import com.jiqu.download.AppInfo;
import com.jiqu.download.DownloadManager;
import com.jiqu.fragment.EvaluationFragment;
import com.jiqu.fragment.GameFragment;
import com.jiqu.fragment.InformationFragment;
import com.jiqu.fragment.RecommendFragment;
import com.jiqu.fragment.ToolFragment;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.NetReceiver;
import com.jiqu.tools.NetReceiver.OnNetChangeListener;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.CustomDialog;

import de.greenrobot.dao.query.QueryBuilder;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnClickListener,OnNetChangeListener{
	private float Rx,Ry;
	private Button accountImg;
	private EditText searchEd;
	private Button searchBtn;
	private Button download;
	private RelativeLayout searchRel;
	
	private LinearLayout top;
	private RelativeLayout toolTop;
	private LinearLayout bottomPanel;
	
	private TextView toolTopTx;
	private ImageView toolTopImg;
	
	private ImageView recommendImg,informationImg,gameImg,evaluationImg,toolImg;
	private TextView recommendTx,informationTx,gameTx,evaluationTx,toolTx;
	private LinearLayout recommendLin,informationLin,evaluationLin,gameLin,toolLin;
	
	private FragmentManager mFragmentManager;
	private RecommendFragment recommendFragment;
	private InformationFragment informationFragment;
	private GameFragment gameFragment;
	private EvaluationFragment evaluationFragment;
	private ToolFragment toolFragment;
	private List<Fragment> fragments = new ArrayList<Fragment>();
	private List<ImageView> imageViews = new ArrayList<ImageView>();
	private List<TextView> textViews = new ArrayList<TextView>();
	private int currentIndex = 0;
	
	private long mExitTime = 0;
	
	private NetReceiver netReceiver;
	
	private CustomDialog dialog;
	
	private boolean firstIn = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MetricsTool.initMetrics(getWindowManager());
		
		netReceiver = NetReceiver.getInstance();
		netReceiver.setNetChangeListener(this);
		netReceiver.registerReceive(StoreApplication.context);
		
		dialog = new CustomDialog(this)
				.setNegativeButton(new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				})
				.setPositiveButton(new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						DownloadManager.getInstance().startAll();
						dialog.dismiss();
					}
				}).initView();
		
		Rx = MetricsTool.Rx;
		Ry = MetricsTool.Ry;
		setContentView(R.layout.main);
		init();
		setOnclick();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		netReceiver.unregisterReceive(StoreApplication.context);
	}
	
	/**
	 * 初始化界面
	 */
	private void init(){
		top = (LinearLayout) findViewById(R.id.top);
		toolTop = (RelativeLayout) findViewById(R.id.toolTop);
		
		toolTopTx = (TextView) findViewById(R.id.toolTopTx);
		toolTopImg = (ImageView) findViewById(R.id.toolTopImg);
		
		bottomPanel = (LinearLayout) findViewById(R.id.bottomPanel);
		
		accountImg = (Button) findViewById(R.id.accountImg);
		searchEd = (EditText) findViewById(R.id.searchEd);
		searchBtn = (Button) findViewById(R.id.searchBtn);
		download = (Button) findViewById(R.id.download);
		searchRel = (RelativeLayout) findViewById(R.id.searchRel);
		
		recommendImg = (ImageView) findViewById(R.id.recommendImg);
		informationImg = (ImageView) findViewById(R.id.informationImg);
		gameImg = (ImageView) findViewById(R.id.gameImg);
		evaluationImg = (ImageView) findViewById(R.id.evaluationImg);
		toolImg = (ImageView) findViewById(R.id.toolImg);
		
		recommendTx = (TextView) findViewById(R.id.recommendTx);
		informationTx = (TextView) findViewById(R.id.informationTx);
		gameTx = (TextView) findViewById(R.id.gameTx);
		evaluationTx = (TextView) findViewById(R.id.evaluationTx);
		toolTx = (TextView) findViewById(R.id.toolTx);
		
		recommendLin = (LinearLayout) findViewById(R.id.recommendLin);
		informationLin = (LinearLayout) findViewById(R.id.informationLin);
		evaluationLin = (LinearLayout) findViewById(R.id.evaluationLin);
		gameLin = (LinearLayout) findViewById(R.id.gameLin);
		toolLin = (LinearLayout) findViewById(R.id.toolLin);
		
		
		imageViews.add(recommendImg);
		imageViews.add(informationImg);
		imageViews.add(gameImg);
		imageViews.add(evaluationImg);
		imageViews.add(toolImg);
		
		textViews.add(recommendTx);
		textViews.add(informationTx);
		textViews.add(gameTx);
		textViews.add(evaluationTx);
		textViews.add(toolTx);
		
		mFragmentManager = getSupportFragmentManager();
		
		recommendFragment = new RecommendFragment();
		informationFragment = new InformationFragment();
		gameFragment = new GameFragment();
		evaluationFragment = new EvaluationFragment();
		toolFragment = new ToolFragment();
		
		fragments.add(recommendFragment);
		fragments.add(informationFragment);
		fragments.add(gameFragment);
		fragments.add(evaluationFragment);
		fragments.add(toolFragment);
		
		FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
		mFragmentTransaction.add(R.id.homeFrameLayout, recommendFragment);
//		mFragmentTransaction.add(R.id.homeFrameLayout, informationFragment);
//		mFragmentTransaction.hide(informationFragment);
//		mFragmentTransaction.add(R.id.homeFrameLayout, gameFragment);
//		mFragmentTransaction.hide(gameFragment);
//		mFragmentTransaction.add(R.id.homeFrameLayout, evaluationFragment);
//		mFragmentTransaction.hide(evaluationFragment);
		mFragmentTransaction.add(R.id.homeFrameLayout, toolFragment);
		mFragmentTransaction.hide(toolFragment);
		
		mFragmentTransaction.commit();
		currentIndex = 0;
		changeFocusState(currentIndex, true);
		
		setViewSize();
	}
	
	private void setViewSize(){
		UIUtil.setViewSize(bottomPanel, MetricsTool.width, 180 * Rx);
		UIUtil.setViewSize(top, MetricsTool.width, 180 * Rx);
		UIUtil.setViewSize(toolTop, MetricsTool.width, 180 * Rx);
		UIUtil.setViewSize(accountImg, 75 * Rx, 75 * Rx);
		UIUtil.setViewSize(searchRel, 70 * Rx, 70 * Rx);
		UIUtil.setViewSize(searchBtn, 50 * Rx, 50 * Rx);
		UIUtil.setViewSize(download, 50 * Rx, 50 * Rx);
		UIUtil.setViewSize(recommendImg, 80 * Rx, 80 * Rx);
		UIUtil.setViewSize(informationImg, 80 * Rx, 80 * Rx);
		UIUtil.setViewSize(gameImg, 80 * Rx, 80 * Rx);
		UIUtil.setViewSize(evaluationImg, 80 * Rx, 80 * Rx);
		UIUtil.setViewSize(toolImg, 80 * Rx, 80 * Rx);
		UIUtil.setViewSize(toolTopImg, 64 * Rx, 64 * Rx);
		
		UIUtil.setTextSize(recommendTx, 30);
		UIUtil.setTextSize(informationTx, 30);
		UIUtil.setTextSize(gameTx, 30);
		UIUtil.setTextSize(evaluationTx, 30);
		UIUtil.setTextSize(toolTx, 30);
		UIUtil.setTextSize(searchEd, 30);
		UIUtil.setTextSize(toolTopTx, 45);
	}
	
	private void setOnclick(){
		recommendLin.setOnClickListener(this);
		informationLin.setOnClickListener(this);
		evaluationLin.setOnClickListener(this);
		gameLin.setOnClickListener(this);
		toolLin.setOnClickListener(this);
		
		searchEd.setOnClickListener(this);
		accountImg.setOnClickListener(this);
		download.setOnClickListener(this);
	}
	
	@SuppressWarnings("deprecation")
	private void changeFocusState(int index,boolean isFocus){
		switch (index) {
		case 0:
			if (isFocus) {
				recommendImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.tuijian_press));
				recommendTx.setTextColor(getResources().getColor(R.color.bottomFocus));
			}else {
				recommendImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.tuijian));
				recommendTx.setTextColor(getResources().getColor(R.color.white));
			}
			break;

		case 1:
			if (isFocus) {
				informationImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.zixun_press));
				informationTx.setTextColor(getResources().getColor(R.color.bottomFocus));
			}else {
				informationImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.zixun));
				informationTx.setTextColor(getResources().getColor(R.color.white));
			}
			break;
		case 2:
			if (isFocus) {
				gameImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.youxi_press));
				gameTx.setTextColor(getResources().getColor(R.color.bottomFocus));
			} else {
				gameImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.youxi));
				gameTx.setTextColor(getResources().getColor(R.color.white));
			}
			break;
		case 3:
			if (isFocus) {
				evaluationImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.ceping_press));
				evaluationTx.setTextColor(getResources().getColor(R.color.bottomFocus));
			} else {
				evaluationImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.ceping));
				evaluationTx.setTextColor(getResources().getColor(R.color.white));
			}
			break;
		case 4:
			if (isFocus) {
				toolImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.gongju_press));
				toolTx.setTextColor(getResources().getColor(R.color.bottomFocus));
			} else {
				toolImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.gongju));
				toolTx.setTextColor(getResources().getColor(R.color.white));
			}
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
		switch (v.getId()) {
		case R.id.recommendLin:
			if (currentIndex != 0) {
				toolTop.setVisibility(View.INVISIBLE);
				top.setVisibility(View.VISIBLE);
				changeFocusState(currentIndex, false);
				if (!recommendFragment.isAdded()) {
					mFragmentTransaction.add(R.id.homeFrameLayout, recommendFragment);
				}
				mFragmentTransaction.hide(fragments.get(currentIndex));
				mFragmentTransaction.show(recommendFragment);
				currentIndex = 0;
				changeFocusState(currentIndex, true);
			}
			break;

		case R.id.informationLin:
			if (currentIndex != 1) {
				toolTop.setVisibility(View.INVISIBLE);
				top.setVisibility(View.VISIBLE);
				changeFocusState(currentIndex, false);
				if (!informationFragment.isAdded()) {
					mFragmentTransaction.add(R.id.homeFrameLayout, informationFragment);
				}
				mFragmentTransaction.hide(fragments.get(currentIndex));
				mFragmentTransaction.show(informationFragment);
				currentIndex = 1;
				changeFocusState(currentIndex, true);
			}
			break;
			
		case R.id.gameLin:
			if (currentIndex != 2) {
				toolTop.setVisibility(View.INVISIBLE);
				top.setVisibility(View.VISIBLE);
				changeFocusState(currentIndex, false);
				if (!gameFragment.isAdded()) {
					mFragmentTransaction.add(R.id.homeFrameLayout, gameFragment);
				}
				mFragmentTransaction.hide(fragments.get(currentIndex));
				mFragmentTransaction.show(gameFragment);
				currentIndex = 2;
				changeFocusState(currentIndex, true);
			}
			break;
			
		case R.id.evaluationLin:
			if (currentIndex != 3) {
				toolTop.setVisibility(View.INVISIBLE);
				top.setVisibility(View.VISIBLE);
				changeFocusState(currentIndex, false);
				if (!evaluationFragment.isAdded()) {
					mFragmentTransaction.add(R.id.homeFrameLayout, evaluationFragment);
				}
				mFragmentTransaction.hide(fragments.get(currentIndex));
				mFragmentTransaction.show(evaluationFragment);
				currentIndex = 3;
				changeFocusState(currentIndex, true);
			}
			break;
			
		case R.id.toolLin:
			if (currentIndex != 4) {
				toolTop.setVisibility(View.VISIBLE);
				top.setVisibility(View.INVISIBLE);
				changeFocusState(currentIndex, false);
				if (!toolFragment.isAdded()) {
					mFragmentTransaction.add(R.id.homeFrameLayout, toolFragment);
				}
				mFragmentTransaction.hide(fragments.get(currentIndex));
				mFragmentTransaction.show(toolFragment);
				currentIndex = 4;
				changeFocusState(currentIndex, true);
			}
			break;
		case R.id.searchEd:
			startActivity(new Intent(this, SearchActivity.class));
			break;
		
		case R.id.accountImg:
			QueryBuilder qb = StoreApplication.daoSession.getAccountDao().queryBuilder();
			Account account = (Account) qb.unique();
			if (account != null) {
				startActivity(new Intent(this, ShowAccountInformatiomActivity.class));
			}else {
				startActivity(new Intent(this, MemberLoginActivity.class));
			}
			break;
			
		case R.id.download:
			startActivity(new Intent(this, DownloadManagerActivity.class));
		}
		mFragmentTransaction.commit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
	       if (keyCode == KeyEvent.KEYCODE_BACK) {
               if ((System.currentTimeMillis() - mExitTime) > 2000) {
                       Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                       mExitTime = System.currentTimeMillis();
                       
               } else {
            	   DownloadManager.getInstance().pauseExit();
            	   Process.killProcess(Process.myPid());
               }
               return true;
       }
       return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onNetChange(int netType) {
		// TODO Auto-generated method stub
		if ((netType == NetReceiver.NET_NOBILE || netType == NetReceiver.NET_NONE)
				&& !firstIn) {
			firstIn = false;
			if (!dialog.isShowing()) {
				DownloadManager.getInstance().pauseAllExit();
				dialog.show();
			}
		}
	}
}