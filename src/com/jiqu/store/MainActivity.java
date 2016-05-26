package com.jiqu.store;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.jiqu.activity.DownloadManagerActivity;
import com.jiqu.activity.MemberLoginActivity;
import com.jiqu.activity.SearchActivity;
import com.jiqu.activity.ShowAccountInformatiomActivity;
import com.jiqu.application.StoreApplication;
import com.jiqu.database.Account;
import com.jiqu.download.DownloadManager;
import com.jiqu.fragment.EvaluationFragment;
import com.jiqu.fragment.GameFragment;
import com.jiqu.fragment.GameNewFragment;
import com.jiqu.fragment.InformationFragment;
import com.jiqu.fragment.InformationNewFragment;
import com.jiqu.fragment.RecommendFragment;
import com.jiqu.fragment.ToolFragment;
import com.jiqu.interfaces.LoginOutObserver;
import com.jiqu.object.UpgradeVersionInfo;
import com.jiqu.tools.Constants;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.NetReceiver;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.NetReceiver.OnNetChangeListener;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.CustomDialog;
import com.jiqu.view.NetChangeDialog;
import com.vr.store.R;

import de.greenrobot.dao.query.QueryBuilder;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class MainActivity extends FragmentActivity implements OnClickListener,OnNetChangeListener,LoginOutObserver{
	private final String CHECKNE_WVERSION_REQUEST = "checkNewversionRequest";
	private float Rx,Ry;
	private ImageView accountImg;
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
	private InformationNewFragment informationNewFragment;
	private GameFragment gameFragment;
	private GameNewFragment gameNewFragment;
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
	
	private RequestTool requestTool;
	private HashMap<String, Object> map = new HashMap<String, Object>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestTool = RequestTool.getInstance();
		MetricsTool.initMetrics(getWindowManager());
		netReceiver = NetReceiver.getInstance();
		netReceiver.setNetChangeListener(this);
		netReceiver.registerReceive(StoreApplication.context);
		
		StoreApplication.setLoginOutObserver(this);
		
		new NetChangeDialog(this).show();
		
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
		
		checkNewVersion();
	}
	
	private void checkNewVersion(){
		map.put("version", Constants.VERSION_CODE);
		map.put("package", Constants.PACKAGENAME);
		map.put("channel", StoreApplication.CHANNEL);
		requestTool.startStringRequest(Method.POST, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				UpgradeVersionInfo upgradeVersionInfo = JSON.parseObject(arg0, UpgradeVersionInfo.class);
				if (upgradeVersionInfo != null) {
					if (upgradeVersionInfo.getStatus() == 1) {
					}else if (upgradeVersionInfo.getStatus() == 0) {
						Log.i("UpgradeVersion", "latest version !");
					}
				}
			}
		}, RequestTool.VR_HELPER_UPGRADE_URL, new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				Log.i("TAG", "onErrorResponse");
			}
			
		}, map, CHECKNE_WVERSION_REQUEST);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		netReceiver.unregisterReceive(StoreApplication.context);
		requestTool.stopRequest(CHECKNE_WVERSION_REQUEST);
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
		
		accountImg = (ImageView) findViewById(R.id.accountImg);
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
		informationNewFragment = new InformationNewFragment();
//		gameFragment = new GameFragment();
		gameNewFragment = new GameNewFragment();
		evaluationFragment = new EvaluationFragment();
		toolFragment = new ToolFragment();
		
		fragments.add(recommendFragment);
//		fragments.add(informationFragment);
//		fragments.add(gameFragment);
		fragments.add(informationNewFragment);
		fragments.add(gameNewFragment);
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
		
		toolTop.setBackgroundDrawable(UIUtil.readBitmapDrawable(this, R.drawable.tool_top_bg));
		
		toolTopImg.setImageBitmap(UIUtil.readBitmap(this, R.drawable.shezhi_icon));
		searchRel.setBackgroundDrawable(UIUtil.readBitmapDrawable(this, R.drawable.sousuobg));
		searchBtn.setBackgroundDrawable(UIUtil.readBitmapDrawable(this, R.drawable.sousuoicon));
		download.setBackgroundDrawable(UIUtil.readBitmapDrawable(this, R.drawable.top_xiazai_icon));
		
		recommendImg.setImageBitmap(UIUtil.readBitmap(this, R.drawable.tuijian));
		informationImg.setImageBitmap(UIUtil.readBitmap(this, R.drawable.zixun));
		gameImg.setImageBitmap(UIUtil.readBitmap(this, R.drawable.youxi));
		evaluationImg.setImageBitmap(UIUtil.readBitmap(this, R.drawable.ceping));
		toolImg.setImageBitmap(UIUtil.readBitmap(this, R.drawable.gongju));
		
		initIconData();
	}
	
	private void initIconData(){
		File file = new File(Constants.ACCOUNT_ICON);
		Account account = StoreApplication.getInstance().daoSession.getAccountDao().queryBuilder().unique();
		if (file.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(Constants.ACCOUNT_ICON);
			if (account != null) {
				ImageListener listener = ImageLoader.getImageListener(accountImg, bitmap, bitmap);
				StoreApplication.getInstance().getImageLoader().get(account.getPhoto(), listener);
			}else {
				accountImg.setBackgroundResource(R.drawable.yonghuicon);
			}
		}else {
			if (account != null) {
				ImageListener listener = ImageLoader.getImageListener(accountImg, R.drawable.yonghuicon, R.drawable.yonghuicon);
				StoreApplication.getInstance().getImageLoader().get(account.getPhoto(), listener);
			}else {
				accountImg.setBackgroundResource(R.drawable.yonghuicon);
			}
		}
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
				recommendImg.setImageBitmap(UIUtil.readBitmap(this, R.drawable.tuijian_press));
				recommendTx.setTextColor(getResources().getColor(R.color.bottomFocus));
			}else {
				recommendImg.setImageBitmap(UIUtil.readBitmap(this, R.drawable.tuijian));
				recommendTx.setTextColor(getResources().getColor(R.color.white));
			}
			break;

		case 1:
			if (isFocus) {
				informationImg.setImageBitmap(UIUtil.readBitmap(this, R.drawable.zixun_press));
				informationTx.setTextColor(getResources().getColor(R.color.bottomFocus));
			}else {
				informationImg.setImageBitmap(UIUtil.readBitmap(this, R.drawable.zixun));
				informationTx.setTextColor(getResources().getColor(R.color.white));
			}
			break;
		case 2:
			if (isFocus) {
				gameImg.setImageBitmap(UIUtil.readBitmap(this, R.drawable.youxi_press));
				gameTx.setTextColor(getResources().getColor(R.color.bottomFocus));
			} else {
				gameImg.setImageBitmap(UIUtil.readBitmap(this, R.drawable.youxi));
				gameTx.setTextColor(getResources().getColor(R.color.white));
			}
			break;
		case 3:
			if (isFocus) {
				evaluationImg.setImageBitmap(UIUtil.readBitmap(this, R.drawable.ceping_press));
				evaluationTx.setTextColor(getResources().getColor(R.color.bottomFocus));
			} else {
				evaluationImg.setImageBitmap(UIUtil.readBitmap(this, R.drawable.ceping));
				evaluationTx.setTextColor(getResources().getColor(R.color.white));
			}
			break;
		case 4:
			if (isFocus) {
				toolImg.setImageBitmap(UIUtil.readBitmap(this, R.drawable.gongju_press));
				toolTx.setTextColor(getResources().getColor(R.color.bottomFocus));
			} else {
				toolImg.setImageBitmap(UIUtil.readBitmap(this, R.drawable.gongju));
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
//				mFragmentTransaction.replace(R.id.homeFrameLayout, recommendFragment);
				currentIndex = 0;
				changeFocusState(currentIndex, true);
			}
			break;

		case R.id.informationLin:
			if (currentIndex != 1) {
				toolTop.setVisibility(View.INVISIBLE);
				top.setVisibility(View.VISIBLE);
				changeFocusState(currentIndex, false);
				if (!informationNewFragment.isAdded()) {
					mFragmentTransaction.add(R.id.homeFrameLayout, informationNewFragment);
				}
				mFragmentTransaction.hide(fragments.get(currentIndex));
				mFragmentTransaction.show(informationNewFragment);
//				mFragmentTransaction.replace(R.id.homeFrameLayout, informationFragment);
				currentIndex = 1;
				changeFocusState(currentIndex, true);
			}
			break;
			
		case R.id.gameLin:
			if (currentIndex != 2) {
				toolTop.setVisibility(View.INVISIBLE);
				top.setVisibility(View.VISIBLE);
				changeFocusState(currentIndex, false);
//				if (!gameFragment.isAdded()) {
//					mFragmentTransaction.add(R.id.homeFrameLayout, gameFragment);
//				}
//				mFragmentTransaction.hide(fragments.get(currentIndex));
//				mFragmentTransaction.show(gameFragment);
				if (!gameNewFragment.isAdded()) {
					mFragmentTransaction.add(R.id.homeFrameLayout, gameNewFragment);
				}
				mFragmentTransaction.hide(fragments.get(currentIndex));
				mFragmentTransaction.show(gameNewFragment);
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
//		if ((netType == NetReceiver.NET_NOBILE || netType == NetReceiver.NET_NONE)
//				&& !firstIn) {
//			if (!dialog.isShowing()) {
//				DownloadManager.getInstance().pauseAllExit();
//				dialog.show();
//			}
//		}else {
//			if (firstIn) {
//				firstIn = false;
//			}
//		}
	}

	@Override
	public void onLoginOut() {
		// TODO Auto-generated method stub
		accountImg.setImageBitmap(null);
		accountImg.setBackgroundResource(R.drawable.yonghuicon);
	}

	@Override
	public void onRefresh(Account account) {
		// TODO Auto-generated method stub
		if (account != null) {
			ImageListener listener = ImageLoader.getImageListener(accountImg, R.drawable.yonghuicon, R.drawable.yonghuicon);
			StoreApplication.getInstance().getImageLoader().get(account.getPhoto(), listener);
		}
	}

}