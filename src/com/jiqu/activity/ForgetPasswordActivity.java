package com.jiqu.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.jiqu.download.StringUtil;
import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.MD5;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.PasswordView;
import com.jiqu.view.TitleView;

public class ForgetPasswordActivity extends BaseActivity implements OnClickListener{
	private static final String GETCODE_TAG = "getcode";
	private static final String RESET_TAG = "resetPassword";
	private EditText accountEd;
	private EditText codesEd;
	private Button getCodes;
	private RelativeLayout codesLin;
	private TextView timeCount;
	private PasswordView inputPassword,confirmPassword;
	private Button commit;
	private TitleView title;
	
	private RequestTool requestTool; 
	
	private boolean getCoding = false;
	private boolean resetingPassword = false;
	
	@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			requestTool = RequestTool.getInstance();
			init();
		}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.forget_password;
	}

	private void init(){
		codesLin = (RelativeLayout) findViewById(R.id.codesLin);
		accountEd = (EditText) findViewById(R.id.account);
		codesEd = (EditText) findViewById(R.id.codesEd);
		getCodes = (Button) findViewById(R.id.getCodes);
		timeCount = (TextView) findViewById(R.id.timeCount);
		inputPassword = (PasswordView) findViewById(R.id.inputPassword);
		confirmPassword = (PasswordView) findViewById(R.id.confirmPassword);
		commit = (Button) findViewById(R.id.commit);
		title = (TitleView) findViewById(R.id.titleView);
		
		inputPassword.editText.setHint(R.string.password);
		confirmPassword.editText.setHint(R.string.confirmPassword);
		inputPassword.img.setVisibility(View.GONE);
		confirmPassword.img.setVisibility(View.GONE);
		inputPassword.addTextChanedListener();
		confirmPassword.addTextChanedListener();
		
		commit.setOnClickListener(this);
		getCodes.setOnClickListener(this);
		
		initViewSize();
		title.tip.setText(R.string.setPasswordAgain);
		title.setActivity(this);
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(accountEd, 800 * Rx, 100 * Ry);
		UIUtil.setViewSize(codesLin, 0, 100 * Ry);
		UIUtil.setViewSize(inputPassword, 0, 100 * Ry);
		UIUtil.setViewSize(confirmPassword, 0, 100 * Ry);
		
		UIUtil.setViewSize(inputPassword.visibleButton, 56 * Ry, 56 * Ry);
		UIUtil.setViewSize(confirmPassword.visibleButton, 56 * Ry, 56 * Ry);
		UIUtil.setViewSize(commit, 0, 100 * Ry);
		
		UIUtil.setTextSize(timeCount, 25);
		UIUtil.setTextSize(getCodes, 45);
		UIUtil.setTextSize(accountEd, 45);
		UIUtil.setTextSize(codesEd, 45);
		UIUtil.setTextSize(inputPassword.editText, 45);
		UIUtil.setTextSize(confirmPassword.editText, 45);
		UIUtil.setTextSize(commit, 45);
		
		UIUtil.setViewPadding(accountEd, (int)(30 * Rx), 0, (int)(30 * Rx), 0);
		UIUtil.setViewPadding(codesEd, (int)(30 * Rx), 0, (int)(10 * Rx), 0);
		UIUtil.setViewPadding(inputPassword.editText, (int)(30 * Rx), 0, (int)(30 * Rx), 0);
		UIUtil.setViewPadding(confirmPassword.editText, (int)(30 * Rx), 0, (int)(30 * Rx), 0);
		
		try {
			UIUtil.setViewSizeMargin(accountEd, 0, 420 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(codesLin, 0, 60 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(timeCount, 0, 5 * Ry, 20 * Rx, 0);
			UIUtil.setViewSizeMargin(inputPassword, 0, 60 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(confirmPassword, 0, 60 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(commit, 0, 205 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(inputPassword.visibleButton, 0, 0, 30 * Rx, 0);
			UIUtil.setViewSizeMargin(confirmPassword.visibleButton, 0, 0, 30 * Rx, 0);
			UIUtil.setViewSizeMargin(getCodes, 0, 0, 30 * Rx, 0);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == commit.getId()) {
			if (getCoding) {
				UIUtil.showToast(R.string.getCoding);
			}else {
				if (resetingPassword) {
					UIUtil.showToast(R.string.resetingPassword);
				}else {
					String account = accountEd.getText().toString().trim();
					String code = codesEd.getText().toString().trim();
					String password = inputPassword.getText().toString().trim();
					String rePassword = confirmPassword.getText().toString().trim();
					if (checkValue(account, code, password, rePassword)) {
						resetingPassword = true;
						resetPassword(account, code, password, rePassword);
					}
				}
			}
			
		}else if (v.getId() == getCodes.getId()) {
			if (getCoding) {
				UIUtil.showToast(R.string.getCoding);
			}else {
				String account = accountEd.getText().toString().trim();
				if (TextUtils.isEmpty(account)) {
					UIUtil.showToast(R.string.accountIsNull);
				}else if (!StringUtil.isEmail(account)) {
					UIUtil.showToast(R.string.emailError);
				}else {
					getCoding = true;
					getCode(account);
				}
			}
		}
	}
	
	private boolean checkValue(String account,String code,String password,String rePassword){
		if (TextUtils.isEmpty(account)) {
			UIUtil.showToast(R.string.accountNotExist);
			return false;
		}else if (TextUtils.isEmpty(code)) {
			UIUtil.showToast(R.string.codeIsNull);
			return false;
		}else if (TextUtils.isEmpty(password)) {
			UIUtil.showToast(R.string.passwordIsNull);
			return false;
		}else if (TextUtils.isEmpty(rePassword) || !password.equals(rePassword)) {
			UIUtil.showToast(R.string.confirmPasswordIsNull);
			return false;
		}else {
			return true;
		}
	}
	
	private void getCode(String account){
		String url = RequestTool.GET_RESET_CODE_URL + "?auth=" + account + "&token=" + MD5.GetMD5Code(account + RequestTool.PRIKEY);
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				Log.i("TAG", arg0);
				getCoding = false;
				if (arg0.contains("status")) {
					int status = JSON.parseObject(arg0).getIntValue("status");
					if (status == 0 || status == -1 || status == -9) {
						/** 获取失败**/
						UIUtil.showToast(R.string.getCodeFaile);
					}else if (status == 1) {
						/** 获取成功 **/
					}else if (status == -2) {
						/** 用户不存在 **/
						UIUtil.showToast(R.string.accountNotExist);
					}
				}else {
					UIUtil.showToast(R.string.getCodeFaile);
				}
			}
		}, url, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				getCoding = false;
				Log.i("TAG", arg0.toString());
			}
		}, requestTool.getMap(), GETCODE_TAG);
	}
	
	private void resetPassword(String account,String code,String password,String rePassword){
		requestTool.getMap().clear();
		requestTool.setParam("auth", account);
		requestTool.setParam("vercode", code);
		requestTool.setParam("password", MD5.GetMD5Code(password));
		requestTool.setParam("repasswd", MD5.GetMD5Code(rePassword));
		requestTool.setParam("token", MD5.GetMD5Code(account + code + MD5.GetMD5Code(password) + MD5.GetMD5Code(rePassword) + RequestTool.PRIKEY));
		
		requestTool.startStringRequest(Method.POST, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				resetingPassword = false;
				if (arg0.contains("status")) {
					int status = JSON.parseObject(arg0).getIntValue("status");
					if (status == 0 || status == -1 || status == -5 || status == -9) {
						/** 重置失败 **/
						UIUtil.showToast(R.string.resetFailed);
					}else if (status == 1) {
						/** 成功 **/
						UIUtil.showToast(R.string.resetSuccess);
						ForgetPasswordActivity.this.finish();
					}else if (status == -2) {
						/** 用户名不存在 **/
						UIUtil.showToast(R.string.accountNotExist);
					}else if (status == -3) {
						/** 验证码不正确 **/
						UIUtil.showToast(R.string.codeWrong);
					}else if (status == -4) {
						/** 两次密码不一致 **/
						UIUtil.showToast(R.string.confirmPasswordIsNull);
					}else if (status == -6) {
						/** 验证码超时 **/
						UIUtil.showToast(R.string.codeTimeOut);
					}
				}
			}
		}, RequestTool.RESETPASSWORD_URL, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				resetingPassword = false;
				UIUtil.showToast(R.string.resetFailed);
			}
		}, requestTool.getMap(), RESET_TAG);
	}
}
