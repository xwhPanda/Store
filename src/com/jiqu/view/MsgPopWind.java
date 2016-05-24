package com.jiqu.view;

import com.vr.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;

public class MsgPopWind extends PopupWindow {
	private Button delete,shield,blacklist;


	public MsgPopWind(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context){
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.msg_pop_layout, null);
		delete = (Button) view.findViewById(R.id.delete);
		shield = (Button) view.findViewById(R.id.shield);
		blacklist = (Button) view.findViewById(R.id.blacklist);
		this.setWidth((int)(210 * MetricsTool.Rx));
		this.setHeight((int)(220 * MetricsTool.Ry));
		this.setOutsideTouchable(true);
		this.setFocusable(true);
		this.setClippingEnabled(true);
		this.setBackgroundDrawable(new BitmapDrawable());
		
		initViewSize();
		
		setContentView(view);
	}
	
	public void setDeleteListener(OnClickListener listener){
		delete.setOnClickListener(listener);
	}
	
	public void setShieldListener(OnClickListener listener){
		shield.setOnClickListener(listener);
	}
	
	private void initViewSize(){
		UIUtil.setTextSize(delete, 30);
		UIUtil.setTextSize(shield, 30);
		UIUtil.setTextSize(blacklist, 30);
		
		try {
			UIUtil.setViewSizeMargin(shield, 0, 30 * MetricsTool.Ry, 0, 0);
			UIUtil.setViewSizeMargin(blacklist, 0, 10 * MetricsTool.Ry, 0, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
