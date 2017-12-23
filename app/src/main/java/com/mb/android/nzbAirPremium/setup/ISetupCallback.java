
package com.mb.android.nzbAirPremium.setup;

import java.util.Map;

public interface ISetupCallback {

	void responseToken(String token, Throwable error);

	void responseConfiguration(Map<String, String> configuration, Throwable error);
}
