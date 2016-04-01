package com.jiqu.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.List;

import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.TitleView;

public class DeepClearActivity extends BaseActivity implements OnClickListener {
	private TitleView titleView;
	private RelativeLayout scanRel;
	private ImageView scanBg;
	private RelativeLayout scanTipRel;
	private TextView scanScore, scanTip;
	private RelativeLayout closeProcessRel, clearRubishRel, autoStartRel;
	private ImageView closeProcessImg, clearRubishImg, autoStartImg;
	private TextView closeProcessTitle, closeProcessCount, clearRubishTitle, clearRubishCount, autoStartTitle, autoStartCount;
	private Button speedUp;
	private SQLiteDatabase db;

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

		titleView.setActivity(this);
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.tip.setText(R.string.deepClear);

		speedUp.setOnClickListener(this);

		initViewSize();
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
		UIUtil.setViewSize(autoStartImg, 56 * Rx, 56 * Rx);
		UIUtil.setViewHeight(speedUp, 110 * Ry);

		try {
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
			start();
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
				int total = 0;
				for (PackageInfo info : packinfos) {
					String packname = info.packageName;
					Cursor cursor = db.rawQuery("select filepath from softdetail where apkname=?", new String[] { packname });
					while (cursor.moveToNext()) {
						String path = cursor.getString(0);
						File file = new File(Environment.getExternalStorageDirectory(), path);

						try {
							Log.i("TAG", "path : " + path + " +size : " + getFolderSize(file));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					total++;
					cursor.close();
					Message msg = Message.obtain();
					msg.obj = "清除  " + packname;
				}
				Message msg = Message.obtain();
				msg.obj = "恭喜没有垃圾文件";
				db.close();
			}

		}.start();
	}

	public static long getFolderSize(java.io.File file) throws Exception {
		long size = 0;
		java.io.File[] fileList = file.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isDirectory()) {
				size = size + getFolderSize(fileList[i]);
			} else {
				size = size + fileList[i].length();
			}
		}
		return size;
	}
}
