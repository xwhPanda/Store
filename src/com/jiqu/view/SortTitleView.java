package com.jiqu.view;

import com.jiqu.store.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class SortTitleView extends RelativeLayout implements OnClickListener{
	private ImageButton sortBackBtn;
	private TextView sortContent;
	private ImageButton sortShareBtn;
	private Activity activity;

	public SortTitleView(Context context){
		super(context);
		init(context);
	}
	
	public SortTitleView(Context context,AttributeSet attributeSet){
		super(context, attributeSet);
		init(context);
	}
	
	public SortTitleView(Context context,AttributeSet attributeSet , int style){
		super(context, attributeSet, style);
		init(context);
	}
	
	private void init(Context context){
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.sort_title, null);
		sortBackBtn = (ImageButton) view.findViewById(R.id.sortBackBtn);
		sortContent = (TextView) view.findViewById(R.id.sortContent);
		sortShareBtn = (ImageButton) view.findViewById(R.id.sortShareBtn);
		
		setListener();
	}
	
	private void setListener(){
		sortBackBtn.setOnClickListener(this);
		sortShareBtn.setOnClickListener(this);
	}
	
	public ImageButton getBackBtn(){
		return sortBackBtn;
	}
	
	public TextView getSortContent(){
		return sortContent;
	}
	
	public ImageButton getShareBtn(){
		return sortShareBtn;
	}
	
	//将此布局所在的Activity传进来，点击back按钮的时候关闭
	public void setActivity(Activity activity){
		this.activity = activity;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sortBackBtn:
			if (activity != null) {
				activity.finish();
			}
			break;

		case R.id.sortShareBtn:
			//分享待实现
			break;
		}
	}
}
