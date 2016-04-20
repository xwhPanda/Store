package com.jiqu.adapter;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

public class ViewPagerAdapter extends PagerAdapter {
	private ImageView[] contentImgs;

	public ViewPagerAdapter(Context context,ImageView[] contentImgs) {
		// TODO Auto-generated constructor stub
		this.contentImgs = contentImgs;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contentImgs.length;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}
	
	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
		((ViewPager) container).removeView(contentImgs[position]);
	}
	
	@Override
	public Object instantiateItem(View container, int position) {
		// TODO Auto-generated method stub
		((ViewPager)container).addView(contentImgs[position]);
		return contentImgs[position];
	}

}
