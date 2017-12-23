
package com.mb.android.nzbAirPremium.ui.listAdapters;

import com.mb.nzbair.providers.domain.UsenetPostResult;

public interface IListDataSourceCallback {

	void onResponseRequestPosts(UsenetPostResult posts, Throwable error);
}
