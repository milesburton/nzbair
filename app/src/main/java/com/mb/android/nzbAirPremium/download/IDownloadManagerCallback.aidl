package com.mb.android.nzbAirPremium.download;

interface IDownloadManagerCallback {

	
	void onDownloadUpdate(String dm);
	
	void onDownloadComplete(String uuid);
	
	void onDownloadAdded(String uuid);
	
	void onDownloadRemoved(String uuid);

}