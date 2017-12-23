
package com.mb.android.nzbAirPremium.preferences.domain;


import com.mb.android.preferences.annotations.ConfigDescription;
import com.mb.android.preferences.annotations.ConfigMetadata;
import com.mb.android.preferences.annotations.PreferenceType;
import com.mb.nzbair.providers.NewznabProvider;
import com.mb.nzbair.providers.Provider;

public class NewznabConfig extends ProviderConfig {

	@ConfigMetadata(id = "name", required = true, type = PreferenceType.String)
	@ConfigDescription(title = "Name", description = "Name of your Newznab host")
	private String name = "";

	@ConfigMetadata(id = "apiUrl", required = true, type = PreferenceType.String)
	@ConfigDescription(title = "Newznab Server URL", description = "Location of your API. For example: http://nzb.su/api")
	private String apiUrl = "";

	@ConfigMetadata(id = "apikey", required = true, type = PreferenceType.String)
	@ConfigDescription(title = "API Key", description = "Your API Key")
	private String apiKey = "";

	@Override
	public void update(Provider provider) {
		final NewznabProvider newznab = (NewznabProvider) provider;
		newznab.configure(name, apiKey, apiUrl);
	}

	@Override
	public Provider createProvider(int version) {
		return new NewznabProvider(getId(), name, version, apiKey, apiUrl);
	}

	@Override
	public String getName() {
		if (name == null || name.equals("")) {
			return "Newznab";
		} else {
			return name;
		}
	}
}
