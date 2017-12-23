
package com.mb.nzbair.providers;

import java.util.List;

import com.mb.nzbair.providers.base.BaseProvider.NoSuchCategoryException;
import com.mb.nzbair.providers.base.DetailsProxy;
import com.mb.nzbair.providers.domain.category.Category;
import com.mb.nzbair.providers.interfaces.BrowseService;
import com.mb.nzbair.providers.interfaces.FavouritesService;

public interface Provider {

	public abstract Category getCategory(String id) throws NoSuchCategoryException;

	public abstract String getId();

	public abstract BrowseService getBrowseService();

	public abstract FavouritesService getFavouritesService();

	public abstract List<Category> getCategories();

	public abstract Category getProviderCategory();

	public abstract DetailsProxy getDetailsService();

	public abstract String getName();

}