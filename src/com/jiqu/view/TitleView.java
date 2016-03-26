package com.jiqu.view;

import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TitleView extends RelativeLayout implements OnClickListener{
	private View view;
	private RelativeLayout parentView;
	public ImageButton back;
	public Button editBtn;
	public TextView tip;
	private Activity activity;

	public TitleView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public TitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public TitleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	private void init(Context context){
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.account_title, this);
		back = (ImageButton) view.findViewById(R.id.backBtn);
		tip = (TextView) view.findViewById(R.id.tip);
		editBtn = (Button) view.findViewById(R.id.editBtn);
		parentView = (RelativeLayout) view.findViewById(R.id.parentView);
		
		back.setOnClickListener(this);
		
		setSize();
	}
	
	public void setActivity(Activity activity){
		this.activity = activity;
	}
	
	public void setSize(){
		UIUtil.setViewSize(back, 40 * MetricsTool.Rx, 40 * MetricsTool.Rx);
		UIUtil.setViewSize(editBtn, 64 * MetricsTool.Rx, 64 * MetricsTool.Rx);
		UIUtil.setTextSize(tip, 45);
		UIUtil.setViewSize(parentView, MetricsTool.width, 165 * MetricsTool.Ry);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backBtn) {
			if (activity != null) {
				activity.finish();
			}
		}
	}

}