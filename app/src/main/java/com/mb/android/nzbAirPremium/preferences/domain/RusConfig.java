
package com.mb.android.nzbAirPremium.preferences.domain;

import com.mb.android.preferences.annotations.ConfigDescription;
import com.mb.android.preferences.annotations.ConfigMetadata;
import com.mb.android.preferences.annotations.PreferenceType;
import com.mb.nzbair.providers.NZBRusProvider;
import com.mb.nzbair.providers.Provider;

public class RusConfig extends ProviderConfig {

	@ConfigMetadata(id = "uid", required = true, type = PreferenceType.String)
	@ConfigDescription(title = "UID", description = "Your user id, available from the RSS/API page")
	private  String uid = "";

	@ConfigMetadata(id = "apikey", required = true, type = PreferenceType.String)
	@ConfigDescription(title = "API Key", description = "API Key")
	private String apikey = "";

	private static final String id = "nzbrus";
	private static final String name = "NZB-R-Us";

	public RusConfig() {
		super.setId(id);
	}

	@Override
	public void update(Provider provider) {
		final NZBRusProvider newzbinProvider = (NZBRusProvider) provider;
		newzbinProvider.configure(name, uid, apikey);
	}

	@Override
	public Provider createProvider(int version) {
		return new NZBRusProvider(version, id, name, uid, apikey);
	}

	@Override
	public String getName() {
		return "NZB-R-Us";
	}

}
