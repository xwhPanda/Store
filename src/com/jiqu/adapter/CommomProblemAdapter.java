package com.jiqu.adapter;

import java.util.List;

import com.jiqu.object.CommonProblemItem;
import com.vr.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

public class CommomProblemAdapter extends BaseAdapter {
	private Context context;
	private List<CommonProblemItem> problemItems;
	public CommomProblemAdapter(Context context,List<CommonProblemItem> items) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.problemItems = items;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return problemItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return problemItems.get(position);
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
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.commom_problem_item_layout, null);
			holder.problem = (TextView) convertView.findViewById(R.id.problem);
			holder.infoBtn = (ImageView) convertView.findViewById(R.id.infoBtn);
			
			UIUtil.setTextSize(holder.problem, 35);
			UIUtil.setViewSize(holder.infoBtn, 36 * MetricsTool.Rx, 36 * MetricsTool.Rx);
			
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (180 * MetricsTool.Ry));
			convertView.setLayoutParams(lp);
			
			try {
				UIUtil.setViewSizeMargin(holder.problem, 30 * MetricsTool.Rx, 0, 0, 0);
				UIUtil.setViewSizeMargin(holder.infoBtn, 0, 0, 30 * MetricsTool.Rx, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			convertView.setTag(holder);
		}else {
			holder = (Holder) convertView.getTag();
		}
		
		holder.problem.setText(problemItems.get(position).getTitle());
		return convertView;
	}
	
	class Holder{
		private TextView problem;
		private ImageView infoBtn;
	}
}
