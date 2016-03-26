package com.jiqu.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.jiqu.application.StoreApplication;
import com.jiqu.database.DownloadAppinfo;
import com.jiqu.download.AppInfo;
import com.jiqu.download.AppUtil;
import com.jiqu.download.DownloadInfo;
import com.jiqu.download.DownloadManager;
import com.jiqu.download.DownloadManager.DownloadObserver;
import com.jiqu.download.FileUtil;
import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameAdapter extends BaseAdapter implements DownloadObserver{
	private List<AppInfo> informations;
	private LayoutInflater inflater;
	private Holder1 holder1 = null;
	private Holder3 holder3 = null;
	
	private List<Holder2> mDisplayedHolders;
	private Context context;
	
	public GameAdapter(Context context,List<AppInfo> informations){
		this.informations = informations;
		inflater = LayoutInflater.from(context);
		mDisplayedHolders = new ArrayList<GameAdapter.Holder2>();
		this.context = context;
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
	
	public void addItems(List<AppInfo> infos){
		informations.addAll(infos);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		int type = getItemViewType(position);
		Holder2 holder2 = null;
		switch (type) {
		case 0:
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.game_sort_title, null);
				holder1 = new Holder1();
				holder1.sortName = (TextView) convertView.findViewById(R.id.sortName);
				holder1.moreTx = (TextView) convertView.findViewById(R.id.moreTx);
				holder1.moreBtn = (ImageView) convertView.findViewById(R.id.moreBt);
				
				UIUtil.setTextSize(holder1.sortName, 42);
				UIUtil.setTextSize(holder1.moreTx, 35);
				UIUtil.setViewSize(holder1.moreBtn, 36 * MetricsTool.Rx, 36 * MetricsTool.Rx);
				convertView.setTag(holder1);
			}else {
				holder1 = (Holder1) convertView.getTag();
			}
			
			break;

		case 1:
			if (convertView == null) {
				holder2 = new Holder2(context);
			}else {
				holder2 = (Holder2) convertView.getTag();
			}
			
			holder2.setData(informations.get(position));
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
		return type == 1?holder2.getRootView():convertView;
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

	private String getSize(long size){
		String str = "0 M";
		if ((size / 1024) == 0) {
			return str;
		}else {
			if (size / 1024 / 1024 == 0) {
				return size / 1024 + " KB";
			}else {
				if (size / 1024 / 1024 / 1024 == 0) {
					return size / 1024 / 1024 + "M";
				}else {
					if(size / 1024 / 1024 / 1024 / 1024 == 0){
						return size / 1024 / 1024 / 1024 + "G";
					}
				}
			}
		}
		
		return str;
	}
	
	private class Holder1{
		private TextView sortName;
		private TextView moreTx;
		private ImageView moreBtn;
	}
	
	private class Holder2{
		private ImageView icon;
		private TextView gameName;
		private TextView gameDes;
		private RatingBar gameScore;
		private TextView gameSize;
		private Button downloadBtn;
		private TextView subscriptTx;
		private ImageView hotIcon;
		
		private RelativeLayout informationLin;
		private LinearLayout subscriptLin;
		
		private View rootView;
		
		private int mState;
		private float mProgress;
		public AppInfo mData;
		private DownloadManager mDownloadManager;
		private boolean hasAttached;
		
		private Context context;
		
		public Holder2(Context context){
			rootView = initView();
			rootView.setTag(this);
			this.context = context;
		}
		
		public View getRootView(){
			return rootView;
		}
		
		private View initView(){
			View view = AppUtil.inflate(R.layout.recommend_game_item);
			icon = (ImageView) view.findViewById(R.id.icon);
			gameName = (TextView) view.findViewById(R.id.gameName);
			gameDes = (TextView) view.findViewById(R.id.gameDes);
			gameScore = (RatingBar) view.findViewById(R.id.gameScore);
			gameSize = (TextView) view.findViewById(R.id.gameSize);
			downloadBtn = (Button) view.findViewById(R.id.downloadBtn);
			subscriptTx = (TextView) view.findViewById(R.id.subscriptTx);
			hotIcon = (ImageView) view.findViewById(R.id.hotIcon);
			
			gameDes.setText("哈佛哈减肥哈弗哈佛哈佛哈克繁华过后");
			
			informationLin = (RelativeLayout) view.findViewById(R.id.informationLin);
			subscriptLin = (LinearLayout) view.findViewById(R.id.subscriptLin);
			
			downloadBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (mState == DownloadManager.STATE_NONE 
							|| mState == DownloadManager.STATE_PAUSED
							|| mState == DownloadManager.STATE_ERROR) {
						mDownloadManager.download(mData);
					}else if (mState == DownloadManager.STATE_DOWNLOADING
							|| mState == DownloadManager.STATE_WAITING) {
						mDownloadManager.pause(mData);
					}else if (mState == DownloadManager.STATE_DOWNLOADED) {
						mDownloadManager.install(mData);
					}
				}
			});
			
			initViewSize();
			
			return view;
		}
		
		private void initViewSize(){
			UIUtil.setViewSize(icon, 170 * MetricsTool.Rx, 170 * MetricsTool.Rx);
			UIUtil.setViewSize(downloadBtn, 75 * MetricsTool.Rx, 75 * MetricsTool.Rx);
			UIUtil.setViewSize(subscriptLin, 85 * MetricsTool.Rx, 85 * MetricsTool.Rx);
			
			UIUtil.setTextSize(gameName, 35);
			UIUtil.setTextSize(gameDes, 25);
			UIUtil.setTextSize(gameSize, 25);
			UIUtil.setTextSize(subscriptTx, 35);
			
			try {
				UIUtil.setViewSizeMargin(icon, 35 * MetricsTool.Rx, 30 * MetricsTool.Ry, 35 * MetricsTool.Rx, 30 * MetricsTool.Ry);
				UIUtil.setViewSizeMargin(downloadBtn, 30, 0, 35 * MetricsTool.Rx, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private void setData(AppInfo data){
			if (mDownloadManager == null) {
				mDownloadManager = DownloadManager.getInstance();
			}
			String filePath = FileUtil.getDownloadDir(AppUtil.getContext()) + File.separator + data.getName() + ".apk";
			boolean existsFile = FileUtil.isExistsFile(filePath);
			if (existsFile) {
				long fileSize = FileUtil.getFileSize(filePath);
				if (fileSize == Long.parseLong(data.getSize())) {
					DownloadAppinfo downloadInfo = AppInfo.toDownloadAppInfo(data);
					downloadInfo.setCurrentSize(fileSize);
					downloadInfo.setHasFinished(true);
					downloadInfo.setProgress(1.0f);
					downloadInfo.setDownloadState(DownloadManager.STATE_DOWNLOADED);
					DownloadManager.DBManager.insertOrReplace(downloadInfo);
					mDownloadManager.setDownloadInfo(data.getId(), downloadInfo);
				}
			}
			
			DownloadAppinfo downloadInfo = mDownloadManager.getDownloadInfo(data.getId());
			if (downloadInfo != null) {
				mState = downloadInfo.getDownloadState();
				mProgress = downloadInfo.getProgress();
			}else {
				mState = DownloadManager.STATE_NONE;
				mProgress = 0;
			}
			this.mData = data;
			refreshView();
		}
		
		private AppInfo getData(){
			return mData;
		}
		
		private void refreshView(){
			gameName.setText(mData.getName());
//			gameDes.setText(mData.getDesinfo());
			gameScore.setRating((float) 4.5);
			gameSize.setText(getSize(Long.parseLong(mData.getSize())));
			
			@SuppressWarnings("deprecation")
			ImageRequest imageRequest = new ImageRequest(
					mData.getLogo_url_160(),  
			        new Response.Listener<Bitmap>() {  
			            @Override  
			            public void onResponse(Bitmap response) {  
			                icon.setImageBitmap(response); 
			            }  
			        }, 0, 0, Config.RGB_565, new Response.ErrorListener() {  
			            @Override  
			            public void onErrorResponse(VolleyError error) {  
			            	icon.setImageResource(R.drawable.ic_launcher);  
			            }  
			        });
			StoreApplication.getInstance().getRequestQueue().add(imageRequest);
			
			String  path=FileUtil.getDownloadDir(AppUtil.getContext()) + File.separator + mData.getName() + ".apk";
			hasAttached = FileUtil.isValidAttach(path, false);

			DownloadAppinfo downloadInfo = mDownloadManager.getDownloadInfo(mData.getId());
			if (downloadInfo != null && hasAttached) {
				if(downloadInfo.getHasFinished()){

					mState = DownloadManager.STATE_DOWNLOADED;
				}else{
					mState = DownloadManager.STATE_PAUSED;

				}

			} else {
				mState = DownloadManager.STATE_NONE;
				if(downloadInfo !=null){
					downloadInfo.setDownloadState(mState);
				}
			}
			refreshState(mState, mProgress);
		}
		
		public void refreshState(int state , float progress){
			mState = state;
			mProgress = progress;
			downloadBtn.clearAnimation();
			switch (mState) {
			case DownloadManager.STATE_NONE:
				downloadBtn.setBackgroundResource(R.drawable.download_selector);
				break;

			case DownloadManager.STATE_PAUSED:
				downloadBtn.setBackgroundResource(R.drawable.zanting);
				break;
			case DownloadManager.STATE_ERROR:
				downloadBtn.setBackgroundResource(R.drawable.download_selector);
				break;
			case DownloadManager.STATE_WAITING:
				downloadBtn.setBackgroundResource(R.drawable.dengdai);
				Animation animation = AnimationUtils.loadAnimation(context, R.anim.wating_rorating); 
				downloadBtn.setAnimation(animation);
				downloadBtn.startAnimation(animation);
				break;
			case DownloadManager.STATE_DOWNLOADING:
				downloadBtn.setBackgroundResource(R.drawable.jixu);
				break;
			case DownloadManager.STATE_DOWNLOADED:
				downloadBtn.setBackgroundResource(R.drawable.runing_selector);
				break;
			default:
				break;
			}
		}
	}
	
	private class Holder3{
		private TextView title;
	}

	private void refreshHolder(final DownloadAppinfo info){
		List<Holder2> displayedHolder2s = getDisplayedHolders();
		for(int i = 0; i < displayedHolder2s.size(); i++){
			final Holder2 holder2 = displayedHolder2s.get(i);
			AppInfo appInfo = holder2.getData();
			if (appInfo.getId() == info.getId()) {
				AppUtil.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						holder2.refreshState(info.getDownloadState(), info.getProgress());
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
		refreshHolder(info);
	}
}
