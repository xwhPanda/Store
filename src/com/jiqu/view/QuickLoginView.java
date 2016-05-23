package com.jiqu.view;

import java.util.Map;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.vr.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.umeng.UMengManager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class QuickLoginView extends RelativeLayout{
	private Context context;
	private RelativeLayout titleRel;
	private ImageView leftLine;
	private ImageView rightLine;
	private TextView title;
	private RelativeLayout iconRel;
	private Button qqLogin,weixinLogin;
	private Button sinaLogin;
	
	public QuickLoginView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public QuickLoginView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public QuickLoginView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	
	private void initView(Context context){
		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.quick_login_layout, this);
		
		titleRel = (RelativeLayout) view.findViewById(R.id.titleRel);
		leftLine = (ImageView) view.findViewById(R.id.leftLine);
		rightLine = (ImageView) view.findViewById(R.id.rightLine);
		title = (TextView) view.findViewById(R.id.title);
		iconRel = (RelativeLayout) view.findViewById(R.id.iconRel);
		qqLogin = (Button) view.findViewById(R.id.qqLogin);
		weixinLogin = (Button) view.findViewById(R.id.weixinLogin);
		sinaLogin = (Button) view.findViewById(R.id.sinaLogin);
		
		initViewSize(view);
	}
	
	public void setQqClickListener(OnClickListener listener){
		qqLogin.setOnClickListener(listener);
	}
	
	public void setWeiXinClickListener(OnClickListener listener){
		weixinLogin.setOnClickListener(listener);
	}
	
	public void setSinaClickListener(OnClickListener listener){
		sinaLogin.setOnClickListener(listener);
	}
	
	private void initViewSize(View view){
		
		UIUtil.setViewSize(leftLine, 425 * MetricsTool.Rx, 3);
		UIUtil.setViewSize(rightLine, 425 * MetricsTool.Rx, 3);
		UIUtil.setViewSize(qqLogin, 160 * MetricsTool.Rx, 160 * MetricsTool.Rx);
		UIUtil.setViewWidth(weixinLogin, 160 * MetricsTool.Rx);
		UIUtil.setViewWidth(sinaLogin, 160 * MetricsTool.Rx);
		
		UIUtil.setTextSize(title, 35);
		
		try {
			UIUtil.setViewSizeMargin(leftLine, 10 * MetricsTool.Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(rightLine, 0, 0, 10 * MetricsTool.Rx, 0);
			UIUtil.setViewSizeMargin(weixinLogin, 165 * MetricsTool.Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(sinaLogin, 165 * MetricsTool.Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(titleRel, 0, 40 * MetricsTool.Ry, 0, 0);
			UIUtil.setViewSizeMargin(iconRel, 0, 180 * MetricsTool.Ry, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
