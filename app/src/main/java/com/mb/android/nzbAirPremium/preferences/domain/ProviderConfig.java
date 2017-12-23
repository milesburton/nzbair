
package com.mb.android.nzbAirPremium.preferences.domain;

import com.mb.android.preferences.domain.Config;
import com.mb.nzbair.providers.Provider;

public abstract class ProviderConfig extends Config {

	public abstract void update(Provider provider);

	public abstract Provider createProvider(int version);

}
