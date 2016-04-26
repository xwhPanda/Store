package com.jiqu.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.jiqu.adapter.GameAdapter;
import com.jiqu.download.StringUtil;
import com.jiqu.object.GameDetailInfo;
import com.jiqu.object.GameInfo;
import com.jiqu.object.InstalledApp;
import com.jiqu.object.SearchInfo;
import com.jiqu.object.SearchKeyword;
import com.jiqu.store.BaseActivity;
import com.jiqu.store.R;
import com.jiqu.tools.InstalledAppTool;
import com.jiqu.tools.RequestTool;
import com.jiqu.tools.UIUtil;
import com.jiqu.view.SearchItemView;

public class SearchActivity extends BaseActivity implements OnClickListener,Listener<JSONObject>,ErrorListener{
	private RelativeLayout searchLayout;
	private Button backBtn;
	private RelativeLayout searchEditLayout;
	private Button searchBtn;
	private EditText searchEdit;
	private LinearLayout keywordLin;
	private ListView searchListView;
	
	private RequestTool requestTool;
	private GameAdapter adapter;
	private List<GameInfo> gameInfos = new ArrayList<GameInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
		
		requestTool = RequestTool.getInstance();
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
		searchListView = (ListView) findViewById(R.id.searchListView);
		
		backBtn.setOnClickListener(this);
		searchBtn.setOnClickListener(this);
		
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
		
		adapter = new GameAdapter(this, gameInfos,false,false);
		searchListView.setAdapter(adapter);
		
		searchListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				startActivity(new Intent(SearchActivity.this, DetailActivity.class).putExtra("p_id", gameInfos.get(position).getP_id()));
			}
		});
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
	
	private String getSearchText(){
		return searchEdit.getText().toString();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (adapter != null) {
			adapter.startObserver();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (adapter != null) {
			adapter.stopObserver();
		}
		requestTool.stopSearchRequest();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backBtn) {
			finish();
		}else if (v.getId() == R.id.searchBtn) {
			String text = getSearchText();
			if (!TextUtils.isEmpty(text)) {
				requestTool.initParam();
				requestTool.setParam("text", text);
				requestTool.startSearchRequest(this, this);
			}else {
				Toast.makeText(this, "请输入要搜索的内容", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onErrorResponse(VolleyError arg0) {
		// TODO Auto-generated method stub
		keywordLin.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onResponse(JSONObject arg0) {
		// TODO Auto-generated method stub
		keywordLin.setVisibility(View.INVISIBLE);
		searchListView.setVisibility(View.VISIBLE);
		SearchInfo searchInfo = JSON.parseObject(arg0.toString(), SearchInfo.class);
		if (searchInfo != null && searchInfo.getItem() != null) {
			gameInfos.clear();
			Collections.addAll(gameInfos, searchInfo.getItem());
			List<InstalledApp> apps = InstalledAppTool.getPersonalApp(this);
			for (GameInfo gameInfo : gameInfos) {
				gameInfo.setAdapterType(1);
				int state = InstalledAppTool.contain(apps,gameInfo.getPackagename(), Integer.parseInt(gameInfo.getVersion_code()));
				if (state != -1) {
					gameInfo.setState(state);
				}
			}
			adapter.notifyDataSetChanged();
		}
	}
	
	
}
