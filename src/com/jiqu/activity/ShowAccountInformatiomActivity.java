package com.jiqu.activity;

import java.util.ArrayList;
import java.util.List;

import com.jiqu.adapter.AccountInformationAdapter;
import com.jiqu.object.AccountInformation;
import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.store.R.layout;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.TitleView;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ShowAccountInformatiomActivity extends BaseActivity implements OnClickListener{
	private TitleView titleView;
	private ListView accountInformationList;
	private AccountInformationAdapter adapter;
	private ImageView accountImg;
	private TextView level;
	private Button modiftBtn;
	
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
		accountInformationList = (ListView) findViewById(R.id.accountInformationList);
		accountImg = (ImageView) findViewById(R.id.accountImg);
		level = (TextView) findViewById(R.id.level);
		modiftBtn = (Button) findViewById(R.id.modifyBtn);
		
		modiftBtn.setOnClickListener(this);
		
		titleView.setActivity(this);
		titleView.tip.setText(R.string.memberInformation);
		
		List<AccountInformation> informations = new ArrayList<AccountInformation>();
		for(int i = 0;i < 5;i++){
			AccountInformation information = new AccountInformation();
			information.setTitle("昵称");
			information.setValue("德玛西亚 !");
			informations.add(information);
		}
		
		accountInformationList.setAdapter(new AccountInformationAdapter(ShowAccountInformatiomActivity.this, informations));
		
		initViewSize();
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(accountImg, 250 * Rx, 250 * Rx);
		UIUtil.setViewSize(level, 155 * Rx, 50 * Ry);
		UIUtil.setViewHeight(modiftBtn, 100 * Ry);
		
		UIUtil.setTextSize(level, 25);
		UIUtil.setTextSize(modiftBtn, 50);
		level.setText("LV5");
		
		accountInformationList.setDividerHeight((int)(30 * Ry));
		
		try {
			UIUtil.setViewSizeMargin(accountImg, 0, 240 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(level, 0, 40 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(accountInformationList, 20 * Rx, 55 * Ry, 20 * Rx, 0);
			UIUtil.setViewSizeMargin(modiftBtn, 0, 60 * Ry, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}	
