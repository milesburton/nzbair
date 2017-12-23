
package com.mb.android.nzbAirPremium.ui.fragments;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;
import com.mb.android.nzbAirPremium.ui.listAdapters.IListDataSource;
import com.mb.android.nzbAirPremium.ui.listAdapters.PostListAdapter;
import com.mb.nzbair.providers.Provider;
import com.mb.nzbair.providers.ProviderFactory.InvalidProviderException;
import com.mb.nzbair.providers.domain.UsenetPostResult;
import com.mb.nzbair.providers.domain.category.Category;
import com.mb.nzbair.providers.interfaces.BrowseCallback;

public class SearchPostsFragment extends BasePosts implements BrowseCallback {

	private static final String TAG = SearchPostsFragment.class.getName();

	String keywords = "";

	public static Fragment getInstance(Provider provider, String keywords, Category cat) throws InvalidProviderException {

		final SearchPostsFragment frag = new SearchPostsFragment();
		frag.directLoad = true;
		frag.provider = provider;
		frag.category = cat;
		frag.keywords = keywords;
		frag.directLoad = true;
		return frag;
	}

	@Override
	protected void onSetTitle(ActionBar actionbar) {
		if (provider == null) {
			return;
		}

		if (category != null) {
			actionbar.setTitle(keywords + " - " + category.getTitle() + " > " + provider.getName());
		} else {
			actionbar.setTitle(keywords + " - " + provider.getName());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		setListAdapter(new PostListAdapter(getActivity(), this));
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	protected void reattachProviderListeners() {

		if (provider.getBrowseService() != null) {
			provider.getBrowseService().addListener(this);
		}
	}

	@Override
	protected void getStateFromBundle(Bundle state) {

		if (!directLoad) {
			super.getStateFromBundle(state.getBundle(SearchManager.APP_DATA));

			if (state != null && state.containsKey(SearchManager.QUERY)) {
				keywords = state.getString(SearchManager.QUERY);
			} else if (getActivity().getIntent().hasExtra(SearchManager.QUERY)) {
				keywords = getActivity().getIntent().getStringExtra(SearchManager.QUERY);
			}
		}
	}

	@Override
	protected void detachListeners() {
		if (provider != null && provider.getBrowseService() != null) {
			provider.getBrowseService().removeListener(this);
		}
		super.detachListeners();
	}

	@Override
	protected void attachListeners() {

		if (provider != null && provider.getBrowseService() != null) {
			provider.getBrowseService().addListener(this);
		}
		super.attachListeners();
	}

	@Override
	protected void createDataSource() {

		datasource = new IListDataSource() {

			@Override
			public void requestData(int offset, int limit) {
				if (loading) {
					return;
				}
				setLoadingFooterVisibility(true);
				if (provider == null) {
					Log.e(TAG, "Provider is null!");
				}
				if (keywords == null) {
					Log.e(TAG, "Keywords are null");
				}
				provider.getBrowseService().search(keywords, category, offset, limit);
			}
		};

	}

	@Override
	public void onResponseSearch(UsenetPostResult posts, Throwable error) {
		loadPostResponseFromService(posts, error);
	}

	@Override
	public void onResponseCategory(UsenetPostResult postResult, Throwable error) {

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		try {
			if (this.keywords != null) {
				outState.putString(SearchManager.QUERY, this.keywords);
			}
		} catch (final Exception ex) {
			Log.e(TAG, ex.toString());
			ex.printStackTrace();
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	public String getKeywords() {

		return keywords;
	}

}
