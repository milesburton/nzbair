
package com.mb.android.nzbAirPremium.setup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import android.content.Context;

import com.mb.nzbair.remote.AsyncDownloadTask;
import com.mb.nzbair.remote.HttpGetter;
import com.mb.nzbair.remote.HttpRequestCompleteCallback;
import com.mb.nzbair.remote.ThreadedDownloadTask;
import com.mb.nzbair.remote.converters.ObjectConverter;
import com.mb.nzbair.remote.converters.StringConverter;
import com.mb.nzbair.remote.domain.HttpRequestComplete;
import com.mb.nzbair.remote.domain.RequestFor;
import com.mb.nzbair.remote.domain.WithCallback;

public class SetupService implements ISetup, HttpRequestCompleteCallback {

	static String TAG = SetupService.class.getName();

	private final AsyncDownloadTask downloader;
	private final List<ISetupCallback> callbacks = new ArrayList<ISetupCallback>();

	private static final String baseUrl = "http://setup.nzbair.com";

	public SetupService(Context context) {

		downloader = new ThreadedDownloadTask(Executors.newSingleThreadExecutor());

	}

	@Override
	public void removeCallback(ISetupCallback callback) {

		callbacks.remove(callback);
	}

	@Override
	public void requestToken() {

		final RequestFor<String> r = new RequestFor<String>(baseUrl + "/setup/create", new StringConverter());

		final WithCallback c = new WithCallback(this, SetupRequest.RequestToken.toString());

		downloader.request(new HttpGetter<String>(r, c));
	}

	@Override
	public void addCallback(ISetupCallback callback) {

		if (!callbacks.contains(callback)) {
			callbacks.add(callback);
		}
	}

	@Override
	public void downloadComplete(HttpRequestComplete rc) {

		switch (SetupRequest.valueOf(rc.getRequestId())) {
		case RequestToken:
			for (final ISetupCallback callback : callbacks) {
				callback.responseToken((String) rc.getResponse(), rc.getError());
			}
			break;
		case RequestData:

			for (final ISetupCallback callback : callbacks) {
				callback.responseConfiguration((Map<String, String>) rc.getResponse(), rc.getError());

			}
			break;

		}
	}

	@Override
	public void requestSetupData(String code, String pin) {

		final String url = baseUrl + "/setup/get/" + code + "/" + pin;

		final Map<String, String> hashMap = new HashMap<String, String>();
		final ObjectConverter<Map> con = new ObjectConverter<Map>((Class<Map>) hashMap.getClass());

		final RequestFor<Map> r = new RequestFor<Map>(url, con);
		final WithCallback c = new WithCallback(this, SetupRequest.RequestData.toString());

		downloader.request(new HttpGetter<Map>(r, c));
	}

	public enum SetupRequest {
		RequestToken, RequestData
	}

}
