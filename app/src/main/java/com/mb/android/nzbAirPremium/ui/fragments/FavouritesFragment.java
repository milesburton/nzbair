
package com.mb.android.nzbAirPremium.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.ui.DetailsActivity;
import com.mb.android.nzbAirPremium.ui.helper.BookmarkHelper;
import com.mb.android.nzbAirPremium.ui.helper.ContextHelper;
import com.mb.android.nzbAirPremium.ui.helper.ThrowableHelper;
import com.mb.android.nzbAirPremium.ui.listAdapters.FavouritesListAdapter;
import com.mb.android.nzbAirPremium.ui.listAdapters.IListDataSource;
import com.mb.android.ui.listeners.OnCustomClickListener;
import com.mb.nzbair.providers.Provider;
import com.mb.nzbair.providers.domain.UsenetPost;
import com.mb.nzbair.providers.domain.UsenetPostResult;
import com.mb.nzbair.providers.interfaces.FavouritesCallback;

public class FavouritesFragment extends BasePosts implements OnCustomClickListener<UsenetPost>, FavouritesCallback {

	private static final String TAG = FavouritesFragment.class.getName();

	public static final String LOAD_FAVOURITES = "0";

	public static FavouritesFragment getInstance(Provider provider) {
		final FavouritesFragment frag = new FavouritesFragment();
		frag.directLoad = true;
		frag.provider = provider;
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		setListAdapter(new FavouritesListAdapter(getActivity(), this));
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected void detachListeners() {
		if (provider != null && provider.getFavouritesService() != null) {
			provider.getFavouritesService().removeListener(this);
		}
		super.detachListeners();
	}

	@Override
	protected void attachListeners() {
		if (provider != null && provider.getFavouritesService() != null) {
			provider.getFavouritesService().addListener(this);
		}
		super.attachListeners();
	}

	@Override
	public void onResponseFavourites(UsenetPostResult posts, Throwable error) {
		loadPostResponseFromService(posts, error);
	}

	@Override
	public void onResponseRemoveFavourite(UsenetPost post, final Boolean result, final Throwable error) {
		favouriteResponse(result, "Bookmark removed", "Already removed", error);
	}

	private void favouriteResponse(final boolean response, final String positive, final String negative, final Throwable error) {
		guiThread.post(new Runnable() {

			@Override
			public void run() {
				final Context con = ContextHelper.getContext(TAG, getActivity());
				if (con == null) {
					return;
				}

				String text = "";
				if (error != null) {

					ThrowableHelper.showError(con, error);
					return;
				}
				if (response) {
					text = positive;
				} else {
					text = negative;
				}

				try {
					Toast.makeText(con, text, Toast.LENGTH_SHORT).show();
				} catch (final NullPointerException ex) {
					Log.e(TAG, "Context is missing");
					ex.printStackTrace();
				}
			}
		});
	}

	@Override
	public void OnClick(View aView, int position, UsenetPost item) {
		final Context con = ContextHelper.getContext(TAG, getActivity());
		if (con == null) {
			return;
		}
		try {
			switch (aView.getId()) {
			case R.id.DeleteItem:

				getListAdapter().getModel().remove(item);
				getListAdapter().notifyDataSetChanged();
				provider.getFavouritesService().requestRemoveFavourite(item);
				BookmarkHelper.removeBookmark(guiThread, con, item);
				break;
			case R.id.row_favourite:

				final Intent i = new Intent(con, DetailsActivity.class);
				i.putExtra(DetailsActivity.EXTRA_POST, item);
				i.putExtra(DetailsActivity.EXTRA_PROVIDER_ID, provider.getId());
				startActivity(i);
				break;
			}
		} catch (final Exception ex) {
			Log.e(TAG, "Could not get UsenetPost: " + ex.toString());
			ex.printStackTrace();
		}

	}

	@Override
	protected void createDataSource() {

		datasource = new IListDataSource() {

			@Override
			public void requestData(int offset, int limit) {
				setLoadingFooterVisibility(true);
				provider.getFavouritesService().requestFavourites(offset, limit);
			}
		};
	}

	@Override
	public String getKeywords() {

		return null;
	}

	@Override
	public void onResponseAddFavourite(UsenetPost post, Boolean response, Throwable error) {
	}

	@Override
	public void OnLongClick(View aView, int position, UsenetPost payload) {
	}

	@Override
	protected void onSetTitle(ActionBar actionbar) {
		if (provider != null) {
			actionbar.setTitle(provider.getName() + " Favourites");
		}
	}
}
