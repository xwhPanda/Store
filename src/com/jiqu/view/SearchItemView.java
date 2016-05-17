package com.jiqu.view;

import java.util.ArrayList;
import java.util.List;

import com.jiqu.interfaces.SearcheListener;
import com.jiqu.object.SearchKeyword;
import com.vr.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SearchItemView extends RelativeLayout {
	public RelativeLayout titleView;
	public ImageView titleImg;
	public TextView titleContent;
	public LinearLayout keywordLin;
	private List<SearchKeyword> keywords;
	private SearcheListener listener;
	private Context context;
	
	public SearchItemView(Context context) {
		super(context);
		initView(context);
	}

	public SearchItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public SearchItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private void initView(Context context){
		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.search_itemview_layout, this);
		titleView = (RelativeLayout) view.findViewById(R.id.titleView);
		titleImg = (ImageView) view.findViewById(R.id.titleImg);
		titleContent = (TextView) view.findViewById(R.id.titleContent);
		keywordLin = (LinearLayout) view.findViewById(R.id.keywordLin);
		
		initViewSize();
	}
	
	private void initViewSize(){
		UIUtil.setTextSize(titleContent, 45);
		UIUtil.setViewSize(titleImg, 56 * MetricsTool.Rx, 56 * MetricsTool.Rx);
		
		try {
			UIUtil.setViewSizeMargin(titleView, 25 * MetricsTool.Rx, 0, 0, 0);
			
			UIUtil.setViewSizeMargin(titleContent, 5 * MetricsTool.Rx, 0, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setSearchListener(SearcheListener listener){
		this.listener = listener;
	}
	
	public void setData(final List<SearchKeyword> keywordList){
		keywords = keywordList;
		int size = keywordList.size();
		List<LinearLayout> layouts = new ArrayList<LinearLayout>();
		int index = 0;
		if (size > 0) {
			for(int i = 0;i < size;i++){
				final SearchKeyword keyword = keywordList.get(i);
				if (i % 3 == 0 && i < size) {
					LinearLayout layout = new LinearLayout(context);
					layout.setOrientation(LinearLayout.HORIZONTAL);
					index = i / 3;
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					if (index != 0) {
						params.topMargin = (int)(15 * MetricsTool.Rx);
					}else {
						params.topMargin = (int)(30 * MetricsTool.Rx);
					}
					layout.setLayoutParams(params);
					layouts.add(layout); 
				}
				TextView textView = new TextView(context);
				textView.setClickable(true);
				textView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (listener != null) {
							listener.onSearch(keyword.getKeyword());
						}
					}
				});
				textView.setText(keywordList.get(i).getKeyword());
				if (keywordList.get(i).getBackgroundType() == 0) {
					textView.setTextColor(getResources().getColor(R.color.blue));
					textView.setBackgroundResource(R.drawable.xiankuang_blue);
					titleContent.setTextColor(getResources().getColor(R.color.blue));
				}else if (keywordList.get(i).getBackgroundType() == 1) {
					textView.setBackgroundResource(R.drawable.xiankuangyellow);
					textView.setTextColor(getResources().getColor(R.color.yellow));
					titleContent.setTextColor(getResources().getColor(R.color.yellow));
				}
				textView.setGravity(Gravity.CENTER);
				android.widget.LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int)(330 * MetricsTool.Rx), (int)(100 * MetricsTool.Ry));
				if (i % 3 == 1) {
					lp.leftMargin = (int)(15 * MetricsTool.Rx);
					lp.rightMargin = (int)(15 * MetricsTool.Rx);
				}
				textView.setLayoutParams(lp);
				UIUtil.setTextSize(textView, 40);
				layouts.get(index).addView(textView);
			}
			for(LinearLayout layout : layouts){
				keywordLin.addView(layout);
			}
		}
	}
}
