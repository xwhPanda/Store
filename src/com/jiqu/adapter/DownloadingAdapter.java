package com.jiqu.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.jiqu.application.StoreApplication;
import com.jiqu.database.DownloadAppinfo;
import com.jiqu.download.AppUtil;
import com.jiqu.download.DownloadManager;
import com.jiqu.download.DownloadManager.DownloadObserver;
import com.jiqu.download.FileUtil;
import com.vr.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.NetReceiver;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.NetChangeDialog;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

public class DownloadingAdapter extends BaseAdapter implements DownloadObserver {
	private Context context;
	private List<DownloadAppinfo> downloadAppinfos;
	private List<Holder> mDisplayedHolders;
	private Handler handler;

	private Map<String, Boolean> checkMap = new ConcurrentHashMap<String, Boolean>();
	
	private List<DownloadAppinfo> deleteAppinfos = new ArrayList<DownloadAppinfo>();

	public DownloadingAdapter(Context context, List<DownloadAppinfo> downloadAppinfos, Handler handler) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.handler = handler;
		mDisplayedHolders = new ArrayList<DownloadingAdapter.Holder>();
		this.downloadAppinfos = downloadAppinfos;
	}

	public void putAllMap(boolean isChecked) {
		for (DownloadAppinfo downloadAppinfo : downloadAppinfos) {
			checkMap.put(downloadAppinfo.getId(), isChecked);
		}
	}
	
	public void showAllCheckbox(boolean visible){
		ArrayList<Holder> holders = (ArrayList<Holder>) getDisplayedHolders();
		for (int i = 0; i < holders.size(); i++) {
			if (visible) {
				holders.get(i).checkBox.setVisibility(View.VISIBLE);
			}else {
				holders.get(i).checkBox.setVisibility(View.INVISIBLE);
			}
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

	public void startObserver() {
		DownloadManager.getInstance().registerObserver(this);
	}

	public void stopObserver() {
		DownloadManager.getInstance().unRegisterObserver(this);
	}
	
	public List<DownloadAppinfo> getList(){
		return downloadAppinfos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		DownloadAppinfo appinfo = downloadAppinfos.get(position);
		if (convertView == null) {
			holder = new Holder(context);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.setData(appinfo,position);
		mDisplayedHolders.add(holder);

		return holder.getRootView();
	}

	public void clearHolders() {
		mDisplayedHolders.clear();
	}
	
	public void deleteFromDb(){
		for (DownloadAppinfo info : deleteAppinfos) {
			DownloadManager.DBManager.getDownloadAppinfoDao().delete(info);
		}
	}

	private class Holder {
		private CheckBox checkBox;
		private ImageView appIcon;
		private TextView appName;
		private ProgressBar downloadPrg;
		private TextView progressTx;
		private Button pause;
		private Button deleted;

		private Context context;
		private View rootView;
		private DownloadAppinfo info;
		private String appSize;
		private NetChangeDialog netChangeDialog;

		public Holder(Context context) {
			this.context = context;
			rootView = initView();
			rootView.setTag(this);
		}

		public View getRootView() {
			return rootView;
		}

		public DownloadAppinfo getData() {
			return info;
		}

		private View initView() {
			netChangeDialog = new NetChangeDialog(context);
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.downloading_item_layout, null);

			checkBox = (CheckBox) view.findViewById(R.id.checkBox);
			appIcon = (ImageView) view.findViewById(R.id.appIcon);
			appName = (TextView) view.findViewById(R.id.appName);
			downloadPrg = (ProgressBar) view.findViewById(R.id.downloadPrg);
			progressTx = (TextView) view.findViewById(R.id.progressTx);
			pause = (Button) view.findViewById(R.id.pause);
			deleted = (Button) view.findViewById(R.id.deleted);
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
					DownloadManager.getInstance().download(info);
					netChangeDialog.dismiss();
				}
			});
			pause.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int state = info.getDownloadState();
					if (state == DownloadManager.STATE_DOWNLOADING || state == DownloadManager.STATE_WAITING) {
						DownloadManager.getInstance().pause(info);
						pause.setBackgroundResource(R.drawable.zanting);
					} else if (state == DownloadManager.STATE_ERROR || state == DownloadManager.STATE_NONE || state == DownloadManager.STATE_PAUSED) {
						if (NetReceiver.NET_TYPE == NetReceiver.NET_WIFI) {
							if (FileUtil.checkSDCard()) {
								if (Float.parseFloat(info.getAppSize()) * 3 >= FileUtil.getSDcardAvailableSpace()) {
									Toast.makeText(context, "可用空间不足", Toast.LENGTH_SHORT).show();
									return;
								}
							} else {
								if (Float.parseFloat(info.getAppSize()) * 3 >= FileUtil.getDataStorageAvailableSpace()) {
									Toast.makeText(context, "可用空间不足", Toast.LENGTH_SHORT).show();
									return;
								}
							}
							DownloadManager.getInstance().download(info);
							pause.setBackgroundResource(R.drawable.jixu);
						} else {
							netChangeDialog.show();
						}
					}
				}
			});
			
			deleted.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					DownloadManager.getInstance().cancel(info);
					deleteAppinfos.add(info);
//					DownloadManager.DBManager.getDownloadAppinfoDao().delete(info);
					mDisplayedHolders.remove(this);
					downloadAppinfos.remove(info);
					notifyDataSetChanged();
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
							
						}
					});
				}
			});

			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isCheck) {
					// TODO Auto-generated method stub
					checkMap.put(info.getId(), isCheck);
				}
			});

			initViewSize(view);

			return view;
		}

		private void initViewSize(View view) {
			UIUtil.setViewSize(checkBox, 56 * MetricsTool.Rx, 56 * MetricsTool.Ry);
			UIUtil.setViewSize(appIcon, 160 * MetricsTool.Rx, 160 * MetricsTool.Ry);
			UIUtil.setViewSize(pause, 96 * MetricsTool.Rx, 76 * MetricsTool.Rx);
			UIUtil.setViewSize(deleted, 76 * MetricsTool.Rx, 76 * MetricsTool.Rx);
			UIUtil.setViewSize(downloadPrg, 545 * MetricsTool.Rx, 20 * MetricsTool.Rx);
			UIUtil.setViewWidth(appName, 545 * MetricsTool.Rx);

			UIUtil.setTextSize(appName, 40);
			UIUtil.setTextSize(progressTx, 30);

			AbsListView.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (255 * MetricsTool.Ry));
			view.setLayoutParams(lp);

			try {
				UIUtil.setViewSizeMargin(checkBox, 20 * MetricsTool.Rx, 0, 20 * MetricsTool.Rx, 0);
				UIUtil.setViewSizeMargin(appName, 20 * MetricsTool.Rx, 60 * MetricsTool.Ry, 0, 0);
				UIUtil.setViewSizeMargin(downloadPrg, 0, 10 * MetricsTool.Ry, 0, 0);
				UIUtil.setViewSizeMargin(progressTx, 0, 10 * MetricsTool.Ry, 0, 0);
				UIUtil.setViewSizeMargin(deleted , 40 * MetricsTool.Rx, 0, 0, 0);
				UIUtil.setViewSizeMargin(pause , 20 * MetricsTool.Rx, 0, 0, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void setData(DownloadAppinfo appinfo,int position) {
			this.info = appinfo;
			appSize = FileUtil.getFileSize(Long.parseLong(appinfo.getAppSize()));
			if (appinfo.getIconByte() != null) {
				appIcon.setImageBitmap(UIUtil.bytesToBitmap(appinfo.getIconByte()));
			}else {
				ImageListener listener = new ImageListener() {
					
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						appIcon.setImageResource(R.drawable.default_tubiao);
					}
					
					@Override
					public void onResponse(ImageContainer arg0, boolean arg1) {
						// TODO Auto-generated method stub
						if (arg0.getBitmap() != null) {
							appIcon.setImageBitmap(arg0.getBitmap());
						}else {
							appIcon.setImageResource(R.drawable.default_tubiao);
						}
					}
				};
				StoreApplication.getInstance().getImageLoader().get(info.getIconUrl(), listener);
			}
			appName.setText(appinfo.getAppName());

			if (checkMap.containsKey(info.getId())) {
				checkBox.setChecked(checkMap.get(info.getId()));
			}else {
				checkBox.setChecked(false);
			}
			refreshView(appinfo);
		}

		public void refreshView(DownloadAppinfo appinfo) {
			pause.clearAnimation();
			downloadPrg.setProgress((int) (appinfo.getProgress() * 100));
			progressTx.setText(FileUtil.getFileSize(appinfo.getCurrentSize()) + "/" + appSize);
			switch (appinfo.getDownloadState()) {
			case DownloadManager.STATE_NONE:
				pause.setBackgroundResource(R.drawable.download_selector);
				break;

			case DownloadManager.STATE_PAUSED:
				pause.setBackgroundResource(R.drawable.zanting);
				break;
			case DownloadManager.STATE_ERROR:
				pause.setBackgroundResource(R.drawable.xiazai_failed);
				break;
			case DownloadManager.STATE_WAITING:
				pause.setBackgroundResource(R.drawable.wait_anim);
				AnimationDrawable animationDrawable = (AnimationDrawable) pause.getBackground();
				animationDrawable.start();
//				Animation animation = AnimationUtils.loadAnimation(context, R.anim.wating_rorating);
//				pause.setAnimation(animation);
//				pause.startAnimation(animation);
				break;
			case DownloadManager.STATE_DOWNLOADING:
				pause.setBackgroundResource(R.drawable.jixu);
				break;
			case DownloadManager.STATE_DOWNLOADED:
				synchronized (handler) {
					Message msg = handler.obtainMessage();
					msg.obj = appinfo;
					msg.what = 1;
					handler.sendMessage(msg);
				}
				pause.setBackgroundResource(R.drawable.runing_selector);
				break;
			case DownloadManager.STATE_UNZIPING:
				synchronized (handler) {
					Message msg = handler.obtainMessage();
					msg.obj = appinfo;
					msg.what = 1;
					handler.sendMessage(msg);
				}
				pause.setBackgroundResource(R.drawable.jieya);
				break;
			}
		}
	}

	public List<Holder> getDisplayedHolders() {
		synchronized (mDisplayedHolders) {
			return new ArrayList<Holder>(mDisplayedHolders);
		}
	}

	private void refreshHolder(DownloadAppinfo info) {
		List<Holder> displayedHolder2s = getDisplayedHolders();
		for (int i = 0; i < displayedHolder2s.size(); i++) {
			final Holder holder = displayedHolder2s.get(i);
			if (holder != null) {
				final DownloadAppinfo appInfo = holder.getData();
				if (appInfo.getId().equals(info.getId())) {
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
	}

	public void startDownloadAll() {
		int time = 0;
		for (DownloadAppinfo downloadAppinfo : downloadAppinfos) {
			if (checkMap.get(downloadAppinfo.getId())) {
				Message msg = handler2.obtainMessage();
				msg.what = 1;
				msg.obj = downloadAppinfo;
				handler2.sendMessageDelayed(msg, time);
				time += 50;
			}
		}
	}

	Handler handler2 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				DownloadManager.getInstance().download((DownloadAppinfo) msg.obj);
			}
		};
	};

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

	@Override
	public void onUnZipProgressed(DownloadAppinfo info, int progress) {
		// TODO Auto-generated method stub
		
	}

}
