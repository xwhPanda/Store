package com.jiqu.view;

import com.vr.store.R;
import com.jiqu.application.StoreApplication;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FriendShareItem extends RelativeLayout {
	private View view;
	private ImageView img;
	private TextView title;
	private Button share;


	public FriendShareItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context,attrs);
		initViewSize();
	}


	
	private void initView(Context context,AttributeSet attrs){
		LayoutInflater inflater = LayoutInflater.from(StoreApplication.context);
		view = inflater.inflate(R.layout.friend_share_item, this);
		img = (ImageView) view.findViewById(R.id.img);
		title = (TextView) view.findViewById(R.id.title);
		share = (Button) view.findViewById(R.id.share);
		
		TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.ShareAttr);
		int imgBg = ta.getResourceId(R.styleable.ShareAttr_imgBg, 0);
		int titleText = ta.getResourceId(R.styleable.ShareAttr_titleText, 0);
		int shareBg = ta.getResourceId(R.styleable.ShareAttr_shareBg, 0);
		
		if (imgBg != 0) {
			img.setBackgroundResource(imgBg);
		}
		if (titleText != 0) {
			title.setText(titleText);
		}
		if (shareBg != 0) {
			share.setBackgroundResource(shareBg);
		}
		
		ta.recycle();
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(img, 76 * MetricsTool.Rx, 76 * MetricsTool.Rx);
		UIUtil.setViewSize(share, 36 * MetricsTool.Rx, 36 * MetricsTool.Rx);
		
		UIUtil.setTextSize(title, 35);
		
		try {
			UIUtil.setViewSizeMargin(img, 30 * MetricsTool.Rx, 0, 40 * MetricsTool.Rx, 0);
			UIUtil.setViewSizeMargin(share, 0, 0, 40 * MetricsTool.Rx, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setImgBg(int resId){
		img.setBackgroundResource(resId);
	}
	
	public void setTitleContent(int resId){
		title.setText(resId);
	}
	
	public Button getShareBtn(){
		return share;
	}
	
	public void setShareBg(int resId){
		share.setBackgroundResource(resId);
	}
}
