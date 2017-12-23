
package com.mb.android.nzbAirPremium.preferences;

import com.mb.android.nzbAirPremium.app.Air;
import com.mb.android.nzbAirPremium.preferences.domain.ProviderConfig;
import com.mb.nzbair.providers.Provider;
import com.mb.nzbair.providers.ProviderFactory;

public class ProviderConfigFactoryLoader {

	public void loadProviderConfig(ProviderConfig config) {

		if (isNewProvider(config)) {
			addNewProviderToFactory(config);
		} else {
			updateExistingProvider(config);
		}
	}

	public void loadDefaultProvider(String providerId) {
		ProviderFactory.getInstance().setDefaultSearchProvider(providerId);
	}

	private void updateExistingProvider(ProviderConfig config) {
		final Provider provider = ProviderFactory.getInstance().getProvider(config.getId());
		config.update(provider);
	}

	private void addNewProviderToFactory(ProviderConfig config) {
		final Air session = Air.get();
		final Provider provider = config.createProvider(session.getVersionCode());
		ProviderFactory.getInstance().addProvider(provider);
	}

	private boolean isNewProvider(ProviderConfig providerConfig) {
		return !ProviderFactory.getInstance().hasProvider(providerConfig.getId());
	}
}
