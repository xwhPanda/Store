package com.jiqu.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.jiqu.application.StoreApplication;
import com.jiqu.database.DownloadAppinfo;
import com.jiqu.database.DownloadAppinfoDao.Properties;
import com.jiqu.download.AppUtil;
import com.jiqu.download.DownloadManager;
import com.jiqu.download.FileUtil;
import com.jiqu.download.UnZipManager;
import com.jiqu.store.R;
import com.jiqu.tools.Constant;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.RatingBarView;

import de.greenrobot.dao.query.QueryBuilder;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

public class DownloadedAdapter extends BaseAdapter {
	private Context context;
	private List<DownloadAppinfo> downloadAppinfos;
	private List<Holder> mDisplayHolders;

	public DownloadedAdapter(Context context,List<DownloadAppinfo> downloadAppinfos) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.downloadAppinfos = downloadAppinfos;
		mDisplayHolders = new ArrayList<DownloadedAdapter.Holder>();
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder(context);
		}else {
			holder = (Holder) convertView.getTag();
		}
		holder.setData(downloadAppinfos.get(position));
		mDisplayHolders.add(holder);
		return holder.getRootView();
	}
	
	public void clearHolders(){
		mDisplayHolders.clear();
	}
	
	public void refreshHolderForChecked(final boolean isChecked){
		for(final Holder holder : mDisplayHolders){
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
	
	private List<Holder> getDisplayHolders(){
		synchronized (mDisplayHolders) {
			return new ArrayList<DownloadedAdapter.Holder>(mDisplayHolders);
		}
	}
	
	public void deleteAll(){
		List<Holder> holders = getDisplayHolders();
		for(final Holder holder : holders){
			if (holder.isChecked) {
				AppUtil.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						QueryBuilder qb = DownloadManager.DBManager.getDownloadAppinfoDao().queryBuilder();
						DownloadAppinfo appinfo = holder.getData();
						Intent intent = new Intent();
						intent.setAction("deleted_downloaded_files_action");
						intent.putExtra("pkg", appinfo.getPackageName());
						context.sendBroadcast(intent);
						if (appinfo != null) {
							DownloadAppinfo info = (DownloadAppinfo) qb.where(Properties.Id.eq(appinfo.getId())).unique();
							if (info != null) {
								DownloadManager.DBManager.getDownloadAppinfoDao().delete(info);
								if (info.getIsZip()) {
									File file1 = new File(info.getZipPath());
									if (file1.exists()) {
										file1.delete();
									}
									File file2 = new File(info.getUnzipPath());
									if (file2.exists()) {
										file2.delete();
									}
								}else {
									File file = new File(info.getApkPath());
									if (file.exists()) {
										file.delete();
									}
								}
								DownloadManager.DBManager.getDownloadAppinfoDao().delete(info);
							}else {
								if (appinfo.getIsZip()) {
									File file1 = new File(appinfo.getZipPath());
									if (file1.exists()) {
										file1.delete();
									}
									File file2 = new File(appinfo.getUnzipPath());
									if (file2.exists()) {
										file2.delete();
									}
								}else {
									File file = new File(appinfo.getApkPath());
									if (file.exists()) {
										file.delete();
									}
								}
							}
						}
						synchronized (downloadAppinfos) {
							downloadAppinfos.remove(holder.getData());
						}
						synchronized (mDisplayHolders) {
							holder.isChecked = false;
							mDisplayHolders.remove(holder);
						}
						notifyDataSetChanged();
					}
				});
			}
		}
	}
	
	private class Holder{
		private Context context;
		private View rootView;
		
		private CheckBox checkBox;
		private ImageView appIcon;
		private TextView appName;
		private TextView appDes;
		private RatingBarView appScore;
		private TextView appSize;
		private Button open;
		
		private DownloadAppinfo info;
		private int[] resIDs = new int[3];
		public boolean isChecked;
		
		public Holder(Context context){
			this.context = context;
			rootView = initView();
			rootView.setTag(this);
		}
		
		public DownloadAppinfo getData(){
			return info;
		}
		
		private View initView(){
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.downloaded_item_layout, null);
			checkBox = (CheckBox) view.findViewById(R.id.checkBox);
			appIcon = (ImageView) view.findViewById(R.id.appIcon);
			appName = (TextView) view.findViewById(R.id.appName);
			appDes = (TextView) view.findViewById(R.id.appDes);
			appScore = (RatingBarView) view.findViewById(R.id.appScore);
			appSize = (TextView) view.findViewById(R.id.appSize);
			open = (Button) view.findViewById(R.id.open);
			
			resIDs[0] = R.drawable.ratingbg;
			resIDs[1] = R.drawable.rating_sencond_progress;
			resIDs[2] = R.drawable.rating_progress;
			appScore.setResID(resIDs);
			
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					 setCheckedValue(isChecked);
				}
			});
			
			open.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (info != null) {
						if (!info.getIsZip()) {
							if (info.getHasFinished()) {
								if (info.getDownloadState() == DownloadManager.STATE_INSTALLED) {
									DownloadManager.getInstance().open(info.getPackageName());
								}else if (info.getDownloadState() == DownloadManager.STATE_DOWNLOADED) {
									DownloadManager.getInstance().install(info);
								}
							}
						}else {
							if (info.getHasFinished()) {
								if (info.getDownloadState() == DownloadManager.STATE_UNZIP_FAILED
										|| info.getDownloadState() == DownloadManager.STATE_DOWNLOADED) {
									UnZipManager.getInstance().unzip(info, Constant.PASSWORD);
								}else if (info.getDownloadState() == DownloadManager.STATE_UNZIPED) {
									DownloadManager.getInstance().install(info);
								}else if (info.getDownloadState() == DownloadManager.STATE_INSTALLED) {
									DownloadManager.getInstance().open(info.getPackageName());
								}
							}
						}
					}
				}
			});
			
			initViewSize(view);
			return view;
		}
		
		private void setCheckedValue(boolean isChecked){
			this.isChecked = isChecked;
		}
		
		private void initViewSize(View view){
			UIUtil.setViewSize(checkBox, 56 * MetricsTool.Rx, 56 * MetricsTool.Ry);
			UIUtil.setViewSize(appIcon, 160 * MetricsTool.Rx, 160 * MetricsTool.Ry);
			UIUtil.setViewSize(open, 60 * MetricsTool.Rx, 60 * MetricsTool.Rx);
			
			UIUtil.setTextSize(appName, 40);
			UIUtil.setTextSize(appDes, 30);
			UIUtil.setTextSize(appSize, 30);
			
			AbsListView.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (255 * MetricsTool.Ry));
			view.setLayoutParams(lp);
			
			try {
				UIUtil.setViewSizeMargin(checkBox, 20 * MetricsTool.Rx, 0, 20 * MetricsTool.Rx, 0);
				UIUtil.setViewSizeMargin(appName, 20 * MetricsTool.Rx, 60 * MetricsTool.Ry, 0, 0);
				UIUtil.setViewSizeMargin(appDes, 0, 10 * MetricsTool.Ry, 20 * MetricsTool.Rx, 0);
				UIUtil.setViewSizeMargin(appScore, 0, 10 * MetricsTool.Ry, 0, 0);
				UIUtil.setViewSizeMargin(open, 0, 0, 60 * MetricsTool.Rx, 0);
				UIUtil.setViewSizeMargin(appSize, 30 * MetricsTool.Rx, 0, 0, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private View getRootView(){
			return rootView;
		}
		
		public void setData(DownloadAppinfo info){
			this.info = info;
			ImageListener listener = ImageLoader.getImageListener(appIcon,R.drawable.ic_launcher, R.drawable.ic_launcher);
			StoreApplication.getInstance().getImageLoader().get(info.getIconUrl(), listener);
			checkBox.setChecked(isChecked);
			appName.setText(info.getAppName());
			appDes.setText("换个哦it回家哈哈佛入伍辐射范围荣夫妇警方随即离开家里居然叫我加快老旧家具ioui哦");
			appScore.setRating(2.5);
			appSize.setText(FileUtil.getSize(Long.parseLong(info.getAppSize())));
			
		}
	}

}
