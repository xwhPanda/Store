package com.jiqu.interfaces;

import com.jiqu.object.InstalledApp;

public interface UninstallStateObserver {
	public void onUninstallStateChanged(InstalledApp app);
}
