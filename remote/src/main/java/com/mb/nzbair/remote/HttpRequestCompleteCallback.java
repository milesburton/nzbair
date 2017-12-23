
package com.mb.nzbair.remote;

import com.mb.nzbair.remote.domain.HttpRequestComplete;

public interface HttpRequestCompleteCallback {

	void downloadComplete(HttpRequestComplete r);
}
