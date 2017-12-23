
package com.mb.android.nzbAirPremium.ui.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.actionbarsherlock.view.MenuItem;
import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.ui.MainMenuActivity;
import com.mb.android.nzbAirPremium.ui.preferences.AirPreferenceLauncherActivity;
import com.mb.android.nzbAirPremium.ui.preferences.ProviderConfigListActivity;

public class MenuHelper {

	public static boolean onOptionsItemSelected(Activity ac, Context con, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:

			final Intent settingsIntent = new Intent(con, AirPreferenceLauncherActivity.class);
			con.startActivity(settingsIntent);
			return true;

		case R.id.action_search:

			ac.onSearchRequested();
			return true;

		case android.R.id.home:

			final Intent homeIntent = new Intent(con, MainMenuActivity.class);
			homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			con.startActivity(homeIntent);
			ac.finish();
			return true;

		case R.id.action_switch_default_provider:

			con.startActivity(new Intent(con, ProviderConfigListActivity.class));
			return true;
		}

		return false;
	}
}
