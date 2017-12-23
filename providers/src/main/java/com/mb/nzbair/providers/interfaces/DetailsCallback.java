
package com.mb.nzbair.providers.interfaces;

import com.mb.nzbair.providers.domain.UsenetPost;

public interface DetailsCallback {

	void responseDetails(UsenetPost post, Throwable error);
}
