
package com.mb.android.nzbAirPremium.ui;

import android.os.Bundle;

import com.actionbarsherlock.view.MenuItem;
import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.ui.fragments.BrowseMetadata;

public class SearchPostsActivity extends BasePosts {

	final String TAG = SearchPostsActivity.class.getCanonicalName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_posts);
	}

	@Override
	protected void onStart() {

		fragment = this.getSupportFragmentManager().findFragmentById(R.id.fragment_search_posts);
		super.onStart();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.bookmarkButton:
			final BrowseMetadata md = (BrowseMetadata) fragment;
			saveService.toggleSaveSearch(md.getCategory(), md.getProvider().getId(), md.getKeywords());
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
