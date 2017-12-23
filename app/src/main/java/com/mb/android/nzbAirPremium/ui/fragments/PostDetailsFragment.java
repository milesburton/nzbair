
package com.mb.android.nzbAirPremium.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.ui.helper.UsenetPostConverter;
import com.mb.android.nzbAirPremium.ui.listAdapters.KeyValueAdapter;
import com.mb.nzbair.providers.domain.UsenetPost;
import com.mb.nzbair.providers.interfaces.DetailsCallback;

public class PostDetailsFragment extends ListFragment implements DetailsCallback, FragmentMetadata {

	private static final String TAG = PostDetailsFragment.class.getCanonicalName();
	private Handler guiThread;
	private KeyValueAdapter adapter;

	private UsenetPost post;

	private static String EXTRA_POST = "post";

	public static Fragment getInstance(UsenetPost payload) {
		final PostDetailsFragment fragment = new PostDetailsFragment();
		fragment.post = payload;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.fragment_pager_list_post_details, container, false);

		guiThread = new Handler();
		adapter = new KeyValueAdapter(getActivity());
		setListAdapter(adapter);

		if (resultsAreCached(savedInstanceState)) {
			post = (UsenetPost) savedInstanceState.getSerializable(EXTRA_POST);
		}
		if (post != null) {
			updateAndRenderPost();
		}

		return v;
	}

	@Override
	public void responseDetails(final UsenetPost post, final Throwable error) {
		this.post = post;

		if (guiThread == null) {
			return;
		}
		guiThread.post(new Runnable() {

			@Override
			public void run() {
				updateAndRenderPost();
			}
		});
	}

	private void updateAndRenderPost() {
		if (post != null) {
			adapter.setList(UsenetPostConverter.convert(post));
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		try {
			if (this.post != null) {
				outState.putSerializable(EXTRA_POST, this.post);
			}
		} catch (final Exception ex) {
			Log.e(TAG, ex.toString());
			ex.printStackTrace();
		}
		super.onSaveInstanceState(outState);
	}

	protected boolean resultsAreCached(Bundle inState) {
		return !(inState == null || inState.getSerializable(EXTRA_POST) == null);
	}

	@Override
	public String getTitle() {

		return "Details";
	}

}
