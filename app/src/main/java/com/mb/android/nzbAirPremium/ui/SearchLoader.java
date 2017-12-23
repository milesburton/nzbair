
package com.mb.android.nzbAirPremium.ui;

import com.mb.nzbair.providers.Provider;
import com.mb.nzbair.providers.domain.category.Category;

public interface SearchLoader {

	void loadSearch(Category category, Provider provider, String query);
}
