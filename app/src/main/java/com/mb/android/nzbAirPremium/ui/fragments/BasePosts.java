
package com.mb.android.nzbAirPremium.ui.fragments;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.app.Air;
import com.mb.android.nzbAirPremium.ui.DetailsActivity;
import com.mb.android.nzbAirPremium.ui.helper.ContextHelper;
import com.mb.android.nzbAirPremium.ui.helper.SABHelper;
import com.mb.android.nzbAirPremium.ui.listAdapters.IListDataSource;
import com.mb.android.nzbAirPremium.ui.listAdapters.IPostListAdapter;
import com.mb.android.ui.listeners.OnCustomClickListener;
import com.mb.nzbair.providers.Provider;
import com.mb.nzbair.providers.ProviderFactory;
import com.mb.nzbair.providers.ProviderFactory.InvalidProviderException;
import com.mb.nzbair.providers.domain.UsenetPost;
import com.mb.nzbair.providers.domain.UsenetPostResult;
import com.mb.nzbair.providers.domain.category.Category;
import com.mb.nzbair.seh.ProviderException;

/**
 * Responsible for displaying a list of posts provided by a search or category
 * listing
 */
public abstract class BasePosts extends SherlockFragment implements OnCustomClickListener<UsenetPost>, OnScrollListener, BrowseMetadata, Refreshable {

	private static final String TAG = BasePosts.class.getName();
	protected Handler guiThread;

	public static final String EXTRA_POSTS = "posts";
	public static final String EXTRA_CATEGORY = "category";
	public static final String EXTRA_PROVIDER_ID = "provider";

	protected Provider provider;
	protected UsenetPostResult model = new UsenetPostResult();
	protected boolean directLoad = false;
	private IPostListAdapter listAdapter;
	protected Category category;
	private final int retryFailedLimit = 5;
	private int retryCurrent = 0;

	protected IListDataSource datasource = null;
	private View loadingView = null;

	boolean loading = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		final View v = inflater.inflate(R.layout.layout_list_footer, container, false);

		guiThread = new Handler();

		final ListView lv = (ListView) v.findViewById(android.R.id.list);
		loadingView = inflater.inflate(R.layout.listview_footer_loading, lv, false);
		lv.addFooterView(loadingView, null, false);

		return v;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		try {
			getStateFromBundle(savedInstanceState != null ? savedInstanceState : getActivity().getIntent().getExtras());
			createDataSource();
			attachListeners();
			setupListView();

			if (resultsAreCached(savedInstanceState)) {
				onRestoreInstanceState(savedInstanceState);
			} else {
				datasource.requestData(model.getOffset(), model.getLimit());

			}

			setTitle();

		} catch (final NullPointerException ex) {
			setFooterText("Web-Service error. Please try again");
			Log.e(TAG, "Error processing intent. Captured error");
			ex.printStackTrace();
		}
	}

	private void setupListView() {
		final ListView lv = (ListView) getView().findViewById(android.R.id.list);
		listAdapter.setModel(model.getPosts());
		lv.setAdapter(listAdapter);
		lv.setOnScrollListener(this);
	}

	protected abstract void createDataSource();

	protected void getStateFromBundle(Bundle savedState) {
		try {
			if (directLoad) {
				return;
			}

			if (savedState != null) {
				if (savedState.containsKey(EXTRA_CATEGORY)) {
					this.category = (Category) savedState.getSerializable(EXTRA_CATEGORY);
				}

				if (savedState.containsKey(EXTRA_PROVIDER_ID)) {
					final String providerId = savedState.getString(EXTRA_PROVIDER_ID);
					this.provider = ProviderFactory.getInstance().getProvider(providerId);
				}
			}
			if (this.provider == null) {
				this.provider = ProviderFactory.getInstance().getDefaultProvider();
			}

		} catch (final InvalidProviderException ex) {
			Log.e(TAG, "Could not find requested provider");
			Toast.makeText(Air.get().getApplicationContext(), "Could not find a search provider. Please check your configuration.", Toast.LENGTH_LONG).show();
			getActivity().finish();
		}
	}

	protected boolean resultsAreCached(Bundle inState) {
		return !(inState == null || inState.getSerializable(EXTRA_POSTS) == null);
	}

	private void setFooterText(String text) {
		final View v = ContextHelper.getView(TAG, this);
		if (v == null) {
			return;
		}
		final TextView footer = (TextView) v.findViewById(R.id.footer);
		footer.setText(text);
	}

	private void updateList(final UsenetPostResult posts) {
		retryCurrent = 0;
		model.setTotalresults(posts.getTotalresults());
		model.setLimit(posts.getLimit());
		model.setOffset(posts.getOffset());
		model.getPosts().addAll(posts.getPosts());
		listAdapter.notifyDataSetChanged();

		if (posts.getPosts().size() == 0) {
			setFooterText("No posts");
		} else {
			setFooterText("Showing " + listAdapter.getModel().size() + " results of " + model.getTotalresults());
		}
	}

	boolean shouldRetryFetchingPosts() {
		return retryCurrent < retryFailedLimit;
	}

	void retryIfNoPosts() {
		if (shouldRetryFetchingPosts()) {
			retryCurrent++;
			setFooterText("Translation error. Autorefreshing...");
			datasource.requestData(model.getOffset(), model.getLimit());
		} else {
			setFooterText("Translation error");
			retryCurrent = 0;
		}
	}

	protected void loadPostResponseFromService(final UsenetPostResult posts, final Throwable error) {
		guiThread.post(new Runnable() {

			@Override
			public void run() {
				setLoadingFooterVisibility(false);
				renderThrowPostsException(error);

				if (posts != null) {
					updateList(posts);
				}

				if (error == null && posts == null) {
					retryIfNoPosts();
				}

			}
		});
	}

	void renderThrowPostsException(Throwable error) {
		if (error == null) {
			return;
		}

		try {
			throw error;

		} catch (final MalformedURLException ex) {
			setFooterText("Bad URL");
		} catch (final IOException ex) {
			setFooterText("Connection Error");
		} catch (final ProviderException ex) {
			setFooterText(ex.getProviderErrorString());
		} catch (final Throwable ex) {
			setFooterText("Unknown error");
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		try {
			if (this.model != null) {
				outState.putSerializable(EXTRA_POSTS, this.model);
			}
			outState.putSerializable(EXTRA_CATEGORY, this.category);
			outState.putString(EXTRA_PROVIDER_ID, this.provider.getId());
		} catch (final Exception ex) {
			Log.e(TAG, ex.toString());
			ex.printStackTrace();
		}
		super.onSaveInstanceState(outState);
	}

	protected void onRestoreInstanceState(Bundle inState) {
		if (resultsAreCached(inState)) {
			try {
				final UsenetPostResult posts = (UsenetPostResult) inState.getSerializable(EXTRA_POSTS);
				this.category = (Category) inState.getSerializable(EXTRA_CATEGORY);
				getStateFromBundle(inState);
				updateList(posts);
			} catch (final Exception ex) {
				ex.printStackTrace();
				this.loadPostResponseFromService(null, ex);
			}

		}
	}

	@Override
	public void OnClick(View aView, int position, UsenetPost payload) {
		final Context con = ContextHelper.getContext(TAG, getActivity());
		if (con == null) {
			return;
		}
		final Intent i = new Intent().setClass(con, DetailsActivity.class);
		if (payload == null || provider == null) {
			return;

		}
		i.putExtra(DetailsActivity.EXTRA_POST, payload);
		i.putExtra(DetailsActivity.EXTRA_PROVIDER_ID, provider.getId());
		startActivity(i);

	}

	@Override
	public void OnLongClick(View aView, int position, UsenetPost payload) {
		final Context con = ContextHelper.getActivity(TAG, this);
		if (con == null || payload == null) {
			return;
		}
		final SABHelper helper = new SABHelper(con, guiThread);
		helper.addByUrlRequest(payload);
	}

	@Override
	public void OnTouch(View aView, int position, UsenetPost payload) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		final int lastItem = (firstVisibleItem + visibleItemCount);
		if (!loading && (lastItem == totalItemCount) && this.model.getTotalresults() > totalItemCount) {
			datasource.requestData(lastItem, this.model.getLimit());
		}

	}

	protected void setLoadingFooterVisibility(boolean isVisible) {
		final View v = ContextHelper.getView(TAG, this);
		if (v == null) {
			return;
		}

		loading = isVisible;
		final View footerView = getView().findViewById(R.id.loadingfooter);

		if (isVisible) {
			setFooterText("Requesting data from remote service...");
			footerView.setVisibility(View.VISIBLE);
		} else {
			footerView.setVisibility(View.GONE);
		}

	}

	@Override
	public void refresh() {
		try {
			model.setPosts(new ArrayList<UsenetPost>());
			listAdapter.setModel(model.getPosts());
			listAdapter.notifyDataSetChanged();
			datasource.requestData(0, model.getLimit());
		} catch (final Exception ex) {
			Log.e(TAG, "Couldn't refresh");
			ex.printStackTrace();
		}
	}

	private void setTitle() {
		final ActionBar actionBar = this.getSherlockActivity().getSupportActionBar();
		onSetTitle(actionBar);
	}

	protected abstract void onSetTitle(ActionBar actionbar);

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onResume() {
		attachListeners();
		super.onResume();
	}

	@Override
	public void onPause() {
		detachListeners();
		super.onPause();
	}

	@Override
	public void onStop() {
		detachListeners();
		super.onStop();
	}

	protected void attachListeners() {
	}

	protected void detachListeners() {
	};

	@Override
	public Category getCategory() {

		return category;
	}

	@Override
	public Provider getProvider() {

		return provider;
	}

	@Override
	public UsenetPost getPost() {
		return null;
	}

	public UsenetPostResult getModel() {
		return model;
	}

	public IPostListAdapter getListAdapter() {
		return listAdapter;
	}

	public void setListAdapter(IPostListAdapter adapter) {
		listAdapter = adapter;
	}

}