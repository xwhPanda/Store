package com.jiqu.activity;

import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.VolleyError;
import com.jiqu.adapter.ViewPagerAdapter;
import com.jiqu.application.StoreApplication;
import com.jiqu.database.DownloadAppinfo;
import com.jiqu.database.DownloadAppinfoDao.Properties;
import com.jiqu.download.DownloadManager;
import com.jiqu.download.FileUtil;
import com.jiqu.download.DownloadManager.DownloadObserver;
import com.jiqu.download.UnZipManager;
import com.jiqu.object.GameDetailData;
import com.jiqu.object.GameDetailInfo;
import com.jiqu.object.InstalledApp;
import com.jiqu.store.BaseActivity;
import com.jiqu.store.SplashActivity;
import com.umeng.analytics.MobclickAgent;
import com.vr.store.R;
import com.jiqu.tools.Constants;
import com.jiqu.tools.InstalledAppTool;
import com.jiqu.tools.NetReceiver;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.LoadStateView;
import com.jiqu.view.ProgressButton;
import com.jiqu.view.ProgressButton.OnProgressButtonClickListener;
import com.jiqu.view.RatingBarView;
import com.jiqu.view.TitleView;

import de.greenrobot.dao.query.QueryBuilder;

public class DetailActivity extends BaseActivity implements Listener<String>, 
	ErrorListener, OnPageChangeListener, OnClickListener, DownloadObserver ,OnProgressButtonClickListener{
	private final String GAME_DETAIL_REQUEST = "gameDetailRequest";
	private RelativeLayout parent;
	private TitleView titleView;
	private LoadStateView loadView;
	private ScrollView scrollView;
	private LinearLayout gameIconLin;
	private ImageView gameIcon;
	private RelativeLayout titleLine;
	private TextView gameName;
	private TextView downloadCount;
	private TextView version;
	private TextView type;
	private TextView size;
	private LinearLayout controlWayLin;
	private RelativeLayout headControlRel;
	private ImageView headControlImg;
	private TextView headControlTx;
	private RelativeLayout handleRel;
	private ImageView handleImg;
	private TextView handleTx;
	private RelativeLayout vertigoLin;
	private TextView vertigo;
	private ImageView vertigoValue;
	private TextView low;
	private TextView high;
	private ImageView view;
	private ViewPager viewPager;
	private RelativeLayout pagerRel;
	private LinearLayout viewGroup;
	private ProgressButton download;

	private LinearLayout scoreLin;
	private RatingBarView screenSenseBar, immersionBar, gameplayBar, difficultyBar;
	private TextView screenSense, immersion, gameplay, difficultyNumber;
	private RelativeLayout screenSenseRel, immersionRel, gameplayRel, difficultyNumberRel;
	private TextView comprehensiveEvaluation;
	private TextView evaluationScore;
	private RatingBarView comprehensiveBar;
	private InstalledAppTool installedAppTool;

	private int[] IDs = new int[3];
	private int[] blueIDs = new int[3];

	private RequestTool requestTool;
	// 游戏ID
	private String id;
	private String name;
	// 0：根据id请求数据；1：不需要请求数据，数据是传过来的
	private int requestType = 0;
	//是否是推送打开
	private String isPush = "false";
	private ImageView[] imgs;
	private ImageView[] radioImgs;

	private int state = -1;
	private DownloadAppinfo downloadAppinfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestTool = RequestTool.getInstance();
		id = getIntent().getStringExtra("id");
		requestType = getIntent().getIntExtra("requestType", 0);
		name = getIntent().getStringExtra("name");
		isPush = getIntent().getStringExtra("isPush");
		
		initView();

		if (requestType == 0) {
			titleView.tip.setText(name);
			loadDetailData(RequestTool.GAME_DETAIL_URL + "?id=" + id);
		} else if (requestType == 1) {
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("DetailActivity");
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("DetailActivity");
		MobclickAgent.onPause(this);
	}

	private void loadDetailData(String url) {
		requestTool.getMap().clear();
		requestTool.startStringRequest(Method.GET, this, url, this, requestTool.getMap(), GAME_DETAIL_REQUEST);
	}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.detail_layout;
	}

	private void initStar() {
		IDs[0] = R.drawable.ratingbg;
		IDs[1] = R.drawable.rating_sencond_progress;
		IDs[2] = R.drawable.rating_progress;

		blueIDs[0] = R.drawable.star3_blue;
		blueIDs[1] = R.drawable.star2_blue;
		blueIDs[2] = R.drawable.star1_blue;
	}

	private void initView() {
		installedAppTool = new InstalledAppTool();
		initStar();

		parent = (RelativeLayout) findViewById(R.id.parent);
		parent.setBackgroundDrawable(StoreApplication.BG_IMG);
		titleView = (TitleView) findViewById(R.id.titleView);
		loadView = (LoadStateView) findViewById(R.id.loadView);
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		gameIconLin = (LinearLayout) findViewById(R.id.gameIconLin);
		gameIcon = (ImageView) findViewById(R.id.gameIcon);
		titleLine = (RelativeLayout) findViewById(R.id.titleLine);
		gameName = (TextView) findViewById(R.id.gameName);
		downloadCount = (TextView) findViewById(R.id.downloadCount);
		version = (TextView) findViewById(R.id.version);
		type = (TextView) findViewById(R.id.type);
		size = (TextView) findViewById(R.id.size);
		controlWayLin = (LinearLayout) findViewById(R.id.controlWayLin);
		headControlRel = (RelativeLayout) findViewById(R.id.headControlRel);
		headControlImg = (ImageView) findViewById(R.id.headControlImg);
		headControlTx = (TextView) findViewById(R.id.headControlTx);
		handleRel = (RelativeLayout) findViewById(R.id.handleRel);
		handleImg = (ImageView) findViewById(R.id.handleImg);
		handleTx = (TextView) findViewById(R.id.handleTx);
		vertigoLin = (RelativeLayout) findViewById(R.id.vertigoLin);
		vertigo = (TextView) findViewById(R.id.vertigo);
		vertigoValue = (ImageView) findViewById(R.id.vertigoValue);
		low = (TextView) findViewById(R.id.low);
		high = (TextView) findViewById(R.id.high);
		view = (ImageView) findViewById(R.id.view);
		pagerRel = (RelativeLayout) findViewById(R.id.pagerRel);
		viewGroup = (LinearLayout) findViewById(R.id.viewGroup);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		download = (ProgressButton) findViewById(R.id.download);

		scoreLin = (LinearLayout) findViewById(R.id.scoreLin);

		screenSense = (TextView) findViewById(R.id.screenSense);
		immersion = (TextView) findViewById(R.id.immersion);
		gameplay = (TextView) findViewById(R.id.gameplay);
		difficultyNumber = (TextView) findViewById(R.id.difficultyNumber);

		screenSenseBar = (RatingBarView) findViewById(R.id.screenSenseBar);
		immersionBar = (RatingBarView) findViewById(R.id.immersionBar);
		gameplayBar = (RatingBarView) findViewById(R.id.gameplayBar);
		difficultyBar = (RatingBarView) findViewById(R.id.difficultyBar);

		screenSenseRel = (RelativeLayout) findViewById(R.id.screenSenseRel);
		immersionRel = (RelativeLayout) findViewById(R.id.immersionRel);
		gameplayRel = (RelativeLayout) findViewById(R.id.gameplayRel);
		difficultyNumberRel = (RelativeLayout) findViewById(R.id.difficultyNumberRel);

		comprehensiveEvaluation = (TextView) findViewById(R.id.comprehensiveEvaluation);
		evaluationScore = (TextView) findViewById(R.id.evaluationScore);
		comprehensiveBar = (RatingBarView) findViewById(R.id.comprehensiveBar);

		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.tip.setText("");
		titleView.tip.setTextColor(getResources().getColor(R.color.blue));
		titleView.editBtn.setVisibility(View.VISIBLE);
		titleView.editBtn.setBackgroundResource(R.drawable.fenxiang_white);
		titleView.setBackListener(this);

		gameIcon.setScaleType(ScaleType.FIT_XY);
		viewPager.setOnPageChangeListener(this);
		download.setOnProgressButtonClickListener(this);

		loadView.loadAgain(this);

		screenSenseBar.setResID(IDs);
		immersionBar.setResID(IDs);
		gameplayBar.setResID(IDs);
		difficultyBar.setResID(IDs);

		comprehensiveBar.setResID(blueIDs);
		comprehensiveBar.setStep(0.5f);

		titleView.setShareListener(this);
		
		initViewSize();
	}

	private void initViewSize() {
		UIUtil.setTextSize(screenSense, 40);
		UIUtil.setTextSize(immersion, 40);
		UIUtil.setTextSize(gameplay, 40);
		UIUtil.setTextSize(difficultyNumber, 40);
		UIUtil.setTextSize(comprehensiveEvaluation, 70);
		UIUtil.setTextSize(evaluationScore, 100);

		UIUtil.setTextSize(gameName, 40);
		UIUtil.setTextSize(downloadCount, 35);
		UIUtil.setTextSize(version, 35);
		UIUtil.setTextSize(type, 35);
		UIUtil.setTextSize(size, 35);
		UIUtil.setTextSize(headControlTx, 45);
		UIUtil.setTextSize(handleTx, 45);
		UIUtil.setTextSize(vertigo, 35);
		UIUtil.setTextSize(low, 25);
		UIUtil.setTextSize(high, 25);
		download.setTextSize((int)(40 * Rx));

		UIUtil.setViewSize(gameIcon, 190 * Rx, 190 * Rx);
		UIUtil.setViewSize(headControlImg, 110 * Rx, 60 * Rx);
		UIUtil.setViewSize(handleImg, 110 * Rx, 60 * Rx);
		UIUtil.setViewSize(headControlRel, 460 * Rx, 90 * Ry);
		UIUtil.setViewSize(handleRel, 460 * Rx, 90 * Ry);
		UIUtil.setViewSize(vertigoValue, 750 * Rx, 30 * Rx);
		UIUtil.setViewSize(pagerRel, 1040 * Rx, 515 * Ry);
		UIUtil.setViewSize(download, 805 * Rx, 95 * Ry);

		UIUtil.setViewHeight(scoreLin, 350 * Ry);
		UIUtil.setViewHeight(vertigoLin, 120 * Ry);

		comprehensiveBar.setStarSize(47, 47);

		try {
			UIUtil.setViewSizeMargin(gameIcon, 50 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(titleLine, 30 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(gameIconLin, 0, 45 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(downloadCount, 0, 25 * Ry, 25 * Rx, 0);
			UIUtil.setViewSizeMargin(type, 0, 25 * Ry, 25 * Rx, 0);
			UIUtil.setViewSizeMargin(controlWayLin, 0, 50 * Ry, 0, 50 * Ry);
			UIUtil.setViewSizeMargin(headControlRel, 0, 0, 60 * Rx, 0);
			UIUtil.setViewSizeMargin(headControlTx, 15 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(handleTx, 15 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(vertigoValue, 35 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(low, 0, 5 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(high, 0, 5 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(pagerRel, 0, 15 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(download, 0, 105 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(viewGroup, 0, 0, 0, 30 * Ry);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onErrorResponse(VolleyError arg0) {
		// TODO Auto-generated method stub
		loadView.loadDataFail();
	}

	@Override
	public void onResponse(String arg0) {
		// TODO Auto-generated method stub
		loadView.loadDataSuccess();
		loadView.setVisibility(View.GONE);
		scrollView.setVisibility(View.VISIBLE);
		if (arg0 != null) {
			GameDetailData data = JSON.parseObject(arg0.toString(), GameDetailData.class);
			if (data.getStatus() == 1 && data.getData() != null) {
				setData(data.getData());
			}
		}
	}

	private void setData(GameDetailInfo info) {
		DownloadManager.getInstance().registerObserver(this);
		Bitmap bitmap = UIUtil.readBitmap(this, R.drawable.default_tubiao);
		ImageListener listener = ImageLoader.getImageListener(gameIcon, bitmap, bitmap);
		StoreApplication.getInstance().getImageLoader().get(info.getIcon(), listener, (int) (190 * Rx), (int) (190 * Rx));
		titleView.tip.setText(info.getApply_name());
		gameName.setText(info.getApply_name());
		downloadCount.setText(info.getDown() + "人下载");
		version.setText("版本:" + info.getVersion_name());
		type.setText(info.getColumn());
		size.setText(UIUtil.getDataSize(Long.parseLong(info.getSize())));

		int support = Integer.parseInt(info.getDevice_support());
		if (support == 1) {
			headControlRel.setBackgroundColor(getResources().getColor(R.color.blue));
			handleRel.setBackgroundColor(getResources().getColor(R.color.blue));
		} else if (support == 2) {
			headControlRel.setBackgroundColor(getResources().getColor(R.color.blue));
			handleRel.setBackgroundColor(getResources().getColor(R.color.detailControl));
		} else if (support == 3) {
			headControlRel.setBackgroundColor(getResources().getColor(R.color.detailControl));
			handleRel.setBackgroundColor(getResources().getColor(R.color.blue));
		}

		int count = info.getPic().size();
		if (count > 0) {
			imgs = new ImageView[count];
			radioImgs = new ImageView[count];
			for (int i = 0; i < count; i++) {
				ImageView view = new ImageView(this);
				Bitmap bitmapPager = UIUtil.readBitmap(this, R.drawable.recommend_viewpager_default);
				ImageListener imageListener = ImageLoader.getImageListener(view, bitmapPager, bitmapPager);
				StoreApplication.getInstance().getImageLoader().get(info.getPic().get(i), imageListener, (int) (1040 * Rx), (int) (515 * Ry));
				view.setScaleType(ScaleType.FIT_XY);
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				view.setLayoutParams(lp);
				imgs[i] = view;

				ImageView radio = new ImageView(this);
				if (i == 0) {
					radio.setBackgroundResource(R.drawable.dian_blue);
				} else {
					radio.setBackgroundResource(R.drawable.dian_white);
				}
				android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams((int) (20 * Rx), (int) (20 * Rx));
				if (i != 0) {
					params.leftMargin = (int) (10 * Rx);
				}
				radio.setLayoutParams(params);
				radioImgs[i] = radio;
				viewGroup.addView(radio);
			}
			ViewPagerAdapter adapter = new ViewPagerAdapter(this, imgs);
			viewPager.setAdapter(adapter);
		}

		float vertigo = info.getGame_feeling().getF5();
		if (vertigo == 0) {
			vertigoValue.setBackgroundResource(R.drawable.jindu1);
		} else if (vertigo > 0 && vertigo < 1.2) {
			vertigoValue.setBackgroundResource(R.drawable.jindu2);
		} else if (vertigo >= 1.4 && vertigo <= 2.0) {
			vertigoValue.setBackgroundResource(R.drawable.jindu3);
		} else if (vertigo > 2.0) {
			vertigoValue.setBackgroundResource(R.drawable.jindu4);
		}

		screenSenseBar.setRating(info.getGame_feeling().getF1());
		immersionBar.setRating(info.getGame_feeling().getF2());
		gameplayBar.setRating(info.getGame_feeling().getF3());
		difficultyBar.setRating(info.getGame_feeling().getF4());

		evaluationScore.setText(info.getScore());
		comprehensiveBar.setRating(Float.parseFloat(info.getStar()));

		QueryBuilder<DownloadAppinfo> qb = StoreApplication.daoSession.getDownloadAppinfoDao().queryBuilder();
		downloadAppinfo = qb.where(Properties.Id.eq(Long.parseLong(id))).unique();
		if (downloadAppinfo != null) {
			state = downloadAppinfo.getDownloadState();
			download.setProgress((int)(downloadAppinfo.getProgress() * 100));
		} else {
			downloadAppinfo = GameDetailInfo.toDownloadAppinfo(info);
			state = downloadAppinfo.getDownloadState();
			List<InstalledApp> apps = installedAppTool.getPersonalApp(this);
			int stateNum = InstalledAppTool.contain(apps, downloadAppinfo.getPackageName(), Integer.parseInt(downloadAppinfo.getVersionCode()));
			if (stateNum != -1) {
				state = stateNum;
				downloadAppinfo.setDownloadState(state);
				DownloadManager.DBManager.getDownloadAppinfoDao().insertOrReplace(downloadAppinfo);
			}
		}
		refreshState(state);
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
		for (int i = 0; i < radioImgs.length; i++) {
			if (arg0 == i) {
				radioImgs[i].setBackgroundResource(R.drawable.dian_blue);
			} else {
				radioImgs[i].setBackgroundResource(R.drawable.dian_white);
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == loadView.getLoadBtn()) {
			loadDetailData(RequestTool.GAME_DETAIL_URL + "?id=" + id);
			loadView.showLoading();
		} else if (v == download) {
			if (downloadAppinfo != null) {
				if (state == DownloadManager.STATE_DOWNLOADING) {
					DownloadManager.getInstance().pause(downloadAppinfo);
				} else if (state == DownloadManager.STATE_UNZIP_FAILED) {
					if (downloadAppinfo != null) {
						UnZipManager.getInstance().unzip(downloadAppinfo, Constants.PASSWORD, null);
					}
				} else if (state == DownloadManager.STATE_UNZIPING) {
					download.setEnabled(false);
				} else if (state == DownloadManager.STATE_WAITING) {
					DownloadManager.getInstance().cancel(downloadAppinfo);
				} else if (state == DownloadManager.STATE_INSTALLED) {
					DownloadManager.getInstance().open(downloadAppinfo.getPackageName());
				} else if (state == DownloadManager.STATE_PAUSED) {
					if (NetReceiver.NET_WIFI == NetReceiver.NET_TYPE) {
						if (FileUtil.checkSDCard()) {
							if (Long.parseLong(downloadAppinfo.getAppSize()) * 3 >= FileUtil.getSDcardAvailableSpace()) {
								Toast.makeText(this, "可用空间不足", Toast.LENGTH_SHORT).show();
								return;
							}
						} else {
							if (Long.parseLong(downloadAppinfo.getAppSize()) * 3 >= FileUtil.getDataStorageAvailableSpace()) {
								Toast.makeText(this, "可用空间不足", Toast.LENGTH_SHORT).show();
								return;
							}
						}
						DownloadManager.getInstance().download(downloadAppinfo);
					}
				} else if (state == DownloadManager.STATE_ERROR || state == DownloadManager.STATE_NONE || state == DownloadManager.STATE_NEED_UPDATE) {
					if (NetReceiver.NET_WIFI == NetReceiver.NET_TYPE) {
						if (FileUtil.checkSDCard()) {
							if (Long.parseLong(downloadAppinfo.getAppSize()) * 3 >= FileUtil.getSDcardAvailableSpace()) {
								Toast.makeText(this, "可用空间不足", Toast.LENGTH_SHORT).show();
								return;
							}
						} else {
							if (Long.parseLong(downloadAppinfo.getAppSize()) * 3 >= FileUtil.getDataStorageAvailableSpace()) {
								Toast.makeText(this, "可用空间不足", Toast.LENGTH_SHORT).show();
								return;
							}
						}
						DownloadManager.getInstance().download(downloadAppinfo);
					} else {
						Toast.makeText(this, "没有连接wifi", Toast.LENGTH_SHORT).show();
					}
				} else {
					if (downloadAppinfo.getIsZip()) {
						if (state == DownloadManager.STATE_UNZIPED) {
							DownloadManager.getInstance().install(downloadAppinfo);
						}
					} else {
						if (state == DownloadManager.STATE_INSTALLED) {
							DownloadManager.getInstance().install(downloadAppinfo);
						}
					}
				}
			}
		}else if (v == titleView.editBtn) {
			if (downloadAppinfo != null) {
				Bundle bundle = new Bundle();
				bundle.putString("title", downloadAppinfo.getAppName());
				bundle.putString("content", downloadAppinfo.getDes());
				bundle.putString("url", downloadAppinfo.getUrl());
				bundle.putString("image", downloadAppinfo.getIconUrl());
				startActivity(new Intent(DetailActivity.this, ShareActivity.class)
				.putExtra("bundle", bundle));
			}
		}else if (v == titleView.backLin) {
			if ("true".equals(isPush)
					&& Constants.ACTIVITY_LIST.size() <= 1) {
				startActivity(new Intent(this, SplashActivity.class));
			}
			finish();
		}
	}

	private void refreshState(int state) {
		this.state = state;
		if (state == DownloadManager.STATE_DOWNLOADING) {
			download.setEnabled(true);
			download.setText("正在下载");
		} else if (state == DownloadManager.STATE_UNZIP_FAILED) {
			download.setEnabled(true);
			download.setText("解压失败");
		} else if (state == DownloadManager.STATE_UNZIPING) {
			download.setEnabled(false);
			download.setText("正在解压");
		} else if (state == DownloadManager.STATE_WAITING) {
			download.setEnabled(true);
			download.setText("等待下载");
		} else if (state == DownloadManager.STATE_INSTALLED) {
			download.setEnabled(true);
			download.setText("打开");
		} else if (state == DownloadManager.STATE_PAUSED) {
			download.setEnabled(true);
			download.setText("继续下载");
		} else if (state == DownloadManager.STATE_NONE) {
			download.setEnabled(true);
			download.setText("下载");
		} else if (state == DownloadManager.STATE_NEED_UPDATE) {
			download.setEnabled(true);
			download.setText("升级");
		} else if (state == DownloadManager.STATE_ERROR) {
			download.setEnabled(true);
			download.setText("下载失败");
		} else {
			download.setEnabled(true);
			if (downloadAppinfo.getIsZip()) {
				if (state == DownloadManager.STATE_UNZIPED) {
					download.setText("安装");
				}
			} else {
				if (state == DownloadManager.STATE_INSTALLED) {
					download.setText("安装");
				}
			}
		}
	}

	@Override
	public void onDownloadStateChanged(DownloadAppinfo info) {
		// TODO Auto-generated method stub
		downloadAppinfo = info;
		handler.sendEmptyMessage(2);
	}

	@Override
	public void onDownloadProgressed(DownloadAppinfo info) {
		// TODO Auto-generated method stub
		if (downloadAppinfo != null && downloadAppinfo.getPackageName().equals(info.getPackageName())) {
			Message msg = handler.obtainMessage();
			msg.arg1 = (int)(info.getProgress() * 100);
			msg.what = 1;
			handler.sendMessage(msg);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		DownloadManager.getInstance().unRegisterObserver(this);
		requestTool.stopRequest(GAME_DETAIL_REQUEST);
	}

	@Override
	protected void installEvent(String installPackageName) {
		// TODO Auto-generated method stub
		if (downloadAppinfo != null && installPackageName.equals(downloadAppinfo.getPackageName())) {
			state = DownloadManager.STATE_INSTALLED;
			downloadAppinfo.setDownloadState(state);
			DownloadManager.getInstance().DBManager.getDownloadAppinfoDao().insertOrReplace(downloadAppinfo);
			refreshState(state);
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				download.setProgress(msg.arg1);
			} else if (msg.what == 2) {
				refreshState(downloadAppinfo.getDownloadState());
			}
		};
	};

	@Override
	public void onUnZipProgressed(DownloadAppinfo info, int progress) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClickListener() {
		// TODO Auto-generated method stub
		if (downloadAppinfo != null) {
			if (state == DownloadManager.STATE_DOWNLOADING) {
				DownloadManager.getInstance().pause(downloadAppinfo);
			} else if (state == DownloadManager.STATE_UNZIP_FAILED) {
				if (downloadAppinfo != null) {
					UnZipManager.getInstance().unzip(downloadAppinfo, Constants.PASSWORD, null);
				}
			} else if (state == DownloadManager.STATE_UNZIPING) {
				download.setEnabled(false);
			} else if (state == DownloadManager.STATE_WAITING) {
				DownloadManager.getInstance().cancel(downloadAppinfo);
			} else if (state == DownloadManager.STATE_INSTALLED) {
				DownloadManager.getInstance().open(downloadAppinfo.getPackageName());
			} else if (state == DownloadManager.STATE_PAUSED) {
				if (NetReceiver.NET_WIFI == NetReceiver.NET_TYPE) {
					if (FileUtil.checkSDCard()) {
						if (Long.parseLong(downloadAppinfo.getAppSize()) * 3 >= FileUtil.getSDcardAvailableSpace()) {
							Toast.makeText(this, "可用空间不足", Toast.LENGTH_SHORT).show();
							return;
						}
					} else {
						if (Long.parseLong(downloadAppinfo.getAppSize()) * 3 >= FileUtil.getDataStorageAvailableSpace()) {
							Toast.makeText(this, "可用空间不足", Toast.LENGTH_SHORT).show();
							return;
						}
					}
					DownloadManager.getInstance().download(downloadAppinfo);
				}
			} else if (state == DownloadManager.STATE_ERROR || state == DownloadManager.STATE_NONE || state == DownloadManager.STATE_NEED_UPDATE) {
				if (NetReceiver.NET_WIFI == NetReceiver.NET_TYPE) {
					if (FileUtil.checkSDCard()) {
						if (Long.parseLong(downloadAppinfo.getAppSize()) * 3 >= FileUtil.getSDcardAvailableSpace()) {
							Toast.makeText(this, "可用空间不足", Toast.LENGTH_SHORT).show();
							return;
						}
					} else {
						if (Long.parseLong(downloadAppinfo.getAppSize()) * 3 >= FileUtil.getDataStorageAvailableSpace()) {
							Toast.makeText(this, "可用空间不足", Toast.LENGTH_SHORT).show();
							return;
						}
					}
					DownloadManager.getInstance().download(downloadAppinfo);
				} else {
					Toast.makeText(this, "没有连接wifi", Toast.LENGTH_SHORT).show();
				}
			} else {
				if (downloadAppinfo.getIsZip()) {
					if (state == DownloadManager.STATE_UNZIPED) {
						DownloadManager.getInstance().install(downloadAppinfo);
					}
				} else {
					if (state == DownloadManager.STATE_INSTALLED) {
						DownloadManager.getInstance().install(downloadAppinfo);
					}
				}
			}
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