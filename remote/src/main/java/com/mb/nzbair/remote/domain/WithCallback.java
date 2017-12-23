
package com.mb.nzbair.remote.domain;

import com.mb.nzbair.remote.HttpRequestCompleteCallback;

public class WithCallback {

	private final HttpRequestCompleteCallback callback;
	private String requestId;
	private Object payload;

	public WithCallback(HttpRequestCompleteCallback callback) {
		this.callback = callback;
	}

	public WithCallback(HttpRequestCompleteCallback callback, String requestId) {
		this.callback = callback;
		this.requestId = requestId;
	}

	public WithCallback addPayload(Object payload) {

		this.payload = payload;
		return this;
	}

	public boolean hasPayload() {
		return this.payload != null;
	}

	public HttpRequestCompleteCallback getCallback() {
		return callback;
	}

	public String getRequestId() {
		return requestId;
	}

	public Object getPayload() {
		return payload;
	}

}
