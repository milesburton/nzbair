
package com.mb.nzbair.remote;

import com.mb.nzbair.remote.domain.RequestFor;
import com.mb.nzbair.remote.domain.WithCallback;
import com.mb.nzbair.remote.response.RestResponse;

public class HttpGetter<T> extends HttpClientAsync<T> {

	public HttpGetter(RequestFor<T> r, WithCallback c) {

		super(r, c);
	}

	@Override
	protected T doRequest(RequestFor<T> r) throws Exception {

		final RestClient c = new RestClient(new DefaultHttpClientWithInvalidSslSupressor().getNewHttpClient());
		final RestResponse rr = c.get(r.getUrl(), r.getParams(), r.getHeaders());

		return r.getResponseConverter().convert(rr);
	}

}
