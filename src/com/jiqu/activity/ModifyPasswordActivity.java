package com.jiqu.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.jiqu.application.StoreApplication;
import com.jiqu.store.BaseActivity;
import com.umeng.analytics.MobclickAgent;
import com.vr.store.R;
import com.jiqu.tools.Constants;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.PasswordView;
import com.jiqu.view.TitleView;

public class ModifyPasswordActivity extends BaseActivity {
	private RelativeLayout parent;
	private TitleView titleView;
	private PasswordView passwordView,confirmPasswordView;
	private Button commit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
	}
	
	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.modify_password_layout;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("ModifyPasswordActivity");
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("ModifyPasswordActivity");
		MobclickAgent.onPause(this);
	}
	
	private void initView(){
		parent = (RelativeLayout) findViewById(R.id.parent);
		parent.setBackgroundDrawable(StoreApplication.BG_IMG);
		titleView = (TitleView) findViewById(R.id.titleView);
		passwordView = (PasswordView) findViewById(R.id.passwordView);
		confirmPasswordView = (PasswordView) findViewById(R.id.confirmPasswordView);
		commit = (Button) findViewById(R.id.commit);
		
		titleView.setActivity(this);
		titleView.tip.setText(R.string.modifyPassword);
		titleView.back.setBackgroundResource(R.drawable.guanbi);
		
		passwordView.img.setVisibility(View.GONE);
		passwordView.editText.setHint(R.string.password);
		passwordView.addTextChanedListener();
		confirmPasswordView.img.setVisibility(View.GONE);
		confirmPasswordView.editText.setHint(R.string.confirmPassword);
		confirmPasswordView.addTextChanedListener();
		
		initViewSize();
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(passwordView, 800 * Rx, 100 * Ry);
		UIUtil.setViewHeight(confirmPasswordView, 100 * Ry);
		UIUtil.setViewHeight(commit, 100 * Ry);
		
		UIUtil.setTextSize(commit, 50);
		
		try {
			UIUtil.setViewSizeMargin(passwordView, 0, 595 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(confirmPasswordView, 0, 30 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(commit, 0, 340 * Ry, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void removeFromActivityList() {
		// TODO Auto-generated method stub
		Constants.ACTIVITY_LIST.remove(this);
	}

	@Override
	public void addToActivityList() {
		// TODO Auto-generated method stub
		Constants.ACTIVITY_LIST.add(this);
	}
}
