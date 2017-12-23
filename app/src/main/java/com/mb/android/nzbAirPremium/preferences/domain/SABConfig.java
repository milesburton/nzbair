
package com.mb.android.nzbAirPremium.preferences.domain;

import com.mb.android.preferences.annotations.ConfigDescription;
import com.mb.android.preferences.annotations.ConfigMetadata;
import com.mb.android.preferences.annotations.PreferenceType;
import com.mb.android.preferences.domain.Config;
import com.mb.nzbair.sabnzb.service.Sab;
import com.mb.nzbair.sabnzb.service.SabService;

public class SABConfig extends Config {

	@ConfigMetadata(id = "servername", required = true, type = PreferenceType.String)
	@ConfigDescription(title = "Name", description = "A friendly name for your SAB server")
	private String servername = "";

	// PREMIUM_START
	@ConfigMetadata(id = "serverUrl", required = true, type = PreferenceType.String)
	@ConfigDescription(title = "SAB Server URL", description = "Location of your API. Usually http://your-ip-here.com:8080/sabnzbd/api or https://your-ip-here.com:9090/sabnzbd/api")
	private String url = "";

	@ConfigMetadata(id = "apikey", required = true, type = PreferenceType.String)
	@ConfigDescription(title = "API Key", description = "Your SABNzbd API Key")
	private String apiKey = "";

	public String getServername() {
		return servername;
	}

	public String getUrl() {
		return url;
	}

	public String getApiKey() {
		return apiKey;
	}

	public Sab createSab() {
		final Sab sabService = new SabService(getId(), url, apiKey);
		return sabService;
	}

	public void update(Sab sabService) {
		sabService.configure(getId(), url, apiKey);
	}

	// PREMIUM_END

	@Override
	public String getName() {
		if (servername == null || servername.equals("")) {
			return "New SABNzbd+ Server";
		} else {
			return servername;
		}

	}

}
