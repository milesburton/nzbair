
package com.mb.android.nzbAirPremium.preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.mb.android.nzbAirPremium.preferences.domain.SABConfig;
import com.mb.android.preferences.domain.Config;
import com.mb.android.preferences.manager.ConfigFilter;
import com.mb.android.preferences.manager.ConfigManager;
import com.mb.android.preferences.manager.OnConfigLoadedListener;
import com.mb.nzbair.sabnzb.service.Sab;
import com.mb.nzbair.sabnzb.service.SabProxy;

public class SabManager {

	// PREMIUM_START
	// private final String TAG = ProviderManager.class.getCanonicalName();

	private final String DEFAULT_SAB_KEY = "defaultSab";
	private final ConfigManager manager;
	private final SABConfigFactoryLoader factoryLoader = new SABConfigFactoryLoader();

	public SabManager(ConfigManager manager) {
		this.manager = manager;
		manager.listenForConfigLoaded(configLoadedListener);
	}

	public Map<String, Config> getConfigList() {
		final Map<String, Config> config = manager.getConfigurationFor(new ConfigFilter() {

			@Override
			public boolean isMatch(Config config) {
				return config instanceof SABConfig;
			}
		});
		return config;
	}

	public void saveSelected(SABConfig config) {
		final SharedPreferences preferences = manager.getSharedPreferences();
		final Editor editor = preferences.edit();
		editor.putString(DEFAULT_SAB_KEY, config.getId());
		editor.commit();
		factoryLoader.loadDefaultSab(getActive());
	}

	public void delete(SABConfig config) {
		SabProxy.getProxy().removeService(config.getId());
		manager.removeConfig(config);
	}

	public Sab getActive() {
		final SharedPreferences preferences = manager.getSharedPreferences();
		final String key = preferences.getString(DEFAULT_SAB_KEY, "");
		return SabProxy.getProxy().getService(key);
	}

	public List<Config> getSupportedConfigList() {
		final List<Config> supportedConfigList = new ArrayList<Config>();
		supportedConfigList.add(new SABConfig());
		return supportedConfigList;
	}

	private final OnConfigLoadedListener configLoadedListener = new OnConfigLoadedListener() {

		@Override
		public void configLoaded(Map<String, Config> configuration) {
			for (final Config config : configuration.values()) {
				if (config instanceof SABConfig) {
					factoryLoader.loadSabConfig((SABConfig) config);
				}
			}

			factoryLoader.loadDefaultSab(getActive());
		}

	};
	// PREMIUM_END
}
