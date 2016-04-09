package com.jiqu.adapter;

import java.util.List;

import com.jiqu.object.FriendItem;
import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShareAddFriendAdapter extends BaseAdapter {
	private Context context;
	private List<FriendItem> friendItems;

	public ShareAddFriendAdapter(Context context,List<FriendItem> friendItems) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.friendItems = friendItems;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return friendItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return friendItems.get(position);
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
			convertView = holder.getRootView();
		}else {
			holder = (Holder) convertView.getTag();
		}
		holder.setData(friendItems.get(position));
		return convertView;
	}

	
	class Holder{
		private ImageView icon;
		private TextView nickName;
		private TextView distance;
		private LinearLayout infoLin;
		private Button add;
		
		private View rootView;
		
		private FriendItem data;
		
		public Holder(){
			rootView = initView();
			rootView.setTag(this);
			
			initViewSize();
		}
		
		public View getRootView(){
			return rootView;
		}
		
		public void setData(FriendItem data){
			this.data = data;
			
			icon.setBackgroundColor(context.getResources().getColor(R.color.red));
			nickName.setText("缝合伤口缝合拉萨");
			distance.setText("司法局发顺丰啊");
		}
		
		private View initView(){
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.add_friend_item, null);
			
			icon = (ImageView) view.findViewById(R.id.icon);
			infoLin = (LinearLayout) view.findViewById(R.id.infoLin);
			nickName = (TextView) view.findViewById(R.id.nickName);
			distance = (TextView) view.findViewById(R.id.distance);
			add = (Button) view.findViewById(R.id.add);
			
			return view;
		}
		
		private void initViewSize(){
			UIUtil.setViewSize(icon, 125 * MetricsTool.Rx, 125 * MetricsTool.Rx);
			UIUtil.setViewSize(add, 95 * MetricsTool.Rx, 75 * MetricsTool.Ry);
			
			UIUtil.setTextSize(nickName, 35);
			UIUtil.setTextSize(distance, 30);
			
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (180 * MetricsTool.Ry));
			rootView.setLayoutParams(lp);
			
			try {
				UIUtil.setViewSizeMargin(icon, 30 * MetricsTool.Rx, 0, 0, 0);
				UIUtil.setViewSizeMargin(infoLin, 30 * MetricsTool.Rx, 0, 0, 0);
				UIUtil.setViewSizeMargin(add, 0, 0, 40 * MetricsTool.Rx, 0);
				UIUtil.setViewSizeMargin(distance, 0, 10 * MetricsTool.Ry, 0, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
