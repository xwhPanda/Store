package com.jiqu.activity;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jiqu.adapter.BatteryAdapter;
import com.jiqu.object.BatteryInfo;
import com.jiqu.object.BatterySipper;
import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.TitleView;
import com.jiqu.view.WaveView;

public class PowerManagerActivity extends BaseActivity {
	private TitleView titleView;
	private ListView appUseBatteryListView;
	
	//headview
	private View headView;
	private WaveView waveView;
	private TextView batteryUse;
	private ImageView view1,view2;
	private RelativeLayout saveBatteryRel,showPreBatteryRel;
	private TextView saveBatteryTx,showPreBatteryTx;
	private ToggleButton saveBatteryTog,showPreBatteryTog;
	
	private BatteryAdapter adapter;
	private List<BatterySipper> sippers;
	private BatteryInfo batteryInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
	}
	
	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.power_manager_layout;
	}
	
	private void initView(){
		initHeadView();
		titleView = (TitleView) findViewById(R.id.titleView);
		appUseBatteryListView = (ListView) findViewById(R.id.appUseBatteryListView);
		
		appUseBatteryListView.addHeaderView(headView);
		
		titleView.setActivity(this);
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.tip.setText(R.string.batteryManager);
		
		waveView.setFillPercent(0.3f);
		waveView.wave();
		
		initViewSize();
		
		batteryInfo = new BatteryInfo(this);
		sippers = batteryInfo.getTaskInfos();
		adapter = new BatteryAdapter(this, sippers);
		appUseBatteryListView.setAdapter(adapter); 
	}
	
	private void initHeadView(){
		LayoutInflater inflater = LayoutInflater.from(this);
		headView = inflater.inflate(R.layout.battery_head_layout, null);
		
		waveView = (WaveView) headView.findViewById(R.id.waveView);
		batteryUse = (TextView) headView.findViewById(R.id.batteryUse);
		view1 = (ImageView) headView.findViewById(R.id.view1);
		view2 = (ImageView) headView.findViewById(R.id.view2);
		saveBatteryRel = (RelativeLayout) headView.findViewById(R.id.saveBatteryRel);
		showPreBatteryRel = (RelativeLayout) headView.findViewById(R.id.showPreBatteryRel);
		saveBatteryTx = (TextView) headView.findViewById(R.id.saveBatteryTx);
		showPreBatteryTx = (TextView) headView.findViewById(R.id.showPreBatteryTx);
		saveBatteryTog = (ToggleButton) headView.findViewById(R.id.saveBatteryTog);
		showPreBatteryTog = (ToggleButton) headView.findViewById(R.id.showPreBatteryTog);
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(waveView, 345 * Rx, 345 * Rx);
		UIUtil.setViewHeight(saveBatteryRel, 115 * Ry);
		UIUtil.setViewHeight(showPreBatteryRel, 115 * Ry);
		
		UIUtil.setTextSize(batteryUse, 36);
		UIUtil.setTextSize(saveBatteryTx, 40);
		UIUtil.setTextSize(showPreBatteryTx, 40);
		
		try {
			UIUtil.setViewSizeMargin(waveView, 0, 240 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(batteryUse, 0, 30 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(view1, 0, 85 * Ry, 0, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
