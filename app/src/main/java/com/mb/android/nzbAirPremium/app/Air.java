
package com.mb.android.nzbAirPremium.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mb.android.nzbAirPremium.favourites.SaveFavouritesProvider;
import com.mb.android.nzbAirPremium.favourites.SaveSearchProvider;
import com.mb.android.nzbAirPremium.preferences.ConfigStrategyFactory;
import com.mb.android.nzbAirPremium.preferences.ProviderManager;
import com.mb.android.nzbAirPremium.preferences.SabManager;
import com.mb.android.nzbAirPremium.preferences.domain.GeneralConfig;
import com.mb.android.preferences.manager.ConfigManager;
import com.mb.android.preferences.processor.ConfigProcessorStrategy;
import com.mb.nzbair.providers.ProviderFactory;

public class Air extends Application {

	private static Air instance;
	// PREMIUM_START
	private SabManager sabManager;
	// PREMIUM_END
	private ConfigManager configManager;
	private ProviderManager providerManager;
	private boolean firstRun = true;
	private final String FIRST_RUN_KEY = "firstRun";

	public static Air get() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;

		final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		setupFirstRun(sharedPreferences);

		configManager = new ConfigManager(sharedPreferences);
		providerManager = new ProviderManager(configManager);
		// PREMIUM_START
		sabManager = new SabManager(configManager);
		// PREMIUM_END

		setupSaveProviders();
		setupConfigManager();
		getConfigManager().loadFullConfig();
	}

	private void setupFirstRun(SharedPreferences sharedPreferences) {

		firstRun = sharedPreferences.getBoolean(FIRST_RUN_KEY, true);
		if (firstRun) {
			sharedPreferences.edit().putBoolean(FIRST_RUN_KEY, false).commit();
		}
	}

	private void setupSaveProviders() {
		ProviderFactory.getInstance().addProvider(new SaveFavouritesProvider(getApplicationContext()));
		ProviderFactory.getInstance().addProvider(new SaveSearchProvider(getApplicationContext()));
	}

	private void setupConfigManager() {
		final ConfigStrategyFactory AirConfigStrategyManager = new ConfigStrategyFactory(configManager.getSharedPreferences());

		for (final ConfigProcessorStrategy strategy : AirConfigStrategyManager.getSupportedConfigStrategies()) {
			configManager.addConfigProcessorStrategy(strategy);
		}
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

	public ProviderManager getProviderManager() {
		return providerManager;
	}

	// PREMIUM_START
	public SabManager getSabManager() {
		return sabManager;
	}

	// PREMIUM_END

	public static boolean isPremium() {
		boolean isPremium = false;

		// PREMIUM_START
		isPremium = true;
		// PREMIUM_END

		return isPremium;
	}

	public GeneralConfig getGeneralConfig() {
		return (GeneralConfig) configManager.getConfig("general");
	}

	public int getVersionCode() {
		int versionCode = 0;
		try {
			versionCode = getApplicationContext().getPackageManager().getPackageInfo("com.mb.android.nzbAirPremium", 0).versionCode;
			return versionCode;
		} catch (final Exception ex) {

		}
		return -1;
	}

	public String getDefaultSharedPreferencesName() {
		return getApplicationContext().getPackageName() + "_preferences";
	}

	public boolean isFirstRun() {
		final boolean firstRunCopy = firstRun;
		firstRun = false;
		return firstRunCopy;
	}

}
