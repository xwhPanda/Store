package com.jiqu.activity;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jiqu.adapter.BatteryAdapter;
import com.jiqu.object.BatteryInfo;
import com.jiqu.object.BatterySipper;
import com.jiqu.store.BaseActivity;
import com.vr.store.R;
import com.jiqu.tools.Constants;
import com.jiqu.tools.SharePreferenceTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.tools.Utils;
import com.jiqu.view.TitleView;
import com.jiqu.view.WaveHelper;
import com.jiqu.view.WaveView;

public class PowerManagerActivity extends BaseActivity implements OnCheckedChangeListener{
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
	private WaveHelper waveHelper;
	
	private BluetoothAdapter bluetoothAdapter;
	private SharedPreferences preferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_BATTERY_CHANGED);
		filter.addAction(Intent.ACTION_BATTERY_LOW);
		filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
		registerReceiver(receiver, filter);
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
		
		initViewSize();
		
		waveHelper = new WaveHelper(waveView);
		waveView.setBorder((int)(8 * Rx), getResources().getColor(R.color.blue));
		waveView.setTextSize((int)(60 * Rx));
		
		batteryInfo = new BatteryInfo(this);
		sippers = new ArrayList<BatterySipper>();
		adapter = new BatteryAdapter(this, sippers);
		appUseBatteryListView.setAdapter(adapter); 
		
		new GetProcessTask().execute();
		initData();
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
		
		showPreBatteryTog.setOnCheckedChangeListener(this);
		saveBatteryTog.setOnCheckedChangeListener(this);
	}
	
	private void initData(){
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		preferences = getSharedPreferences(Constants.BATTERY_SHARE_PREFERENCE_NAME, MODE_PRIVATE);
		showPreBatteryTog.setChecked(SharePreferenceTool.getBooleanFromPreferences(preferences, Constants.DISPLAY_BATTERY_PERCENT, true));
		saveBatteryTog.setChecked(SharePreferenceTool.getBooleanFromPreferences(preferences, Constants.POWER_SAVING_MODE, false));
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(waveView, 345 * Rx, 345 * Rx);
		UIUtil.setViewHeight(saveBatteryRel, 115 * Ry);
		UIUtil.setViewHeight(showPreBatteryRel, 115 * Ry);
		UIUtil.setViewSize(showPreBatteryTog, 110 * Rx, 50 * Ry);
		UIUtil.setViewSize(saveBatteryTog, 110 * Rx, 50 * Ry);
		
		UIUtil.setTextSize(batteryUse, 36);
		UIUtil.setTextSize(saveBatteryTx, 40);
		UIUtil.setTextSize(showPreBatteryTx, 40);
		
		try {
			UIUtil.setViewSizeMargin(saveBatteryTx, 50 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(showPreBatteryTx, 50 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(saveBatteryTog, 0, 0, 50 * Rx, 0);
			UIUtil.setViewSizeMargin(showPreBatteryTog, 0, 0, 50 * Rx, 0);
			UIUtil.setViewSizeMargin(appUseBatteryListView, 0, 165 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(waveView, 0, 80 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(batteryUse, 0, 30 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(view1, 0, 85 * Ry, 0, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class GetProcessTask extends AsyncTask<Void, integer, List<BatterySipper>>{


		@Override
		protected void onPostExecute(List<BatterySipper> result) {
			// TODO Auto-generated method stub
			sippers.clear();
			sippers.addAll(result);
			adapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}

		@Override
		protected List<BatterySipper> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return batteryInfo.getTaskInfos();
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		waveHelper.cancel();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		waveHelper.start();
	}
	
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	};
	
	BroadcastReceiver receiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
				int percent = Utils.getBatteryPercentage(intent);
				int status=intent.getIntExtra("status",BatteryManager.BATTERY_STATUS_UNKNOWN);
				waveView.setText(String.valueOf(percent) + "%");
				waveView.setWaterLevelRatio(percent / 100.0f);
				if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
					batteryUse.setText("正在充电");
				}else if (status == BatteryManager.BATTERY_STATUS_FULL) {
					batteryUse.setText("已充满");
				}
			}else if (action.equals(Intent.ACTION_BATTERY_LOW)) {
				batteryUse.setText("电量过低,请及时充电");
			}else if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
				batteryUse.setText("");
			}
		}
		
	};

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (buttonView == showPreBatteryTog) {
			waveView.setTextVisible(isChecked);
			SharePreferenceTool.setValuePreferences(preferences, Constants.DISPLAY_BATTERY_PERCENT, isChecked);
		}else if (buttonView == saveBatteryTog) {
			SharePreferenceTool.setValuePreferences(preferences, Constants.POWER_SAVING_MODE, isChecked);
			bluetoothAdapter.disable();
			if (isChecked) {
				try {
					int screenMode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
					int screenBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
					if (screenBrightness > 50) {
						if (screenMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
							setScreenMode(Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
						}
						setScreenBrightness(50.0F);
					}

				} catch (SettingNotFoundException e) {
					e.printStackTrace();
				}
			}else {
				
			}
		}
	}
	
	private void setScreenMode(int value) {
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, value);
    }
	
	private void setScreenBrightness(float value) {
        Window mWindow = getWindow();
        WindowManager.LayoutParams mParams = mWindow.getAttributes();
        float f = value / 255.0F;
        mParams.screenBrightness = f;
        mWindow.setAttributes(mParams);

        // 保存设置的屏幕亮度值
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, (int) value);
    }
}
