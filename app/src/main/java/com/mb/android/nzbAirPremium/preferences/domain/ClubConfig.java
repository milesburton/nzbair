
package com.mb.android.nzbAirPremium.preferences.domain;

import com.mb.nzbair.providers.ClubProvider;
import com.mb.nzbair.providers.Provider;

public class ClubConfig extends ProviderConfig {

	public ClubConfig() {
		setId("club");
	}

	@Override
	public void update(Provider provider) {

	}

	@Override
	public Provider createProvider(int version) {
		return new ClubProvider(version);
	}

	@Override
	public String getName() {
		return "NZBClub";
	}

}
