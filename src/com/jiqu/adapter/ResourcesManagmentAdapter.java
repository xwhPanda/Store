package com.jiqu.adapter;

import java.util.List;

import com.jiqu.object.ResourcesItem;
import com.jiqu.store.R;
import com.jiqu.tools.MetricsTool;
import com.jiqu.tools.UIUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ResourcesManagmentAdapter extends BaseAdapter {
	private Context context;
	private List<ResourcesItem> resourcesItems;
	
	public ResourcesManagmentAdapter(Context context,List<ResourcesItem> resourcesItems) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.resourcesItems = resourcesItems;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return resourcesItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return resourcesItems.get(position);
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
			holder = new Holder();
			convertView = holder.getRootView();
		}else {
			holder = (Holder) convertView.getTag();
		}
		holder.initData();
		return convertView;
	}

	class Holder{
		private ImageView appIcon;
		private RelativeLayout infomationRel;
		private RelativeLayout versionRel;
		private TextView appName;
		private TextView oldVersion;
		private ImageView versionImg;
		private TextView newVersion;
		private RelativeLayout sizeRel;
		private TextView updatePrecent;
		private TextView size;
		private Button update;
		private ResourcesItem data;
		
		private View rootView;
		
		public Holder(){
			rootView = initView();
			rootView.setTag(this);
			
			initViewSize();
		}
		
		public View getRootView(){
			return rootView;
		}
		
		private View initView(){
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.resource_management_item, null);
			
			appIcon = (ImageView) view.findViewById(R.id.appIcon);
			infomationRel = (RelativeLayout) view.findViewById(R.id.infomationRel);
			versionRel = (RelativeLayout) view.findViewById(R.id.versionRel);
			appName = (TextView) view.findViewById(R.id.appName);
			oldVersion = (TextView) view.findViewById(R.id.oldVersion);
			versionImg = (ImageView) view.findViewById(R.id.versionImg);
			newVersion = (TextView) view.findViewById(R.id.newVersion);
			sizeRel = (RelativeLayout) view.findViewById(R.id.sizeRel);
			updatePrecent = (TextView) view.findViewById(R.id.updatePrecent);
			size = (TextView) view.findViewById(R.id.size);
			update = (Button) view.findViewById(R.id.update);
			
			update.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});
			
			return view;
		}
		
		private void initViewSize(){
			UIUtil.setViewSize(appIcon, 170 * MetricsTool.Rx, 170 * MetricsTool.Rx);
			UIUtil.setViewSize(update, 95 * MetricsTool.Rx, 75 * MetricsTool.Ry);
			UIUtil.setViewSize(versionImg, 60 * MetricsTool.Rx, 30 * MetricsTool.Rx);
			
			UIUtil.setTextSize(appName, 40);
			UIUtil.setTextSize(oldVersion,40);
			UIUtil.setTextSize(newVersion, 40);
			UIUtil.setTextSize(updatePrecent, 35);
			UIUtil.setTextSize(size, 35);
			
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (290 * MetricsTool.Ry));
			rootView.setLayoutParams(lp);
			
			try {
				UIUtil.setViewSizeMargin(sizeRel, 0, 30 * MetricsTool.Ry, 0, 0);
				UIUtil.setViewSizeMargin(appIcon, 50 * MetricsTool.Rx, 0, 0, 0);
				UIUtil.setViewSizeMargin(oldVersion, 30 * MetricsTool.Rx, 0, 0, 0);
				UIUtil.setViewSizeMargin(versionImg, 10 * MetricsTool.Rx, 0, 0, 0);
				UIUtil.setViewSizeMargin(newVersion, 10 * MetricsTool.Rx, 0, 0, 0);
				UIUtil.setViewSizeMargin(size, 30 * MetricsTool.Rx, 0, 0, 0);
				UIUtil.setViewSizeMargin(infomationRel, 35 * MetricsTool.Rx, 0, 35 * MetricsTool.Rx, 0);
				UIUtil.setViewSizeMargin(update, 0, 0, 50 * MetricsTool.Rx, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void initData(){
			appIcon.setBackgroundColor(context.getResources().getColor(R.color.red));
			appName.setText("德玛西亚");
			oldVersion.setText("5.0.0");
			newVersion.setText("6.0.0");
			updatePrecent.setText("司法实践粉红色客户司");
			size.setText("50MB");
		}
	}
}
