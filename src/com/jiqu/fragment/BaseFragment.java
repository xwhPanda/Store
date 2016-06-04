package com.jiqu.fragment;

import com.jiqu.database.Account;
import com.jiqu.interfaces.LoginOutObserver;
import com.jiqu.tools.InstalledAppTool;
import com.jiqu.tools.MetricsTool;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment implements LoginOutObserver{
	public Activity activity;
	public float Rx,Ry;
	public InstalledAppTool installedAppTool;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = getActivity();
		installedAppTool = new InstalledAppTool();
		Rx = MetricsTool.Rx;
		Ry = MetricsTool.Ry;
		init();
	}
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return initView();
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initData();
	}
	
	public abstract View initView();
	
	public void initData(){};
	
	public void init(){};
	
	@Override
	public void onLoginOut() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onRefresh(Account account) {
		// TODO Auto-generated method stub
		
	}
}
