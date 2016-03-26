package com.jiqu.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.store.R.id;
import com.jiqu.store.R.layout;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.PasswordView;
import com.jiqu.view.TitleView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MemberLoginActivity extends BaseActivity implements OnClickListener{
	private TitleView titleView;
	private ImageView accountIcon;
	private Button forgetPassword,register;
	private PasswordView accountInputView,passwordInputView;
	private Button login;
	private LinearLayout forgetPdLin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		init();
		setListener();
	}
	
	private void init(){
		titleView = (TitleView) findViewById(R.id.titleView);
		accountIcon = (ImageView) findViewById(R.id.accountIcon);
		forgetPassword = (Button) findViewById(R.id.forgetPassword);
		register = (Button) findViewById(R.id.register);
		accountInputView = (PasswordView) findViewById(R.id.accountInputView);
		passwordInputView = (PasswordView) findViewById(R.id.passwordInputView);
		login = (Button) findViewById(R.id.login);
		forgetPdLin = (LinearLayout) findViewById(R.id.forgetPdLin);
		
		accountInputView.visibleButton.setVisibility(View.GONE);
		accountInputView.editText.setInputType(InputType.TYPE_CLASS_TEXT);
		accountInputView.img.setBackgroundResource(R.drawable.people);
		passwordInputView.img.setBackgroundResource(R.drawable.suo);
		passwordInputView.addTextChanedListener();
		
		titleView.setActivity(this);
		titleView.tip.setText(R.string.loginTopTip);
		initSize();
	}
	
	private void initSize(){
		UIUtil.setViewSize(accountIcon, 250 * Rx, 250 * Rx);
		UIUtil.setViewSize(accountInputView, 800 * Rx, 100 * Ry);
		UIUtil.setViewSize(passwordInputView, 800 * Rx, 100 * Ry);
		UIUtil.setViewHeight(login, 100 * Ry);
		
		UIUtil.setTextSize(login, 50);
		UIUtil.setTextSize(forgetPassword, 35);
		UIUtil.setTextSize(register, 35);
		
		
		try {
			UIUtil.setViewSizeMargin(accountIcon, 0, 380 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(accountInputView, 0, 95 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(passwordInputView, 0, 35 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(login, 0, 500 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(forgetPdLin, 0, 45 * Ry, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setListener(){
		forgetPassword.setOnClickListener(this);
		register.setOnClickListener(this);
		
	}
	
	@Override
	public int getContentView() {
		//返回对应的布局ID
		return R.layout.rember_login;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.forgetPassword:
			startActivity(new Intent(this, ForgetPasswordActivity.class));
			break;
			
		case R.id.register:
			startActivity(new Intent(this, RegisterActivity.class));
			break;
		}
	}
}