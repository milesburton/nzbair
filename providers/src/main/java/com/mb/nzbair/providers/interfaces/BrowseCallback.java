
package com.mb.nzbair.providers.interfaces;

import com.mb.nzbair.providers.domain.UsenetPostResult;

public interface BrowseCallback {

	void onResponseCategory(UsenetPostResult postResult, Throwable error);

	void onResponseSearch(UsenetPostResult postResult, Throwable error);
}
