
package com.mb.android.nzbAirPremium.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.actionbarsherlock.view.MenuItem;
import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.ui.fragments.BasePosts;
import com.mb.android.nzbAirPremium.ui.fragments.BrowseMetadata;
import com.mb.android.nzbAirPremium.ui.fragments.Refreshable;
import com.mb.android.nzbAirPremium.ui.helper.MenuHelper;
import com.mb.nzbair.providers.Provider;
import com.mb.nzbair.providers.ProviderFactory;
import com.mb.nzbair.providers.ProviderFactory.InvalidProviderException;

public abstract class BaseBrowse extends BaseFragmentActivity {

	private static final String TAG = BaseBrowse.class.getCanonicalName();

	protected Fragment fragment;

	BrowseMetadata metadata;
	Refreshable refreshable;

	@Override
	protected void onStart() {
		super.onStart();
		if (fragment != null) {
			if (fragment instanceof BrowseMetadata) {
				metadata = (BrowseMetadata) fragment;
			}
			if (fragment instanceof Refreshable) {
				refreshable = (Refreshable) fragment;
			}
		}
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected Bundle getSearchBundleForContext() {
		final Bundle aBundle = new Bundle();
		try {
			Provider searchProvider = null;

			if (metadata != null) {

				// If rootcategory exists, perform contextual search
				if (metadata.getCategory() != null) {
					aBundle.putSerializable(BasePosts.EXTRA_CATEGORY, metadata.getCategory());
				}

				if (metadata.getProvider() != null) {
					searchProvider = metadata.getProvider();
				}
			}

			if (searchProvider == null) {
				searchProvider = ProviderFactory.getInstance().getDefaultProvider();
			}

			aBundle.putString(BasePosts.EXTRA_PROVIDER_ID, searchProvider.getId());

		} catch (final InvalidProviderException ex) {
			Log.e(TAG, "Could not find requested provider");
			ex.printStackTrace();
		}

		return aBundle;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.action_refresh:
			if (refreshable != null) {
				refreshable.refresh();
			}
			return true;

		default:
			MenuHelper.onOptionsItemSelected(this, this, item);
		}

		return super.onOptionsItemSelected(item);

	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {

		getSupportMenuInflater().inflate(R.menu.browse_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

}
