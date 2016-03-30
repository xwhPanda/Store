package com.jiqu.interfaces;

public interface DialogDismissObserver {
	public void onDialogStateChange(int state);
	public void onDialogSave(int type ,String value);
}
