package com.jiqu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.jiqu.object.BatterySipper;
import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

public class BatteryAdapter extends BaseAdapter {
	private List<BatterySipper> sippers;
	private Context context;

	public BatteryAdapter(Context context,List<BatterySipper> sippers ) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.sippers = sippers;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return sippers.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return sippers.get(position);
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
			LayoutInflater inflater = LayoutInflater.from(context);
			holder = new Holder();
			convertView = inflater.inflate(R.layout.battery_item_layout, null);
			holder.parentView = (RelativeLayout) convertView.findViewById(R.id.parent);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.appName = (TextView) convertView.findViewById(R.id.appName);
			holder.usedBattery = (TextView) convertView.findViewById(R.id.usedBattery);
			
			UIUtil.setViewSize(holder.icon, 120 * MetricsTool.Rx, 120 * MetricsTool.Rx);
			UIUtil.setViewHeight(holder.parentView, 192 * MetricsTool.Ry);
			UIUtil.setTextSize(holder.appName, 40);
			UIUtil.setTextSize(holder.usedBattery, 40);
			
			try {
				UIUtil.setViewSizeMargin(holder.icon, 35 * MetricsTool.Rx, 0, 35 * MetricsTool.Rx, 0);
				UIUtil.setViewSizeMargin(holder.usedBattery, 0, 0, 35 * MetricsTool.Rx, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			convertView.setTag(holder);
		}else {
			holder = (Holder) convertView.getTag();
		}
		
		holder.icon.setBackgroundDrawable(sippers.get(position).getIcon());
		holder.appName.setText(sippers.get(position).getName());
		holder.usedBattery.setText(sippers.get(position).getPercentOfTotal() + "%");
		return convertView;
	}

	class Holder{
		private RelativeLayout parentView;
		private ImageView icon;
		private TextView appName;
		private TextView usedBattery;
	}
}
