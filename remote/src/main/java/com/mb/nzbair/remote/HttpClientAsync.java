
package com.mb.nzbair.remote;

import android.util.Log;

import com.mb.nzbair.remote.domain.HttpRequestComplete;
import com.mb.nzbair.remote.domain.RequestFor;
import com.mb.nzbair.remote.domain.WithCallback;

public abstract class HttpClientAsync<T> implements Runnable {

	private static final String TAG = HttpClientAsync.class.getCanonicalName();

	private final WithCallback callbackConfig;
	private final RequestFor<T> request;

	public HttpClientAsync(RequestFor<T> r, WithCallback c) {

		this.request = r;
		this.callbackConfig = c;
	}

	@Override
	public void run() {

		final HttpRequestComplete httpRequest = new HttpRequestComplete();
		httpRequest.addRequestId(callbackConfig.getRequestId());

		if (callbackConfig.hasPayload()) {
			httpRequest.setPayload(callbackConfig.getPayload());
		}

		try {
			httpRequest.addResponse(doRequest(request));
		} catch (final Exception ex) {
			Log.e(TAG, "Http Request Failed", ex);
			httpRequest.addException(ex);
		}

		try {
			callbackConfig.getCallback().downloadComplete(httpRequest);

		} catch (final Exception ex) {
			Log.e(TAG, "Http Request callback threw an exception", ex);
			ex.printStackTrace();
		}

	}

	protected abstract T doRequest(RequestFor<T> request) throws Exception;

}
