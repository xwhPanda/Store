package com.jiqu.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.jiqu.adapter.MessageAdapter;
import com.jiqu.adapter.PrivateMessageAdapter;
import com.jiqu.object.MessageDataInfo;
import com.jiqu.object.MessageInfo;
import com.jiqu.object.PrivateMessageDataInfo;
import com.jiqu.object.PrivateMessageInfo;
import com.jiqu.store.BaseActivity;
import com.vr.store.R;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.TitleView;

public class MessageCenterActivity extends BaseActivity implements OnClickListener{
	private final String MESSAGE_REQUEST = "messageRequest";
	private final String PRIVATE_REQUEST = "privateReuqest";
	private TitleView titleView;
	private Button notice;
	private Button privateLetter;
	private ListView contentView,messageContentView;
	private PullToRefreshLayout refreshView,messageRefreshView;
	private RelativeLayout btnLayout;
	private RequestTool requestTool;
	private Map<String, Object> messageMap = new HashMap<String, Object>();
	private Map<String, Object> privateMap = new HashMap<String, Object>();
	private List<MessageDataInfo> messageDataInfos = new ArrayList<MessageDataInfo>();
	private List<PrivateMessageDataInfo> privateMessageDataInfos = new ArrayList<PrivateMessageDataInfo>();
	private MessageAdapter messageAdapter;
	private PrivateMessageAdapter privateMessageAdapter;
	private int messagePageNum = 1;
	private int privatePageNum = 1;
	private boolean isPrivateFirst = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestTool = RequestTool.getInstance();
		initView();
		loadMessageData(RequestTool.MESSAGE_LIST_URL);
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
		titleView.tip.setText(R.string.messageCenter);
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		
		notice.setOnClickListener(this);
		privateLetter.setOnClickListener(this);
		
		initViewSize();
		
		changeButtonState(notice);
		
		messageAdapter = new MessageAdapter(this, messageDataInfos);
		contentView.setAdapter(messageAdapter);
		
		privateMessageAdapter = new PrivateMessageAdapter(this, privateMessageDataInfos);
		messageContentView.setAdapter(privateMessageAdapter);
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
			if (isPrivateFirst) {
				isPrivateFirst = false;
				loadPrivateMessageData(RequestTool.PRIVATE_LIST_URL);
			}
			
		}
	}
	
	private void loadMessageData(String url){
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				MessageInfo messageInfo = JSON.parseObject(arg0, MessageInfo.class);
				if (messageInfo != null) {
					if (messageInfo.getStatus() == 1) {
						Collections.addAll(messageDataInfos, messageInfo.getData());
						messageAdapter.notifyDataSetChanged();
					}
				}
			}
		}, url, new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				
			}
			
		}, messageMap, MESSAGE_REQUEST);
	}
	
	private void loadPrivateMessageData(String url){
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				PrivateMessageInfo privateMessageInfo = JSON.parseObject(arg0, PrivateMessageInfo.class);
				if (privateMessageInfo != null) {
					if (privateMessageInfo.getStatus() == 1) {
						Collections.addAll(privateMessageDataInfos, privateMessageInfo.getData());
						privateMessageAdapter.notifyDataSetChanged();
					}
				}
			}
		}, url, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				
			}
		}, privateMap, PRIVATE_REQUEST);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		requestTool.stopRequest(MESSAGE_REQUEST);
		requestTool.stopRequest(PRIVATE_REQUEST);
	}
}
