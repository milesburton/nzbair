
package com.mb.android.nzbAirPremium.ui.preferences;

//PREMIUM_START
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;

import com.actionbarsherlock.view.MenuItem;
import com.mb.android.nzbAirPremium.app.Air;
import com.mb.android.nzbAirPremium.preferences.SabManager;
import com.mb.android.nzbAirPremium.preferences.domain.SABConfig;
import com.mb.android.nzbAirPremium.ui.helper.MenuHelper;
import com.mb.android.preferences.domain.Config;
import com.mb.android.preferences.manager.ConfigFilter;
import com.mb.android.preferences.ui.ConfigListActivity;
import com.mb.android.preferences.ui.GenericPreferenceActivity;

public class SabConfigListActivity extends ConfigListActivity {

	private final static int ContextMenuDelete = 1;
	private SabManager configManager;

	public SabConfigListActivity() {
		super(new ConfigFilter() {

			@Override
			public boolean isMatch(Config config) {
				return config instanceof SABConfig;
			}
		}, Air.get().getConfigManager());

		configManager = Air.get().getSabManager();
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

	@Override
	public void onAddConfig() {
		startActivity(new Intent(this, AirPreferenceActivity.class).putExtra(GenericPreferenceActivity.ConfigCanonicalClassKey, SABConfig.class.getCanonicalName()));
	}

	@Override
	public void onDefaultSelected(Config config) {
		configManager.saveSelected((SABConfig) config);
	}

	@Override
	public void onConfigSelected(Config config) {
		startActivity(new Intent(this, AirPreferenceActivity.class).putExtra(GenericPreferenceActivity.ConfigCanonicalClassKey, config.getClass().getCanonicalName()).putExtra(GenericPreferenceActivity.ConfigIdKey, config.getId()));
	}

	@Override
	public void onConfigContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("Configuration Actions");
		menu.add(0, ContextMenuDelete, 0, "Delete");
	}

	@Override
	public void onConfigDeleted(Config config) {
		configManager.delete((SABConfig) config);
	}

	@Override
	public boolean isDefaultConfig(Config config) {
		return configManager.getActive() != null && configManager.getActive().getId().equals(config.getId());
	}

}
// PREMIUM_END