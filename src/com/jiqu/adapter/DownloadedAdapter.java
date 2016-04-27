package com.jiqu.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

	private Map<Long, Boolean> checkMap = new ConcurrentHashMap<Long, Boolean>();

	public DownloadedAdapter(Context context, List<DownloadAppinfo> downloadAppinfos) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.downloadAppinfos = downloadAppinfos;
	}

	public void putAllMap(boolean isChecked) {
		for (DownloadAppinfo downloadAppinfo : downloadAppinfos) {
			checkMap.put(downloadAppinfo.getId(), isChecked);
		}
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
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.setData(downloadAppinfos.get(position));
		return holder.getRootView();
	}

	public void deleteAll() {
		for (final DownloadAppinfo downloadAppinfo : downloadAppinfos) {
			if (checkMap.get(downloadAppinfo.getId())) {
				AppUtil.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						QueryBuilder qb = DownloadManager.DBManager.getDownloadAppinfoDao().queryBuilder();
						Intent intent = new Intent();
						intent.setAction("deleted_downloaded_files_action");
						intent.putExtra("pkg", downloadAppinfo.getPackageName());
						context.sendBroadcast(intent);
						if (downloadAppinfo != null) {
							DownloadAppinfo info = (DownloadAppinfo) qb.where(Properties.Id.eq(downloadAppinfo.getId())).unique();
							if (info != null) {
								if (info.getIsZip()) {
									File file1 = new File(info.getZipPath());
									if (file1.exists()) {
										file1.delete();
									}
									File file2 = new File(info.getUnzipPath());
									if (file2.exists()) {
										file2.delete();
									}
								} else {
									File file = new File(info.getApkPath());
									if (file.exists()) {
										file.delete();
									}
								}
								DownloadManager.DBManager.getDownloadAppinfoDao().delete(info);
							} else {
								if (downloadAppinfo.getIsZip()) {
									File file1 = new File(downloadAppinfo.getZipPath());
									if (file1.exists()) {
										file1.delete();
									}
									File file2 = new File(downloadAppinfo.getUnzipPath());
									if (file2.exists()) {
										file2.delete();
									}
								} else {
									File file = new File(downloadAppinfo.getApkPath());
									if (file.exists()) {
										file.delete();
									}
								}
							}
						}
						synchronized (downloadAppinfos) {
							downloadAppinfos.remove(downloadAppinfo);
						}
						notifyDataSetChanged();
					}
				});
			}
		}
	}

	private class Holder {
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

		public Holder(Context context) {
			this.context = context;
			rootView = initView();
			rootView.setTag(this);
		}

		public DownloadAppinfo getData() {
			return info;
		}

		private View initView() {
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
			appScore.setStep(1.0f);

			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					checkMap.put(info.getId(), isChecked);
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
								} else if (info.getDownloadState() == DownloadManager.STATE_DOWNLOADED) {
									DownloadManager.getInstance().install(info);
								}
							}
						} else {
							if (info.getHasFinished()) {
								if (info.getDownloadState() == DownloadManager.STATE_UNZIP_FAILED || info.getDownloadState() == DownloadManager.STATE_DOWNLOADED) {
									UnZipManager.getInstance().unzip(info, Constant.PASSWORD, null);
								} else if (info.getDownloadState() == DownloadManager.STATE_UNZIPED) {
									DownloadManager.getInstance().install(info);
									// UnZipManager.getInstance().unzip(info,
									// Constant.PASSWORD,null);
								} else if (info.getDownloadState() == DownloadManager.STATE_INSTALLED) {
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

		private void initViewSize(View view) {
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

		private View getRootView() {
			return rootView;
		}

		public void setData(DownloadAppinfo info) {
			this.info = info;
			ImageListener listener = ImageLoader.getImageListener(appIcon, R.drawable.ic_launcher, R.drawable.ic_launcher);
			StoreApplication.getInstance().getImageLoader().get(info.getIconUrl(), listener);
			if (checkMap.containsKey(info.getId())) {
				checkBox.setChecked(checkMap.get(info.getId()));
			}
			appName.setText(info.getAppName());
			appDes.setText(info.getDes());
			appScore.setRating(info.getScore());
			appSize.setText(FileUtil.getSize(Long.parseLong(info.getAppSize())));

		}
	}

}
