
package com.mb.nzbair.providers.base;

import java.util.Hashtable;
import java.util.Map;

import com.mb.nzbair.providers.converters.UsenetPostConverter;
import com.mb.nzbair.providers.domain.UsenetPost;
import com.mb.nzbair.providers.interfaces.DetailsCallback;
import com.mb.nzbair.remote.HttpGetter;
import com.mb.nzbair.remote.HttpRequestCompleteCallback;
import com.mb.nzbair.remote.domain.HttpRequestComplete;
import com.mb.nzbair.remote.domain.RequestFor;
import com.mb.nzbair.remote.domain.WithCallback;

public class RemoteDetailsProxy extends DetailsProxy implements HttpRequestCompleteCallback {

	private Map<String, String> params = new Hashtable<String, String>();

	public RemoteDetailsProxy(String providerId, Map<String, String> params, int appVersion) {

		super(providerId, "detail", appVersion);
		params.put("version", ((Integer) appVersion).toString());
		configure(params);
	}

	public void configure(Map<String, String> params) {
		this.params = params;
	}

	@Override
	public void requestPostDetails(UsenetPost post) {

		final RequestFor<UsenetPost> r = new RequestFor<UsenetPost>(withDetailsUrl(post.getId()), new UsenetPostConverter()).addParam(params);

		final WithCallback c = new WithCallback(this);

		startTask(new HttpGetter<UsenetPost>(r, c));
	}

	private String withDetailsUrl(String postId) {
		return super.withApiUrl() + postId;
	}

	@Override
	public void downloadComplete(HttpRequestComplete rc) {

		for (final DetailsCallback callback : this.getListeners()) {
			callback.responseDetails((UsenetPost) rc.getResponse(), rc.getError());
		}
	}

	public Map<String, String> getParams() {
		return params;
	}
}
