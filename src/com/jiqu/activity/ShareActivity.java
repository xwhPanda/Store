package com.jiqu.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.jiqu.adapter.ShareAddFriendAdapter;
import com.jiqu.object.FriendItem;
import com.jiqu.store.BaseActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.vr.store.R;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.FriendShareItem;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.PullableListView;
import com.jiqu.view.TitleView;

public class ShareActivity extends BaseActivity {
	private TitleView titleView;
	private FriendShareItem contacts,qq,weixin,weibo;
	private PullToRefreshLayout refreshView;
	private ListView friendListView;
	private TextView inviteTip;
	
	private ShareAddFriendAdapter adapter;
	private List<FriendItem> friendItems = new ArrayList<FriendItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
	}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.share_layout;
	}
	
	private void initView(){
		titleView = (TitleView) findViewById(R.id.titleView);
		contacts = (FriendShareItem) findViewById(R.id.contacts);
		qq = (FriendShareItem) findViewById(R.id.qq);
		weixin = (FriendShareItem) findViewById(R.id.weixin);
		weibo = (FriendShareItem) findViewById(R.id.weibo);
		
		inviteTip = (TextView) findViewById(R.id.inviteTip);
		refreshView = (PullToRefreshLayout) findViewById(R.id.refreshView);
		friendListView = (PullableListView) findViewById(R.id.friendListView);
		
		titleView.setActivity(this);
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.tip.setText(R.string.friendShare);
		
		initViewSize();
		
		for(int i = 0;i < 10;i++){
			FriendItem shareItem = new FriendItem();
			friendItems.add(shareItem);
		}
		adapter = new ShareAddFriendAdapter(this, friendItems);
		friendListView.setAdapter(adapter);
		
		weibo.getShareBtn().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new ShareAction(ShareActivity.this)
				.setPlatform(SHARE_MEDIA.SINA)
				.setCallback(new UMShareListener() {
					
					@Override
					public void onResult(SHARE_MEDIA arg0) {
						// TODO Auto-generated method stub
					}
					
					@Override
					public void onError(SHARE_MEDIA arg0, Throwable arg1) {
						// TODO Auto-generated method stub
					}
					
					@Override
					public void onCancel(SHARE_MEDIA arg0) {
						// TODO Auto-generated method stub
					}
				})
				.withText("")
				.share();
			}
		});
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult( requestCode, resultCode, data);
    }

	
	private void initViewSize(){
		UIUtil.setViewHeight(contacts, 180 * Ry);
		UIUtil.setViewHeight(qq, 180 * Ry);
		UIUtil.setViewHeight(weibo, 180 * Ry);
		UIUtil.setViewHeight(weixin, 180 * Ry);
		UIUtil.setViewHeight(inviteTip, 140 * Ry);
		
		UIUtil.setTextSize(inviteTip, 35);
		
		UIUtil.setViewPadding(inviteTip, (int)(30 * Rx), 0, 0, 0);
		
		try {
			UIUtil.setViewSizeMargin(contacts, 0, 165 * Ry, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
