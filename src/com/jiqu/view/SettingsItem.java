package com.jiqu.view;

import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SettingsItem extends RelativeLayout {
	private TextView title;
	private LinearLayout changeThreadPoolSizeLin;
	private Button subtract,add;
	private TextView size;
	private Button next;
	private ToggleButton toggle;
	private TextView versionName;

	public SettingsItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public SettingsItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
		initViewVisible(attrs);
	}

	public SettingsItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	
	private void initView(Context context){
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.setting_item_layout, this);
		title = (TextView) view.findViewById(R.id.title);
		changeThreadPoolSizeLin = (LinearLayout) view.findViewById(R.id.changeThreadPoolSizeLin);
		subtract = (Button) view.findViewById(R.id.subtract);
		add = (Button) view.findViewById(R.id.add);
		size = (TextView) view.findViewById(R.id.size);
		next = (Button) view.findViewById(R.id.next);
		toggle = (ToggleButton) view.findViewById(R.id.toggle);
		versionName = (TextView) view.findViewById(R.id.versionName);
		
		initViewSize();
	}
	
	@SuppressLint("Recycle")
	private void initViewVisible(AttributeSet attrs){
		TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.settingViewDefineAttr);
		boolean isThreadPool = ta.getBoolean(R.styleable.settingViewDefineAttr_isThreadPool, false);
		boolean isAutoCheckUpdate = ta.getBoolean(R.styleable.settingViewDefineAttr_isAutoCheckUpdate, false);
		boolean isCheckUpdate = ta.getBoolean(R.styleable.settingViewDefineAttr_isCheckUpdate, false);
		boolean isNext = ta.getBoolean(R.styleable.settingViewDefineAttr_isNext, false);
		
		if (isNext) {
			setViewsVisible(next);
		}else if (isThreadPool) {
			setViewsVisible(changeThreadPoolSizeLin);
		}else if (isAutoCheckUpdate) {
			setViewsVisible(toggle);
		}else if (isCheckUpdate) {
			setViewsVisible(versionName);
		}
	}
	
	private void setViewsVisible(View visibleView){
		if (changeThreadPoolSizeLin != visibleView) {
			changeThreadPoolSizeLin.setVisibility(View.GONE);
		}
		if (next != visibleView) {
			next.setVisibility(View.GONE);
		}
		if (toggle != visibleView) {
			toggle.setVisibility(View.GONE);
		}
		if (versionName != visibleView) {
			versionName.setVisibility(View.GONE);
		}
		visibleView.setVisibility(View.VISIBLE);
	}
	
	private void initViewSize(){
		UIUtil.setTextSize(title, 35);
		UIUtil.setTextSize(size, 25);
		UIUtil.setTextSize(versionName, 30);
		
		UIUtil.setViewSize(subtract, 80 * MetricsTool.Rx, 55 * MetricsTool.Ry);
		UIUtil.setViewSize(add, 80 * MetricsTool.Rx, 55 * MetricsTool.Ry);
		UIUtil.setViewSize(size, 90 * MetricsTool.Rx, 55 * MetricsTool.Ry);
		UIUtil.setViewSize(next, 36 * MetricsTool.Rx, 36 * MetricsTool.Ry);
		UIUtil.setViewSize(toggle, 110 * MetricsTool.Rx, 45 * MetricsTool.Ry);
		
		try {
			UIUtil.setViewSizeMargin(title, 35 * MetricsTool.Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(changeThreadPoolSizeLin, 0, 0, 35 * MetricsTool.Rx, 0);
			UIUtil.setViewSizeMargin(next, 0, 0, 35 * MetricsTool.Rx, 0);
			UIUtil.setViewSizeMargin(toggle, 0, 0, 35 * MetricsTool.Rx, 0);
			UIUtil.setViewSizeMargin(versionName, 0, 0, 35 * MetricsTool.Rx, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public TextView getVersionNameTextView(){
		return versionName;
	}
	
	public TextView getTitleTextView(){
		return title;
	}
	
	public ToggleButton getToggleButton(){
		return toggle;
	}
	
	public TextView getThreadSizeTextView(){
		return size;
	}
	
	public Button getSubtractButton(){
		return subtract;
	}
	
	public Button getAddButton(){
		return add;
	}
}
