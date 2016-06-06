package com.jiqu.adapter;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.jiqu.activity.SortInfoActivity;
import com.jiqu.application.StoreApplication;
import com.jiqu.database.DownloadAppinfo;
import com.jiqu.download.AppUtil;
import com.jiqu.download.DownloadManager;
import com.jiqu.download.DownloadManager.DownloadObserver;
import com.jiqu.download.FileUtil;
import com.jiqu.download.UnZipManager;
import com.jiqu.object.GameInfo;
import com.vr.store.R;
import com.jiqu.tools.Constants;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.NetReceiver;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.NetChangeDialog;
import com.jiqu.view.RatingBarView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GameAdapter extends BaseAdapter implements DownloadObserver{
	private List<GameInfo> informations;
	private LayoutInflater inflater;
	private Holder1 holder1 = null;
	private Holder3 holder3 = null;
	
	private List<Holder2> mDisplayedHolders;
	private Context context;
	private int[] resIds = new int[3];
	
	private boolean hotIconVisible;
	private boolean subscriptVisible;
	
	private int colorId = R.color.itemBgColor;
	
	public GameAdapter(Context context,List<GameInfo> informations,boolean hotIconVisible,boolean subscriptVisible){
		this.informations = informations;
		inflater = LayoutInflater.from(context);
		mDisplayedHolders = new ArrayList<GameAdapter.Holder2>();
		this.context = context;
		this.hotIconVisible = hotIconVisible;
		this.subscriptVisible = subscriptVisible;
		
		initIds();
	}
	
	public void setList(List<GameInfo> list){
		informations = list;
	}
	
	public void setItemBackgroundColor(int colorId){
		this.colorId = colorId;
	}
	
	private void initIds(){
		resIds[0] = R.drawable.ratingbg;
		resIds[1] = R.drawable.rating_sencond_progress;
		resIds[2] = R.drawable.rating_progress;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return informations.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return informations.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return informations.get(position).getAdapterType();
	}
	
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 3;
	}
	
	public void startObserver() {
		DownloadManager.getInstance().registerObserver(this);
	}

	public void stopObserver() {
		DownloadManager.getInstance().unRegisterObserver(this);
	}
	
	public void addItems(List<GameInfo> infos){
		informations.addAll(infos);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		int type = getItemViewType(position);
		Holder2 holder2 = null;
		switch (type) {
		case 1:
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.game_sort_title, null);
				holder1 = new Holder1();
				holder1.sortName = (TextView) convertView.findViewById(R.id.sortName);
				holder1.moreTx = (TextView) convertView.findViewById(R.id.moreTx);
				holder1.moreBtn = (ImageView) convertView.findViewById(R.id.moreBt);
				holder1.moreLin = (LinearLayout) convertView.findViewById(R.id.moreLin);
				
				holder1.moreBtn.setImageBitmap(UIUtil.readBitmap(context, R.drawable.more));
				UIUtil.setTextSize(holder1.sortName, 42);
				UIUtil.setTextSize(holder1.moreTx, 35);
				UIUtil.setViewSize(holder1.moreBtn, 36 * MetricsTool.Rx, 36 * MetricsTool.Rx);
				
				holder1.moreLin.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						context.startActivity(new Intent(context, SortInfoActivity.class)
						.putExtra("fromWhere", 2)
						.putExtra("gameType", informations.get(position).getGameType()));
					}
				});
				
				convertView.setTag(holder1);
			}else {
				holder1 = (Holder1) convertView.getTag();
			}
			holder1.sortName.setText(informations.get(position).getTitle());
			break;

		case 0:
			if (convertView == null) {
				holder2 = new Holder2(context,hotIconVisible,subscriptVisible);
				convertView = holder2.getRootView();
				
			}else {
				holder2 = (Holder2) convertView.getTag();
			}
			holder2.setBackground(colorId);
			holder2.setData(GameInfo.toDownloadAppInfo(informations.get(position)));
			mDisplayedHolders.add(holder2);
			break;
		case 2:
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.game_sort_single_tx_title, null);
				holder3 = new Holder3();
				holder3.title = (TextView) convertView.findViewById(R.id.title);
				convertView.setTag(holder3);
			}else {
				holder3 = (Holder3) convertView.getTag();
			}
			break;
		}
		return convertView;
	}
	
	public List<Holder2> getDisplayedHolders() {
		synchronized (mDisplayedHolders) {
			return new ArrayList<Holder2>(mDisplayedHolders);
		}
	}
	
	public void clearAllItem() {
		if (informations != null){
			informations.clear();
		}
		if (mDisplayedHolders != null) {
			mDisplayedHolders.clear();
		}
	}

	private class Holder1{
		private TextView sortName;
		private TextView moreTx;
		private ImageView moreBtn;
		private LinearLayout moreLin;
	}
	
	private class Holder2{
		private RelativeLayout parentView;
		private ImageView icon;
		private TextView gameName;
		private TextView gameDes;
		private RatingBarView gameScore;
		private TextView gameSize;
		private RelativeLayout downloadRel;
		private Button downloadBtn;
		private TextView stateText;
		private TextView subscriptTx;
		private ImageView hotIcon;
		
		private RelativeLayout informationLin;
		private LinearLayout subscriptLin;
		
		private View rootView;
		
		private boolean installDialogShowed = false;
		
		private NetChangeDialog netChangeDialog;
		
		private int mState;
		public DownloadAppinfo mData;
		private DownloadManager mDownloadManager;
		private boolean hasAttached;
		
		private boolean hotIConVisible;
		private boolean subscriptVisible;
		
		private Context context;
		
		public Holder2(Context context,boolean hotIConVisible,boolean subscriptVisible){
			this.hotIConVisible = hotIConVisible;
			this.subscriptVisible = subscriptVisible;
			this.context = context;
			rootView = initView();
			rootView.setTag(this);
		}
		
		public View getRootView(){
			return rootView;
		}
		
		public void setBackground(int colorId){
			parentView.setBackgroundColor(colorId);
		}
		
		private View initView(){
			netChangeDialog = new NetChangeDialog(context);
			View view = AppUtil.inflate(R.layout.recommend_game_item);
			parentView = (RelativeLayout) view.findViewById(R.id.parentView);
			icon = (ImageView) view.findViewById(R.id.icon);
			gameName = (TextView) view.findViewById(R.id.gameName);
			gameDes = (TextView) view.findViewById(R.id.gameDes);
			gameScore = (RatingBarView) view.findViewById(R.id.gameScore);
			gameSize = (TextView) view.findViewById(R.id.gameSize);
			downloadRel = (RelativeLayout) view.findViewById(R.id.downloadRel);
			downloadBtn = (Button) view.findViewById(R.id.downloadBtn);
			stateText = (TextView) view.findViewById(R.id.stateText);
			subscriptTx = (TextView) view.findViewById(R.id.subscriptTx);
			hotIcon = (ImageView) view.findViewById(R.id.hotIcon);
			
			hotIcon.setImageBitmap(UIUtil.readBitmap(context, R.drawable.hot_icon));
			
			gameScore.setResID(resIds);
			gameScore.setStep(0.5f);
			
			informationLin = (RelativeLayout) view.findViewById(R.id.informationLin);
			subscriptLin = (LinearLayout) view.findViewById(R.id.subscriptLin);
			subscriptLin.setBackgroundDrawable(UIUtil.readBitmapDrawable(context, R.drawable.subscript_red));
			
			netChangeDialog.setPositiveListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					netChangeDialog.dismiss();
				}
			});
			
			netChangeDialog.setNegativeListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mDownloadManager.download(mData);
					netChangeDialog.dismiss();
				}
			});
			
			downloadBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (mState == DownloadManager.STATE_NONE 
							|| mState == DownloadManager.STATE_PAUSED
							|| mState == DownloadManager.STATE_ERROR
							|| mState == DownloadManager.STATE_NEED_UPDATE) {
						if (NetReceiver.NET_TYPE == NetReceiver.NET_WIFI) {
							if (FileUtil.checkSDCard()) {
								if (Float.parseFloat(mData.getAppSize()) * 3 >= FileUtil.getSDcardAvailableSpace()) {
									Toast.makeText(context, "可用空间不足", Toast.LENGTH_SHORT).show();
									return;
								}
							}else {
								if (Float.parseFloat(mData.getAppSize()) * 3 >= FileUtil.getDataStorageAvailableSpace()) {
									Toast.makeText(context, "可用空间不足", Toast.LENGTH_SHORT).show();
									return;
								}
							}
							mDownloadManager.download(mData);
						}else {
							netChangeDialog.show();
						}
					}else if (mState == DownloadManager.STATE_DOWNLOADING
							|| mState == DownloadManager.STATE_WAITING) {
						mDownloadManager.pause(mData);
					}else if (mState == DownloadManager.STATE_DOWNLOADED 
							|| mState == DownloadManager.STATE_UNZIPED) {
						mDownloadManager.install(mData);
					}else if (mState == DownloadManager.STATE_INSTALLED) {
						mDownloadManager.open(mData.getPackageName());
					}else if (mState == DownloadManager.STATE_UNZIP_FAILED) {
						UnZipManager.getInstance().unzip(mData, Constants.PASSWORD,handler);
						mData.setDownloadState(DownloadManager.STATE_UNZIPING);
						DownloadAppinfo info = mDownloadManager.getDownloadInfo(mData.getId());
						info.setDownloadState(DownloadManager.STATE_UNZIPING);
						mDownloadManager.DBManager.getDownloadAppinfoDao().insertOrReplace(info);
						refreshState(info.getDownloadState());
					}
				}
			});
			
			if (hotIConVisible) {
				hotIcon.setVisibility(View.VISIBLE);
			}else {
				hotIcon.setVisibility(View.GONE);
			}
			
			if (subscriptVisible) {
				subscriptLin.setVisibility(View.VISIBLE);
			}else {
				subscriptLin.setVisibility(View.GONE);
			}
			
			initViewSize();
			
			return view;
		}
		
		Handler handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				if (msg.what == 2) {
					downloadBtn.setText(msg.arg1 + "%");
				}
			};
		};
		
		private void initViewSize(){
			UIUtil.setViewSize(icon, 170 * MetricsTool.Rx, 170 * MetricsTool.Rx);
			UIUtil.setViewSize(downloadBtn, 96 * MetricsTool.Rx, 76 * MetricsTool.Rx);
			UIUtil.setViewSize(subscriptLin, 85 * MetricsTool.Rx, 85 * MetricsTool.Rx);
			UIUtil.setViewSize(hotIcon, 55 * MetricsTool.Rx, 35 * MetricsTool.Rx);
			UIUtil.setViewWidth(downloadRel, 160 * MetricsTool.Rx);
			
			UIUtil.setTextSize(gameName, 35);
			UIUtil.setTextSize(gameDes, 25);
			UIUtil.setTextSize(gameSize, 25);
			UIUtil.setTextSize(subscriptTx, 35);
			UIUtil.setTextSize(stateText, 30);
			
			UIUtil.setTextSize(downloadBtn, 20);
			
			try {
				UIUtil.setViewSizeMargin(icon, 35 * MetricsTool.Rx, 30 * MetricsTool.Ry, 35 * MetricsTool.Rx, 30 * MetricsTool.Ry);
				UIUtil.setViewSizeMargin(downloadRel,0, 0, 35 * MetricsTool.Rx, 0);
				UIUtil.setViewSizeMargin(stateText,0, 10 * MetricsTool.Ry, 0, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private void setData(DownloadAppinfo data){
			if (mDownloadManager == null) {
				mDownloadManager = DownloadManager.getInstance();
			}
			String filePath = "";
			if (data.getIsZip()) {
				filePath = data.getZipPath();
			}else {
				filePath = data.getApkPath();
			}
			
			mState = data.getDownloadState();
			DownloadAppinfo downloadInfo = mDownloadManager.getDownloadInfo(data.getId());
			if (data.getDownloadState() != DownloadManager.STATE_INSTALLED
					&& data.getDownloadState() != DownloadManager.STATE_NEED_UPDATE) {
				hasAttached = FileUtil.isValidAttach(filePath, false);
				if (downloadInfo != null) {
					if (hasAttached) {
						if(!downloadInfo.getHasFinished()){
								if ((downloadInfo.getDownloadState() == DownloadManager.STATE_DOWNLOADING
										|| downloadInfo.getDownloadState() == DownloadManager.STATE_WAITING) 
										&& !mDownloadManager.isDownloading(downloadInfo.getId())) {
									mState = DownloadManager.STATE_PAUSED;
								}else {
									mState = downloadInfo.getDownloadState();
								}
						}else {
							if (downloadInfo.getDownloadState() == DownloadManager.STATE_UNZIPING
									&& !UnZipManager.getInstance().isUnZiping(downloadInfo.getPackageName())) {
								mState = DownloadManager.STATE_UNZIP_FAILED;
							}else {
								mState = downloadInfo.getDownloadState();
							}
						}
						downloadInfo.setDownloadState(mState);
						mDownloadManager.DBManager.getDownloadAppinfoDao().insertOrReplace(downloadInfo);
					}else {
						if (downloadInfo.getDownloadState() == DownloadManager.STATE_WAITING
								&& mDownloadManager.isDownloading(downloadInfo.getId())) {
							mState = downloadInfo.getDownloadState();
						}else {
							mState = DownloadManager.STATE_NONE;
							mDownloadManager.DBManager.delete(data);
						}
					}
				}else {
					mState = DownloadManager.STATE_NONE;
				}
			}else {
				if (downloadInfo != null) {
					downloadInfo.setHasFinished(true);
					downloadInfo.setDownloadState(data.getDownloadState());
					mDownloadManager.DBManager.getDownloadAppinfoDao().insertOrReplace(downloadInfo);
				}else {
					data.setHasFinished(true);
//					mDownloadManager.DBManager.getDownloadAppinfoDao().insertOrReplace(data);
				}
			}
			this.mData = data;
			refreshView();
		}
		
		private DownloadAppinfo getData(){
			return mData;
		}
		
		private void refreshView(){
			gameName.setText(mData.getAppName());
			gameDes.setText(mData.getDes());
			gameScore.setRating(Float.parseFloat(mData.getScore()));
			gameSize.setText(FileUtil.getSize(Long.parseLong(mData.getAppSize())));
			ImageListener listener = ImageLoader.getImageListener(icon, R.drawable.default_tubiao, R.drawable.default_tubiao);
			StoreApplication.getInstance().getImageLoader().get(mData.getIconUrl(), listener);
			
			refreshState(mState);
		}
		
		public void refreshState(int state){
			mState = state;
			downloadBtn.clearAnimation();
			switch (mState) {
			case DownloadManager.STATE_NONE:
				downloadBtn.setText("");
				downloadBtn.setBackgroundResource(R.drawable.download_selector);
				stateText.setText("下载");
				break;
			case DownloadManager.STATE_PAUSED:
				downloadBtn.setText("");
				downloadBtn.setBackgroundResource(R.drawable.zanting);
				stateText.setText("继续");
				break;
			case DownloadManager.STATE_ERROR:
				downloadBtn.setText("");
				downloadBtn.setBackgroundResource(R.drawable.xiazai_failed);
				stateText.setText("下载失败");
				break;
			case DownloadManager.STATE_WAITING:
				downloadBtn.setText("");
				downloadBtn.setBackgroundResource(R.drawable.wait_anim);
				AnimationDrawable animationDrawable = (AnimationDrawable) downloadBtn.getBackground();
				animationDrawable.start();
//				Animation animation = AnimationUtils.loadAnimation(context, R.anim.wating_rorating);
//				downloadBtn.setAnimation(animation);
//				downloadBtn.startAnimation(animation);
				stateText.setText("等待下载");
				break;
			case DownloadManager.STATE_DOWNLOADING:
				downloadBtn.setText("");
				downloadBtn.setBackgroundResource(R.drawable.jixu);
				stateText.setText("正在下载");
				break;
			case DownloadManager.STATE_DOWNLOADED:
				downloadBtn.setText("");
				downloadBtn.setBackgroundResource(R.drawable.runing_selector);
					if (mData.getIsZip()) {
						stateText.setText("解压");
					}else {
						stateText.setText("安装");
					}
				break;
			case DownloadManager.STATE_UNZIPING:
				downloadBtn.setBackgroundResource(R.drawable.jieya);
				stateText.setText("正在解压");
				break;
			case DownloadManager.STATE_UNZIPED:
				downloadBtn.setText("");
				downloadBtn.setBackgroundResource(R.drawable.runing_selector);
				stateText.setText("安装");
				break;
			case DownloadManager.STATE_INSTALLED:
				downloadBtn.setText("");
				downloadBtn.setBackgroundResource(R.drawable.runing_selector);
				stateText.setText("打开");
				break;
			case DownloadManager.STATE_NEED_UPDATE:
				downloadBtn.setText("");
				downloadBtn.setBackgroundResource(R.drawable.shengji);
				stateText.setText("升级");
				break;
			case DownloadManager.STATE_UNZIP_FAILED:
				downloadBtn.setText("");
				downloadBtn.setBackgroundResource(R.drawable.xiazai_failed);
				stateText.setText("解压失败");
			default:
				break;
			}
		}
		
		public void setUnZipProgress(int progress){
			downloadBtn.setText(progress + "%");
		}
		
//		Handler handler = new Handler(){
//			public void handleMessage(android.os.Message msg) {
//				int what = msg.what;
//				if (what == UnZipManager.UNZIP_SUCCESS) {
//					Toast.makeText(context, "解压完成", Toast.LENGTH_SHORT).show();
//					DownloadAppinfo info = mDownloadManager.getDownloadInfo(mData.getId());
//					info.setDownloadState(DownloadManager.STATE_UNZIPED);
//					DownloadManager.DBManager.insertOrReplace(info);
//				}else if (what == UnZipManager.UNZIP_FAILE) {
//					Toast.makeText(context, "解压失败", Toast.LENGTH_SHORT).show();
//					DownloadAppinfo info = mDownloadManager.getDownloadInfo(mData.getId());
//					info.setDownloadState(DownloadManager.STATE_UNZIP_FAILED);
//					DownloadManager.DBManager.insertOrReplace(info);
//				}
//			};
//		};
	}
	
	private class Holder3{
		private TextView title;
	}

	private void refreshHolder(final DownloadAppinfo info){
		List<Holder2> displayedHolder2s = getDisplayedHolders();
		for(int i = 0; i < displayedHolder2s.size(); i++){
			final Holder2 holder2 = displayedHolder2s.get(i);
			DownloadAppinfo appInfo = holder2.getData();
			if (appInfo.getId().equals(info.getId())) {
				AppUtil.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						holder2.refreshState(info.getDownloadState());
					}
				});
			}
		}
	}
	
	@Override
	public void onDownloadStateChanged(DownloadAppinfo info) {
		// TODO Auto-generated method stub
		refreshHolder(info);
	}

	@Override
	public void onDownloadProgressed(DownloadAppinfo info) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onUnZipProgressed(DownloadAppinfo info, final int progress) {
		// TODO Auto-generated method stub
		List<Holder2> displayedHolder2s = getDisplayedHolders();
		for(int i = 0; i < displayedHolder2s.size(); i++){
			final Holder2 holder2 = displayedHolder2s.get(i);
			DownloadAppinfo appInfo = holder2.getData();
			if (appInfo.getId().equals(info.getId())) {
				AppUtil.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						holder2.setUnZipProgress(progress);
					}
				});
			}
		}
	}
}
