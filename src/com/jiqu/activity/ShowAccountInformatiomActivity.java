package com.jiqu.activity;

import java.util.Calendar;

import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.SpinnerView;
import com.jiqu.view.TitleView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShowAccountInformatiomActivity extends BaseActivity implements OnClickListener{
	private TitleView titleView;
	private ImageView accountImg;
	private TextView nickName;
	private TextView level;
	private Button modiftBtn;
	private SpinnerView genderSpinner;
	private RelativeLayout genderRel,birthRel,phoneRel,qqRel;
	private TextView gender,birth,phone,qq;
	
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
		gender = (TextView) findViewById(R.id.gender);
		birth = (TextView) findViewById(R.id.birth);
		phone = (TextView) findViewById(R.id.phone);
		qq = (TextView) findViewById(R.id.qq);
		genderRel = (RelativeLayout) findViewById(R.id.genderRel);
		birthRel = (RelativeLayout) findViewById(R.id.birthRel);
		phoneRel = (RelativeLayout) findViewById(R.id.phoneRel);
		qqRel = (RelativeLayout) findViewById(R.id.qqRel);
		genderSpinner = (SpinnerView) findViewById(R.id.genderSpinner);
		
		genderSpinner.setPopSize(true);
		
		modiftBtn.setOnClickListener(this);
		
		titleView.setActivity(this);
		titleView.tip.setText(R.string.memberInformation);
		
		initViewSize();
		initData();
	}
	
	private void initData(){
		genderSpinner.setData(UIUtil.getDataFromXML(this, R.array.gender));
	}
	
	
	private void initViewSize(){
		UIUtil.setViewSize(accountImg, 250 * Rx, 250 * Rx);
		UIUtil.setViewSize(level, 155 * Rx, 50 * Ry);
		UIUtil.setViewHeight(modiftBtn, 100 * Ry);
		UIUtil.setViewSize(genderRel, 1040 * Rx, 175 * Ry);
		UIUtil.setViewHeight(birthRel, 175 * Ry);
		UIUtil.setViewHeight(phoneRel, 175 * Ry);
		UIUtil.setViewHeight(qqRel, 175 * Ry);
		
		UIUtil.setTextSize(gender, 40);
		UIUtil.setTextSize(birth, 40);
		UIUtil.setTextSize(phone, 40);
		UIUtil.setTextSize(qq, 40);
		UIUtil.setTextSize(level, 25);
		UIUtil.setTextSize(modiftBtn, 50);
		level.setText("LV5");
		
		Log.i("TAG", "" + Calendar.getInstance().getTime().getYear());
		Calendar calendar = Calendar.getInstance();
		Log.i("TAG", calendar.get(Calendar.YEAR) + "");
		
		try {
			UIUtil.setViewSizeMargin(accountImg, 0, 240 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(level, 0, 40 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(modiftBtn, 0, 60 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(birthRel, 0, 35 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(phoneRel, 0, 35 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(qqRel, 0, 35 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(gender, 35 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(birth, 35 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(phone, 35 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(qq, 35 * Rx, 0, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}	
