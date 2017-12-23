
package com.mb.android.nzbAirPremium.favourites;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;

import com.mb.android.nzbAirPremium.db.DbEntry;
import com.mb.android.nzbAirPremium.db.SaveService;
import com.mb.nzbair.providers.base.BaseProvider;
import com.mb.nzbair.providers.domain.category.Category;
import com.mb.nzbair.providers.domain.category.SearchCategory;
import com.mb.nzbair.providers.interfaces.BrowseService;
import com.mb.nzbair.providers.interfaces.FavouritesService;

public class SaveSearchProvider extends BaseProvider {

	public static final String BUCKET_NAME = "savesearchb";

	private final Context context;

	public SaveSearchProvider(Context context) {
		super("", "");
		this.context = context;
	}

	@Override
	public BrowseService getBrowseService() {
		return null;
	}

	@Override
	public List<Category> getCategories() {

		final List<Category> categories = new ArrayList<Category>();
		final SaveService db = new SaveService(context);
		final List<DbEntry> list = db.getEntries(BUCKET_NAME);

		for (final DbEntry e : list) {
			final SearchCategory s = new SearchCategory();
			s.setTitle(e.getKey());
			// liable to cause problems if the provider isn't ready yet
			s.setProvider(e.getExtra());
			s.setSubcatid(e.getValue());
			categories.add(s);
		}

		Collections.sort(categories, new CategoryComparer());

		return categories;

	}

	@Override
	public FavouritesService getFavouritesService() {

		return null;
	}

	@Override
	public String getId() {

		return BUCKET_NAME;
	}

	@Override
	public String getName() {

		return "Saved Searches";
	}

	@Override
	public Category getCategory(String id) {

		return null;
	}

	@Override
	public Category getProviderCategory() {
		final SearchCategory aCategory = new SearchCategory(getId(), getName());
		aCategory.setProvider(getId());

		return aCategory;
	}

}
