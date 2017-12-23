
package com.mb.nzbair.remote;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;

import android.util.Log;

import com.mb.nzbair.remote.request.Body;
import com.mb.nzbair.remote.response.HttpRestResponse;
import com.mb.nzbair.remote.response.RestResponse;
import com.mb.nzbair.remote.utils.QueryStringBuilder;

public class RestClient {

	private static final String TAG = RestClient.class.getCanonicalName();

	private final HttpClient httpClient;

	public RestClient(HttpClient httpClient) {

		this.httpClient = httpClient;
		
		new InvalidSslCertHandler().setAllowInvalidCertificates();
	}

	public RestResponse get(String resource, Map<String, String> queryString, List<Header> httpHeaders) throws ClientProtocolException, IOException {

		final HttpGet httpGet = new HttpGet(withUri(resource, queryString));
		setHeaders(httpGet, httpHeaders);
		return execute(httpGet);
	}

	public RestResponse post(String resource, Body body, Map<String, String> queryString, List<Header> httpHeaders) throws ClientProtocolException, IOException {

		final HttpPost httpPost = new HttpPost(withUri(resource, queryString));
		setHeaders(httpPost, httpHeaders);
		httpPost.setEntity(body.getHttpEntity());

		return execute(httpPost);
	}

	RestResponse put(String resource, Body body, Map<String, String> queryString, List<Header> httpHeaders) throws ClientProtocolException, IOException {

		final HttpPut httpPut = new HttpPut(withUri(resource, queryString));
		setHeaders(httpPut, httpHeaders);
		httpPut.setEntity(body.getHttpEntity());

		return execute(httpPut);
	}

	private void setHeaders(HttpRequestBase httpRequest, List<Header> headers) {

		for (final Header header : headers) {
			httpRequest.addHeader(header);
		}
	}

	private RestResponse execute(HttpUriRequest httpRequest) throws ClientProtocolException, IOException {

		final HttpResponse httpResponse = httpClient.execute(httpRequest);
		final RestResponse restResponse = new HttpRestResponse(httpResponse);

		return restResponse;
	}

	private String withUri(String resource, Map<String, String> queryString) {

		final String qs = QueryStringBuilder.buildQueryString(queryString);
		final String uri = resource + "?" + qs;

		Log.i(TAG, "Requesting URI: " + uri);

		return uri;
	}
}
