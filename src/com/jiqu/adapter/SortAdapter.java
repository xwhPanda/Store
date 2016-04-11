package com.jiqu.adapter;

import java.util.List;

import com.jiqu.object.SortItem;
import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class SortAdapter extends BaseAdapter {
	private Context context;
	private List<SortItem> sortItems;
	
	public SortAdapter(Context context, List<SortItem> sortItems){
		this.context = context;
		this.sortItems = sortItems;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return sortItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return sortItems.get(position);
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
		holder.setData(sortItems.get(position));
		return convertView;
	}

	class Holder{
		private TextView sortTitle;
		private ImageView sortImg;
		private TextView sortGamesInfo;
		
		private View rootView;
		
		public Holder(){
			rootView = initView();
			rootView.setTag(this);
			
			initViewSize();
		}
		
		public View getRootView(){
			return rootView;
		}
		
		public void setData(SortItem data){
			sortTitle.setBackgroundColor(context.getResources().getColor(R.color.red));
			sortTitle.setText(data.getTitle());
			sortImg.setBackgroundColor(context.getResources().getColor(R.color.red));
			sortGamesInfo.setText(Html.fromHtml("<font color=\"white\">" +data.getTotal() + "</font>" + "<font color=\"white\">" + "fsjfhsfh hl" + "</font>" + "<font color=\"green\">" + data.getNewAddCount() + "</font>"));
		}
		
		private View initView(){
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.sort_item_layout, null);
			
			sortTitle = (TextView) view.findViewById(R.id.sortTitle);
			sortImg = (ImageView) view.findViewById(R.id.sortImg);
			sortGamesInfo = (TextView) view.findViewById(R.id.sortGamesInfo);
			
			return view;
		}
		
		private void initViewSize(){
			UIUtil.setTextSize(sortTitle, 45);
			UIUtil.setTextSize(sortGamesInfo, 35);
			UIUtil.setViewSize(sortImg, 192 * MetricsTool.Rx, 192 * MetricsTool.Rx);
			
			UIUtil.setViewHeight(sortTitle, 83 * MetricsTool.Ry);
			
			try {
				UIUtil.setViewSizeMargin(sortImg, 0, 70 * MetricsTool.Ry, 0, 0);
				UIUtil.setViewSizeMargin(sortGamesInfo, 0, 50 * MetricsTool.Ry, 0, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			LayoutParams lp = new LayoutParams((int)(485 * MetricsTool.Rx), (int) (485 * MetricsTool.Ry)); 
			rootView.setLayoutParams(lp);
		}
	}
}