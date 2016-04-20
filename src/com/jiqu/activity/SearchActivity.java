package com.jiqu.activity;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.jiqu.object.SearchKeyword;
import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.SearchItemView;

public class SearchActivity extends BaseActivity implements OnClickListener{
	private RelativeLayout searchLayout;
	private Button backBtn;
	private RelativeLayout searchEditLayout;
	private Button searchBtn;
	private EditText searchEdit;
	private LinearLayout keywordLin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
	}

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.search_list_item;
	}

	private void initView(){
		searchLayout = (RelativeLayout) findViewById(R.id.searchLayout);
		backBtn = (Button) findViewById(R.id.backBtn);
		searchEditLayout = (RelativeLayout) findViewById(R.id.searchEditLayout);
		searchBtn = (Button) findViewById(R.id.searchBtn);
		searchEdit = (EditText) findViewById(R.id.searchEdit);
		
		keywordLin = (LinearLayout) findViewById(R.id.keywordLin);
		
		backBtn.setOnClickListener(this);
		
		List<SearchKeyword> keywords = new ArrayList<SearchKeyword>();
		for(int j = 0 ; j < 4 ; j++){
			keywords.clear();
			for(int i = 0;i <6;i++){
				SearchKeyword searchKeyword = new SearchKeyword();
				searchKeyword.setKeyword("德玛西亚");
				if (j == 0) {
					searchKeyword.setBackgroundType(1);
				}
				keywords.add(searchKeyword);
			}
			SearchItemView itemView = new SearchItemView(this);
			itemView.setData(keywords);
			if (j == 0) {
				itemView.titleImg.setBackgroundResource(R.drawable.sousuo2);
				itemView.titleContent.setText("艾欧尼亚");
			}else {
				itemView.titleContent.setText("裁决之地");
				itemView.titleImg.setVisibility(View.GONE);
			}
			keywordLin.setOrientation(LinearLayout.VERTICAL);
			if (j != 0) {
				ImageView imageView = new ImageView(this);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, (int)(4 * Ry));
				lp.topMargin = (int)(30 * Ry);
				lp.bottomMargin = (int)(30 * Ry);
				imageView.setLayoutParams(lp);
				imageView.setBackgroundResource(R.drawable.xian);
				keywordLin.addView(imageView);
			}
			keywordLin.addView(itemView);
		}
		
		initViewSize();
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(searchLayout, LayoutParams.MATCH_PARENT, 180 * Ry);
		UIUtil.setViewSize(backBtn, 36 * Rx, 36 * Rx);
		UIUtil.setViewSize(searchEditLayout, 840 * Rx, 70 * Ry);
		UIUtil.setViewSize(searchBtn, 50 * Rx, 50 * Rx);
		
		UIUtil.setTextSize(searchEdit, 30);
		
		
		try {
			UIUtil.setViewSizeMargin(backBtn, 30 * Rx, 0, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backBtn) {
			finish();
		}
	}
}
