package com.jiqu.activity;

import org.json.JSONObject;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.jiqu.download.StringUtil;
import com.jiqu.object.AccountResponeInfo;
import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.Constant;
import com.jiqu.tools.MD5;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.PasswordView;
import com.jiqu.view.QuickLoginView;
import com.jiqu.view.TitleView;

public class RegisterActivity extends BaseActivity implements OnClickListener{
	private TitleView titleView;
	private PasswordView nickNameView,emailView,passwordView,confirmPasswordView;
	private Button registerBtn;
	private QuickLoginView quickLoginView;
	
	private RequestTool requestTool;
	private static final String REGISTER_TAG = "register";
	private boolean registering = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestTool = RequestTool.getInstance();
		
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
		emailView.editText.setInputType(InputType.TYPE_CLASS_TEXT);
		
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
		if (!registering) {
			checkValue();
		}else {
			Toast.makeText(this, R.string.registering, Toast.LENGTH_SHORT).show();
		}
	}

	private void checkValue(){
		String account = nickNameView.getText();
		String email = emailView.getText();
		String password = passwordView.getText();
		String rePassword = confirmPasswordView.getText();
		
		if (TextUtils.isEmpty(account)) {
			Toast.makeText(this, R.string.accountIsNull,Toast.LENGTH_SHORT).show();
		}else if(account.length() > Constant.ACCOUNT_MAX_LENGTH){
			Toast.makeText(this, R.string.accountIsTooLong,Toast.LENGTH_SHORT).show();
		}else if (TextUtils.isEmpty(email)) {
			Toast.makeText(this, R.string.emailIsNull,Toast.LENGTH_SHORT).show();
		}else if (!StringUtil.isEmail(email)) {
			Toast.makeText(this, R.string.emailError,Toast.LENGTH_SHORT).show();
		}else if (TextUtils.isEmpty(password)) {
			Toast.makeText(this, R.string.passwordIsNull,Toast.LENGTH_SHORT).show();
		}else if (TextUtils.isEmpty(rePassword)) {
			Toast.makeText(this, R.string.confirmPasswordIsNull,Toast.LENGTH_SHORT).show();
		}else if (!password.equals(rePassword)) {
			Toast.makeText(this, R.string.confirmPasswordIsNull,Toast.LENGTH_SHORT).show();
		}else {
			registering = true;
			registerRequest(account, email, 1, MD5.GetMD5Code(password), MD5.GetMD5Code(rePassword));
		}
	}
	
	private void registerRequest(String username,String email,int gender,String password,String rePassword){
		requestTool.getMap().clear();
		requestTool.setParam("username", username);
		requestTool.setParam("email", email);
		requestTool.setParam("gender", gender + "");
		requestTool.setParam("password", password);
		requestTool.setParam("rePasswd", rePassword);
		requestTool.startStringRequest(new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				registering = false;
				AccountResponeInfo responeInfo = JSON.parseObject(arg0.toString(), AccountResponeInfo.class);
				if (responeInfo.getStatus() == -1 || responeInfo.getStatus() == 0) {
					/**注册失败**/
					Toast.makeText(RegisterActivity.this, R.string.registerFailed, Toast.LENGTH_SHORT).show();
				}else if (responeInfo.getStatus() == -3) {
					/**邮箱格式错误**/
					Toast.makeText(RegisterActivity.this, R.string.emailError, Toast.LENGTH_SHORT).show();
				}else if (responeInfo.getStatus() == -4) {
					/**帐号已经存在**/
					Toast.makeText(RegisterActivity.this, R.string.accountExist, Toast.LENGTH_SHORT).show();
				}else if (responeInfo.getStatus() == -5) {
					/**邮箱已经注册过**/
					Toast.makeText(RegisterActivity.this, R.string.emailExist, Toast.LENGTH_SHORT).show();
				}else if (responeInfo.getStatus() == 1) {
					/**注册成功**/
					Toast.makeText(RegisterActivity.this, R.string.registerSuccess, Toast.LENGTH_SHORT).show();
					RegisterActivity.this.finish();
				}
			}
		}, RequestTool.REGISTER_URL,new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				registering = false;
				Toast.makeText(RegisterActivity.this, R.string.registerFailed, Toast.LENGTH_SHORT).show();
			}
		},requestTool.getMap(),REGISTER_TAG);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		requestTool.stopRequest(REGISTER_TAG);
	}
}
