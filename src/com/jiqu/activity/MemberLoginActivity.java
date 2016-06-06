package com.jiqu.activity;


import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.jiqu.application.StoreApplication;
import com.jiqu.database.Account;
import com.jiqu.interfaces.LoginOutObserver;
import com.jiqu.object.AccountInformation;
import com.jiqu.object.AccountResponeInfo;
import com.jiqu.store.BaseActivity;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.vr.store.R;
import com.jiqu.tools.MD5;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.umeng.UMengManager;
import com.jiqu.view.NetChangeDialog;
import com.jiqu.view.PasswordView;
import com.jiqu.view.QuickLoginView;
import com.jiqu.view.TitleView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MemberLoginActivity extends BaseActivity implements OnClickListener{
	private final String LOGIN_TAG = "login";
	private final String OTHER_REGISTER = "otherRegisterRequest";
	private RelativeLayout parent;
	private TitleView titleView;
	private ImageView accountIcon;
	private Button forgetPassword,register;
	private PasswordView accountInputView,passwordInputView;
	private Button login;
	private LinearLayout forgetPdLin;
	private QuickLoginView quickLoginView;
	
	private RequestTool requestTool;
	private boolean loging = false;
	
	private UMengManager mUMengManager;
	private ProgressDialog loginDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestTool = RequestTool.getInstance();
		
		init();
		setListener();
	}
	
	private void init(){
		mUMengManager = UMengManager.getInstance();
		loginDialog = new ProgressDialog(this);
		parent = (RelativeLayout) findViewById(R.id.parent);
		parent.setBackgroundDrawable(StoreApplication.BG_IMG);
		titleView = (TitleView) findViewById(R.id.titleView);
		accountIcon = (ImageView) findViewById(R.id.accountIcon);
		forgetPassword = (Button) findViewById(R.id.forgetPassword);
		register = (Button) findViewById(R.id.register);
		accountInputView = (PasswordView) findViewById(R.id.accountInputView);
		passwordInputView = (PasswordView) findViewById(R.id.passwordInputView);
		login = (Button) findViewById(R.id.login);
		forgetPdLin = (LinearLayout) findViewById(R.id.forgetPdLin);
		quickLoginView = (QuickLoginView) findViewById(R.id.quickLoginView);
		
		accountInputView.visibleButton.setVisibility(View.GONE);
		accountInputView.editText.setInputType(InputType.TYPE_CLASS_TEXT);
		accountInputView.img.setBackgroundResource(R.drawable.people);
		passwordInputView.img.setBackgroundResource(R.drawable.suo);
		passwordInputView.addTextChanedListener();
		
		titleView.setActivity(this);
		titleView.tip.setText(R.string.loginTopTip);
		
		quickLoginView.setQqClickListener(this);
		quickLoginView.setWeiXinClickListener(this);
		quickLoginView.setSinaClickListener(this);
		
		initSize();
	}
	
	private void initSize(){
		UIUtil.setViewSize(accountIcon, 250 * Rx, 250 * Rx);
		UIUtil.setViewSize(accountInputView, 800 * Rx, 100 * Ry);
		UIUtil.setViewSize(passwordInputView, 800 * Rx, 100 * Ry);
		UIUtil.setViewHeight(login, 100 * Ry);
		UIUtil.setViewHeight(quickLoginView, 480 * Ry);
		
		UIUtil.setTextSize(login, 50);
		UIUtil.setTextSize(forgetPassword, 35);
		UIUtil.setTextSize(register, 35);
		
		try {
			UIUtil.setViewSizeMargin(accountIcon, 0, 365 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(accountInputView, 0, 140 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(passwordInputView, 0, 66 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(login, 0, 120 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(forgetPdLin, 0, 30 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(passwordInputView.visibleButton, 0, 0, 30 * Rx, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setListener(){
		forgetPassword.setOnClickListener(this);
		register.setOnClickListener(this);
		login.setOnClickListener(this);
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
		case R.id.login:
			if (!loging) {
				String account = accountInputView.getText().trim();
				String password = passwordInputView.getText().trim();
				if (checkValue(account,password)) {
					loging = true;
					login(account,password);
				}
			}else {
				Toast.makeText(this, R.string.loging, Toast.LENGTH_SHORT).show();
			}
			break;
			
		case R.id.forgetPassword:
			startActivity(new Intent(this, ForgetPasswordActivity.class));
			break;
			
		case R.id.register:
			startActivity(new Intent(this, RegisterActivity.class));
			break;
		
		case R.id.sinaLogin:
			mUMengManager.sinaAuth(this, new UMAuthListener() {
				
				@Override
				public void onError(SHARE_MEDIA arg0, int arg1, Throwable arg2) {
					// TODO Auto-generated method stub
					Log.i("TAG", "onError");
				}
				
				@Override
				public void onComplete(SHARE_MEDIA arg0, int arg1, Map<String, String> arg2) {
					// TODO Auto-generated method stub
					Log.i("TAG", "onComplete");
				}
				
				@Override
				public void onCancel(SHARE_MEDIA arg0, int arg1) {
					// TODO Auto-generated method stub
					Log.i("TAG", "onCancel");
				}
			});
			break;
		case R.id.qqLogin:
			mUMengManager.qqAuth(this, new UMAuthListener() {
				
				@Override
				public void onError(SHARE_MEDIA arg0, int arg1, Throwable arg2) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onComplete(SHARE_MEDIA arg0, int arg1, Map<String, String> data) {
					// TODO Auto-generated method stub
					final String uid = data.get("uid");
					mUMengManager.getQqInfo(MemberLoginActivity.this, new UMAuthListener() {
						
						@Override
						public void onError(SHARE_MEDIA arg0, int arg1, Throwable arg2) {
							// TODO Auto-generated method stub
							arg2.printStackTrace();
							Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
						}
						
						@Override
						public void onComplete(SHARE_MEDIA arg0, int arg1, Map<String, String> data) {
							// TODO Auto-generated method stub
							String nickname = data.get("screen_name");
							int gender = 1;
							if (data.get("gender").equals("女")) {
								gender = 2;
							}
							String image = data.get("profile_image_url");
							loginDialog.show();
							qqLogin(nickname, gender, uid, MD5.GetMD5Code(uid + String.valueOf(gender) + RequestTool.PRIKEY),image);
						}
						
						@Override
						public void onCancel(SHARE_MEDIA arg0, int arg1) {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(), "取消登录", Toast.LENGTH_SHORT).show();
						}
					});
				}
				
				@Override
				public void onCancel(SHARE_MEDIA arg0, int arg1) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "取消登录", Toast.LENGTH_SHORT).show();
				}
			});
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		mUMengManager.onActivityResult(requestCode, resultCode, data);
	}
	
	private boolean checkValue(String account,String password){
		
		if (TextUtils.isEmpty(account)) {
			Toast.makeText(this, R.string.accountIsNull,Toast.LENGTH_SHORT).show();
			return false;
		}else if (TextUtils.isEmpty(password)) {
			Toast.makeText(this, R.string.passwordIsNull,Toast.LENGTH_SHORT).show();
			return false;
		}else {
			return true;
		}
	}
	
	private void qqLogin(String nickname,int gender,String uid,String token,final String image){
		requestTool.getMap().clear();
		requestTool.setParam("nickname", nickname);
		requestTool.setParam("gender", gender);
		requestTool.setParam("uid", uid);
		requestTool.setParam("token", token);
		requestTool.startStringRequest(Method.POST, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				AccountResponeInfo responeInfo = JSON.parseObject(arg0, AccountResponeInfo.class);
				Account account = null;
				if (responeInfo.getPhoto() == null) {
					account = AccountInformation.toAccount(responeInfo.getData(), image);
				}
				StoreApplication.daoSession.getAccountDao().insertOrReplace(account);
				if (StoreApplication.loginOutObservers != null) {
					for(LoginOutObserver observer : StoreApplication.loginOutObservers){
						observer.onRefresh(account);
					}
				}
				loginDialog.dismiss();
				MemberLoginActivity.this.finish();
			}
		}, RequestTool.OTHER_REGISTER_URL, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				loginDialog.dismiss();
			}
		}, requestTool.getMap(), OTHER_REGISTER);
	}
	
	private void login(String account,String password){
		requestTool.getMap().clear();
		requestTool.setParam("auth", account);
		requestTool.setParam("password", MD5.GetMD5Code(password));
		
		requestTool.startStringRequest(Method.POST,new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				loging = false;
				AccountResponeInfo accountResponeInfo = JSON.parseObject(arg0, AccountResponeInfo.class);
				if (accountResponeInfo.getStatus() == 0) {
					/**用户不存在**/
					Toast.makeText(MemberLoginActivity.this, R.string.accountNotExist, Toast.LENGTH_SHORT).show();
				}else if (accountResponeInfo.getStatus() == 1) {
					/**登录成功**/
					Toast.makeText(MemberLoginActivity.this, R.string.longinSuccess, Toast.LENGTH_SHORT).show();
					Account account = AccountInformation.toAccount(accountResponeInfo.getData(),accountResponeInfo.getPhoto());
					StoreApplication.daoSession.getAccountDao().insertOrReplace(account);
					if (StoreApplication.loginOutObservers != null) {
						for(LoginOutObserver observer : StoreApplication.loginOutObservers){
							observer.onRefresh(account);
						}
					}
					MemberLoginActivity.this.finish();
				}else if (accountResponeInfo.getStatus() == -1) {
					/**登录失败**/
					Toast.makeText(MemberLoginActivity.this, R.string.loginFailed, Toast.LENGTH_SHORT).show();
				}else if (accountResponeInfo.getStatus() == -2) {
					/**密码错误**/
					Toast.makeText(MemberLoginActivity.this, R.string.passwordWrong, Toast.LENGTH_SHORT).show();
				}
			}
		}, RequestTool.LOGIN_URL,new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				loging = false;
				Toast.makeText(MemberLoginActivity.this, R.string.loginFailed, Toast.LENGTH_SHORT).show();
			}
			
		}, requestTool.getMap(),LOGIN_TAG);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		requestTool.stopRequest(LOGIN_TAG);
	}
}