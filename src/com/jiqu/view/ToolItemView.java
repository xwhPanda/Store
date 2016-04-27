package com.jiqu.view;

import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ToolItemView extends LinearLayout {
	private LayoutInflater inflater;
	private View view;
	private ImageView img;
	private TextView title;

	public ToolItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	public ToolItemView(Context context,AttributeSet attributeSet){
		super(context, attributeSet);
		init(context);
	}
	
	private void init(Context context){
		inflater = LayoutInflater.from(context);
		//
		view = inflater.inflate(R.layout.tool_item, this);
		img = (ImageView) view.findViewById(R.id.img);
		title = (TextView) view.findViewById(R.id.title);
		
		initViewSize();
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(img, 120 * MetricsTool.Rx, 120 * MetricsTool.Ry);
		UIUtil.setTextSize(title, 35);
	}

	public void setImgBgRes(int res){
		img.setBackgroundResource(res);
	}
	
	public void setItemBgRes(int res){
		view.setBackgroundResource(res);
	}
	
	public void setTitleText(String text){
		title.setText(text);
	}
	
	public void setTitleText(int text){
		title.setText(text);
	}
	
	public void setTitleColor(int color){
		title.setTextColor(color);
	}
}
