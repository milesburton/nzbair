
package com.mb.nzbair.providers.base;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.mb.nzbair.remote.HttpClientAsync;
import com.mb.nzbair.remote.HttpRequestCompleteCallback;
import com.mb.nzbair.remote.domain.HttpRequestComplete;

public abstract class BaseProxy<Callback> implements HttpRequestCompleteCallback {

	private final List<Callback> callbacks = new ArrayList<Callback>();
	private final String providerId;
	private final String apiMethod;
	private final ExecutorService httpClientThread;

	private int appVersion;

	public BaseProxy(String providerId, String apiMethod, int appVersion) {

		this.providerId = providerId;
		this.apiMethod = apiMethod;
		httpClientThread = Executors.newSingleThreadExecutor();
	}

	protected String withApiUrl() {
		return getProtocol() + "api.nzbair.com/providers/" + providerId + "/" + apiMethod + "/";
	}

	protected String withApiUrl(Integer offset, Integer limit) {

		return withApiUrl() + offset.toString() + "/" + limit.toString();

	}

	private String getProtocol() {

		return "https://";
	}

	protected void startTask(HttpClientAsync t) {

		httpClientThread.submit(t);
	}

	protected List<Callback> getListeners() {
		return callbacks;
	}

	protected String toStr(boolean bool) {
		return (bool ? "true" : "false");
	}

	@Override
	public abstract void downloadComplete(HttpRequestComplete r);

	protected void setAppVersion(int appVersion) {
		this.appVersion = appVersion;
	}

	protected int getAppVersion() {
		return appVersion;
	}

	public void addListener(Callback callback) {

		if (!this.callbacks.contains(callback)) {
			this.callbacks.add(callback);
		}

	}

	public void removeListener(Callback callback) {

		this.callbacks.remove(callback);
	}

}
