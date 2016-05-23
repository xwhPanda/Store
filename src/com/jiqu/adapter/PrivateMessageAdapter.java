package com.jiqu.adapter;

import java.util.List;

import com.jiqu.object.PrivateMessageDataInfo;
import com.vr.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.MsgPopWind;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class PrivateMessageAdapter extends BaseAdapter {
	private Context context;
	private List<PrivateMessageDataInfo> messages;

	public PrivateMessageAdapter(Context context,List<PrivateMessageDataInfo> messages) {
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
		holder.setData(messages.get(position));
		holder.setIndex(position);
		
		return holder.getRootView();
	}

	private class Holder{
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
		private PrivateMessageDataInfo info;
		
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
		
		public void setData(PrivateMessageDataInfo info){
			this.info = info;
			message.setText(info.getContent());
			time.setText(UIUtil.getFormatedDateTime("yyyy-mm-dd", Long.parseLong(info.getTime())));
			nickName.setText(info.getTitle());
		}
		
		private View initView(){
			Rx = MetricsTool.Rx;
			Ry = MetricsTool.Ry;
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.private_message_item, null);
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
				}
			});
			return view;
		}
		
		private void initViewSize(View view){
			UIUtil.setViewSize(operateBtn, 36 * Rx, 36 * Rx);
			
			UIUtil.setTextSize(nickName, 40);
			UIUtil.setTextSize(message, 30);
			UIUtil.setTextSize(time, 25);
			
			android.widget.AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, (int)(265 * Ry));
			view.setLayoutParams(lp);
			
			try {
				UIUtil.setViewSizeMargin(timeLin, 0, 70 * Ry, 50 * Rx, 0);
				UIUtil.setViewSizeMargin(messageLin, 0, 0, 50 * Rx, 0);
				UIUtil.setViewSizeMargin(operateBtn, 20 * Rx, 0, 0, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
