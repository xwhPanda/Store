package com.jiqu.adapter;

import java.util.List;

import com.jiqu.object.ThematicInfo;
import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ThematicAdapter extends BaseAdapter {
	private Context context;
	private List<ThematicInfo> thematicInfos;

	public ThematicAdapter(Context context, List<ThematicInfo> thematicInfos) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.thematicInfos = thematicInfos;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return thematicInfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return thematicInfos.get(position);
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
		} else {
			holder = (Holder) convertView.getTag();
		}
		return convertView;
	}
	
	class Holder1{
		private TextView thematicTitle;
		private ImageView horLine;
		private ImageView verLine;
		
		private View rootView;
		
		public Holder1(){
			rootView = initView();
			rootView.setTag(this);
			
			initViewSize();
		}
		
		private View initView(){
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.thematic_item_title_layout, null);
			
			return view;
		}
		
		private void initViewSize(){
			UIUtil.setTextSize(thematicTitle, 45);
		}
	}

	class Holder {
		private ImageView thematicImg;
		private LinearLayout titleLin;
		private ImageView titleImg;
		private TextView thematicTitle;
		private TextView leftBrackets;
		private TextView count;
		private ImageView heart;
		private TextView rightBrackets;

		private View rootView;

		public Holder() {
			rootView = initView();
			rootView.setTag(this);

			initViewSize();
		}

		private View getRootView() {
			return rootView;
		}

		private View initView() {
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.thematic_item_layout, null);

			thematicImg = (ImageView) view.findViewById(R.id.thematicImg);
			titleLin = (LinearLayout) view.findViewById(R.id.titleLin);
			titleImg = (ImageView) view.findViewById(R.id.titleImg);
			thematicTitle = (TextView) view.findViewById(R.id.thematicTitle);
			leftBrackets = (TextView) view.findViewById(R.id.leftBrackets);
			count = (TextView) view.findViewById(R.id.count);
			heart = (ImageView) view.findViewById(R.id.heart);
			rightBrackets = (TextView) view.findViewById(R.id.rightBrackets);

			return view;
		}

		private void initViewSize() {
			UIUtil.setTextSize(thematicTitle, 35);
			UIUtil.setTextSize(leftBrackets, 30);
			UIUtil.setTextSize(rightBrackets, 30);
			UIUtil.setTextSize(count, 30);

			UIUtil.setViewSize(titleImg, 56 * MetricsTool.Rx, 36 * MetricsTool.Rx);
			UIUtil.setViewSize(thematicImg, 510 * MetricsTool.Rx, 335 * MetricsTool.Ry);
			UIUtil.setViewSize(heart, 28 * MetricsTool.Rx, 28 * MetricsTool.Rx);

			try {
				UIUtil.setViewSizeMargin(leftBrackets, 15 * MetricsTool.Rx, 0, 0, 0);
				UIUtil.setViewSizeMargin(thematicTitle, 5 * MetricsTool.Rx, 0, 0, 0);
				UIUtil.setViewSizeMargin(titleLin, 0, 30 * MetricsTool.Ry, 0, 0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
