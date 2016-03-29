package com.jiqu.store;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.jiqu.activity.AppUninstallActivity;
import com.jiqu.activity.DownloadManagerActivity;
import com.jiqu.activity.GameEvaluationInformationActivity;
import com.jiqu.activity.MemberLoginActivity;
import com.jiqu.activity.MessageCenterActivity;
import com.jiqu.activity.ModifyPasswordActivity;
import com.jiqu.activity.SearchActivity;
import com.jiqu.activity.SettingActivity;
import com.jiqu.activity.ShowAccountInformatiomActivity;
import com.jiqu.download.AppInfo;
import com.jiqu.fragment.EvaluationFragment;
import com.jiqu.fragment.GameFragment;
import com.jiqu.fragment.InformationFragment;
import com.jiqu.fragment.RecommendFragment;
import com.jiqu.fragment.ToolFragment;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.SharePreferenceTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.CustomDialog;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends FragmentActivity implements OnClickListener,Listener<String>,ErrorListener{
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
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MetricsTool.initMetrics(getWindowManager());
		
		Rx = MetricsTool.Rx;
		Ry = MetricsTool.Ry;
		setContentView(R.layout.main);
		init();
		setOnclick();
		
//		new CustomDialog(this).setNegativeButton(new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				dialog.dismiss();
//			}
//		}).initView().show();
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
//		mFragmentTransaction.add(R.id.homeFrameLayout, toolFragment);
//		mFragmentTransaction.hide(toolFragment);
		
		mFragmentTransaction.commit();
		currentIndex = 0;
		changeFocusState(currentIndex, true);
		
		
//		String url_qihoo360 = "http://recommend.api.sj.360.cn/inew/getRecomendApps?iszip=1&logo_type=2&deflate_field=1&apiversion=2&os=19&model=G620S-UL00&sn=4.589389937671455&cu=qualcomm+technologies%2C+inc+msm8916&bannertype=1&withext=1&vc=300030184&zjbb=1&datatype=adgame&page=1&fm=home&m=b033525c2a96a00c2bfd48c673522449&m2=3363e38bf819414c6c81d886ff878e2a&v=3.1.84&re=1&ch=432403&ppi=720x1280&startCount=1&snt=-1";
//		final StringRequest stringRequest = new StringRequest(url_qihoo360, this, this);
//		StoreApplication.getInstance().getRequestQueue().add(stringRequest);
		
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

	@SuppressLint("NewApi")
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
				top.setVisibility(View.GONE);
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
			startActivity(new Intent(this, ShowAccountInformatiomActivity.class));
			break;
		}
		mFragmentTransaction.commit();
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResponse(String response) {
		// TODO Auto-generated method stub
		try {
			List<AppInfo> resultList = new ArrayList<AppInfo>();
			JSONObject obj = new JSONObject(response);
			int errNo = obj.getInt("errno");
			if (errNo == 0) {
				JSONObject object = (JSONObject) obj.get("data");
				JSONArray array = (JSONArray) object.get("fixed");
				resultList = JSON.parseArray(array.toString(), AppInfo.class);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}
}
