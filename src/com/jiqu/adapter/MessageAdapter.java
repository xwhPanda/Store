package com.jiqu.adapter;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.jiqu.application.StoreApplication;
import com.jiqu.object.MessageDataInfo;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;
import com.vr.store.R;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {
	private Context context;
	private List<MessageDataInfo> dataInfos;
	private LayoutInflater inflater;

	public MessageAdapter(Context context,List<MessageDataInfo> dataInfos) {
		this.context = context;
		this.dataInfos = dataInfos;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataInfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataInfos.get(position);
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
		MessageDataInfo dataInfo = dataInfos.get(position);
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.message_item_layout, null);
			holder.messageIcon = (ImageView) convertView.findViewById(R.id.messageIcon);
			holder.messageName = (TextView) convertView.findViewById(R.id.messageName);
			holder.messageDes = (TextView) convertView.findViewById(R.id.messageDes);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			
			UIUtil.setViewSize(holder.messageIcon, 330 * MetricsTool.Rx, 200 * MetricsTool.Ry);
			
			UIUtil.setTextSize(holder.messageName, 35);
			UIUtil.setTextSize(holder.messageDes, 30);
			UIUtil.setTextSize(holder.time, 25);
			
			try {
				UIUtil.setViewSizeMargin(holder.messageIcon, 30 * MetricsTool.Rx, 15 * MetricsTool.Ry, 30 * MetricsTool.Rx, 15 * MetricsTool.Ry);
				UIUtil.setViewSizeMargin(holder.messageName, 0, 0, 30 * MetricsTool.Rx, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			convertView.setTag(holder);
		}else {
			holder = (Holder) convertView.getTag();
		}
		if (!TextUtils.isEmpty(dataInfo.getPic())) {
			ImageListener listener = ImageLoader.getImageListener(holder.messageIcon, R.drawable.zixun_item_default, R.drawable.zixun_item_default);
			StoreApplication.getInstance().getImageLoader().get(dataInfo.getPic(), listener);
		}else {
			holder.messageIcon.setBackgroundResource(R.drawable.zixun_item_default);
		}
		if (!TextUtils.isEmpty(dataInfo.getTitle())) {
			holder.messageName.setText(dataInfo.getTitle());
		}
		holder.messageDes.setText(dataInfo.getContent());
		holder.time.setText(UIUtil.getFormatedDateTime("yyyy-MM-dd", Long.parseLong(dataInfo.getTime())));
		return convertView;
	}

	class Holder{
		private ImageView messageIcon;
		private RelativeLayout timeRel;
		private TextView time;
		private RelativeLayout infomationRel;
		private TextView messageName;
		private TextView messageDes;
	}
}
