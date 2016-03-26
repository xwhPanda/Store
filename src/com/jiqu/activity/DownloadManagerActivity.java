package com.jiqu.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.TitleView;

public class DownloadManagerActivity extends BaseActivity implements OnClickListener,OnCheckedChangeListener{
	private TitleView titleView;
	private LinearLayout btnLin;
	private Button downloading,downloaded;
	private RelativeLayout allStartRel;
	private CheckBox allStartCB,allDeleteCB;
	private Button allStartBtn,allDeleteBtn;
	private RelativeLayout allDeleteRel;
	private ListView downloadingList,downloadedList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
	}
	
	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.download_manager_layout;
	}
	
	private void initView(){
		titleView = (TitleView) findViewById(R.id.titleView);
		btnLin = (LinearLayout) findViewById(R.id.btnLin);
		downloading = (Button) findViewById(R.id.downloading);
		downloaded = (Button) findViewById(R.id.downloaded);
		allStartRel = (RelativeLayout) findViewById(R.id.allStartRel);
		allDeleteCB = (CheckBox) findViewById(R.id.allDeleteCB);
		allStartCB = (CheckBox) findViewById(R.id.allStartCB);
		allStartBtn = (Button) findViewById(R.id.allStartBtn);
		allDeleteBtn = (Button) findViewById(R.id.allDeleteBtn);
		allDeleteRel = (RelativeLayout) findViewById(R.id.allDeleteRel);
		downloadingList = (ListView) findViewById(R.id.downloadingList);
		downloadedList = (ListView) findViewById(R.id.downloadedList);
		
		titleView.setActivity(this);
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.tip.setText(R.string.downloadManager);
		
		downloaded.setOnClickListener(this);
		downloading.setOnClickListener(this);
		allDeleteBtn.setOnClickListener(this);
		allStartBtn.setOnClickListener(this);
		allDeleteCB.setOnCheckedChangeListener(this);
		allStartCB.setOnCheckedChangeListener(this);
		
		initViewSize();
	}
	
	private void initViewSize(){
		UIUtil.setViewHeight(allDeleteRel, 170 * Ry);
		UIUtil.setViewHeight(allStartRel, 170 * Ry);
		UIUtil.setViewSize(downloading, 525 * Rx, 85 * Ry);
		UIUtil.setViewSize(downloaded, 525 * Rx, 85 * Ry);
		UIUtil.setViewSize(allDeleteCB, 66 * Rx, 66 * Rx);
		UIUtil.setViewSize(allDeleteBtn, 320 * Rx, 115 * Rx);
		UIUtil.setViewSize(allStartCB, 66 * Rx, 66 * Rx);
		UIUtil.setViewSize(allStartBtn, 320 * Rx, 115 * Rx);
		
		UIUtil.setTextSize(downloaded, 45);
		UIUtil.setTextSize(downloading, 45);
		UIUtil.setTextSize(allDeleteBtn, 55);
		UIUtil.setTextSize(allStartBtn, 55);
		
		try {
			UIUtil.setViewSizeMargin(btnLin, 0, 190 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(allStartCB, 23 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(allStartBtn, 0, 0, 60 * Rx, 0);
			UIUtil.setViewSizeMargin(allDeleteCB, 23 * Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(allDeleteBtn, 0, 0, 60 * Rx, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
