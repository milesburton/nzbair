
package com.mb.android.nzbAirPremium.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.ui.BrowsePostsActivity;
import com.mb.android.nzbAirPremium.ui.FavouritesActivity;
import com.mb.android.nzbAirPremium.ui.FavouritesLoader;
import com.mb.android.nzbAirPremium.ui.PostLoader;
import com.mb.android.nzbAirPremium.ui.helper.ContextHelper;
import com.mb.android.nzbAirPremium.ui.listAdapters.BasicCategoryAdapter;
import com.mb.android.ui.listeners.OnCustomClickListener;
import com.mb.nzbair.providers.Provider;
import com.mb.nzbair.providers.ProviderFactory;
import com.mb.nzbair.providers.ProviderFactory.InvalidProviderException;
import com.mb.nzbair.providers.base.BaseProvider.NoSuchCategoryException;
import com.mb.nzbair.providers.domain.UsenetPost;
import com.mb.nzbair.providers.domain.category.BranchCategory;
import com.mb.nzbair.providers.domain.category.Category;
import com.mb.nzbair.providers.domain.category.FavouritesCategory;
import com.mb.nzbair.providers.domain.category.ParentCategory;

public class BrowseCategoryFragment extends SherlockFragment implements OnCustomClickListener<Category>, BrowseMetadata, Refreshable {

	private static final String TAG = BrowseCategoryFragment.class.getName();

	private Category rootCategory = null;
	protected static List<Category> categories = new ArrayList<Category>();

	public static final String VIEW_CATEGORY = "1";

	public static final String EXTRA_PROVIDER_ID = "provider";
	public static final String EXTRA_CATEGORIES = "category";

	protected int selectedItem = 0;

	private BasicCategoryAdapter adapter;
	protected Provider provider;

	protected boolean directLoad = false;
	protected PostLoader postLoader = null;
	protected FavouritesLoader favLoader = null;
	private Category startupCategory = null;

	public static BrowseCategoryFragment getInstance(PostLoader postLoader, FavouritesLoader favLoader, Provider provider, Category startupCategory) throws InvalidProviderException, NoSuchCategoryException {
		final BrowseCategoryFragment frag = new BrowseCategoryFragment();
		frag.directLoad = true;
		frag.provider = provider;
		frag.postLoader = postLoader;
		frag.favLoader = favLoader;
		if (startupCategory != null) {
			frag.startupCategory = startupCategory;

		}

		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		super.onCreateView(inflater, container, savedInstanceState);
		View v = null;
		try {
			v = inflater.inflate(R.layout.layout_main_menu, container, false);

			adapter = new BasicCategoryAdapter(getActivity(), this, this);

			final ListView aView = (ListView) v.findViewById(android.R.id.list);
			aView.setAdapter(adapter);

		} catch (final Exception ex) {
			Log.e(TAG, "Crashed starting browseCategoryActivity." + ex.toString());
			ex.printStackTrace();
		}

		return v;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		try {
			if (!directLoad && savedInstanceState == null) {
				provider = ProviderFactory.getInstance().getProvider(getActivity().getIntent().getExtras().getString(EXTRA_PROVIDER_ID));
			}

			if (provider == null) {
				provider = ProviderFactory.getInstance().getDefaultProvider();
			}

		} catch (final InvalidProviderException e) {
			Log.e(TAG, "Failed to get provider");
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);

		if (resultsAreCached(savedInstanceState)) {
			onRestoreInstanceState(savedInstanceState);
		} else if (startupCategory != null) {
			categories = startupCategory.getChildren();
			rootCategory = startupCategory;
		} else {
			categories = provider.getCategories();
		}

		loadCategories();
	}

	protected void loadCategories() {
		
		adapter.setModel(categories);
		adapter.notifyDataSetChanged();
	}

	private void showParentCategory() {
		
		if (rootCategory == null) {
			return;
		}
		showCategories(rootCategory.getSiblings(), rootCategory.getParent(), 0);
	}

	private void showCategories(List<Category> categories, Category parent, int position) {
		
		rootCategory = parent;
		BrowseCategoryFragment.categories = categories;
		loadCategories();
	}

	protected void showPosts(Category category, int position) {
		
		final Context con = ContextHelper.getContext(TAG, getActivity());
		final Activity a = ContextHelper.getActivity(TAG, this);
		final View v = ContextHelper.getView(TAG, this);
		if (con == null || a == null || v == null) {
			return;
		}

		if (postLoader == null) {
			final Intent i = new Intent(con, BrowsePostsActivity.class);
			i.putExtra(BasePosts.EXTRA_CATEGORY, category);
			i.putExtra(BasePosts.EXTRA_PROVIDER_ID, category.getProviderId());
			i.setAction(BrowsePostsFragment.LOAD_CATEGORY);
			startActivity(i);

		} else {
			postLoader.loadPosts(category.getProviderId(), category);
		}

	}

	private void showFavourites(int position) {
		
		final Context con = ContextHelper.getContext(TAG, getActivity());
		final Activity a = ContextHelper.getActivity(TAG, this);
		final View v = ContextHelper.getView(TAG, this);
		if (con == null || a == null || v == null) {
			return;
		}

		if (favLoader == null) {
			final Intent i = new Intent(con, FavouritesActivity.class);
			i.setAction(FavouritesFragment.LOAD_FAVOURITES); // TODO
			i.putExtra("provider", this.provider.getId());
			startActivity(i);
		} else {
			favLoader.loadFavourites(provider);
		}
	}

	@Override
	public void OnClick(View aView, int position, Category payload) {
		
		try {
			final Category category = payload;

			if (category instanceof FavouritesCategory) {
				showFavourites(position);
			} else if (category instanceof ParentCategory) {
				showParentCategory();

			} else if (!category.hasChildren() || category instanceof BranchCategory) {
				showPosts(category, position);
			} else {
				showCategories(category.getChildren(), category, position);
			}

		} catch (final Exception ex) {
			Log.e(TAG, ex.toString());
			ex.printStackTrace();
		}
	}

	@Override
	public void OnLongClick(View aView, int position, Category payload) {

	}

	@Override
	public void OnTouch(View aView, int position, Category payload) {

		selectedItem = position;
	}

	protected void onRestoreInstanceState(Bundle inState) {
		
		if (resultsAreCached(inState)) {
			try {
				if (inState.containsKey(EXTRA_CATEGORIES)) {
					final Category[] cats = (Category[]) inState.getSerializable(EXTRA_CATEGORIES);
					for (final Category cat : cats) {
						adapter.getModel().add(cat);
					}
				}

				if (inState.containsKey(EXTRA_PROVIDER_ID)) {
					provider = ProviderFactory.getInstance().getProvider(inState.getString(EXTRA_PROVIDER_ID));
				}
			} catch (final Exception ex) {
				ex.printStackTrace();
			}

		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		
		try {
			Category[] cats = new Category[adapter.getModel().size()];
			cats = adapter.getModel().toArray(cats);
			outState.putSerializable(EXTRA_CATEGORIES, cats);
			outState.putString(EXTRA_PROVIDER_ID, provider.getId());
		} catch (final Exception ex) {
			Log.e(TAG, ex.toString());
			ex.printStackTrace();
		}
		super.onSaveInstanceState(outState);
	}

	protected boolean resultsAreCached(Bundle inState) {
		return !(inState == null || inState.getSerializable(EXTRA_CATEGORIES) == null);
	}

	@Override
	public Category getCategory() {
		return rootCategory;
	}

	@Override
	public Provider getProvider() {
		return provider;
	}

	@Override
	public UsenetPost getPost() {

		return null;
	}

	@Override
	public String getKeywords() {

		return null;
	}

	@Override
	public void refresh() {
		categories = provider.getCategories();
		loadCategories();
	}

}
