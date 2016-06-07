package com.jiqu.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiqu.adapter.ShareAddFriendAdapter;
import com.jiqu.application.StoreApplication;
import com.jiqu.object.FriendItem;
import com.jiqu.store.BaseActivity;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.vr.store.R;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.FriendShareItem;
import com.jiqu.view.PullToRefreshLayout;
import com.jiqu.view.TitleView;

public class ShareActivity extends BaseActivity {
	private RelativeLayout parent;
	private TitleView titleView;
	private FriendShareItem qq,weixin,weibo;
	
	private ShareAddFriendAdapter adapter;
	private List<FriendItem> friendItems = new ArrayList<FriendItem>();
	
	private String url = "http://www.baidu.com";
	private UMImage image;
	private String content;
	private String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		image = new UMImage(this, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
		content = "极趣VR助手是国内领先的虚拟现实平台" +
				"，拥有最新最全的VR游戏和最权威、全面的VR行业资讯" +
				"，专业游戏评测,，致力于为用户提供最好的精品VR游戏。";
		title = "极趣VR助手";
		
		Bundle bundle = getIntent().getBundleExtra("bundle");
		if (bundle != null) {
			content = bundle.getString("content");
			url = bundle.getString("url");
			title = bundle.getString("title");
			if (bundle.containsKey("image")) {
				image = new UMImage(this, bundle.getString("image"));
			}
		}
		
		initView();
	}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.share_layout;
	}
	
	private void initView(){
		parent = (RelativeLayout) findViewById(R.id.parent);
		parent.setBackgroundDrawable(StoreApplication.BG_IMG);
		titleView = (TitleView) findViewById(R.id.titleView);
		qq = (FriendShareItem) findViewById(R.id.qq);
		weixin = (FriendShareItem) findViewById(R.id.weixin);
		weibo = (FriendShareItem) findViewById(R.id.weibo);
		
//		inviteTip = (TextView) findViewById(R.id.inviteTip);
		refreshView = (PullToRefreshLayout) findViewById(R.id.refreshView);
//		friendListView = (PullableListView) findViewById(R.id.friendListView);
		
		titleView.setActivity(this);
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.tip.setText(R.string.friendShare);
		
		initViewSize();
		
		for(int i = 0;i < 10;i++){
			FriendItem shareItem = new FriendItem();
			friendItems.add(shareItem);
		}
		
//		adapter = new ShareAddFriendAdapter(this, friendItems);
//		friendListView.setAdapter(adapter);
		
		weibo.getShareBtn().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Config.OpenEditor = false;
				com.umeng.socialize.utils.Log.LOG = false;
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
				.withTargetUrl(url)
				.withTitle(title)
				.withMedia(image)
				.withText(content)
				.share();
			}
		});
		
		qq.getShareBtn().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UMImage image = new UMImage(ShareActivity.this
						, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
				new ShareAction(ShareActivity.this)
				.setPlatform(SHARE_MEDIA.QQ)
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
				.withMedia(image)
				.withTargetUrl(url)
				.withTitle(title)
				.withText(content)
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
		UIUtil.setViewHeight(qq, 180 * Ry);
		UIUtil.setViewHeight(weibo, 180 * Ry);
		UIUtil.setViewHeight(weixin, 180 * Ry);
//		UIUtil.setViewHeight(inviteTip, 140 * Ry);
//		
//		UIUtil.setTextSize(inviteTip, 35);
//		
//		UIUtil.setViewPadding(inviteTip, (int)(30 * Rx), 0, 0, 0);
	}

}
