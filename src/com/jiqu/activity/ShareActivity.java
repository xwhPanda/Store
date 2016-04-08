package com.jiqu.activity;

import android.os.Bundle;

import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.FriendShareItem;
import com.jiqu.view.TitleView;

public class ShareActivity extends BaseActivity {
	private TitleView titleView;
	private FriendShareItem contacts,qq,weixin,weibo;

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
		
		titleView.setActivity(this);
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.tip.setText(R.string.friendShare);
		
		initViewSize();
	}
	
	private void initViewSize(){
		UIUtil.setViewHeight(contacts, 180 * Ry);
		UIUtil.setViewHeight(qq, 180 * Ry);
		UIUtil.setViewHeight(weibo, 180 * Ry);
		UIUtil.setViewHeight(weixin, 180 * Ry);
		
		try {
			UIUtil.setViewSizeMargin(contacts, 0, 165 * Ry, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
