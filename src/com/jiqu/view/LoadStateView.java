package com.jiqu.view;

import com.vr.store.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoadStateView extends RelativeLayout {
	private View view;
	private LinearLayout loadRel;
	private TextView loadingText;
	private DotsTextView dots;
	private Button loadAgain;

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
		loadingText = (TextView) view.findViewById(R.id.loadingText);
		dots = (DotsTextView) view.findViewById(R.id.dots);
		loadAgain = (Button) view.findViewById(R.id.loadAgain);
	}
	
	public void loadDataSuccess(){
		dots.stop();
	}
	
	public void loadDataFail(){
		loadRel.setVisibility(View.GONE);
		loadAgain.setVisibility(View.VISIBLE);
	}
	
	public void showLoading(){
		loadRel.setVisibility(View.VISIBLE);
		dots.start();
		loadAgain.setVisibility(View.GONE);
	}
	
	public void loadAgain(OnClickListener onClickListener){
		loadAgain.setOnClickListener(onClickListener);
	}
}
