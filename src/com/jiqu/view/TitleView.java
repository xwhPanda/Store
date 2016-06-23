package com.jiqu.view;

import com.vr.store.R;
import com.jiqu.application.StoreApplication;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TitleView extends RelativeLayout implements OnClickListener{
	private View view;
	private Context context;
	public RelativeLayout parentView;
	public LinearLayout backLin;
	public ImageView back;
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
		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(StoreApplication.context);
		view = inflater.inflate(R.layout.account_title, this);
		back = (ImageView) view.findViewById(R.id.backBtn);
		backLin = (LinearLayout) view.findViewById(R.id.backLin);
		tip = (TextView) view.findViewById(R.id.tip);
		editBtn = (Button) view.findViewById(R.id.editBtn);
		parentView = (RelativeLayout) view.findViewById(R.id.parentView);
		
		backLin.setOnClickListener(this);
		
		setSize();
	}
	
	public void setActivity(Activity activity){
		this.activity = activity;
	}
	
	public void setShareListener(OnClickListener listener){
		editBtn.setOnClickListener(listener);
	}
	
	public void setBackListener(OnClickListener onClickListener){
		backLin.setOnClickListener(onClickListener);
	}
	
	public void setSize(){
		UIUtil.setViewSize(back, 40 * MetricsTool.Rx, 40 * MetricsTool.Rx);
		UIUtil.setViewSize(backLin, 70 * MetricsTool.Rx, 70 * MetricsTool.Rx);
		UIUtil.setViewSize(editBtn, 64 * MetricsTool.Rx, 64 * MetricsTool.Rx);
		UIUtil.setTextSize(tip, 45);
		UIUtil.setViewSize(parentView, MetricsTool.width, 165 * MetricsTool.Ry);
		
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (165 * MetricsTool.Ry));
		view.setLayoutParams(lp);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backLin) {
			if (activity != null) {
				activity.finish();
			}
		}
	}
}