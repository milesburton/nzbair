
package com.mb.nzbair.remote.exception;

public class RestClientException extends RuntimeException {

	private static final long serialVersionUID = 1849382748324L;

	public RestClientException() {

	}

	public RestClientException(Exception cause) {
		super(cause);
	}

}
