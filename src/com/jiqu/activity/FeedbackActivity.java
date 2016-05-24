package com.jiqu.activity;

import java.util.HashMap;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.jiqu.store.BaseActivity;
import com.jiqu.tools.Constants;
import com.jiqu.tools.MD5;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.TitleView;
import com.vr.store.R;

public class FeedbackActivity extends BaseActivity implements OnClickListener{
	private final String FEEDBACK_REQUEST = "feedbackRequest";
	private TitleView titleView;
	private RelativeLayout contentRel;
	private EditText content;
	private TextView contentTip;
	private ImageView img;
	private Button commit;
	private String title;
	
	private RequestTool requestTool;
	private HashMap<String, Object> map = new HashMap<String, Object>();
	private boolean feedbacking = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestTool = RequestTool.getInstance();
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
	
	private void feedback(String content){
		feedbacking = true;
		map.clear();
		map.put("content", content);
		map.put("unique_id", Constants.DEVICE_ID);
		map.put("token", MD5.GetMD5Code(content + Constants.DEVICE_ID + RequestTool.PRIKEY));
		requestTool.startStringRequest(Method.POST, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				JSONObject json = JSON.parseObject(arg0);
				if (json.containsKey("status")) {
					int status = json.getIntValue("status");
					if (status == 1) {
						Toast.makeText(FeedbackActivity.this, "非常感谢您的反馈", Toast.LENGTH_SHORT).show();
						finish();
					}else {
						Toast.makeText(FeedbackActivity.this, "反馈失败", Toast.LENGTH_SHORT).show();
					}
				}
				feedbacking = false;
			}
		}, RequestTool.FEEDBACK_URL, new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				feedbacking = false;
				Toast.makeText(FeedbackActivity.this, "反馈失败", Toast.LENGTH_SHORT).show();
			}
			
		}, map, FEEDBACK_REQUEST);
	}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.feedback_layout;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img:
			
			break;

		case R.id.commit:
			String text = content.getText().toString();
			if (TextUtils.isEmpty(text)) {
				Toast.makeText(this, "请输入反馈内容", Toast.LENGTH_SHORT).show();
			}else if (text.length() < 4 || text.length() > 250) {
				Toast.makeText(this, "字数不符合要求", Toast.LENGTH_SHORT).show();
			}else {
				if (!feedbacking) {
					feedback(text);
				}else {
					Toast.makeText(this, "正在发送", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}
	}
	
}
