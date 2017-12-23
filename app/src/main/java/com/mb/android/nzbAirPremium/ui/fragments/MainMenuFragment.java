
package com.mb.android.nzbAirPremium.ui.fragments;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.app.Air;
import com.mb.android.nzbAirPremium.download.DownloadCategory;
import com.mb.android.nzbAirPremium.ui.BrowseCategoryActivity;
import com.mb.android.nzbAirPremium.ui.BrowseSavedCategoryActivity;
import com.mb.android.nzbAirPremium.ui.CategoryLoader;
import com.mb.android.nzbAirPremium.ui.DownloadsActivity;
import com.mb.android.nzbAirPremium.ui.helper.ContextHelper;
import com.mb.android.nzbAirPremium.ui.listAdapters.BasicCategoryAdapter;
import com.mb.android.preferences.domain.Config;
import com.mb.android.preferences.manager.OnConfigLoadedListener;
import com.mb.android.ui.listeners.OnCustomClickListener;
import com.mb.nzbair.providers.ProviderFactory;
import com.mb.nzbair.providers.domain.category.Category;
import com.mb.nzbair.providers.domain.category.FavouritesCategory;
import com.mb.nzbair.providers.domain.category.SearchCategory;

public class MainMenuFragment extends Fragment implements OnCustomClickListener<Category>, OnConfigLoadedListener {

	static final String TAG = MainMenuFragment.class.getName();

	private List<Category> categories;
	private BasicCategoryAdapter adapter;
	private CategoryLoader catLoader = null;

	public MainMenuFragment() {
		Air.get().getConfigManager().listenForConfigLoaded(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		final View v = inflater.inflate(R.layout.layout_main_menu, container, false);

		adapter = new BasicCategoryAdapter(getActivity(), this, null);
		final ListView aView = (ListView) v.findViewById(android.R.id.list);
		aView.setAdapter(adapter);
		return v;
	}

	@Override
	public void onStart() {
		super.onStart();
		loadCategories();
	}

	private void loadCategories() {
		final ProviderFactory prov = ProviderFactory.getInstance();
		categories = prov.getProvidersAsCategories();
		final DownloadCategory cat = new DownloadCategory();
		categories.add(cat);
		adapter.setModel(categories);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void OnClick(View aView, int position, Category category) {
		showCategories(category, position);
	}

	/* Figures out whether to launch an activity or show in another pane */
	private void showCategories(Category category, int position) {
		final Activity a = ContextHelper.getActivity(TAG, this);
		final View v = ContextHelper.getView(TAG, this);
		if (a == null || v == null) {
			return;
		}

		if (v != null) {
			((ListView) v.findViewById(android.R.id.list)).setItemChecked(position, true);
		}

		// Load Downloads Actiivty
		if (category instanceof DownloadCategory) {
			startActivity(new Intent(a, DownloadsActivity.class));
			return;
		}

		if (catLoader != null) {
			// Let parent activity load the fragment
			catLoader.loadCategory(category);
		} else {
			// Launch as activity
			Intent activity = null;
			if ((category instanceof SearchCategory) || (category instanceof FavouritesCategory)) {
				activity = new Intent(a, BrowseSavedCategoryActivity.class);
			} else {
				activity = new Intent(a, BrowseCategoryActivity.class);
			}

			activity.setAction(BrowseCategoryFragment.VIEW_CATEGORY);
			activity.putExtra(BrowseCategoryFragment.EXTRA_PROVIDER_ID, category.getProviderId());
			startActivity(activity);
		}
	}

	@Override
	public void OnLongClick(View aView, int position, Category payload) {

	}

	@Override
	public void OnTouch(View aView, int position, Category payload) {

	}

	public CategoryLoader getCatLoader() {
		return catLoader;
	}

	public void setCatLoader(CategoryLoader catLoader) {
		this.catLoader = catLoader;
	}

	@Override
	public void configLoaded(Map<String, Config> configuration) {
		loadCategories();
	}

}
