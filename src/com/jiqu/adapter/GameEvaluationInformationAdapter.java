package com.jiqu.adapter;

import java.util.List;

import com.jiqu.object.GameInformation;
import com.vr.store.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GameEvaluationInformationAdapter extends BaseAdapter {
	private Holder1 holder1;
	private Holder2 holder2;
	private Holder3 holder3;
	private LayoutInflater inflater;
	private List<GameInformation> informations;
	
	public GameEvaluationInformationAdapter(Context context,List<GameInformation> informations){
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
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return super.getItemViewType(position);
	}
	
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int type = getItemViewType(position);
		switch (type) {
		case 0:
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.game_evaluation_1, null);
				holder1 = new Holder1();
				holder1.evaluationInformationTitle = (TextView) convertView.findViewById(R.id.evaluationInformationTitle);
			}else {
				holder1 = (Holder1) convertView.getTag();
			}
			break;

		case 1:
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.game_evaluation_2, null);
				holder2 = new Holder2();
				holder2.evaluationTitle = (TextView) convertView.findViewById(R.id.evaluationTitle);
				holder2.evaluationInformation = (TextView) convertView.findViewById(R.id.evaluationInformation);
				holder2.gameIntroductionImg = (ImageView) convertView.findViewById(R.id.gameIntroductionImg);
			}else {
				holder2 = (Holder2) convertView.getTag();
			}
			break;
			
		case 2:
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.game_evaluation_3, null);
				holder3 = new Holder3();
				holder3.praise = (Button) convertView.findViewById(R.id.praise);
				holder3.noPraise = (Button) convertView.findViewById(R.id.noPraise);
			}else {
				holder3 = (Holder3) convertView.getTag();
			}
			break;
		}
		return convertView;
	}

	private class Holder1{
		private TextView evaluationInformationTitle;
	}
	
	private class Holder2{
		private TextView evaluationTitle;
		private TextView evaluationInformation;
		private ImageView gameIntroductionImg;
	}
	
	private class Holder3{
		private Button praise;
		private Button noPraise;
	}
}
