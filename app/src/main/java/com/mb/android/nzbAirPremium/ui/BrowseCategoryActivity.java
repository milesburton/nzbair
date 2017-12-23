
package com.mb.android.nzbAirPremium.ui;

import android.os.Bundle;
import android.util.Log;

import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.ui.fragments.BrowseMetadata;
import com.mb.nzbair.providers.Provider;

public class BrowseCategoryActivity extends BaseBrowse {

	final String TAG = BrowseCategoryActivity.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse_category);
	}

	@Override
	protected void onStart() {
		try {
			fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_browse_category);

			final BrowseMetadata md = (BrowseMetadata) fragment;

			if (md != null) {
				if (md.getCategory() != null) {
					getSupportActionBar().setTitle(md.getProvider().getName() + " > " + md.getCategory().getTitle());
				} else {
					final Provider p = md.getProvider();
					getSupportActionBar().setTitle(p.getName());
				}
			} else {
				Log.e(TAG, "findFragmentById cannot find browse-category");
			}
		} catch (final NullPointerException ex) {
			Log.e(TAG, "Failed to setup action bar");
			ex.printStackTrace();
		}

		super.onStart();
	}
}
