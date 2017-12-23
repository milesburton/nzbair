
package com.mb.android.nzbAirPremium.setup;

interface ISetup {

	void addCallback(ISetupCallback callback);

	void removeCallback(ISetupCallback callback);

	void requestToken();

	void requestSetupData(String code, String pin);
}
