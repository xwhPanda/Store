package com.jiqu.adapter;

import java.util.List;

import org.w3c.dom.Text;

import com.jiqu.object.AccountInformation;
import com.vr.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AccountInformationAdapter extends BaseAdapter {
	private List<AccountInformation> accountInformations;
	private Context context;

	public AccountInformationAdapter(Context context,List<AccountInformation> accountInformations) {
		// TODO Auto-generated constructor stub
		this.accountInformations = accountInformations;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return accountInformations.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return accountInformations.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder(context);
		}else {
			holder = (Holder) convertView.getTag();
		}
		holder.initData(accountInformations.get(position));
		return holder.getRootView();
	}

	class Holder{
		private TextView title;
		private TextView value;
		private View rootView;
		private Context context;
		
		public Holder(Context context){
			this.context = context;
			rootView = initView();
			rootView.setTag(this);
		}
		
		public View getRootView(){
			return rootView;
		}
		
		private View initView(){
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.account_information_item, null);
			
			title = (TextView) view.findViewById(R.id.title);
			value = (TextView) view.findViewById(R.id.value);
			
			initViewSize(view);
			return view;
		}
		
		private void initViewSize(View view){
			UIUtil.setTextSize(title, 35);
			UIUtil.setTextSize(value, 35);
			
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,(int)(150 * MetricsTool.Ry));
			view.setLayoutParams(lp);
			
			try {
				UIUtil.setViewSizeMargin(title, 15 * MetricsTool.Rx, 0, 0, 0);
				UIUtil.setViewSizeMargin(value, 0, 0, 15 * MetricsTool.Rx, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void initData(AccountInformation information){
			title.setText(information.getNickname());
			value.setText(information.getLevel());
		}
	}
	
}
