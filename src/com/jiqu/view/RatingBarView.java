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

public class RatingBarView extends RelativeLayout {
	private ImageView start1,start2,start3,start4,start5;
	private int[] resID = new int[3];
	public float step = 0.2f;

	public RatingBarView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public RatingBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public RatingBarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	
	public void setStep(float step){
		this.step = step;
	}
	
	private void initView(Context context){
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.rating_bar_view_layout, this);
		start1 = (ImageView) view.findViewById(R.id.start1);
		start2 = (ImageView) view.findViewById(R.id.start2);
		start3 = (ImageView) view.findViewById(R.id.start3);
		start4 = (ImageView) view.findViewById(R.id.start4);
		start5 = (ImageView) view.findViewById(R.id.start5);
		
		initViewSize();
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(start1, 40 * MetricsTool.Rx, 40 * MetricsTool.Rx);
		UIUtil.setViewSize(start2, 40 * MetricsTool.Rx, 40 * MetricsTool.Rx);
		UIUtil.setViewSize(start3, 40 * MetricsTool.Rx, 40 * MetricsTool.Rx);
		UIUtil.setViewSize(start4, 40 * MetricsTool.Rx, 40 * MetricsTool.Rx);
		UIUtil.setViewSize(start5, 40 * MetricsTool.Rx, 40 * MetricsTool.Rx);
	}
	
	public void setResID(int[] IDs){
		for(int i = 0;i<IDs.length;i++){
			resID[i] = IDs[i];
		}
	}
	
	public void setStarSize(int width,int height){
		UIUtil.setViewSize(start1, width * MetricsTool.Rx, height * MetricsTool.Rx);
		UIUtil.setViewSize(start2, width * MetricsTool.Rx, height * MetricsTool.Rx);
		UIUtil.setViewSize(start3, width * MetricsTool.Rx, height * MetricsTool.Rx);
		UIUtil.setViewSize(start4, width * MetricsTool.Rx, height * MetricsTool.Rx);
		UIUtil.setViewSize(start5, width * MetricsTool.Rx, height * MetricsTool.Rx);
	}
	
	public void setRating(float rating){
		if (rating < step) {
			start1.setBackgroundResource(resID[0]);
			start2.setBackgroundResource(resID[0]);
			start3.setBackgroundResource(resID[0]);
			start4.setBackgroundResource(resID[0]);
			start5.setBackgroundResource(resID[0]);
		}else if (rating >= step && rating < step * 2) {
			start1.setBackgroundResource(resID[1]);
			start2.setBackgroundResource(resID[0]);
			start3.setBackgroundResource(resID[0]);
			start4.setBackgroundResource(resID[0]);
			start5.setBackgroundResource(resID[0]);
		}else if (rating >= step * 2 && rating < step * 3) {
			start1.setBackgroundResource(resID[2]);
			start2.setBackgroundResource(resID[0]);
			start3.setBackgroundResource(resID[0]);
			start4.setBackgroundResource(resID[0]);
			start5.setBackgroundResource(resID[0]);
		}else if (rating >= step * 3 && rating < step * 4) {
			start1.setBackgroundResource(resID[2]);
			start2.setBackgroundResource(resID[1]);
			start3.setBackgroundResource(resID[0]);
			start4.setBackgroundResource(resID[0]);
			start5.setBackgroundResource(resID[0]);
		}else if (rating >= step * 4 && rating < step * 5) {
			start1.setBackgroundResource(resID[2]);
			start2.setBackgroundResource(resID[2]);
			start3.setBackgroundResource(resID[0]);
			start4.setBackgroundResource(resID[0]);
			start5.setBackgroundResource(resID[0]);
		}else if (rating >= step * 5 && rating < step * 6) {
			start1.setBackgroundResource(resID[2]);
			start2.setBackgroundResource(resID[2]);
			start3.setBackgroundResource(resID[1]);
			start4.setBackgroundResource(resID[0]);
			start5.setBackgroundResource(resID[0]);
		}else if (rating >= step * 6 && rating < step * 7) {
			start1.setBackgroundResource(resID[2]);
			start2.setBackgroundResource(resID[2]);
			start3.setBackgroundResource(resID[2]);
			start4.setBackgroundResource(resID[0]);
			start5.setBackgroundResource(resID[0]);
		}else if (rating >= step * 7 && rating < step * 8) {
			start1.setBackgroundResource(resID[2]);
			start2.setBackgroundResource(resID[2]);
			start3.setBackgroundResource(resID[2]);
			start4.setBackgroundResource(resID[1]);
			start5.setBackgroundResource(resID[0]);
		}else if (rating >= step * 8 && rating < step * 9) {
			start1.setBackgroundResource(resID[2]);
			start2.setBackgroundResource(resID[2]);
			start3.setBackgroundResource(resID[2]);
			start4.setBackgroundResource(resID[2]);
			start5.setBackgroundResource(resID[0]);
		}else if (rating >= step * 9 && rating < step * 10) {
			start1.setBackgroundResource(resID[2]);
			start2.setBackgroundResource(resID[2]);
			start3.setBackgroundResource(resID[2]);
			start4.setBackgroundResource(resID[2]);
			start5.setBackgroundResource(resID[1]);
		}else if (rating >= step * 10) {
			start1.setBackgroundResource(resID[2]);
			start2.setBackgroundResource(resID[2]);
			start3.setBackgroundResource(resID[2]);
			start4.setBackgroundResource(resID[2]);
			start5.setBackgroundResource(resID[2]);
		}
	}
}
