
package com.mb.nzbair.remote.domain;

public class HttpRequestComplete {

	public static final String DEFAULT_REQUEST_ID = "httpRequestComplete";

	private Object response;
	private String requestId;
	private Object payload;
	private Throwable error;

	public HttpRequestComplete() {
	};

	public HttpRequestComplete(String requestId) {
		this.requestId = requestId;
	}

	public HttpRequestComplete addResponse(Object response) {

		this.response = response;
		return this;
	}

	public HttpRequestComplete addException(Throwable error) {

		this.error = error;
		return this;
	}

	public boolean hasError() {
		return this.error != null;
	}

	public boolean hasPayload() {
		return this.payload != null;
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}

	public String getRequestId() {
		return requestId;
	}

	public HttpRequestComplete addRequestId(String requestId) {
		this.requestId = requestId;

		return this;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

	public Throwable getError() {
		return error;
	}

	public void setError(Throwable error) {
		this.error = error;
	}

}
