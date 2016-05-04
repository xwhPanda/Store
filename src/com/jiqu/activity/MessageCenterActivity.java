package com.jiqu.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.jiqu.adapter.InformationAdapter;
import com.jiqu.adapter.PrivateMessageAdapter;
import com.jiqu.object.GameInformation;
import com.jiqu.object.PrivateMessage;
import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.TitleView;

public class MessageCenterActivity extends BaseActivity implements OnClickListener{
	private TitleView titleView;
	private Button notice;
	private Button privateLetter;
	private ListView contentView,messageContentView;
	private PullToRefreshLayout refreshView,messageRefreshView;
	private RelativeLayout btnLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
	}
	
	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.message_center_layout;
	}
	
	private void initView(){
		titleView = (TitleView) findViewById(R.id.titleView);
		notice = (Button) findViewById(R.id.notice);
		privateLetter = (Button) findViewById(R.id.privateLetter);
		refreshView = (PullToRefreshLayout) findViewById(R.id.refreshView);
		contentView = (ListView) findViewById(R.id.contentView);
		messageRefreshView = (PullToRefreshLayout) findViewById(R.id.messageRefreshView);
		messageContentView = (ListView) findViewById(R.id.messageContentView);
		btnLayout = (RelativeLayout) findViewById(R.id.btnLayout);
		
		titleView.setActivity(this);
		titleView.editBtn.setVisibility(View.VISIBLE);
		titleView.tip.setText(R.string.messageCenter);
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		
		notice.setOnClickListener(this);
		privateLetter.setOnClickListener(this);
		
		initViewSize();
		
		changeButtonState(notice);
		
		List<GameInformation> gameInformations = new ArrayList<GameInformation>();
		
		for (int i = 0; i < 30; i++)
		{
			GameInformation game = new GameInformation();
			gameInformations.add(game);
		}
//		InformationAdapter adapter = new InformationAdapter(this, gameInformations,1);
//		contentView.setAdapter(adapter);
		
		List<PrivateMessage> messages = new ArrayList<PrivateMessage>();
		
		for (int i = 0; i < 30; i++)
		{
			PrivateMessage message = new PrivateMessage();
			messages.add(message);
		}
		PrivateMessageAdapter adapter1 = new PrivateMessageAdapter(this, messages);
		messageContentView.setAdapter(adapter1);
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(notice, 525 * Rx, 85 * Ry);
		UIUtil.setViewSize(privateLetter, 525 * Rx, 85 * Ry);
		
		UIUtil.setTextSize(notice, 45);
		UIUtil.setTextSize(privateLetter, 45);
		
		try {
			UIUtil.setViewSizeMargin(btnLayout, 0, 190 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(refreshView, 0, 55 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(messageRefreshView, 0, 55 * Ry, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void changeButtonState(Button button){
		if (button == notice) {
			notice.setTextColor(getResources().getColor(R.color.white));
			notice.setBackgroundResource(R.drawable.xinxi_sel);
			privateLetter.setTextColor(getResources().getColor(R.color.blue));
			privateLetter.setBackgroundResource(R.drawable.xinxi_nor);
		}else if (button == privateLetter) {
			privateLetter.setTextColor(getResources().getColor(R.color.white));
			privateLetter.setBackgroundResource(R.drawable.xinxi_sel);
			notice.setTextColor(getResources().getColor(R.color.blue));
			notice.setBackgroundResource(R.drawable.xinxi_nor);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.notice) {
			changeButtonState(notice);
			messageRefreshView.setVisibility(View.INVISIBLE);
			refreshView.setVisibility(View.VISIBLE);
		}else if (v.getId() == R.id.privateLetter) {
			changeButtonState(privateLetter);
			refreshView.setVisibility(View.INVISIBLE);
			messageRefreshView.setVisibility(View.VISIBLE);
			
		}
	}
}
