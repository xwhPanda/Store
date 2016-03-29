package com.jiqu.view;

import java.util.ArrayList;
import java.util.List;

import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class SpinnerPopView extends PopupWindow {
	private ListView dataList;
	private PopAdapter adapter;
	private List<String> strings = new ArrayList<String>();

	public SpinnerPopView(Context context,boolean isGender) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	
	private void initView(Context context){
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.spinner_pop_layout, null);
		dataList = (ListView) view.findViewById(R.id.dataList);
		this.setWidth((int)(110 * MetricsTool.Rx));
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setOutsideTouchable(true);
		this.setFocusable(true);
		this.setClippingEnabled(true);
		this.setBackgroundDrawable(new BitmapDrawable());
		
		adapter = new PopAdapter(context, strings);
		dataList.setAdapter(adapter);
		setContentView(view);
	}
	
	public void initViewSize(boolean isGender){
		this.setWidth((int)(110 * MetricsTool.Rx));
		if (!isGender) {
			this.setHeight((int)(264 * MetricsTool.Ry));
		}
	}
	
	public void setData(List<String> data){
		strings.clear();
		strings.addAll(data);
		adapter.notifyDataSetChanged();
	}
	
	private class PopAdapter extends BaseAdapter {
		private Context context;
		private List<String> strings;
		
		public PopAdapter(Context context,List<String> strings){
			this.context = context;
			this.strings = strings;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return strings.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return strings.get(position);
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
				convertView = LayoutInflater.from(context).inflate(R.layout.list_item_layout, null);
				holder = new Holder();
				holder.textView = (TextView) convertView.findViewById(R.id.item);
				convertView.setTag(holder);
			}else {
				holder = (Holder) convertView.getTag();
			}
			holder.textView.setText(strings.get(position));
			return convertView;
		}
		
	}
	
	private class Holder{
		private TextView textView;
	}
}
