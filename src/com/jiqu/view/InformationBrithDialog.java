package com.jiqu.view;

import java.util.ArrayList;
import java.util.Calendar;

import com.jiqu.adapter.AbstractWheelTextAdapter;
import com.jiqu.interfaces.DialogDismissObserver;
import com.vr.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import android.view.View.OnClickListener;;

public class InformationBrithDialog extends Dialog implements OnClickListener{
	private Context context;
	private CalendarTextAdapter mYearAdapter;
	private CalendarTextAdapter mMonthAdapter;
	private CalendarTextAdapter mDayAdapter;
	private ArrayList<String> arry_years = new ArrayList<String>();
	private ArrayList<String> arry_months = new ArrayList<String>();
	private ArrayList<String> arry_days = new ArrayList<String>();
	private String selectYear;
	private String selectMonth;
	private String selectDay;
	private DialogDismissObserver observer;
	
	private int month;
	private int day;
	
	private OnBirthListener onBirthListener;
	
	private int currentYear = getYear();
	private int currentMonth = 1;
	private int currentDay = 1;

	private int maxTextSize = 50;
	private int minTextSize = 35;

	private boolean issetdata = false;
	
	private RelativeLayout parent;
	private RelativeLayout btnRel;
	private Button cancle;
	private Button save;
	private LinearLayout brithWheelLin;
	private WheelView yearWheel;
	private WheelView monthWheel;
	private WheelView dayWheel;
	
	public interface OnBirthListener {
		public void onClick(String year, String month, String day);
	}

	public InformationBrithDialog(Context context) {
		super(context,R.style.GenderDialog);
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.brith_dialog_layout);
		
		initView();
		setCancelable(false);
	}
	
	public void setObserver(DialogDismissObserver observer){
		this.observer = observer;
	}
	
	public void initData() {
		setDate(getYear(), getMonth(), getDay());
		this.currentDay = 1;
		this.currentMonth = 1;
	}
	
	/**
	 * 设置年月日
	 * 
	 * @param year
	 * @param month
	 * @param day
	 */
	public void setDate(int year, int month, int day) {
		selectYear = year + "";
		selectMonth = month + "";
		selectDay = day + "";
		issetdata = true;
		this.currentYear = year;
		this.currentMonth = month;
		this.currentDay = day;
		if (year == getYear()) {
			this.month = getMonth();
		} else {
			this.month = 12;
		}
		calDays(year, month);
	}

	/**
	 * 设置年份
	 * 
	 * @param year
	 */
	public int setYear(int year) {
		int yearIndex = 0;
		if (year != getYear()) {
			this.month = 12;
		} else {
			this.month = getMonth();
		}
		for (int i = getYear(); i > 1950; i--) {
			if (i == year) {
				return yearIndex;
			}
			yearIndex++;
		}
		return yearIndex;
	}

	/**
	 * 设置月份
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public int setMonth(int month) {
		int monthIndex = 0;
		calDays(currentYear, month);
		for (int i = 1; i < this.month; i++) {
			if (month == i) {
				return monthIndex;
			} else {
				monthIndex++;
			}
		}
		return monthIndex;
	}
	
	/**
	 * 计算每月多少天
	 * 
	 * @param month
	 * @param leayyear
	 */
	public void calDays(int year, int month) {
		boolean leayyear = false;
		if (year % 4 == 0 && year % 100 != 0) {
			leayyear = true;
		} else {
			leayyear = false;
		}
		for (int i = 1; i <= 12; i++) {
			switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				this.day = 31;
				break;
			case 2:
				if (leayyear) {
					this.day = 29;
				} else {
					this.day = 28;
				}
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				this.day = 30;
				break;
			}
		}
		if (year == getYear() && month == getMonth()) {
			this.day = getDay();
		}
	}
	
	public void initYears() {
		for (int i = getYear(); i > 1950; i--) {
			arry_years.add(i + "");
		}
	}

	public void initMonths(int months) {
		arry_months.clear();
		for (int i = 1; i <= months; i++) {
			arry_months.add(i + "");
		}
	}

	public void initDays(int days) {
		arry_days.clear();
		for (int i = 1; i <= days; i++) {
			arry_days.add(i + "");
		}
	}
	
	/**
	 * 设置字体大小
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSize(String curriteItemText, CalendarTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				UIUtil.setTextSize(textvew, 50);
				textvew.setTextColor(context.getResources().getColor(R.color.yellow));
			} else {
				UIUtil.setTextSize(textvew, 35);
				textvew.setTextColor(context.getResources().getColor(R.color.white));
			}
		}
	}
	
	private void initView(){
		parent = (RelativeLayout) findViewById(R.id.parent);
		btnRel = (RelativeLayout) findViewById(R.id.btnRel);
		cancle = (Button) findViewById(R.id.cancle);
		save = (Button) findViewById(R.id.save);
		brithWheelLin = (LinearLayout) findViewById(R.id.brithWheelLin);
		yearWheel = (WheelView) findViewById(R.id.yearWheel);
		monthWheel = (WheelView) findViewById(R.id.monthWheel);
		dayWheel = (WheelView) findViewById(R.id.dayWheel);
		
		cancle.setOnClickListener(this);
		save.setOnClickListener(this);
		
		if (!issetdata) {
			initData();
		}
		
		initYears();
		mYearAdapter = new CalendarTextAdapter(context, arry_years, setYear(currentYear), maxTextSize, minTextSize);
		yearWheel.setVisibleItems(3);
		yearWheel.setViewAdapter(mYearAdapter);
		yearWheel.setCurrentItem(setYear(currentYear));
		
		initMonths(month);
		mMonthAdapter = new CalendarTextAdapter(context, arry_months, setMonth(currentMonth), maxTextSize, minTextSize);
		monthWheel.setVisibleItems(3);
		monthWheel.setViewAdapter(mMonthAdapter);
		monthWheel.setCurrentItem(setMonth(currentMonth));

		initDays(day);
		mDayAdapter = new CalendarTextAdapter(context, arry_days, currentDay - 1, maxTextSize, minTextSize);
		dayWheel.setVisibleItems(3);
		dayWheel.setViewAdapter(mDayAdapter);
		dayWheel.setCurrentItem(currentDay - 1);
		
		yearWheel.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
				selectYear = currentText;
				setTextviewSize(currentText, mYearAdapter);
				currentYear = Integer.parseInt(currentText);
				setYear(currentYear);
				initMonths(month);
				mMonthAdapter = new CalendarTextAdapter(context, arry_months, 0, maxTextSize, minTextSize);
				monthWheel.setVisibleItems(3);
				monthWheel.setViewAdapter(mMonthAdapter);
				monthWheel.setCurrentItem(0);
			}
		});
		
		yearWheel.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mYearAdapter);
				selectYear = currentText;
			}
		});
		
		monthWheel.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
				selectMonth = currentText;
				setTextviewSize(currentText, mMonthAdapter);
				setMonth(Integer.parseInt(currentText));
				initDays(day);
				mDayAdapter = new CalendarTextAdapter(context, arry_days, 0, maxTextSize, minTextSize);
				dayWheel.setVisibleItems(3);
				dayWheel.setViewAdapter(mDayAdapter);
				dayWheel.setCurrentItem(0);
			}
		});

		monthWheel.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mMonthAdapter);
				selectMonth = currentText;
			}
		});

		dayWheel.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mDayAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mDayAdapter);
				selectDay = currentText;
			}
		});

		dayWheel.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) mDayAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mDayAdapter);
				selectDay = currentText;
			}
		});


		
		initViewSize();
	}
	
	private void initViewSize(){
		UIUtil.setViewSize(cancle, 40 * MetricsTool.Rx, 40 * MetricsTool.Rx);
		UIUtil.setViewSize(save, 40 * MetricsTool.Rx, 40 * MetricsTool.Rx);
		UIUtil.setViewWidth(parent, MetricsTool.width);
		UIUtil.setViewHeight(btnRel, 60 * MetricsTool.Rx);
		UIUtil.setViewHeight(yearWheel, 240 * MetricsTool.Ry);
		UIUtil.setViewHeight(monthWheel, 240 * MetricsTool.Ry);
		UIUtil.setViewHeight(dayWheel, 240 * MetricsTool.Ry);
		
		try {
			UIUtil.setViewSizeMargin(cancle,20 * MetricsTool.Rx, 0, 0, 0);
			UIUtil.setViewSizeMargin(save,0, 0, 20 * MetricsTool.Rx, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}

	public int getMonth() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.MONTH) + 1;
	}

	public int getDay() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.DATE);
	}
	
	private class CalendarTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected CalendarTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxTextSize, minTextSize);
			this.list = list;
			setItemTextResource(R.id.tempValue);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, (int)(80 * MetricsTool.Ry));
			view.setLayoutParams(lp);
			return view;
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index) + "";
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cancle:
			dismiss();
			observer.onDialogStateChange(0);
			break;

		case R.id.save:
			dismiss();
			observer.onDialogSave(1,selectYear + "-" + selectMonth + "-" + selectDay);
			break;
		}
	}
}
