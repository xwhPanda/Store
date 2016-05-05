package com.jiqu.adapter;

import java.util.List;

import com.jiqu.object.GameInformation;
import com.vr.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EvaluationGridViewAdapter extends BaseAdapter {
	private List<GameInformation> informations;
	private LayoutInflater inflater;
	
	public EvaluationGridViewAdapter(Context context,List<GameInformation> informations){
		this.informations = informations;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return informations.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return informations.get(position);
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
			convertView = inflater.inflate(R.layout.evaluation_item, null);
			holder.gameIcon = (ImageView) convertView.findViewById(R.id.gameIcon);
			holder.scoreLin = (LinearLayout) convertView.findViewById(R.id.scoreLin);
			holder.agency = (TextView) convertView.findViewById(R.id.agency);
			holder.score = (TextView) convertView.findViewById(R.id.score);
			holder.nameAndDes = (TextView) convertView.findViewById(R.id.nameAndDes);
			holder.gameEvaluation = (TextView) convertView.findViewById(R.id.gameEvluation);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.parentView = (RelativeLayout) convertView.findViewById(R.id.parentView);
			
			android.widget.AbsListView.LayoutParams lp = new AbsListView.LayoutParams((int) (505 * MetricsTool.Rx), (int) (780 * MetricsTool.Ry));
			convertView.setLayoutParams(lp);
			UIUtil.setViewHeight(holder.gameIcon, 600 * MetricsTool.Ry);
			UIUtil.setViewSize(holder.scoreLin, 148 * MetricsTool.Rx, 165 * MetricsTool.Ry);
			
			UIUtil.setTextSize(holder.agency, 60);
			UIUtil.setTextSize(holder.score, 35);
			UIUtil.setTextSize(holder.nameAndDes, 40);
			UIUtil.setTextSize(holder.gameEvaluation, 35);
			UIUtil.setTextSize(holder.time, 35);
			
			try {
				UIUtil.setViewSizeMargin(holder.nameAndDes, 0, 15 * MetricsTool.Ry, 0, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			convertView.setTag(holder);
		}else {
			holder = (Holder)convertView.getTag();
		}
//		holder.gameIcon.setBackgroundResource(R.drawable.touxiang);
//		holder.scoreLin.setBackgroundResource(R.drawable.ic_launcher);
		holder.nameAndDes.setText("英雄联盟");
		holder.gameEvaluation.setText("僵尸粉丝哦你健身房分裂势力供货商");
		holder.time.setText("2010-8-12");
		
		return convertView;
	}

	private class Holder{
		private RelativeLayout parentView;
		private ImageView gameIcon;
		private LinearLayout scoreLin;
		private TextView agency;
		private TextView score;
		private TextView nameAndDes;
		private TextView gameEvaluation;
		private TextView time;
	}
}
