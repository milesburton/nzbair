
package com.mb.android.nzbAirPremium.ui.fragments;

import com.mb.nzbair.providers.Provider;
import com.mb.nzbair.providers.domain.UsenetPost;
import com.mb.nzbair.providers.domain.category.Category;

public interface BrowseMetadata {

	Category getCategory();

	Provider getProvider();

	UsenetPost getPost();

	String getKeywords();
}
