
package com.mb.android.nzbAirPremium.preferences;

import com.mb.android.nzbAirPremium.preferences.domain.SABConfig;
import com.mb.nzbair.sabnzb.service.Sab;
import com.mb.nzbair.sabnzb.service.SabProxy;

public class SABConfigFactoryLoader {

	// PREMIUM_START
	public void loadSabConfig(SABConfig config) {

		if (isNewProvider(config)) {
			addNewProviderToFactory(config);
		} else {
			updateExistingProvider(config);
		}
	}

	public void loadDefaultSab(Sab service) {
		if (service != null) {
			SabProxy.getProxy().enableService(service);
		}
	}

	private void updateExistingProvider(SABConfig config) {
		final Sab provider = SabProxy.getProxy().getService(config.getId());
		config.update(provider);
	}

	private void addNewProviderToFactory(SABConfig config) {
		final Sab provider = config.createSab();
		SabProxy.getProxy().addService(provider);
	}

	private boolean isNewProvider(SABConfig config) {
		return !SabProxy.getProxy().hasService(config.getId());
	}

	// PREMIUM_END
}
