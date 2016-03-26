package com.jiqu.adapter;

import java.util.List;

import com.jiqu.object.PrivateMessage;
import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.MsgPopWind;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class PrivateMessageAdapter extends BaseAdapter {
	private Context context;
	private List<PrivateMessage> messages;

	public PrivateMessageAdapter(Context context,List<PrivateMessage> messages) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.messages = messages;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return messages.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return messages.get(position);
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
			convertView = holder.getRootView();
		}else {
			holder = (Holder) convertView.getTag();
		}
		holder.setIndex(position);
		
		return holder.getRootView();
	}

	private class Holder{
		private ImageView icon;
		private LinearLayout messageLin;
		private TextView nickName;
		private TextView message;
		private LinearLayout timeLin;
		private TextView time;
		private Button operateBtn;
		private float Rx,Ry;
		
		private View rootView;
		private Context context;
		
		private int index;
		
		public Holder(Context context){
			this.context = context;
			
			rootView = initView();
			rootView.setTag(this);
		}
		
		public void setIndex(int index){
			this.index = index;
		}
		
		public View getRootView(){
			return rootView;
		}
		
		private View initView(){
			Rx = MetricsTool.Rx;
			Ry = MetricsTool.Ry;
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.private_message_item, null);
			icon = (ImageView) view.findViewById(R.id.icon);
			messageLin = (LinearLayout) view.findViewById(R.id.messageLin);
			nickName = (TextView) view.findViewById(R.id.nickName);
			message = (TextView) view.findViewById(R.id.message);
			timeLin = (LinearLayout) view.findViewById(R.id.timeLin);
			time = (TextView) view.findViewById(R.id.time);
			operateBtn = (Button) view.findViewById(R.id.operateBtn);
			
			initViewSize(view);
			
			operateBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					MsgPopWind popWind = new MsgPopWind(context);
					popWind.showAsDropDown(operateBtn, (int)(-165 * Rx), 0);
					Log.i("TAG", "index : " + index);
				}
			});
			return view;
		}
		
		private void initViewSize(View view){
			UIUtil.setViewSize(icon, 125 * Rx, 125 * Rx);
			UIUtil.setViewSize(operateBtn, 36 * Rx, 36 * Rx);
			
			UIUtil.setTextSize(nickName, 40);
			UIUtil.setTextSize(message, 30);
			UIUtil.setTextSize(time, 25);
			
			android.widget.AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, (int)(265 * Ry));
			view.setLayoutParams(lp);
			
			try {
				UIUtil.setViewSizeMargin(icon, 25 * Rx, 0, 25 * Rx, 0);
				UIUtil.setViewSizeMargin(timeLin, 0, 70 * Ry, 50 * Rx, 0);
				UIUtil.setViewSizeMargin(messageLin, 0, 0, 50 * Rx, 0);
				UIUtil.setViewSizeMargin(operateBtn, 20 * Rx, 0, 0, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
