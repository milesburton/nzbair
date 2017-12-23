
package com.mb.nzbair.remote.response;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.mb.nzbair.remote.exception.RestClientException;

public class HttpRestResponse implements RestResponse {

	private final HttpResponse httpResponse;

	private String contentType;
	private HttpEntity response;

	public HttpRestResponse(HttpResponse httpResponse) {

		this.httpResponse = httpResponse;

		if (httpResponse.getEntity() != null) {
			this.response = httpResponse.getEntity();

			this.contentType = httpResponse.getEntity().getContentType().getValue();
		}
	}

	@Override
	public InputStream getStream() {

		try {
			return httpResponse.getEntity().getContent();
		} catch (final IllegalStateException e) {
			throw new RestClientException(e);
		} catch (final IOException e) {
			throw new RestClientException(e);
		}
	}

	@Override
	public String getBodyAsString() {

		try {
			return new String(EntityUtils.toByteArray(response));
		} catch (final IOException e) {
			throw new RestClientException(e);
		}
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public HttpResponse getResponse() {
		return httpResponse;
	}

	@Override
	public int getStatus() {
		return httpResponse.getStatusLine().getStatusCode();
	}

}
