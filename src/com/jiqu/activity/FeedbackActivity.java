package com.jiqu.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiqu.store.BaseActivity;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.TitleView;
import com.vr.store.R;

public class FeedbackActivity extends BaseActivity implements OnClickListener{
	private TitleView titleView;
	private RelativeLayout contentRel;
	private EditText content;
	private TextView contentTip;
	private ImageView img;
	private Button commit;
	private String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		title = getIntent().getStringExtra("title");
		initView();
	}
	
	private void initView(){
		titleView = (TitleView) findViewById(R.id.titleView);
		contentRel = (RelativeLayout) findViewById(R.id.contentRel);
		content = (EditText) findViewById(R.id.content);
		contentTip = (TextView) findViewById(R.id.contentTip);
		img = (ImageView) findViewById(R.id.img);
		commit = (Button) findViewById(R.id.commit);
		
		titleView.back.setBackgroundResource(R.drawable.fanhui);
		titleView.tip.setText(title);
		titleView.setActivity(this);
		
		img.setOnClickListener(this);
		commit.setOnClickListener(this);
		
		initViewSize();
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(contentRel, 1010 * Rx, 544 * Rx);
		UIUtil.setViewSize(commit, 974 * Rx, 110 * Rx);
		UIUtil.setViewSize(img, 86 * Rx, 86 * Rx);
		
		UIUtil.setTextSize(content, 40);
		UIUtil.setTextSize(contentTip, 40);
		UIUtil.setTextSize(commit, 50);
		
		try {
			UIUtil.setViewSizeMargin(contentRel, 0, 25 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(img, 0, 25 * Ry, 0, 0);
			UIUtil.setViewSizeMargin(commit, 0, 0, 0, 110 * Ry);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.weibo_share_layout;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img:
			
			break;

		case R.id.commit:
			break;
		}
	}
	
}
