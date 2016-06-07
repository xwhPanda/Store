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
import com.jiqu.download.AppUtil;
import com.jiqu.download.DownloadManager;
import com.jiqu.download.DownloadManager.DownloadObserver;
import com.jiqu.download.FileUtil;
import com.jiqu.download.UnZipManager;
import com.vr.store.R;
import com.jiqu.tools.Constants;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.RatingBarView;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

public class DownloadedAdapter extends BaseAdapter implements DownloadObserver {
	private Context context;
	private List<DownloadAppinfo> downloadAppinfos;
	private List<Holder> mDisplayedHolders;

	private Map<String, Boolean> checkMap = new ConcurrentHashMap<String, Boolean>();

	public DownloadedAdapter(Context context, List<DownloadAppinfo> downloadAppinfos) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.downloadAppinfos = downloadAppinfos;
		mDisplayedHolders = new ArrayList<Holder>();
	}

	public void putAllMap(boolean isChecked) {
		for (DownloadAppinfo downloadAppinfo : downloadAppinfos) {
			checkMap.put(downloadAppinfo.getId(), isChecked);
		}
	}

	public void startObserver() {
		DownloadManager.getInstance().registerObserver(this);
	}

	public void stopObserver() {
		DownloadManager.getInstance().unRegisterObserver(this);
	}

	public List<DownloadAppinfo> getList() {
		return downloadAppinfos;
	}

	public void showAllCheckbox(boolean visible) {
		ArrayList<Holder> holders = (ArrayList<Holder>) getDisplayedHolders();
		for (int i = 0; i < holders.size(); i++) {
			if (visible) {
				holders.get(i).checkBox.setVisibility(View.VISIBLE);
			} else {
				holders.get(i).checkBox.setVisibility(View.INVISIBLE);
			}
		}
	}

	public List<Holder> getDisplayedHolders() {
		synchronized (mDisplayedHolders) {
			return new ArrayList<Holder>(mDisplayedHolders);
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
		mDisplayedHolders.add(holder);
		convertView = holder.getRootView();
		return convertView;
	}

	public void deleteAll() {
		List<DownloadAppinfo> infos = new ArrayList<DownloadAppinfo>(downloadAppinfos);
		for (int i = 0;i < infos.size();i++) {
			DownloadAppinfo downloadAppinfo = infos.get(i);
			if (checkMap.get(downloadAppinfo.getId())) {
				Intent intent = new Intent();
				intent.setAction("deleted_downloaded_files_action");
				intent.putExtra("pkg", downloadAppinfo.getPackageName());
				context.sendBroadcast(intent);
				deleteFile(downloadAppinfo);
				mDisplayedHolders.remove(this);
				Message msg = handler.obtainMessage();
				msg.obj = downloadAppinfo;
				msg.what = 1;
				msg.arg1 = i;
				handler.sendMessage(msg);
			}
		}
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				DownloadAppinfo downloadAppinfo = (DownloadAppinfo) msg.obj;
				downloadAppinfos.remove(downloadAppinfo);
				notifyDataSetChanged();
//				UIUtil.removeListItem(mDisplayedHolders.get(msg.arg1).getParentView());
			}
		};
	};

	public void deleteFile(DownloadAppinfo downloadAppinfo) {
		if (downloadAppinfo != null) {
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
			DownloadManager.DBManager.getDownloadAppinfoDao().delete(downloadAppinfo);
		}
	}

	private class Holder {
		private Context context;
		private View rootView;

		private RelativeLayout parent;
		private CheckBox checkBox;
		private ImageView appIcon;
		private TextView appName;
		private TextView appDes;
		private RatingBarView appScore;
		private RelativeLayout openRel;
		private TextView appSize;
		private Button open;
		private Button delete;
		private TextView state;

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
		
		public View getParentView(){
			return parent;
		}

		private View initView() {
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.downloaded_item_layout, null);
			parent = (RelativeLayout) view.findViewById(R.id.parent);
			checkBox = (CheckBox) view.findViewById(R.id.checkBox);
			appIcon = (ImageView) view.findViewById(R.id.appIcon);
			appName = (TextView) view.findViewById(R.id.appName);
			appDes = (TextView) view.findViewById(R.id.appDes);
			appScore = (RatingBarView) view.findViewById(R.id.appScore);
			appSize = (TextView) view.findViewById(R.id.appSize);
			open = (Button) view.findViewById(R.id.open);
			delete = (Button) view.findViewById(R.id.delete);
			state = (TextView) view.findViewById(R.id.state);
			openRel = (RelativeLayout) view.findViewById(R.id.openRel);

			resIDs[0] = R.drawable.ratingbg;
			resIDs[1] = R.drawable.rating_sencond_progress;
			resIDs[2] = R.drawable.rating_progress;
			appScore.setResID(resIDs);
			appScore.setStep(0.5f);

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
									UnZipManager.getInstance().unzip(info, Constants.PASSWORD, null);
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

			delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					DownloadManager.getInstance().cancel(info);
//					DownloadManager.DBManager.getDownloadAppinfoDao().deleteByKey(info.getId());
					mDisplayedHolders.remove(this);
					UIUtil.removeListItem(rootView,new AnimationListener() {
						
						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationEnd(Animation animation) {
							// TODO Auto-generated method stub
							downloadAppinfos.remove(info);
							notifyDataSetChanged();
						}
					});
				}
			});

			initViewSize(view);
			return view;
		}

		private void initViewSize(View view) {
			UIUtil.setViewSize(checkBox, 56 * MetricsTool.Rx, 56 * MetricsTool.Ry);
			UIUtil.setViewSize(appIcon, 160 * MetricsTool.Rx, 160 * MetricsTool.Ry);
			UIUtil.setViewSize(open, 96 * MetricsTool.Rx, 76 * MetricsTool.Rx);
			UIUtil.setViewSize(delete, 76 * MetricsTool.Rx, 76 * MetricsTool.Rx);
			UIUtil.setViewWidth(openRel, 120 * MetricsTool.Rx);

			UIUtil.setTextSize(appName, 40);
			UIUtil.setTextSize(open, 35);
			UIUtil.setTextSize(appDes, 30);
			UIUtil.setTextSize(appSize, 30);
			UIUtil.setTextSize(state, 30);

			AbsListView.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (255 * MetricsTool.Ry));
			view.setLayoutParams(lp);

			try {
				UIUtil.setViewSizeMargin(checkBox, 20 * MetricsTool.Rx, 0, 20 * MetricsTool.Rx, 0);
				UIUtil.setViewSizeMargin(appName, 20 * MetricsTool.Rx, 60 * MetricsTool.Ry, 0, 0);
				UIUtil.setViewSizeMargin(appDes, 0, 10 * MetricsTool.Ry, 20 * MetricsTool.Rx, 0);
				UIUtil.setViewSizeMargin(appScore, 0, 10 * MetricsTool.Ry, 0, 0);
				UIUtil.setViewSizeMargin(openRel, 0, 0, 20 * MetricsTool.Rx, 0);
				UIUtil.setViewSizeMargin(delete, 0, 0, 20 * MetricsTool.Rx, 0);
				UIUtil.setViewSizeMargin(appSize, 30 * MetricsTool.Rx, 0, 0, 0);
				UIUtil.setViewSizeMargin(state, 0, 10 * MetricsTool.Ry, 0, 0);
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
			appScore.setRating(Float.parseFloat(info.getScore()));
			appSize.setText(FileUtil.getSize(Long.parseLong(info.getAppSize())));

			setState();
		}

		private void setState() {
			switch (info.getDownloadState()) {
			case DownloadManager.STATE_DOWNLOADED:
				open.setText("");
				open.setBackgroundResource(R.drawable.runing_selector);
				if (info.getIsZip()) {
					state.setText("解压");
				} else {
					state.setText("安装");
				}
				break;

			case DownloadManager.STATE_INSTALLED:
				open.setText("");
				open.setBackgroundResource(R.drawable.runing_selector);
				state.setText("打开");
				break;
			case DownloadManager.STATE_UNZIP_FAILED:
				open.setText("");
				open.setBackgroundResource(R.drawable.xiazai_failed);
				state.setText("解压失败");
				break;
			case DownloadManager.STATE_UNZIPING:
				open.setBackgroundResource(R.drawable.jieya);
				state.setText("正在解压");
				break;
			case DownloadManager.STATE_UNZIPED:
				open.setText("");
				open.setBackgroundResource(R.drawable.runing_selector);
				state.setText("安装");
				break;
			}
		}

		public void setUnZipProgress(int progress) {
			open.setText(progress + "%");
		}
	}

	private void refresh(DownloadAppinfo info, final int progress) {
		List<Holder> displayedHolders = getDisplayedHolders();
		for (int i = 0; i < displayedHolders.size(); i++) {
			final Holder holder = displayedHolders.get(i);
			if (holder != null) {
				final DownloadAppinfo appInfo = holder.getData();
				if (appInfo.getId().equals(info.getId())) {
					AppUtil.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							holder.setUnZipProgress(progress);
						}
					});
				}
			}
		}
	}

	@Override
	public void onDownloadStateChanged(DownloadAppinfo info) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onDownloadProgressed(DownloadAppinfo info) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnZipProgressed(DownloadAppinfo info, int progress) {
		// TODO Auto-generated method stub
		refresh(info, progress);
	}
	
}
