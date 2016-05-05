package com.jiqu.view;

import com.jiqu.interfaces.DialogDismissObserver;
import com.vr.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import android.R.dimen;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class InformationGenderDialog extends Dialog implements OnClickListener{
	private Button cancle;
	private Button save;
	private ImageView line;
	private Button man;
	private Button female;
	private RelativeLayout btnRel;
	private RelativeLayout parent;
	private DialogDismissObserver observer;
	private String value;
	private Context context;

	public InformationGenderDialog(Context context) {
		super(context,R.style.GenderDialog);
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gender_dialog_layout);
		
		initView();
		
		setCancelable(false);
	}
	
	private void initView(){
		cancle = (Button) findViewById(R.id.cancle);
		save = (Button) findViewById(R.id.save);
		line = (ImageView) findViewById(R.id.line);
		man = (Button) findViewById(R.id.man);
		female = (Button) findViewById(R.id.female);
		btnRel = (RelativeLayout) findViewById(R.id.btnRel);
		parent = (RelativeLayout) findViewById(R.id.parent);
		
		cancle.setOnClickListener(this);
		save.setOnClickListener(this);
		man.setOnClickListener(this);
		female.setOnClickListener(this);
		
		initViewSize();
	}
	
	public void setObserver(DialogDismissObserver observer){
		this.observer = observer;
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(cancle, 40 * MetricsTool.Rx, 40 * MetricsTool.Rx);
		UIUtil.setViewSize(save, 40 * MetricsTool.Rx, 40 * MetricsTool.Rx);
		UIUtil.setViewWidth(parent, MetricsTool.width);
		UIUtil.setViewHeight(btnRel, 60 * MetricsTool.Rx);
		UIUtil.setViewHeight(man, 120 * MetricsTool.Ry);
		UIUtil.setViewHeight(female, 120 * MetricsTool.Ry);
		
		UIUtil.setTextSize(man, 50);
		UIUtil.setTextSize(female, 50);
		
		try {
			UIUtil.setViewSizeMargin(cancle,20 * MetricsTool.Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(save,0, 0, 20 * MetricsTool.Rx, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cancle:
			dismiss();
			observer.onDialogStateChange(0);
			break;

		case R.id.save:
			dismiss();
			observer.onDialogSave(0,value);
			break;
			
		case R.id.man:
			value = man.getText().toString();
			chageClickState(man);
			break;
			
		case R.id.female:
			value = female.getText().toString();
			chageClickState(female);
			break;
		}
	}
	
	private void chageClickState(Button button){
		if (man == button) {
			man.setTextColor(context.getResources().getColor(R.color.yellow));
			man.setBackgroundDrawable(null);
			female.setTextColor(context.getResources().getColor(R.color.white));
			female.setBackgroundResource(R.drawable.ziliao);
		}else if (female == button) {
			female.setTextColor(context.getResources().getColor(R.color.yellow));
			female.setBackgroundDrawable(null);
			man.setTextColor(context.getResources().getColor(R.color.white));
			man.setBackgroundResource(R.drawable.ziliao);
		}
	}

}
