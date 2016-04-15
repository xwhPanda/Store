package com.jiqu.view;

import com.jiqu.inter.Pullable;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View.MeasureSpec;
import android.widget.ListView;

public class PullableListView extends ListView implements Pullable
{
	private boolean isCanPullDown = false;
	private boolean isCanPullUp = true;
	private boolean hasHead = false;//是否有头部

	public PullableListView(Context context)
	{
		super(context);
	}

	public PullableListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public PullableListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}
	
	public void setCanPullDown(boolean canPullDown){
		isCanPullDown = canPullDown;
	}
	
	public void setCanPullUp(boolean canPullUp){
		isCanPullUp = canPullUp;
	}
	
	public void setHasHead(boolean hasHead){
		this.hasHead = hasHead;
	}

	@Override
	public boolean canPullDown()
	{
		if (isCanPullDown) {
			if (getCount() == 0)
			{
				// 没有item的时候也可以下拉刷新
				return false;
			} else if (getFirstVisiblePosition() == 0
					&& getChildAt(0).getTop() >= 0)
			{
				// 滑到ListView的顶部了
				return true;
			} else
				return false;
		}else {
			return false;
		}
	}

	@Override
	public boolean canPullUp()
	{
		if (isCanPullUp) {
			int count = 0;
			if (hasHead) {
				count = 1;
			}
			if (getCount() == count)
			{
				// 没有item的时候也可以上拉加载
				return false;
			} else if (getLastVisiblePosition() == (getCount() - 1))
			{
				// 滑到底部了
				if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
						&& getChildAt(
								getLastVisiblePosition()
								- getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
					return true;
			}
			return false;
		}else {
			return false;
		}
	}
}
