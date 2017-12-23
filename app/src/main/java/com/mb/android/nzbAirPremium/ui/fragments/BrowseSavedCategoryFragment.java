
package com.mb.android.nzbAirPremium.ui.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;
import com.mb.android.nzbAirPremium.db.CategorySaveService;
import com.mb.android.nzbAirPremium.ui.FavouritesLoader;
import com.mb.android.nzbAirPremium.ui.PostLoader;
import com.mb.android.nzbAirPremium.ui.SearchLoader;
import com.mb.android.nzbAirPremium.ui.SearchPostsActivity;
import com.mb.android.nzbAirPremium.ui.helper.ContextHelper;
import com.mb.nzbair.providers.Provider;
import com.mb.nzbair.providers.ProviderFactory;
import com.mb.nzbair.providers.ProviderFactory.InvalidProviderException;
import com.mb.nzbair.providers.base.BaseProvider.NoSuchCategoryException;
import com.mb.nzbair.providers.domain.category.Category;
import com.mb.nzbair.providers.domain.category.FavouritesCategory;
import com.mb.nzbair.providers.domain.category.SearchCategory;

public class BrowseSavedCategoryFragment extends BrowseCategoryFragment implements Refreshable, OnCreateContextMenuListener {

	static final String TAG = BrowseSavedCategoryFragment.class.getName();

	SearchLoader searchLoader = null;
	CategorySaveService service = null; // used for contexual menu

	public static BrowseSavedCategoryFragment getInstance(PostLoader postLoader, FavouritesLoader favLoader, SearchLoader searchLoader, String providerId) throws InvalidProviderException {
		
		final BrowseSavedCategoryFragment frag = new BrowseSavedCategoryFragment();
		frag.directLoad = true;
		frag.provider = ProviderFactory.getInstance().getProvider(providerId);
		frag.postLoader = postLoader;
		frag.searchLoader = searchLoader;
		return frag;
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		service = new CategorySaveService(getActivity());
	}

	/* Content Menu Options */
	private static final int ContextMenuRemoveFavourite = 1;

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		
		try {
			menu.add(0, ContextMenuRemoveFavourite, 0, "Remove Favourite");
		} catch (final Exception ex) {
			Log.e(TAG, "Context menu failed to populate. " + ex.toString());
			ex.printStackTrace();
		}
	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		final Context con = ContextHelper.getContext(TAG, getActivity());
		final View v = ContextHelper.getView(TAG, this);
		if (con == null || v == null) {
			return false;
		}

		switch (item.getItemId()) {
		case ContextMenuRemoveFavourite:
			try {
				final ListView aView = (ListView) v.findViewById(android.R.id.list);
				final Category aCategory = (Category) aView.getItemAtPosition(selectedItem);

				if (aCategory instanceof FavouritesCategory) {
					service.toggleSaveBookmark(aCategory, aCategory.getProviderId());
				} else if (aCategory instanceof SearchCategory) {
					service.toggleSaveSearch(aCategory, aCategory.getProviderId(), aCategory.getTitle());
				}
				categories = provider.getCategories();
				loadCategories();
				return true;
			} catch (final Exception ex) {
				Toast.makeText(con, "Failed to remove bookmark", Toast.LENGTH_SHORT).show();
				ex.printStackTrace();
			}
		}

		return true;
	}

	@Override
	public void OnClick(View aView, int position, Category category) {
		
		try {
			if (category instanceof SearchCategory) {
				showSearchCategory(category);
			} else if (category instanceof FavouritesCategory) {
				showPosts(category, position);
			}

		} catch (final Exception ex) {
			ex.printStackTrace();
			Log.e(TAG, ex.toString());
		}
	}

	// TODO refactor this
	void showSearchCategory(Category savedCategory) {

		final String keywords = savedCategory.getTitle();
		final Category originalSearchCategory = getOriginalSearchCategory(savedCategory, savedCategory.getProviderId());
		if (searchLoader != null) {
			final Provider provider = ProviderFactory.getInstance().getProvider(savedCategory.getProviderId());
			searchLoader.loadSearch(originalSearchCategory, provider, keywords);
		} else {
			launchSearchActivity(keywords, savedCategory.getProviderId(), originalSearchCategory);
		}
	}

	Category getOriginalSearchCategory(Category category, String providerId) {
		if (category.getId() != null && !category.getId().equals("")) {

			// A search category has a category ID. It relates to the
			// original category which was searched within.
			// We need to fetch the provider id from the search category
			// (The search provider, aka NZBMatrix)
			// Using the provider we request a copy of the original
			// category (if it exists)
			// Set the category of the search and start intent.

			// Phew

			// This is bloody long!
			try {
				return ProviderFactory.getInstance().getProvider(providerId).getCategory(category.getId());
			} catch (final NoSuchCategoryException e) {
				e.printStackTrace();
			} catch (final InvalidProviderException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	void launchSearchActivity(String keywords, String providerId, Category originalSearchCategory) {
		final Context con = ContextHelper.getContext(TAG, getActivity());

		if (con == null) {
			return;
		}

		final Bundle searchBundle = new Bundle();
		if (originalSearchCategory != null) {
			searchBundle.putSerializable(BasePosts.EXTRA_CATEGORY, originalSearchCategory);
		}
		searchBundle.putString(BasePosts.EXTRA_PROVIDER_ID, providerId);

		final Intent intent = new Intent(con, SearchPostsActivity.class);
		intent.setAction(Intent.ACTION_SEARCH);
		intent.putExtra(SearchManager.QUERY, keywords);
		intent.putExtra(SearchManager.APP_DATA, searchBundle);

		startActivity(intent);
	}
}
