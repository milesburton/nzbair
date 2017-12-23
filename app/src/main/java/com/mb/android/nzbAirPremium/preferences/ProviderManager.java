
package com.mb.android.nzbAirPremium.preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.mb.android.nzbAirPremium.preferences.domain.ClubConfig;
import com.mb.android.nzbAirPremium.preferences.domain.IndexConfig;
import com.mb.android.nzbAirPremium.preferences.domain.NewznabConfig;
import com.mb.android.nzbAirPremium.preferences.domain.ProviderConfig;
import com.mb.android.nzbAirPremium.preferences.domain.RusConfig;
import com.mb.android.nzbAirPremium.preferences.domain.SuConfig;
import com.mb.android.preferences.domain.Config;
import com.mb.android.preferences.manager.ConfigFilter;
import com.mb.android.preferences.manager.ConfigManager;
import com.mb.android.preferences.manager.OnConfigLoadedListener;
import com.mb.nzbair.providers.ProviderFactory;

public class ProviderManager {

	private final String DEFAULT_PROVIDER_KEY = "defaultProvider";
	private final ConfigManager manager;

	public ProviderManager(ConfigManager manager) {
		this.manager = manager;
		manager.listenForConfigLoaded(configLoadedListener);
	}

	public Map<String, Config> getProviderConfigList() {
		final Map<String, Config> config = manager.getConfigurationFor(new ConfigFilter() {

			@Override
			public boolean isMatch(Config config) {
				return config instanceof ProviderConfig;
			}
		});
		return config;
	}

	public void saveDefaultProvider(ProviderConfig config) {
		final SharedPreferences preferences = manager.getSharedPreferences();
		final Editor editor = preferences.edit();
		editor.putString(DEFAULT_PROVIDER_KEY, config.getId());
		editor.commit();
	}

	public void delete(ProviderConfig config) {
		ProviderFactory.getInstance().removeProvider(config.getId());
		manager.removeConfig(config);
	}

	public String getDefaultProviderId() {
		final SharedPreferences preferences = manager.getSharedPreferences();
		return preferences.getString(DEFAULT_PROVIDER_KEY, "");
	}

	public List<Config> getSupportedConfigList() {
		
		final List<Config> supportedConfigList = new ArrayList<Config>();
		supportedConfigList.add(new IndexConfig());
		supportedConfigList.add(new SuConfig());
		supportedConfigList.add(new NewznabConfig());
		supportedConfigList.add(new RusConfig());
		supportedConfigList.add(new ClubConfig());

		return supportedConfigList;
	}

	private final OnConfigLoadedListener configLoadedListener = new OnConfigLoadedListener() {

		@Override
		public void configLoaded(Map<String, Config> configuration) {
			final ProviderConfigFactoryLoader factoryLoader = new ProviderConfigFactoryLoader();
			for (final Config config : configuration.values()) {
				if (config instanceof ProviderConfig) {
					factoryLoader.loadProviderConfig((ProviderConfig) config);
				}
			}

			factoryLoader.loadDefaultProvider(getDefaultProviderId());
		}

	};
}
