
package com.mb.nzbair.remote.response;

import java.io.InputStream;

import org.apache.http.HttpResponse;

public interface RestResponse {

	HttpResponse getResponse();

	int getStatus();

	String getBodyAsString();

	String getContentType();

	InputStream getStream();
}
