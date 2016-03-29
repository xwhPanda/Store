package com.jiqu.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.PasswordView;
import com.jiqu.view.TitleView;

public class ForgetPasswordActivity extends BaseActivity implements OnClickListener{
	private EditText accountEd;
	private EditText codesEd;
	private Button getCodes;
	private RelativeLayout codesLin;
	private TextView timeCount;
	private PasswordView inputPassword,confirmPassword;
	private Button commit;
	private TitleView title;
	
	@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
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
			
		}
	}
}
