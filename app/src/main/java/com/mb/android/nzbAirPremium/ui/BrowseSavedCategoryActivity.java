
package com.mb.android.nzbAirPremium.ui;

import android.os.Bundle;
import android.util.Log;

import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.ui.fragments.BasePosts;
import com.mb.nzbair.providers.ProviderFactory;
import com.mb.nzbair.providers.ProviderFactory.InvalidProviderException;

public class BrowseSavedCategoryActivity extends BaseBrowse {

	static String TAG = BrowseSavedCategoryActivity.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse_saved);
	}

	@Override
	protected void onStart() {
		fragment = this.getSupportFragmentManager().findFragmentById(R.id.fragment_browse_saved);
		super.onStart();
	}

	@Override
	protected Bundle getSearchBundleForContext() {
		final Bundle aBundle = new Bundle();

		if (metadata != null) {
			try {
				aBundle.putString(BasePosts.EXTRA_PROVIDER_ID, ProviderFactory.getInstance().getDefaultProvider().getId());
			} catch (final InvalidProviderException ex) {
				Log.e(TAG, "Failed to get provider");
				ex.printStackTrace();
			}
		}
		return aBundle;
	}

}
