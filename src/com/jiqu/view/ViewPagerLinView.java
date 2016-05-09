package com.jiqu.view;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.jiqu.activity.DetailActivity;
import com.jiqu.adapter.ViewPagerAdapter;
import com.jiqu.application.StoreApplication;
import com.jiqu.object.GameInfo;
import com.jiqu.tools.CountDownTimer;
import com.jiqu.tools.MetricsTool;
import com.vr.store.R;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

public class ViewPagerLinView extends RelativeLayout implements OnPageChangeListener{
	private View view;
	private Context context;
	private ViewPager viewPager;
	private LinearLayout radioGroup;
	private Class intentClass;
	private ImageView[] radioImgs;
	private ImageView[] contentImgs;
	private CountDownTimer timer;
	private int currentIndex = 0;

	public ViewPagerLinView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public ViewPagerLinView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public ViewPagerLinView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context){
		this.context = context;
		view = LayoutInflater.from(context).inflate(R.layout.viewpager_lin_layout, this);
		viewPager = (ViewPager) view.findViewById(R.id.viewPager);
		radioGroup = (LinearLayout) view.findViewById(R.id.radioGroup);
		
		viewPager.setOnPageChangeListener(this);
	}
	
	public void setClass(Class intentClass){
		this.intentClass = intentClass;
	}
	
	public void cancleTimer(){
		if (timer != null) {
			timer.cancel();
		}
	}
	
	public void setData(GameInfo[] infos){
		final int count = infos.length;
		radioImgs = new ImageView[count];
		contentImgs = new ImageView[count];
		for (int i = 0; i < count; i++) {
			final GameInfo item = infos[i];
			ImageView view = new ImageView(context);
			LayoutParams lp = new LayoutParams((int)(20 * MetricsTool.Rx), (int) (20 * MetricsTool.Rx));
			if (i != 0) {
				lp.leftMargin = (int) (10 * MetricsTool.Rx);
			}
			 view.setLayoutParams(lp);
			if (i== 0) {
				view.setBackgroundResource(R.drawable.dian_blue);
			}else {
				view.setBackgroundResource(R.drawable.dian_white);
			}
			radioImgs[i] = view;
			radioGroup.addView(view);
			
			ImageView contentImg = new ImageView(context);
			contentImg.setClickable(true);
			contentImg.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					context.startActivity(new Intent(context, intentClass).putExtra("id", item.getId()));
				}
			});
			LayoutParams params = new LayoutParams(android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT,
					android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT);
			contentImg.setLayoutParams(params);
			contentImg.setScaleType(ScaleType.FIT_XY);
			ImageListener listener = ImageLoader.getImageListener(contentImg,R.drawable.recommend_viewpager_default, R.drawable.recommend_viewpager_default);
			if (item.getPic() != null) {
				StoreApplication.getInstance().getImageLoader().get(item.getPic(),listener);
			}
			contentImgs[i] = contentImg;
		}
		ViewPagerAdapter adapter = new ViewPagerAdapter(context, contentImgs);
		viewPager.setAdapter(adapter);
		
		timer = new CountDownTimer(Integer.MAX_VALUE, 5000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				if (viewPager != null && count != 0) {
					viewPager.setCurrentItem(currentIndex % count);
					currentIndex++;
				}
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				
			}
		};
		timer.start();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		for(int i = 0;i<radioImgs.length;i++){
			if (arg0 == i) {
				radioImgs[i].setBackgroundResource(R.drawable.dian_blue);
			}else {
				radioImgs[i].setBackgroundResource(R.drawable.dian_white);
			}
		}
	}
}
