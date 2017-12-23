
package com.mb.nzbair.providers.base;

import com.mb.nzbair.providers.domain.UsenetPost;
import com.mb.nzbair.providers.interfaces.DetailsCallback;
import com.mb.nzbair.remote.domain.HttpRequestComplete;

/*
 * Basic implementation of a details provider. Default implementation
 */
public class DetailsProxy extends BaseProxy<DetailsCallback> {

	public DetailsProxy(String providerId, String apiMethod, int appVersion) {

		super(providerId, apiMethod, appVersion);
	}

	public void requestPostDetails(UsenetPost post) {
		/* just return the same data */
		for (final DetailsCallback callback : this.getListeners()) {
			callback.responseDetails(post, null);
		}
	}

	@Override
	public void downloadComplete(HttpRequestComplete rc) {

	}

}
