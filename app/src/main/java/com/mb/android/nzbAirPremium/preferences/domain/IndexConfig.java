
package com.mb.android.nzbAirPremium.preferences.domain;

import com.mb.nzbair.providers.IndexProvider;
import com.mb.nzbair.providers.Provider;

public class IndexConfig extends ProviderConfig {

	public IndexConfig() {
		setId("index");
	}

	@Override
	public Provider createProvider(int version) {
		return new IndexProvider(version);
	}

	@Override
	public String getName() {
		return "NZBIndex";
	}

	@Override
	public void update(Provider provider) {
	}

}
