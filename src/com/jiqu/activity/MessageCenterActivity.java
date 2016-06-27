package com.jiqu.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.jiqu.adapter.MessageAdapter;
import com.jiqu.adapter.PrivateMessageAdapter;
import com.jiqu.application.StoreApplication;
import com.jiqu.database.Account;
import com.jiqu.database.MessageTable;
import com.jiqu.object.MessageDataInfo;
import com.jiqu.object.MessageInfo;
import com.jiqu.object.PrivateMessageDataInfo;
import com.jiqu.object.PrivateMessageInfo;
import com.jiqu.store.BaseActivity;
import com.umeng.message.entity.UMessage;
import com.vr.store.R;
import com.jiqu.tools.Constants;
import com.jiqu.tools.MD5;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.LoadStateView;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.PullToRefreshLayout.OnRefreshListener;
import com.jiqu.view.TitleView;

import de.greenrobot.dao.query.QueryBuilder;

public class MessageCenterActivity extends BaseActivity implements OnClickListener,OnRefreshListener{
	private final String MESSAGE_REQUEST = "messageRequest";
	private final String PRIVATE_REQUEST = "privateReuqest";
	private RelativeLayout parent;
	private TitleView titleView;
	private Button notice;
	private Button privateLetter;
	private ListView contentView,messageContentView;
	private PullToRefreshLayout refreshView,messageRefreshView;
	private RelativeLayout btnLayout;
	private RelativeLayout messageRel;
	private RelativeLayout privateRel;
	private LoadStateView messageLoadView;
	private LoadStateView privateLoadView;
	private RequestTool requestTool;
	private Map<String, Object> messageMap = new HashMap<String, Object>();
	private Map<String, Object> privateMap = new HashMap<String, Object>();
	private List<MessageDataInfo> messageDataInfos = new ArrayList<MessageDataInfo>();
	private List<PrivateMessageDataInfo> privateMessageDataInfos = new ArrayList<PrivateMessageDataInfo>();
	private MessageAdapter messageAdapter;
	private PrivateMessageAdapter privateMessageAdapter;
	private boolean messageShowing = false;
	private boolean privateShowing = false;
	private int messagePageNum = 1;
	private int privatePageNum = 1;
	private boolean isPrivateFirst = true;
	private Account account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		account = StoreApplication.daoSession.getAccountDao().queryBuilder().unique();
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
		parent = (RelativeLayout) findViewById(R.id.parent);
		parent.setBackgroundDrawable(StoreApplication.BG_IMG);
		titleView = (TitleView) findViewById(R.id.titleView);
		notice = (Button) findViewById(R.id.notice);
		privateLetter = (Button) findViewById(R.id.privateLetter);
		refreshView = (PullToRefreshLayout) findViewById(R.id.refreshView);
		contentView = (ListView) findViewById(R.id.contentView);
		messageRefreshView = (PullToRefreshLayout) findViewById(R.id.messageRefreshView);
		messageContentView = (ListView) findViewById(R.id.messageContentView);
		btnLayout = (RelativeLayout) findViewById(R.id.btnLayout);
		messageRel = (RelativeLayout) findViewById(R.id.messageRel);
		privateRel = (RelativeLayout) findViewById(R.id.privateRel);
		messageLoadView = (LoadStateView) findViewById(R.id.messageLoadView);
		privateLoadView = (LoadStateView) findViewById(R.id.privateLoadView);
		
		titleView.setActivity(this);
		titleView.tip.setText(R.string.messageCenter);
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		
		refreshView.setOnRefreshListener(this);
		messageRefreshView.setOnRefreshListener(this);
		notice.setOnClickListener(this);
		privateLetter.setOnClickListener(this);
		messageLoadView.loadAgain(this);
		privateLoadView.loadAgain(this);
		
		initViewSize();
		
		changeButtonState(notice);
		
		messageAdapter = new MessageAdapter(this, messageDataInfos);
		contentView.setAdapter(messageAdapter);
		
		privateMessageAdapter = new PrivateMessageAdapter(this, privateMessageDataInfos);
		messageContentView.setAdapter(privateMessageAdapter);
		
		contentView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(messageDataInfos.get(position).getUrl())) {
					startActivity(new Intent(MessageCenterActivity.this, GameEvaluationWebInfoActivity.class)
					.putExtra("showShareBtn", false)
					.putExtra("url", messageDataInfos.get(position).getUrl())
					.putExtra("title", messageDataInfos.get(position).getTitle()));
				}
			}
		});
		
		messageContentView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(privateMessageDataInfos.get(position).getUrl())) {
					startActivity(new Intent(MessageCenterActivity.this, GameEvaluationWebInfoActivity.class)
					.putExtra("showShareBtn", false)
					.putExtra("url", privateMessageDataInfos.get(position).getUrl())
					.putExtra("title", privateMessageDataInfos.get(position).getTitle()));
				}
			}
		});
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
			privateRel.setVisibility(View.INVISIBLE);
			messageRel.setVisibility(View.VISIBLE);
		}else if (v.getId() == R.id.privateLetter) {
			changeButtonState(privateLetter);
			messageRel.setVisibility(View.INVISIBLE);
			privateRel.setVisibility(View.VISIBLE);
			if (isPrivateFirst) {
				isPrivateFirst = false;
//				loadPrivateMessageData(RequestTool.PRIVATE_LIST_URL);
				
				privateLoadView.loadDataSuccess();
				privateLoadView.setVisibility(View.GONE);
				messageRefreshView.setVisibility(View.VISIBLE);
				
				loadPrivateMessageFromDB();
			}
		}else if (v == messageLoadView.getLoadBtn()) {
			messageLoadView.showLoading();
			loadMessageData(RequestTool.MESSAGE_LIST_URL);
		}else if (v == privateLoadView.getLoadBtn()) {
			privateLoadView.showLoading();
			loadPrivateMessageData(RequestTool.PRIVATE_LIST_URL);
		}
	}
	
	private void loadMessageData(String url){
		requestTool.startStringRequest(Method.GET, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				messageLoadView.loadDataSuccess();
				messageLoadView.setVisibility(View.GONE);
				refreshView.setVisibility(View.VISIBLE);
				MessageInfo messageInfo = JSON.parseObject(arg0, MessageInfo.class);
				if (messageInfo != null) {
					if (messageInfo.getStatus() == 1) {
						messagePageNum++;
						Collections.addAll(messageDataInfos, messageInfo.getData());
						messageAdapter.notifyDataSetChanged();
					}else if(messageInfo.getStatus() == 0){
						if (messagePageNum != 1) {
							Toast.makeText(MessageCenterActivity.this, R.string.notMore, Toast.LENGTH_SHORT).show();
						}else {
							Toast.makeText(MessageCenterActivity.this, "没有通知", Toast.LENGTH_SHORT).show();
						}
					}
				}
				if (messageShowing) {
					messageShowing = false;
					refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
			}
		}, url, new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				if (messageShowing) {
					messageShowing = false;
					refreshView.refreshFinish(PullToRefreshLayout.FAIL);
				}
				messageLoadView.loadDataFail();
			}
			
		}, messageMap, MESSAGE_REQUEST);
	}
	
	private void loadPrivateMessageData(String url){
		if (account == null ) {
			return;
		}
		privateMap.put("uid", account.getUid());
		privateMap.put("token", MD5.GetMD5Code(account.getUid() + RequestTool.PRIKEY));
		requestTool.startStringRequest(Method.POST, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				privateLoadView.loadDataFail();
				privateLoadView.setVisibility(View.GONE);
				messageRefreshView.setVisibility(View.VISIBLE);
				PrivateMessageInfo privateMessageInfo = JSON.parseObject(arg0, PrivateMessageInfo.class);
				if (privateMessageInfo != null) {
					if (privateMessageInfo.getStatus() == 1) {
						privatePageNum++;
						Collections.addAll(privateMessageDataInfos, privateMessageInfo.getData());
						privateMessageAdapter.notifyDataSetChanged();
					}else if(privateMessageInfo.getStatus() == 0 ){
						if (privatePageNum != 1) {
							Toast.makeText(MessageCenterActivity.this, R.string.notMore, Toast.LENGTH_SHORT).show();
						}else {
							Toast.makeText(MessageCenterActivity.this, "没有私信", Toast.LENGTH_SHORT).show();
						}
					}
				}
				if (privateShowing) {
					privateShowing = false;
					messageRefreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
			}
		}, url, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				if (privateShowing) {
					privateShowing = false;
					messageRefreshView.refreshFinish(PullToRefreshLayout.FAIL);
				}
				privateLoadView.loadDataFail();
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

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
		if (pullToRefreshLayout == refreshView) {
			messageShowing = true;
			loadMessageData(RequestTool.MESSAGE_LIST_URL + "?pageNum=" + messagePageNum);
		}else if (pullToRefreshLayout == messageRefreshView) {
			privateShowing = true;
			loadPrivateMessageData(RequestTool.PRIVATE_LIST_URL + "?pageNum=" + privatePageNum);
//			loadPrivateMessageFromDB();
		}
	}
	
	private void loadPrivateMessageFromDB(){
		QueryBuilder<MessageTable> qb = StoreApplication.daoSession.getMessageTableDao().queryBuilder();
		List<MessageTable> messages = qb.list();
		Collections.reverse(messages);
		for(MessageTable message : messages ){
			try {
				PrivateMessageDataInfo dataInfo = new PrivateMessageDataInfo();
				UMessage uMessage = new UMessage(new JSONObject(message.getMessage()));
				dataInfo.setId(String.valueOf(message.getId()));
				dataInfo.setContent(uMessage.custom);
				Map<String, String> extra = uMessage.extra;
				if (extra != null) {
					dataInfo.setTitle(extra.get("title"));
					dataInfo.setUrl(extra.get("url"));
				}
				dataInfo.setTime(message.getTime());
				privateMessageDataInfos.add(dataInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		privateMessageAdapter.notifyDataSetChanged();
	}
	
	
	@Override
	public void removeFromActivityList() {
		// TODO Auto-generated method stub
		Constants.ACTIVITY_LIST.remove(this);
	}

	@Override
	public void addToActivityList() {
		// TODO Auto-generated method stub
		Constants.ACTIVITY_LIST.add(this);
	}
}
