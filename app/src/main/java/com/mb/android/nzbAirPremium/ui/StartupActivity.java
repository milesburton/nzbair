
package com.mb.android.nzbAirPremium.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.mb.android.nzbAirPremium.app.Air;
import com.mb.android.nzbAirPremium.favourites.SaveFavouritesProvider;
import com.mb.android.nzbAirPremium.favourites.SaveSearchProvider;
import com.mb.android.nzbAirPremium.preferences.domain.GeneralConfig;
import com.mb.android.nzbAirPremium.ui.fragments.BrowseCategoryFragment;
import com.mb.android.nzbAirPremium.ui.fragments.SabDetailsFragment;
import com.mb.android.nzbAirPremium.ui.fragments.SabDownloadsFragment;

public class StartupActivity extends FragmentActivity {

	public static final String TAG = StartupActivity.class.getName();

	private final GeneralConfig generalConfig = Air.get().getGeneralConfig();

	@Override
	public void onCreate(Bundle icicle) {

		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		launchStartupScreen(this);

		finish();
	}

	private void launchStartupScreen(Context con) {

		final String startupScreenId = getStartupScreen(con);
		if (startupScreenId.equals("savedfavourites")) {
			final Intent activity = new Intent(con, BrowseSavedCategoryActivity.class);
			activity.setAction(BrowseCategoryFragment.VIEW_CATEGORY);
			activity.putExtra(BrowseCategoryFragment.EXTRA_PROVIDER_ID, SaveFavouritesProvider.BUCKET_NAME);
			startActivity(activity);
		} else if (startupScreenId.equals("savedsearches")) {
			final Intent activity = new Intent(con, BrowseSavedCategoryActivity.class);
			activity.setAction(BrowseCategoryFragment.VIEW_CATEGORY);
			activity.putExtra(BrowseCategoryFragment.EXTRA_PROVIDER_ID, SaveSearchProvider.BUCKET_NAME);
			startActivity(activity);
		} else
		// PREMIUM_START
		if (startupScreenId.equals("sabnzbdownloads")) {
			startSabDownloads();
		} else if (startupScreenId.equals("sabnzbstatus")) {
			startActivity(new Intent(this, DownloadsActivity.class).putExtra(DownloadsActivity.STARTUP_CLASS, SabDetailsFragment.class.getCanonicalName()));
		} else
		// PREMIUM_END
		{
			startActivity(new Intent(this, MainMenuActivity.class));
		}
	}

	private void startSabDownloads() {

		startActivity(new Intent(this, DownloadsActivity.class).putExtra(DownloadsActivity.STARTUP_CLASS, SabDownloadsFragment.class.getCanonicalName()));
	}

	private String getStartupScreen(Context con) {

		String startupScreenId = generalConfig == null ? "" : generalConfig.getStartupScreen();
		if (startupScreenId == null) {
			startupScreenId = "";
		}
		return startupScreenId;
	}

}
