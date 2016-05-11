package com.jiqu.view;

import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;
import com.vr.store.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoadStateView extends RelativeLayout {
	private View view;
	private LinearLayout loadRel;
	private RelativeLayout loadAgainRel;
	private ImageView loadImg;
	private TextView loadFailTip;
	private TextView loading;
	private Button loadAgain;
	private ImageView loadingImg;
	private AnimationDrawable animationDrawable;

	public LoadStateView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public LoadStateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public LoadStateView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context){
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.load_state_layout, this);
		loadRel = (LinearLayout) view.findViewById(R.id.loadRel);
		loadAgain = (Button) view.findViewById(R.id.loadAgain);
		loadingImg = (ImageView) view.findViewById(R.id.loadingImg);
		loadAgainRel = (RelativeLayout) view.findViewById(R.id.loadAgainRel);
		loadImg = (ImageView) view.findViewById(R.id.loadImg);
		loadFailTip = (TextView) view.findViewById(R.id.loadFailTip);
		loading = (TextView) view.findViewById(R.id.loading);
		
		UIUtil.setViewSize(loadingImg, 166 * MetricsTool.Rx, 166 * MetricsTool.Rx);
		animationDrawable = ((AnimationDrawable)loadingImg.getBackground());
		animationDrawable.start();
		
		UIUtil.setViewSize(loadImg, 297 * MetricsTool.Rx, 314 * MetricsTool.Rx);
		UIUtil.setViewSize(loadAgain, 395 * MetricsTool.Rx, 95 * MetricsTool.Rx);
		
		UIUtil.setTextSize(loading, 35);
		UIUtil.setTextSize(loadFailTip, 35);
		UIUtil.setTextSize(loadAgain, 40);
		
		try {
			UIUtil.setViewSizeMargin(loading, 0, 60 * MetricsTool.Ry, 0, 0);
			UIUtil.setViewSizeMargin(loadFailTip, 0, 45 * MetricsTool.Ry, 0, 0);
			UIUtil.setViewSizeMargin(loadAgain, 0, 92 * MetricsTool.Ry, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadDataSuccess(){
		animationDrawable.stop();
	}
	
	public void loadDataFail(){
		loadRel.setVisibility(View.GONE);
		loadAgainRel.setVisibility(View.VISIBLE);
	}
	
	public void showLoading(){
		loadRel.setVisibility(View.VISIBLE);
		animationDrawable.start();
		loadAgainRel.setVisibility(View.GONE);
	}
	
	public void loadAgain(OnClickListener onClickListener){
		loadAgain.setOnClickListener(onClickListener);
	}
	
	public Button getLoadBtn(){
		return loadAgain;
	}
}
