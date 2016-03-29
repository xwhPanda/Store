package com.jiqu.activity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.PasswordView;
import com.jiqu.view.QuickLoginView;
import com.jiqu.view.TitleView;

public class RegisterActivity extends BaseActivity implements OnClickListener{
	private TitleView titleView;
	private PasswordView nickNameView,emailView,passwordView,confirmPasswordView;
	private Button registerBtn;
	private QuickLoginView quickLoginView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
	}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.register_layout;
	}
	
	private void initView(){
		titleView = (TitleView) findViewById(R.id.titleView);
		nickNameView = (PasswordView) findViewById(R.id.nickNameView);
		emailView = (PasswordView) findViewById(R.id.emailView);
		passwordView = (PasswordView) findViewById(R.id.passwordView);
		confirmPasswordView = (PasswordView) findViewById(R.id.confirmPasswordView);
		registerBtn = (Button) findViewById(R.id.registerBtn);
		quickLoginView = (QuickLoginView) findViewById(R.id.quickLoginView);
		
		setViewSize();
		setViewData();
	}
	
	private void setViewData(){
		titleView.tip.setText(R.string.registerTop);
		titleView.setActivity(this);
		nickNameView.editText.setHint(R.string.account);
		emailView.editText.setHint(R.string.email);
		passwordView.editText.setHint(R.string.password);
		confirmPasswordView.editText.setHint(R.string.confirmPassword);
		
		nickNameView.img.setVisibility(View.GONE);
		nickNameView.visibleButton.setVisibility(View.GONE);
		emailView.img.setVisibility(View.GONE);
		emailView.visibleButton.setVisibility(View.GONE);
		passwordView.img.setVisibility(View.GONE);
		confirmPasswordView.img.setVisibility(View.GONE);
		passwordView.visibleButton.setVisibility(View.GONE);
		confirmPasswordView.visibleButton.setVisibility(View.GONE);
		
		nickNameView.editText.setInputType(InputType.TYPE_CLASS_TEXT);
		
		passwordView.addTextChanedListener();
		confirmPasswordView.addTextChanedListener();
		registerBtn.setOnClickListener(this);
	}
	
	private void setViewSize(){
		UIUtil.setViewSize(nickNameView, 800 * Rx, 100 * Ry);
		UIUtil.setViewHeight(emailView, 100 * Ry);
		UIUtil.setViewHeight(passwordView, 100 * Ry);
		UIUtil.setViewHeight(confirmPasswordView, 100 * Ry);
		UIUtil.setViewHeight(registerBtn, 100 * Ry);
		UIUtil.setViewHeight(quickLoginView, 480 * Ry);
		
		UIUtil.setTextSize(registerBtn, 45);
		
		UIUtil.setViewPadding(nickNameView.editText, (int)(30 * Rx), 0, (int)(30 * Rx), 0);
		UIUtil.setViewPadding(emailView.editText, (int)(30 * Rx), 0, (int)(30 * Rx), 0);
		UIUtil.setViewPadding(passwordView.editText, (int)(30 * Rx), 0, (int)(30 * Rx), 0);
		UIUtil.setViewPadding(confirmPasswordView.editText, (int)(30 * Rx), 0, (int)(30 * Rx), 0);
		
		try {
			UIUtil.setViewSizeMargin(nickNameView, 0, 420 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(emailView, 0, 60 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(passwordView, 0, 60 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(confirmPasswordView, 0, 60 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(registerBtn, 0, 205 * Ry, 0, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
