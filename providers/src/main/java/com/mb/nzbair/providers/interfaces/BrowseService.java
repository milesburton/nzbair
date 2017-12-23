
package com.mb.nzbair.providers.interfaces;

import com.mb.nzbair.providers.domain.category.Category;

public interface BrowseService {

	void search(String keywords, Category category, int offset, int limit);

	void browse(Category aCategory, int offset, int limit);

	void addListener(BrowseCallback callback);

	void removeListener(BrowseCallback callback);
}
