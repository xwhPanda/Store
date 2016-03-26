package com.jiqu.activity;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.PasswordView;
import com.jiqu.view.TitleView;

public class RegisterActivity extends BaseActivity implements OnClickListener,OnCheckedChangeListener{
	private TitleView titleView;
	private PasswordView nickNameView,phoneNumberView,emailView,passwordView,confirmPasswordView;
	private RadioButton manRadio,femaleRadio;
	private RadioGroup genderGroup;
	private Button registerBtn;
	
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
		phoneNumberView = (PasswordView) findViewById(R.id.phoneNumberView);
		emailView = (PasswordView) findViewById(R.id.emailView);
		manRadio = (RadioButton) findViewById(R.id.manRadio);
		femaleRadio = (RadioButton) findViewById(R.id.femaleRadio);
		genderGroup = (RadioGroup) findViewById(R.id.genderGroup);
		passwordView = (PasswordView) findViewById(R.id.passwordView);
		confirmPasswordView = (PasswordView) findViewById(R.id.confirmPasswordView);
		registerBtn = (Button) findViewById(R.id.registerBtn);
		
		setViewSize();
		setViewData();
	}
	
	private void setViewData(){
		titleView.tip.setText(R.string.registerTop);
		titleView.setActivity(this);
		nickNameView.editText.setHint(R.string.nickname);
		phoneNumberView.editText.setHint(R.string.phone);
		emailView.editText.setHint(R.string.email);
		passwordView.editText.setHint(R.string.password);
		confirmPasswordView.editText.setHint(R.string.confirmPassword);
		
		nickNameView.img.setVisibility(View.GONE);
		nickNameView.visibleButton.setVisibility(View.GONE);
		phoneNumberView.img.setVisibility(View.GONE);
		phoneNumberView.visibleButton.setVisibility(View.GONE);
		emailView.img.setVisibility(View.GONE);
		emailView.visibleButton.setVisibility(View.GONE);
		passwordView.img.setVisibility(View.GONE);
		confirmPasswordView.img.setVisibility(View.GONE);
		
		registerBtn.setOnClickListener(this);
		genderGroup.setOnCheckedChangeListener(this);
	}
	
	private void setViewSize(){
		UIUtil.setViewSize(nickNameView, 800 * Rx, 100 * Ry);
		UIUtil.setViewHeight(phoneNumberView, 100 * Ry);
		UIUtil.setViewHeight(emailView, 100 * Ry);
		UIUtil.setViewHeight(passwordView, 100 * Ry);
		UIUtil.setViewHeight(confirmPasswordView, 100 * Ry);
		UIUtil.setViewHeight(registerBtn, 100 * Ry);
		
		UIUtil.setTextSize(manRadio, 35);
		UIUtil.setTextSize(femaleRadio, 35);
		
		
		Drawable[] drawables = manRadio.getCompoundDrawables();
		drawables[0].setBounds(0,0,(int)(48 * Rx),(int)(48 * Rx));
		manRadio.setCompoundDrawables(drawables[0],drawables[1],drawables[2],drawables[3]);
		
		Drawable[] drawables1 = femaleRadio.getCompoundDrawables();
		drawables1[0].setBounds(0,0,(int)(48 * Rx),(int)(48 * Rx));
		femaleRadio.setCompoundDrawables(drawables1[0],drawables1[1],drawables1[2],drawables1[3]);
		
		try {
			UIUtil.setViewSizeMargin(nickNameView, 0, 505 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(phoneNumberView, 0, 30 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(emailView, 0, 30 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(genderGroup, 0, 40 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(femaleRadio, 245 * Ry, 0, 0, 0);
			UIUtil.setViewSizeMargin(passwordView, 0, 40 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(confirmPasswordView, 0, 30 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(registerBtn, 0, 250 * Ry, 0, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		if (checkedId == R.id.manRadio) {
			
		}else if (checkedId == R.id.femaleRadio) {
			
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
