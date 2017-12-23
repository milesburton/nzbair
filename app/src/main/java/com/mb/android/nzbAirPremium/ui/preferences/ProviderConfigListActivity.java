
package com.mb.android.nzbAirPremium.ui.preferences;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;

import com.actionbarsherlock.view.MenuItem;
import com.mb.android.nzbAirPremium.app.Air;
import com.mb.android.nzbAirPremium.preferences.ProviderManager;
import com.mb.android.nzbAirPremium.preferences.domain.ProviderConfig;
import com.mb.android.nzbAirPremium.ui.helper.MenuHelper;
import com.mb.android.preferences.domain.Config;
import com.mb.android.preferences.manager.ConfigFilter;
import com.mb.android.preferences.ui.ConfigListActivity;
import com.mb.android.preferences.ui.GenericPreferenceActivity;

public class ProviderConfigListActivity extends ConfigListActivity {

	private final static int ContextMenuDelete = 1;
	private List<Config> supportedConfigList;
	private ProviderManager providerManager;

	public ProviderConfigListActivity() {
		super(new ConfigFilter() {

			@Override
			public boolean isMatch(Config config) {
				return config instanceof ProviderConfig;
			}
		}, Air.get().getConfigManager());

		providerManager = Air.get().getProviderManager();
		supportedConfigList = providerManager.getSupportedConfigList();
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
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Supported Providers:");
		builder.setItems(getProviderTypeNames(), createNewProviderListener);

		final AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void onDefaultSelected(Config config) {
		providerManager.saveDefaultProvider((ProviderConfig) config);

	}

	@Override
	public void onConfigSelected(Config config) {
		final Intent configIntent = new Intent(this, AirPreferenceActivity.class);
		configIntent.putExtra(GenericPreferenceActivity.ConfigCanonicalClassKey, config.getClass().getCanonicalName());
		if (config.getId() != null && !config.getId().equals("")) {
			configIntent.putExtra(GenericPreferenceActivity.ConfigIdKey, config.getId());
		}
		startActivity(configIntent);
	}

	@Override
	public void onConfigContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("Provider Actions");
		menu.add(0, ContextMenuDelete, 0, "Delete");
	}

	private final DialogInterface.OnClickListener createNewProviderListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int item) {
			final ProviderConfig selectedProviderConfig = (ProviderConfig) supportedConfigList.get(item);
			onConfigSelected(selectedProviderConfig);
		}

	};

	private String[] getProviderTypeNames() {
		final String[] providerTypeNameList = new String[supportedConfigList.size()];

		for (int i = 0; i < supportedConfigList.size(); i++) {
			providerTypeNameList[i] = supportedConfigList.get(i).getName();
		}

		return providerTypeNameList;
	}

	@Override
	public void onConfigDeleted(Config config) {
		providerManager.delete((ProviderConfig) config);
	}

	@Override
	public boolean isDefaultConfig(Config config) {
		return providerManager.getDefaultProviderId().equals(config.getId());
	}

}