
package com.mb.nzbair.remote.domain;

public class HttpRequestCompleteWithError extends HttpRequestComplete {

	public HttpRequestCompleteWithError(Throwable e) {
		super();
		super.setError(e);
	}

}
