
package com.mb.nzbair.remote.domain;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import com.mb.nzbair.remote.converters.HttpResponseConverter;

public class RequestFor<T> {

	private final HttpResponseConverter<T> responseConverter;
	private boolean allowInvalidSSLCertificates = true;
	private String url = "";
	private final Map<String, String> params = new Hashtable<String, String>();
	private final List<Header> headers = new ArrayList<Header>();

	public RequestFor(String url, HttpResponseConverter<T> responseConverter) {

		this.url = url;
		this.responseConverter = responseConverter;
	}

	public RequestFor<T> addParam(String key, String value) {

		params.put(key, value);
		return this;
	}

	public RequestFor<T> addParam(Map<String, String> params) {

		this.params.putAll(params);
		return this;
	}

	public RequestFor<T> addHeader(Header h) {

		headers.add(h);
		return this;
	}

	public RequestFor<T> addHeader(String k, String v) {

		headers.add(new BasicHeader(k, v));
		return this;
	}

	public RequestFor<T> addHeader(List<Header> h) {

		this.headers.addAll(h);
		return this;
	}

	public HttpResponseConverter<T> getResponseConverter() {

		return responseConverter;
	}

	public void shouldAllowInvalidSsl(boolean v) {

		allowInvalidSSLCertificates = true;
	}

	public String getUrl() {

		return url;
	}

	public Map<String, String> getParams() {

		return params;
	}

	public List<Header> getHeaders() {
		return headers;
	}

	public boolean shouldAllowInvalidSsl() {

		return this.allowInvalidSSLCertificates;
	}

}
