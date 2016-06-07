package com.jiqu.view;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.jiqu.activity.DetailActivity;
import com.jiqu.activity.HeadlineActivity;
import com.jiqu.adapter.ViewPagerAdapter;
import com.jiqu.application.StoreApplication;
import com.jiqu.object.GameInfo;
import com.jiqu.object.InformationPagerItemInfo;
import com.jiqu.object.ThematicItem;
import com.jiqu.tools.CountDownTimer;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;
import com.vr.store.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;

public class ViewPagerLinView extends RelativeLayout implements OnPageChangeListener{
	private View view;
	private Context context;
	private ViewPager viewPager;
	private LinearLayout radioGroup;
	@SuppressWarnings("rawtypes")
	private Class intentClass;
	private ImageView[] radioImgs;
	private ImageView[] contentImgs;
	private CountDownTimer timer;
	private int currentIndex = 0;
	private int defaultImgId = 0;

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

	@SuppressWarnings("deprecation")
	private void initView(Context context){
		this.context = context;
		view = LayoutInflater.from(StoreApplication.context).inflate(R.layout.viewpager_lin_layout, this);
		viewPager = (ViewPager) view.findViewById(R.id.viewPager);
		radioGroup = (LinearLayout) view.findViewById(R.id.radioGroup);
		
		viewPager.setOnPageChangeListener(this);
		
		try {
			UIUtil.setViewSizeMargin(radioGroup, 0, 0, 0, 30 * MetricsTool.Ry);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void setClass(Class intentClass){
		this.intentClass = intentClass;
	}
	
	public void setDefaultImgId(int id){
		this.defaultImgId = id;
	}
	
	public void cancleTimer(){
		if (timer != null) {
			timer.cancel();
		}
	}
	
	public void setData(InformationPagerItemInfo[] infos){
		final int count = infos.length;
		radioImgs = new ImageView[count];
		contentImgs = new ImageView[count];
		for (int i = 0; i < count; i++) {
			final InformationPagerItemInfo item = infos[i];
			ImageView view = new ImageView(context);
			LayoutParams lp = new LayoutParams((int)(20 * MetricsTool.Rx), (int)(20 * MetricsTool.Rx));
			if (i != 0) {
				ImageView emptyView = new ImageView(context);
				LayoutParams emptyLp = new LayoutParams((int)(10 * MetricsTool.Rx), (int)(20 * MetricsTool.Rx));
				emptyView.setLayoutParams(emptyLp);
				radioGroup.addView(emptyView);
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
					if (intentClass != null) {
						if (intentClass == HeadlineActivity.class) {
							context.startActivity(new Intent(context, HeadlineActivity.class)
							.putExtra("isWeb", true)
							.putExtra("url", item.getUrl()));
						}
					}
				}
			});
			LayoutParams params = new LayoutParams(android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT,
					android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT);
			contentImg.setLayoutParams(params);
			contentImg.setScaleType(ScaleType.FIT_XY);
			Bitmap bitmap = UIUtil.readBitmap(StoreApplication.context, defaultImgId);
			ImageListener listener = ImageLoader.getImageListener(contentImg, bitmap, bitmap);
			if (item.getPic() != null) {
				StoreApplication.getInstance().getImageLoader().get(item.getPic(),listener,MetricsTool.width,(int)(455 * MetricsTool.Ry));
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
	
	public void setData(GameInfo[] infos){
		final int count = infos.length;
		radioImgs = new ImageView[count];
		contentImgs = new ImageView[count];
		for (int i = 0; i < count; i++) {
			final GameInfo item = infos[i];
			ImageView view = new ImageView(context);
			LayoutParams lp = new LayoutParams((int)(20 * MetricsTool.Rx), (int)(20 * MetricsTool.Rx));
			if (i != 0) {
				ImageView emptyView = new ImageView(context);
				LayoutParams emptyLp = new LayoutParams((int)(10 * MetricsTool.Rx), (int)(20 * MetricsTool.Rx));
				emptyView.setLayoutParams(emptyLp);
				radioGroup.addView(emptyView);
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
					if (intentClass != null) {
						if (intentClass == DetailActivity.class) {
							context.startActivity(new Intent(context, intentClass)
								.putExtra("id", item.getId())
								.putExtra("name", item.getApply_name()));
						}else {
							context.startActivity(new Intent(context, intentClass).putExtra("id", item.getId()));
						}
					}
				}
			});
			LayoutParams params = new LayoutParams(android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT,
					android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT);
			contentImg.setLayoutParams(params);
			contentImg.setScaleType(ScaleType.FIT_XY);
			Bitmap bitmap = UIUtil.readBitmap(StoreApplication.context, defaultImgId);
			ImageListener listener = ImageLoader.getImageListener(contentImg, bitmap, bitmap);
			if (item.getPic() != null) {
				StoreApplication.getInstance().getImageLoader().get(item.getPic(),listener,MetricsTool.width,(int)(455 * MetricsTool.Ry));
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
	
	public void setData(ThematicItem[] infos){
		final int count = infos.length;
		radioImgs = new ImageView[count];
		contentImgs = new ImageView[count];
		for (int i = 0; i < count; i++) {
			final ThematicItem item = infos[i];
			ImageView view = new ImageView(context);
			LayoutParams lp = new LayoutParams((int)(20 * MetricsTool.Rx), (int)(20 * MetricsTool.Rx));
			if (i != 0) {
				ImageView emptyView = new ImageView(context);
				LayoutParams emptyLp = new LayoutParams((int)(10 * MetricsTool.Rx), (int)(20 * MetricsTool.Rx));
				emptyView.setLayoutParams(emptyLp);
				radioGroup.addView(emptyView);
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
					if (intentClass != null) {
						context.startActivity(new Intent(context, intentClass).putExtra("id", item.getId()));
					}
				}
			});
			LayoutParams params = new LayoutParams(android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT,
					android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT);
			contentImg.setLayoutParams(params);
			contentImg.setScaleType(ScaleType.FIT_XY);
			Bitmap bitmap = UIUtil.readBitmap(StoreApplication.context, defaultImgId);
			ImageListener listener = ImageLoader.getImageListener(contentImg, bitmap, bitmap);
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
