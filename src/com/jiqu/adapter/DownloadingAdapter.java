package com.jiqu.adapter;

import java.util.List;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.jiqu.database.DownloadAppinfo;
import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

public class DownloadingAdapter extends BaseAdapter {
	private Context context;
	private List<DownloadAppinfo> downloadAppinfos;

	public DownloadingAdapter(Context context,List<DownloadAppinfo> downloadAppinfos) {
		// TODO Auto-generated constructor stub
		this.context = context;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		DownloadAppinfo appinfo = downloadAppinfos.get(position);
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.downloading_item_layout, null);
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
			holder.appIcon = (ImageView) convertView.findViewById(R.id.appIcon);
			holder.appName = (TextView) convertView.findViewById(R.id.appName);
			holder.downloadPrg = (ProgressBar) convertView.findViewById(R.id.downloadPrg);
			holder.progressTx = (TextView) convertView.findViewById(R.id.progressTx);
			holder.pause = (Button) convertView.findViewById(R.id.pause);
			
			UIUtil.setViewSize(holder.checkBox, 56 * MetricsTool.Rx, 56 * MetricsTool.Ry);
			UIUtil.setViewSize(holder.appIcon, 160 * MetricsTool.Rx, 160 * MetricsTool.Ry);
			UIUtil.setViewSize(holder.pause, 60 * MetricsTool.Rx, 60 * MetricsTool.Rx);
			UIUtil.setViewSize(holder.downloadPrg, 545 * MetricsTool.Rx, 20 * MetricsTool.Rx);
			
			UIUtil.setTextSize(holder.appName, 40);
			UIUtil.setTextSize(holder.progressTx, 30);
			
			AbsListView.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (255 * MetricsTool.Ry));
			convertView.setLayoutParams(lp);
			
			try {
				UIUtil.setViewSizeMargin(holder.checkBox, 20 * MetricsTool.Rx, 0, 20 * MetricsTool.Rx, 0);
				UIUtil.setViewSizeMargin(holder.appName, 20 * MetricsTool.Rx, 60 * MetricsTool.Ry, 0, 0);
				UIUtil.setViewSizeMargin(holder.downloadPrg, 0, 10 * MetricsTool.Ry, 0, 0);
				UIUtil.setViewSizeMargin(holder.progressTx, 0, 10 * MetricsTool.Ry, 0, 0);
				UIUtil.setViewSizeMargin(holder.pause, 0, 0, 60 * MetricsTool.Rx, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			convertView.setTag(holder);
		}else {
			holder = (Holder) convertView.getTag();
		}
		
		return convertView;
	}
	
	private class Holder{
		private CheckBox checkBox;
		private ImageView appIcon;
		private TextView appName;
		private ProgressBar downloadPrg;
		private TextView progressTx;
		private Button pause;
	}

}
