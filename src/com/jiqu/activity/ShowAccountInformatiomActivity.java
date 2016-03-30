package com.jiqu.activity;


import com.jiqu.interfaces.DialogDismissObserver;
import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.InformationBrithDialog;
import com.jiqu.view.InformationGenderDialog;
import com.jiqu.view.TitleView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class ShowAccountInformatiomActivity extends BaseActivity implements OnClickListener,DialogDismissObserver{
	private TitleView titleView;
	private ImageView accountImg;
	private TextView nickName;
	private TextView level;
	private Button modiftBtn;
	private Button loginOut;
	private RelativeLayout genderRel,birthRel,phoneRel,qqRel;
	private TextView gender,birth,phone,qq;
	private Button genderBtn;
	private Button birthBtn;
	private InformationGenderDialog dialog;
	private InformationBrithDialog brithDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
	}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.account_information;
	}
	
	private void initView(){
		titleView = (TitleView) findViewById(R.id.titleView);
		accountImg = (ImageView) findViewById(R.id.accountImg);
		nickName = (TextView) findViewById(R.id.nickName);
		level = (TextView) findViewById(R.id.level);
		modiftBtn = (Button) findViewById(R.id.modifyBtn);
		loginOut = (Button) findViewById(R.id.loginOut);
		gender = (TextView) findViewById(R.id.gender);
		birth = (TextView) findViewById(R.id.birth);
		phone = (TextView) findViewById(R.id.phone);
		qq = (TextView) findViewById(R.id.qq);
		genderRel = (RelativeLayout) findViewById(R.id.genderRel);
		birthRel = (RelativeLayout) findViewById(R.id.birthRel);
		phoneRel = (RelativeLayout) findViewById(R.id.phoneRel);
		qqRel = (RelativeLayout) findViewById(R.id.qqRel);
		genderBtn = (Button) findViewById(R.id.genderBtn);
		birthBtn = (Button) findViewById(R.id.birthBtn);
		
		modiftBtn.setOnClickListener(this);
		genderBtn.setOnClickListener(this);
		birthBtn.setOnClickListener(this);
		
		dialog  = new InformationGenderDialog(this);
		dialog.setObserver(this);
		
		brithDialog = new InformationBrithDialog(this);
		brithDialog.setObserver(this);
		
		titleView.setActivity(this);
		titleView.tip.setText(R.string.memberInformation);
		
		initViewSize();
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(accountImg, 250 * Rx, 250 * Rx);
		UIUtil.setViewSize(level, 155 * Rx, 50 * Ry);
		UIUtil.setViewSize(genderBtn, 35 * MetricsTool.Rx, 35 * MetricsTool.Rx);
		UIUtil.setViewSize(birthBtn, 35 * MetricsTool.Rx, 35 * MetricsTool.Rx);
		UIUtil.setViewHeight(modiftBtn, 100 * Ry);
		UIUtil.setViewHeight(loginOut, 100 * Ry);
		UIUtil.setViewSize(genderRel, 1040 * Rx, 175 * Ry);
		UIUtil.setViewHeight(birthRel, 175 * Ry);
		UIUtil.setViewHeight(phoneRel, 175 * Ry);
		UIUtil.setViewHeight(qqRel, 175 * Ry);
		
		UIUtil.setTextSize(gender, 40);
		UIUtil.setTextSize(birth, 40);
		UIUtil.setTextSize(phone, 40);
		UIUtil.setTextSize(qq, 40);
		UIUtil.setTextSize(genderBtn, 40);
		UIUtil.setTextSize(birthBtn, 40);
		UIUtil.setTextSize(level, 25);
		UIUtil.setTextSize(modiftBtn, 50);
		UIUtil.setTextSize(loginOut,50);
		level.setText("LV5");
		
		try {
			UIUtil.setViewSizeMargin(accountImg, 0, 240 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(level, 0, 40 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(modiftBtn, 0, 60 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(loginOut, 0, 25 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(birthRel, 0, 35 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(phoneRel, 0, 35 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(qqRel, 0, 35 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(gender, 35 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(birth, 35 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(phone, 35 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(qq, 35 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(genderBtn, 0, 0, 35 * Rx, 0);
			UIUtil.setViewSizeMargin(birthBtn, 0, 0, 35 * Rx, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.genderBtn:
			showDialog(dialog);
			setBottomBtnVisible(View.GONE);
			break;

		case R.id.birthBtn:
			showDialog(brithDialog);
			setBottomBtnVisible(View.GONE);
			break;
		}
	}
	
	private void showDialog(Dialog dialog){
		Window window = dialog.getWindow();
		window.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = getWindowManager().getDefaultDisplay().getHeight();
		wl.width = WindowManager.LayoutParams.FILL_PARENT;
        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
		// 设置显示位置
		dialog.onWindowAttributesChanged(wl);
		dialog.show();
	}
	
	private void setBottomBtnVisible(int visible){
		modiftBtn.setVisibility(visible);
		loginOut.setVisibility(visible);
	}

	@Override
	public void onDialogStateChange(int state) {
		// TODO Auto-generated method stub
		if (state == 0) {
			setBottomBtnVisible(View.VISIBLE);
		}
	}

	@Override
	public void onDialogSave(int type , String value) {
		// TODO Auto-generated method stub
		if (type == 0) {//性别选择
			if (value.length() > 0 && !"".equals(value)) {
				genderBtn.setText(value);
				genderBtn.setBackgroundDrawable(null);
				UIUtil.setViewSize(genderBtn, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			}
		}else if (type == 1) {//生日选择
			if (value.length() > 0 && !"".equals(value)) {
				birthBtn.setText(value);
				birthBtn.setBackgroundDrawable(null);
				UIUtil.setViewSize(birthBtn, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			}
		}
		setBottomBtnVisible(View.VISIBLE);
		
	}
}	
