
package com.mb.android.nzbAirPremium.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;
import com.mb.android.nzbAirPremium.ui.listAdapters.IListDataSource;
import com.mb.android.nzbAirPremium.ui.listAdapters.PostListAdapter;
import com.mb.nzbair.providers.ProviderFactory;
import com.mb.nzbair.providers.ProviderFactory.InvalidProviderException;
import com.mb.nzbair.providers.domain.UsenetPostResult;
import com.mb.nzbair.providers.domain.category.Category;
import com.mb.nzbair.providers.interfaces.BrowseCallback;

/**
 * Responsible for displaying a list of posts provided by a search or category
 * listing
 */
public class BrowsePostsFragment extends BasePosts implements BrowseCallback {

	public static final String LOAD_CATEGORY = "0";

	public static BrowsePostsFragment getInstance(Category category) throws InvalidProviderException {
		final BrowsePostsFragment frag = new BrowsePostsFragment();
		frag.directLoad = true;
		frag.provider = ProviderFactory.getInstance().getProvider(category.getProviderId());
		frag.category = category;
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		setListAdapter(new PostListAdapter(getActivity(), this));

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onResponseCategory(UsenetPostResult posts, Throwable error) {
		loadPostResponseFromService(posts, error);
	}

	@Override
	protected void onSetTitle(ActionBar actionbar) {
		if (provider == null) {
			return;
		}

		if (category != null) {
			actionbar.setTitle(provider.getName() + " > " + category.getTitle());
		} else {
			actionbar.setTitle(provider.getName());
		}
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
				provider.getBrowseService().browse(category, offset, limit);
			}
		};
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
	public String getKeywords() {
		return null;
	}

	@Override
	public void onResponseSearch(UsenetPostResult postResult, Throwable error) {

	}

}
