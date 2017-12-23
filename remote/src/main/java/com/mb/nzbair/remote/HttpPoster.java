
package com.mb.nzbair.remote;

import com.mb.nzbair.remote.domain.RequestFor;
import com.mb.nzbair.remote.domain.WithCallback;
import com.mb.nzbair.remote.request.Body;
import com.mb.nzbair.remote.response.RestResponse;

public class HttpPoster<T> extends HttpClientAsync<T> {

	private final Body requestBody;

	public HttpPoster(RequestFor<T> r, WithCallback c, Body requestBody) {
		super(r, c);
		this.requestBody = requestBody;
	}

	@Override
	protected T doRequest(RequestFor<T> r) throws Exception {

		final RestClient c = new RestClient(new DefaultHttpClientWithInvalidSslSupressor().getNewHttpClient());
		final RestResponse rr = c.post(r.getUrl(), requestBody, r.getParams(), r.getHeaders());

		return r.getResponseConverter().convert(rr);
	}
}
