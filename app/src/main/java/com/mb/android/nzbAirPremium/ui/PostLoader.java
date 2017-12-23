
package com.mb.android.nzbAirPremium.ui;

import com.mb.nzbair.providers.domain.category.Category;

public interface PostLoader {

	void loadPosts(String providerId, Category category);
}
