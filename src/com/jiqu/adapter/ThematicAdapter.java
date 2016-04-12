package com.jiqu.adapter;

import java.util.List;

import com.jiqu.object.ThematicInfo;
import com.jiqu.object.ThematicItemInfo;
import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.MaxGridView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ThematicAdapter extends BaseAdapter {
	private Context context;
	private List<ThematicItemInfo> lists;

	public ThematicAdapter(Context context,List<ThematicItemInfo> lists) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.lists = lists;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lists.get(position);
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
		
		holder.initData(lists.get(position));
		return convertView;
	}
	
	private class Holder{
		private LinearLayout thematicTitleLin;
		private TextView verLine;
		private TextView thematicTitle;
		private ImageView horLine;
		private MaxGridView thematicGridView;
		
		private View rootView;
		
		public Holder(){
			rootView = initView();
			rootView.setTag(this);
			initViewSize();
		}
		
		public View getRootView(){
			return rootView;
		}
		
		private View initView(){
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.thematic_item_view_layout, null);
			
			thematicTitleLin = (LinearLayout) view.findViewById(R.id.thematicTitleLin);
			verLine = (TextView) view.findViewById(R.id.verLine);
			thematicTitle = (TextView) view.findViewById(R.id.thematicTitle);
			horLine = (ImageView) view.findViewById(R.id.horLine);
			thematicGridView = (MaxGridView) view.findViewById(R.id.thematicGridView);
			
			return view;
		}
		
		private void initViewSize(){
			UIUtil.setTextSize(verLine, 45);
			UIUtil.setTextSize(thematicTitle, 45);
			
			thematicGridView.setVerticalSpacing((int)(40 * MetricsTool.Ry));
			
			try {
				UIUtil.setViewSizeMargin(verLine, 30 * MetricsTool.Rx , 0, 0, 0);
				UIUtil.setViewSizeMargin(thematicTitle, 10 * MetricsTool.Rx , 0, 0, 0);
				UIUtil.setViewSizeMargin(horLine, 0 , 30 * MetricsTool.Ry, 0, 0);
				UIUtil.setViewSizeMargin(thematicGridView, 0 , 30 * MetricsTool.Ry, 0, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		public void initData(ThematicItemInfo infos){
			thematicTitle.setText(infos.getTitle());
			ThematicItemAdapter adapter = new ThematicItemAdapter(context, infos.getThematicInfos());
			thematicGridView.setAdapter(adapter);
		}
	}

}
