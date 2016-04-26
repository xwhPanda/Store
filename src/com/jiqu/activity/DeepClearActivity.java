package com.jiqu.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.R.integer;
import android.R.raw;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.ClearTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.TitleView;

public class DeepClearActivity extends BaseActivity implements OnClickListener {
	private static final int START_CLEAR_RUBISH = 0;
	private static final int CLEAR_RUBISH_COMPLETED = 1;
	private static final int START_SCAN = 2;
	private static final int SCAN_COMPELTED = 3;
	private static final int START_SCAN_PROGRESS = 4;
	public static final int SCAN_PROCESS_COMPLETED = 5;
	private static final int START_SCAN_RUBISH = 6;
	private static final int SCAN_RUBISH_COMPLETE = 7;
	public static final int KILL_PROCESS_COMPLETED = 8;
	private static final int START_KILL_PROCESS = 9;
	private static final int REFRESH_SCORE = 10;
	private TitleView titleView;
	private RelativeLayout scanRel;
	private ImageView scanBg;
	private RelativeLayout scanTipRel;
	private TextView scanScore, scanTip;
	private RelativeLayout closeProcessRel, clearRubishRel, autoStartRel;
	private ImageView closeProcessImg, clearRubishImg, autoStartImg;
	private TextView closeProcessTitle, closeProcessCount, clearRubishTitle, clearRubishCount, autoStartTitle, autoStartCount;
	private Button speedUp;
	private CheckBox closeProcessBox,clearRubishBox;
	private SQLiteDatabase db;
	private boolean isCleared = false;
	private Animation animation_1,animation_2;
	private long beforeMemory = 0;
	private List<File> files = new ArrayList<File>();
	private boolean runing = true;
	private int score = 100;
	private int memoryPre = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initView();

		initClearDB();
	}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.deep_clear_layout;
	}

	private void initClearDB() {
		File file = new File("/data/data/com.jiqu.store/files/clearpath.db");
		if (!file.exists()) {
			copyfile();
		}
	}

	private void copyfile() {
		try {
			InputStream is = getAssets().open("clearpath.db");
			OutputStream fos = this.openFileOutput("clearpath.db", MODE_PRIVATE);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initView() {
		memoryPre = ClearTool.getInstance().getScore(DeepClearActivity.this);
		beforeMemory = ClearTool.getInstance().getAvailMemory(this);
		animation_1 = AnimationUtils.loadAnimation(DeepClearActivity.this, R.anim.clear_img_anim);
		animation_2 = AnimationUtils.loadAnimation(DeepClearActivity.this, R.anim.clear_small_img_anim);
		titleView = (TitleView) findViewById(R.id.titleView);
		scanRel = (RelativeLayout) findViewById(R.id.scanRel);
		scanBg = (ImageView) findViewById(R.id.scanBg);
		scanTipRel = (RelativeLayout) findViewById(R.id.scanTipRel);
		scanScore = (TextView) findViewById(R.id.scanScore);
		scanTip = (TextView) findViewById(R.id.scanTip);
		closeProcessRel = (RelativeLayout) findViewById(R.id.closeProcessRel);
		clearRubishRel = (RelativeLayout) findViewById(R.id.clearRubishRel);
		autoStartRel = (RelativeLayout) findViewById(R.id.autoStartRel);
		closeProcessImg = (ImageView) findViewById(R.id.closeProcessImg);
		clearRubishImg = (ImageView) findViewById(R.id.clearRubishImg);
		autoStartImg = (ImageView) findViewById(R.id.autoStartImg);
		closeProcessTitle = (TextView) findViewById(R.id.closeProcessTitle);
		closeProcessCount = (TextView) findViewById(R.id.closeProcessCount);
		clearRubishTitle = (TextView) findViewById(R.id.clearRubishTitle);
		clearRubishCount = (TextView) findViewById(R.id.clearRubishCount);
		autoStartTitle = (TextView) findViewById(R.id.autoStartTitle);
		autoStartCount = (TextView) findViewById(R.id.autoStartCount);
		speedUp = (Button) findViewById(R.id.speedUp);
		closeProcessBox = (CheckBox) findViewById(R.id.closeProcessBox);
		clearRubishBox = (CheckBox) findViewById(R.id.clearRubishBox);

		titleView.setActivity(this);
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.tip.setText(R.string.deepClear);

		speedUp.setOnClickListener(this);

		initViewSize();
		
		handler.sendEmptyMessage(START_SCAN);
		
	}

	private void initViewSize() {
		UIUtil.setTextSize(scanTip, 40);
		UIUtil.setTextSize(scanScore, 100);
		UIUtil.setTextSize(closeProcessTitle, 35);
		UIUtil.setTextSize(closeProcessCount, 35);
		UIUtil.setTextSize(clearRubishTitle, 35);
		UIUtil.setTextSize(clearRubishCount, 35);
		UIUtil.setTextSize(autoStartTitle, 35);
		UIUtil.setTextSize(autoStartCount, 35);

		UIUtil.setViewSize(closeProcessRel, 1040 * Rx, 150 * Ry);
		UIUtil.setViewSize(clearRubishRel, 1040 * Rx, 150 * Ry);
		UIUtil.setViewSize(autoStartRel, 1040 * Rx, 150 * Ry);
		UIUtil.setViewSize(scanBg, 345 * Rx, 345 * Rx);
		UIUtil.setViewSize(scanRel, 345 * Rx, 345 * Rx);
		UIUtil.setViewSize(closeProcessImg, 56 * Rx, 56 * Rx);
		UIUtil.setViewSize(clearRubishImg, 56 * Rx, 56 * Rx);
		UIUtil.setViewSize(closeProcessBox, 56 * Rx, 56 * Rx);
		UIUtil.setViewSize(clearRubishBox, 56 * Rx, 56 * Rx);
		UIUtil.setViewSize(autoStartImg, 56 * Rx, 56 * Rx);
		UIUtil.setViewHeight(speedUp, 110 * Ry);

		try {
			UIUtil.setViewSizeMargin(closeProcessBox, 25 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(clearRubishBox, 25 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(closeProcessImg, 25 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(clearRubishImg, 25 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(autoStartImg, 25 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(clearRubishTitle, 25 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(closeProcessTitle, 25 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(autoStartTitle, 25 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(closeProcessCount, 0, 0, 25 * Rx, 0);
			UIUtil.setViewSizeMargin(clearRubishCount, 0, 0, 25 * Rx, 0);
			UIUtil.setViewSizeMargin(autoStartCount, 0, 0, 25 * Rx, 0);
			UIUtil.setViewSizeMargin(speedUp, 0, 450 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(scanRel, 0, 290 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(closeProcessRel, 0, 85 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(clearRubishRel, 0, 35 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(autoStartRel, 0, 35 * Ry, 0, 0);
		} catch (Exception e) {
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.speedUp) {
			if (isCleared) {
				finish();
			}else {
				if (!closeProcessBox.isChecked() && !clearRubishBox.isChecked()) {
					Toast.makeText(this, "请选择需要清理的内容", Toast.LENGTH_SHORT).show();
				}else {
					handler.sendEmptyMessage(START_KILL_PROCESS);
				}
			}
		}
	}
	
	
	/**
	 * 开始清理缓存文件
	 * 
	 * @param v
	 */
	public void start() {
		db = SQLiteDatabase.openDatabase("/data/data/com.jiqu.store/files/clearpath.db", null, SQLiteDatabase.OPEN_READONLY);
		// 获取所有的安装包信息
		new Thread() {

			@Override
			public void run() {
				List<PackageInfo> packinfos = getPackageManager().getInstalledPackages(0);
				long size = 0;
				for (PackageInfo info : packinfos) {
					String packname = info.packageName;
					Cursor cursor = db.rawQuery("select filepath from softdetail where apkname=?", new String[] { packname });
					while (cursor.moveToNext()) {
						String path = cursor.getString(0);
						File file = new File(Environment.getExternalStorageDirectory(), path);
						try {
							size += getFolderSize(file);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						files.add(file);
					}
					cursor.close();
				}
				Message msg = Message.obtain();
				msg.what = SCAN_RUBISH_COMPLETE;
				msg.obj = ClearTool.getDataSize(size);
				handler.sendMessageDelayed(msg, 3 * 1000);
				db.close();
			}

		}.start();
	}
	
	public long getFolderSize(java.io.File file) throws Exception {
		long size = 0;
		java.io.File[] fileList = file.listFiles();
		if (fileList != null && fileList.length > 0) {
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		}
		return size;
	}
	
	private void deleteFolder(File file) throws Exception{
		java.io.File[] fileList = file.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isDirectory()) {
				deleteFolder(fileList[i]);
			} else {
				fileList[i].delete();
			}
		}
	}
	
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case START_KILL_PROCESS://开始清理
				scanBg.setBackgroundResource(R.drawable.saomiao);
				scanBg.startAnimation(animation_1);
				scanScore.setTextColor(getResources().getColor(R.color.green));
				scanScore.setTextColor(getResources().getColor(R.color.green));
				speedUp.setEnabled(false);
				scanTip.setText(R.string.deepClearing);
				speedUp.setBackgroundResource(R.drawable.tuichuzhanghao);
				speedUp.setTextColor(getResources().getColor(R.color.itemDesColor));
				if (closeProcessBox.isChecked()) {
					ClearTool.getInstance().killProcess(DeepClearActivity.this,handler);
				}else {
					sendEmptyMessage(START_CLEAR_RUBISH);
				}
				break;
				
			case START_CLEAR_RUBISH:
				if (clearRubishBox.isChecked()) {
					if (files.size() > 0) {
						for (File file : files) {
							try {
								deleteFolder(file);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						sendEmptyMessage(CLEAR_RUBISH_COMPLETED);
					}else {
						sendEmptyMessage(CLEAR_RUBISH_COMPLETED);
					}
				}else {
					sendEmptyMessage(CLEAR_RUBISH_COMPLETED);
				}
				break;

			case CLEAR_RUBISH_COMPLETED://清理完毕
				isCleared = true;
				speedUp.setEnabled(true);
				scanScore.setText("100");
				stopAnimation();
				scanTip.setText(R.string.deepCleared);
				speedUp.setText(R.string.clearCacheed);
				speedUp.setBackgroundResource(R.drawable.qingjie_btn);
				speedUp.setTextColor(getResources().getColor(R.color.white));
				
				closeProcessCount.setText("清理内存" + ClearTool.getDataSize(ClearTool.getInstance().getAvailMemory(DeepClearActivity.this) - beforeMemory));
				break;
				
			case KILL_PROCESS_COMPLETED:
				sendEmptyMessage(START_CLEAR_RUBISH);
				break;
			
			case START_SCAN://开始扫描
				new RandThread().start();
				sendEmptyMessage(START_SCAN_PROGRESS);
				speedUp.setEnabled(false);
				break;
				
			case SCAN_COMPELTED://扫描完毕
				runing = false;
				if (memoryPre < 60 ) {
					scanScore.setText(memoryPre + 10 + "");
				}else {
					scanScore.setText(memoryPre + "");
				}
				closeProcessBox.setVisibility(View.VISIBLE);
				clearRubishBox.setVisibility(View.VISIBLE);
				closeProcessImg.setVisibility(View.INVISIBLE);
				clearRubishImg.setVisibility(View.INVISIBLE);
				scanBg.clearAnimation();
				clearRubishImg.clearAnimation();
				speedUp.setEnabled(true);
				
				break;
			
			case SCAN_PROCESS_COMPLETED://扫描后台进程完毕
				sendEmptyMessage(START_SCAN_RUBISH);
				closeProcessImg.clearAnimation();
				closeProcessCount.setText(msg.arg1 +"个可清理进程");
				break;
			
			case START_SCAN_PROGRESS://开始扫描后台进程
				scanBg.startAnimation(animation_1);
				closeProcessImg.setBackgroundResource(R.drawable.shuaxin_sel);
				closeProcessImg.startAnimation(animation_2);
				ClearTool.getInstance().getProcess(DeepClearActivity.this, handler);
				break;
			
			case START_SCAN_RUBISH://开始扫描垃圾
				start();
				clearRubishImg.setBackgroundResource(R.drawable.shuaxin_sel);
				clearRubishImg.startAnimation(animation_2);
				break;
				
			case SCAN_RUBISH_COMPLETE://扫描垃圾完毕
				sendEmptyMessage(SCAN_COMPELTED);
				if (msg.obj.equals("0B") || msg.obj.equals("0KB")) {
					clearRubishCount.setText("没有垃圾");
				}else {
					clearRubishCount.setText(msg.obj + "垃圾");
				}
				break;
			case REFRESH_SCORE:
				int num = msg.arg1;
				if (memoryPre + 10 <= score) {
					score = score - num;
				}
				scanScore.setText(score + "");
				break;
			}
		};
	};
	
	private void stopAnimation(){
		scanBg.clearAnimation();
		closeProcessImg.clearAnimation();
		clearRubishImg.clearAnimation();
		autoStartImg.clearAnimation();
	}
	protected void onDestroy() {
		super.onDestroy();
		if (db != null) {
			db.close();
		}
	};
	
	private class RandThread extends Thread{
		@Override
		public void run() {
			while (runing) {
				try {
					int num = (int)((Math.random()) * 8);
					Message msg = handler.obtainMessage();
					msg.what = REFRESH_SCORE;
					msg.arg1 = num;
					handler.sendMessage(msg);
					sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
