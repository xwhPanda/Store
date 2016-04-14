package com.jiqu.adapter;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.jiqu.application.StoreApplication;
import com.jiqu.database.DownloadAppinfo;
import com.jiqu.database.DownloadAppinfoDao.Properties;
import com.jiqu.download.AppUtil;
import com.jiqu.download.DownloadManager;
import com.jiqu.download.DownloadManager.DownloadObserver;
import com.jiqu.download.FileUtil;
import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import de.greenrobot.dao.query.QueryBuilder;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

public class DownloadingAdapter extends BaseAdapter implements DownloadObserver{
	private Context context;
	private List<DownloadAppinfo> downloadAppinfos;
	private List<Holder> mDisplayedHolders;
	private Handler handler;

	public DownloadingAdapter(Context context,List<DownloadAppinfo> downloadAppinfos,Handler handler) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.handler = handler;
		mDisplayedHolders = new ArrayList<DownloadingAdapter.Holder>();
		this.downloadAppinfos = downloadAppinfos;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return downloadAppinfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return downloadAppinfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public void startObserver() {
		DownloadManager.getInstance().registerObserver(this);
	}

	public void stopObserver() {
		DownloadManager.getInstance().unRegisterObserver(this);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		DownloadAppinfo appinfo = downloadAppinfos.get(position);
		if (convertView == null) {
			holder = new Holder(context);
		}else {
			holder = (Holder) convertView.getTag();
		}
		holder.setData(appinfo);
		mDisplayedHolders.add(holder);
		
		return holder.getRootView();
	}
	
	public void clearHolders(){
		mDisplayedHolders.clear();
	}
	
	private class Holder{
		private CheckBox checkBox;
		private ImageView appIcon;
		private TextView appName;
		private ProgressBar downloadPrg;
		private TextView progressTx;
		private Button pause;
		
		private Context context;
		private View rootView;
		private DownloadAppinfo info;
		private boolean isChecked;
		
		public Holder(Context context){
			this.context = context;
			rootView = initView();
			rootView.setTag(this);
		}
		
		public View getRootView(){
			return rootView;
		}
		
		public DownloadAppinfo getData(){
			return info;
		}
		
		private View initView(){
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.downloading_item_layout, null);
			
			checkBox = (CheckBox) view.findViewById(R.id.checkBox);
			appIcon = (ImageView) view.findViewById(R.id.appIcon);
			appName = (TextView) view.findViewById(R.id.appName);
			downloadPrg = (ProgressBar) view.findViewById(R.id.downloadPrg);
			progressTx = (TextView) view.findViewById(R.id.progressTx);
			pause = (Button) view.findViewById(R.id.pause);
			pause.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					QueryBuilder qb = DownloadManager.DBManager.getDownloadAppinfoDao().queryBuilder();
					DownloadAppinfo downloadAppinfo = (DownloadAppinfo) qb.where(Properties.Id.eq(info.getId())).unique();
					if (downloadAppinfo != null) {
						int state = info.getDownloadState();
						if (state == DownloadManager.STATE_DOWNLOADING
								|| state == DownloadManager.STATE_WAITING) {
							DownloadManager.getInstance().pause(info);
							pause.setBackgroundResource(R.drawable.zanting);
						}else if (state == DownloadManager.STATE_ERROR 
								|| state == DownloadManager.STATE_NONE
								|| state == DownloadManager.STATE_PAUSED) {
							DownloadManager.getInstance().download(info);
							pause.setBackgroundResource(R.drawable.jixu);
						}
					}
				}
			});
			
			initViewSize(view);
			
			
			
			return view;
		}
		
		private void initViewSize(View view){
			UIUtil.setViewSize(checkBox, 56 * MetricsTool.Rx, 56 * MetricsTool.Ry);
			UIUtil.setViewSize(appIcon, 160 * MetricsTool.Rx, 160 * MetricsTool.Ry);
			UIUtil.setViewSize(pause, 60 * MetricsTool.Rx, 60 * MetricsTool.Rx);
			UIUtil.setViewSize(downloadPrg, 545 * MetricsTool.Rx, 20 * MetricsTool.Rx);
			
			UIUtil.setTextSize(appName, 40);
			UIUtil.setTextSize(progressTx, 30);
			
			AbsListView.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (255 * MetricsTool.Ry));
			view.setLayoutParams(lp);
			
			try {
				UIUtil.setViewSizeMargin(checkBox, 20 * MetricsTool.Rx, 0, 20 * MetricsTool.Rx, 0);
				UIUtil.setViewSizeMargin(appName, 20 * MetricsTool.Rx, 60 * MetricsTool.Ry, 0, 0);
				UIUtil.setViewSizeMargin(downloadPrg, 0, 10 * MetricsTool.Ry, 0, 0);
				UIUtil.setViewSizeMargin(progressTx, 0, 10 * MetricsTool.Ry, 0, 0);
				UIUtil.setViewSizeMargin(pause, 0, 0, 60 * MetricsTool.Rx, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private void setData(DownloadAppinfo appinfo){
			this.info = appinfo;
			ImageListener listener = ImageLoader.getImageListener(appIcon,R.drawable.ic_launcher, R.drawable.ic_launcher);
			StoreApplication.getInstance().getImageLoader().get(info.getIconUrl(), listener);
			appName.setText(appinfo.getAppName());
			
			checkBox.setChecked(isChecked);
			
			refreshView(appinfo);
		}
		
		public void refreshView(DownloadAppinfo appinfo){
			pause.clearAnimation();
			downloadPrg.setProgress((int)(appinfo.getProgress() * 100));
			progressTx.setText(FileUtil.getFileSize(appinfo.getCurrentSize()) + "/" + FileUtil.getFileSize(Long.parseLong(appinfo.getAppSize())));
			switch (appinfo.getDownloadState()) {
			case DownloadManager.STATE_NONE:
				pause.setBackgroundResource(R.drawable.download_selector);
				break;

			case DownloadManager.STATE_PAUSED:
				pause.setBackgroundResource(R.drawable.zanting);
				break;
			case DownloadManager.STATE_ERROR:
				pause.setBackgroundResource(R.drawable.download_selector);
				break;
			case DownloadManager.STATE_WAITING:
				pause.setBackgroundResource(R.drawable.dengdai);
				Animation animation = AnimationUtils.loadAnimation(context, R.anim.wating_rorating); 
				pause.setAnimation(animation);
				pause.startAnimation(animation);
				break;
			case DownloadManager.STATE_DOWNLOADING:
				pause.setBackgroundResource(R.drawable.jixu);
				break;
			case DownloadManager.STATE_DOWNLOADED:
				synchronized (handler) {
					handler.sendEmptyMessage(1);
				}
				pause.setBackgroundResource(R.drawable.runing_selector);
				break;
			default:
				break;
			}
		}
	}
	
	public List<Holder> getDisplayedHolders() {
		synchronized (mDisplayedHolders) {
			return new ArrayList<Holder>(mDisplayedHolders);
		}
	}

	private void refreshHolder(DownloadAppinfo info){
		List<Holder> displayedHolder2s = getDisplayedHolders();
		for(int i = 0; i < displayedHolder2s.size(); i++){
			final Holder holder = displayedHolder2s.get(i);
			final DownloadAppinfo appInfo = holder.getData();
			if (appInfo.getId().longValue() == info.getId().longValue()) {
				AppUtil.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						holder.refreshView(appInfo);
					}
				});
			}
		}
	}
	
	public void refreshHolderForCheck(final boolean isChecked){
		for(final Holder holder : mDisplayedHolders){
			AppUtil.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					holder.isChecked = isChecked;
					notifyDataSetChanged();
				}
			});
		}
	}
	
	public void startDownloadAll(){
		for(Holder holder : mDisplayedHolders){
			if (holder.isChecked) {
				DownloadManager.getInstance().download(holder.getData());
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
