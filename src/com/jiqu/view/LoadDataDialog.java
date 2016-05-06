package com.jiqu.view;


import com.vr.store.R;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class LoadDataDialog extends Dialog{
	private Animation animation;
	private ImageView loading;
	private Button loadAgain;
	private OnLoadAgain onLoadAgain;

	public LoadDataDialog(Context context) {
		super(context,R.style.LoadDataDialog);
		// TODO Auto-generated constructor stub
		animation = AnimationUtils.loadAnimation(context, R.anim.wating_rorating); 
		initView();
	}

	private void initView(){
		setContentView(R.layout.load_data_dialog_layout);
		loading = (ImageView) findViewById(R.id.loading);
		loadAgain = (Button) findViewById(R.id.loadAgain);
		loadAgain.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (onLoadAgain != null) {
					loadAgain();
					onLoadAgain.loadAgain();
				}
			}
		});
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		loading.setAnimation(animation);
		loading.startAnimation(animation);
	}
	
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
		loading.clearAnimation();
	}
	
	public void setOnLoadAgainListener(OnLoadAgain onLoadAgain){
		this.onLoadAgain = onLoadAgain;
	}
	
	public void loadFail(){
		loading.clearAnimation();
		loading.setVisibility(View.GONE);
		loadAgain.setVisibility(View.VISIBLE);
	}
	
	private void loadAgain(){
		loading.setAnimation(animation);
		loading.startAnimation(animation);
		loading.setVisibility(View.VISIBLE);
		loadAgain.setVisibility(View.GONE);
	}
	
	public interface OnLoadAgain{
		public void loadAgain();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return true;
	}
}
