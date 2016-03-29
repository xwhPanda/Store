package com.jiqu.view;

import java.util.List;

import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class SpinnerView extends RelativeLayout implements OnClickListener{
	private RelativeLayout parent;
	private TextView value;
	private Button spinnerBtn;
	private SpinnerPopView spinnerPopView;

	public SpinnerView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public SpinnerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public SpinnerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context){
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.spinner_layout, this);
		
		parent = (RelativeLayout) view.findViewById(R.id.parent);
		value = (TextView) view.findViewById(R.id.value);
		spinnerBtn = (Button) view.findViewById(R.id.spinnerBtn);
		
		spinnerBtn.setOnClickListener(this);
		
		initViewSize();
		
		spinnerPopView = new SpinnerPopView(context, true);
	}
	
	public void setPopSize(boolean isGender){
		spinnerPopView.initViewSize(isGender);
	}
	
	public void setData(List<String> strings){
		spinnerPopView.setData(strings);
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(parent, 110 * MetricsTool.Rx, 50 * MetricsTool.Ry);
		UIUtil.setViewSize(spinnerBtn, 20 * MetricsTool.Rx, 20 * MetricsTool.Rx);
	}
	
	public String getValue(){
		return value.getText().toString();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.spinnerBtn) {
			spinnerPopView.showAsDropDown(value, 0, 0);
		}
	}
}
