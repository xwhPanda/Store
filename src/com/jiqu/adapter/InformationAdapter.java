package com.jiqu.adapter;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.jiqu.application.StoreApplication;
import com.jiqu.object.GameInformation;
import com.jiqu.object.InformationGallaryItem;
import com.vr.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import android.R.integer;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InformationAdapter extends BaseAdapter {
	private Context context;
	private List<InformationGallaryItem> informations;
	private LayoutInflater inflater;
	/** 0  资讯页  1 消息中心 **/
	private int type = 0;

	public InformationAdapter(Context context, List<InformationGallaryItem> informations,int type) {
		this.context = context;
		this.informations = informations;
		inflater = LayoutInflater.from(context);
		this.type = type;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.information_game_item, null);
			holder.gameIcon = (ImageView) convertView.findViewById(R.id.gameIcon);
			holder.gameName = (TextView) convertView.findViewById(R.id.gameName);
			holder.gameDes = (TextView) convertView.findViewById(R.id.gameDes);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.timeRel = (RelativeLayout) convertView.findViewById(R.id.timeRel);
			holder.zhuanti = (TextView) convertView.findViewById(R.id.zhuanti);
			
			UIUtil.setViewSize(holder.gameIcon, 330 * MetricsTool.Rx, 200 * MetricsTool.Ry);
			UIUtil.setViewSize(holder.img, 36 * MetricsTool.Rx, 36 * MetricsTool.Rx);
			
			UIUtil.setTextSize(holder.gameName, 35);
			UIUtil.setTextSize(holder.gameDes, 30);
			UIUtil.setTextSize(holder.time, 30);
			UIUtil.setTextSize(holder.zhuanti, 30);
			UIUtil.setTextSize(holder.time, 25);
			
			try {
				UIUtil.setViewSizeMargin(holder.gameIcon, 30 * MetricsTool.Rx, 15 * MetricsTool.Ry, 30 * MetricsTool.Rx, 15 * MetricsTool.Ry);
				UIUtil.setViewSizeMargin(holder.gameName, 0, 0, 30 * MetricsTool.Rx, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (type == 0) {
				convertView.setBackgroundColor(context.getResources().getColor(R.color.itemBgColor));
			}
			
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		InformationGallaryItem item = informations.get(position);
		ImageListener listener = ImageLoader.getImageListener(holder.gameIcon, R.drawable.ic_launcher, R.drawable.ic_launcher);
		StoreApplication.getInstance().getImageLoader().get(item.getImg(), listener);
		holder.gameName.setText(item.getTitle());
		holder.gameDes.setText(item.getDesc());
		holder.time.setVisibility(View.INVISIBLE);
		return convertView;
	}
	

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	private class Holder {
		private ImageView gameIcon;
		private TextView gameName;
		private TextView gameDes;
		private ImageView img;
		private TextView time;
		private TextView zhuanti;
		private RelativeLayout timeRel;
	}
}
