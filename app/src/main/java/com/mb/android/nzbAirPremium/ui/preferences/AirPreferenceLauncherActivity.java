
package com.mb.android.nzbAirPremium.ui.preferences;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.view.MenuItem;
import com.mb.android.nzbAirPremium.preferences.domain.GeneralConfig;
import com.mb.android.nzbAirPremium.ui.helper.MenuHelper;
import com.mb.android.preferences.domain.ConfigCategory;
import com.mb.android.preferences.ui.GenericPreferenceActivity;
import com.mb.android.preferences.ui.PreferenceLauncherActivity;

public class AirPreferenceLauncherActivity extends PreferenceLauncherActivity {

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		addCategoryList(new ConfigCategory(android.R.drawable.ic_media_ff, new Intent(this, OneTimeSetupActivity.class), "One Time Setup", "Configure NZBAir using an online tool via your PC"));
		addCategoryList(new ConfigCategory(android.R.drawable.ic_menu_compass, new Intent(this, ProviderConfigListActivity.class), "Provider Setup", "Add or remove search providers"));
		// PREMIUM_START
		addCategoryList(new ConfigCategory(android.R.drawable.ic_menu_set_as, new Intent(this, SabConfigListActivity.class), "SABNzbd+ Setup", "Add or remove SABNzbd+ boxes"));
		// PREMIUM_END
		addCategoryList(new ConfigCategory(android.R.drawable.ic_menu_agenda, new Intent(this, AirPreferenceActivity.class).putExtra(GenericPreferenceActivity.ConfigCanonicalClassKey, GeneralConfig.class.getCanonicalName()), "General setup",
				"Misc options"));
	}

	@Override
	public void onStart() {
		super.onStart();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		MenuHelper.onOptionsItemSelected(this, this, item);
		return super.onOptionsItemSelected(item);
	}
}
