package com.jiqu.interfaces;

import com.jiqu.database.Account;

public interface LoginOutObserver {
	public void onLoginOut();
	public void onRefresh(Account account);
}
