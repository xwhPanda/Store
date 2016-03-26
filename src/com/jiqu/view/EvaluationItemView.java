package com.jiqu.view;

import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/** 游戏测评详情页中下面部分的详细测评 **/
public class EvaluationItemView extends RelativeLayout {
	private TextView evaluationTitle;
	private TextView evaluationInfo;
	private ImageView evaluationImg;

	public EvaluationItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public EvaluationItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public EvaluationItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context){
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.evaluation_information_item, this);
		evaluationTitle = (TextView) view.findViewById(R.id.evaluationTitle);
		evaluationInfo = (TextView) view.findViewById(R.id.evaluationInfo);
		evaluationImg = (ImageView) view.findViewById(R.id.evaluationImg);
		
		initViewSize();
	}
	
	private void initViewSize(){
		UIUtil.setTextSize(evaluationTitle, 35);
		UIUtil.setTextSize(evaluationInfo, 30);
		
		UIUtil.setViewSize(evaluationImg, 1030 * MetricsTool.Rx, 515 * MetricsTool.Ry);
		
		try {
			
			UIUtil.setViewSizeMargin(evaluationInfo, 55 * MetricsTool.Rx, 20 * MetricsTool.Ry, 55 * MetricsTool.Rx, 0);
			UIUtil.setViewSizeMargin(evaluationTitle, 55 * MetricsTool.Rx, 20 * MetricsTool.Ry, 55 * MetricsTool.Rx, 0);
			UIUtil.setViewSizeMargin(evaluationImg, 0, 20 * MetricsTool.Ry, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
