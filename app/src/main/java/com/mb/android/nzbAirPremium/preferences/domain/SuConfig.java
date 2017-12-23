
package com.mb.android.nzbAirPremium.preferences.domain;

import com.mb.android.preferences.annotations.ConfigDescription;
import com.mb.android.preferences.annotations.ConfigMetadata;
import com.mb.android.preferences.annotations.PreferenceType;
import com.mb.nzbair.providers.NewznabProvider;
import com.mb.nzbair.providers.Provider;

public class SuConfig extends ProviderConfig {

	@ConfigMetadata(id = "apiKey", required = true, type = PreferenceType.String)
	@ConfigDescription(title = "Api Key", description = "Your Api Key")
	private String apikey = "";

	private static final String id = "su";
	private static final String name = "NZB.su";
	private static final String apiUrl = "https://nzb.su/api";

	public SuConfig() {
		super.setId("su");
	}

	@Override
	public void update(Provider provider) {
		final NewznabProvider suProvider = (NewznabProvider) provider;
		suProvider.configure(name, apikey, apiUrl);
	}

	@Override
	public Provider createProvider(int version) {
		return new NewznabProvider(id, name, version, apikey, apiUrl);
	}

	@Override
	public String getName() {
		return "NZBSu";
	}

}
