package com.jiqu.view;

import com.jiqu.application.StoreApplication;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;
import com.vr.store.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NetChangeDialog extends Dialog {
	private Context context;
	private TextView tip;
	private RelativeLayout parent;
	private RelativeLayout btnRel;
	private Button cancle;
	private Button confirm;

	public NetChangeDialog(Context context) {
		super(context,R.style.NetChangeDialog);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView(context);
	}
	
	public NetChangeDialog(Context context,int theme){
		super(context, theme);
		this.context = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	private void initView(Context context){
		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(StoreApplication.context);
		View view = inflater.inflate(R.layout.net_change_dialog_layout, null);
		setContentView(view);
		
		tip = (TextView) view.findViewById(R.id.tip);
		parent = (RelativeLayout) view.findViewById(R.id.parent);
		btnRel = (RelativeLayout) view.findViewById(R.id.btnRel);
		cancle = (Button) view.findViewById(R.id.cancle);
		confirm = (Button) view.findViewById(R.id.confirm);
		
		initViewSize();
		
		setCancelable(false);
	}
	
	private void initViewSize() {
		UIUtil.setViewSize(parent, 690 * MetricsTool.Rx, 495 * MetricsTool.Ry);
		UIUtil.setViewSize(cancle, 270 * MetricsTool.Rx, 100 * MetricsTool.Ry);
		UIUtil.setViewWidth(confirm, 270 * MetricsTool.Rx);

		UIUtil.setTextSize(tip, 35);
		UIUtil.setTextSize(cancle, 35);
		UIUtil.setTextSize(confirm, 35);

		try {
			UIUtil.setViewSizeMargin(confirm, 45 * MetricsTool.Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(tip, 0, 150 * MetricsTool.Rx, 0, 0);
			UIUtil.setViewSizeMargin(btnRel, 0, 0, 0, 50 * MetricsTool.Rx);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setContent(String content){
		tip.setText(content);
	}
	
	public void setNegativeText(String negativeText){
		cancle.setText(negativeText);
	}
	
	public void setPositiveText(String positiveText){
		confirm.setText(positiveText);
	}
	
	public void setPositiveListener(android.view.View.OnClickListener onClickListener){
		confirm.setOnClickListener(onClickListener);
	}
	
	public void setNegativeListener(android.view.View.OnClickListener onClickListener){
		cancle.setOnClickListener(onClickListener);
	}
}
