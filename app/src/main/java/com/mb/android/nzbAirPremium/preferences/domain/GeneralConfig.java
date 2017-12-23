
package com.mb.android.nzbAirPremium.preferences.domain;


import com.mb.android.preferences.annotations.ConfigDescription;
import com.mb.android.preferences.annotations.ConfigMetadata;
import com.mb.android.preferences.annotations.ConfigOptions;
import com.mb.android.preferences.annotations.ConfigOption;
import com.mb.android.preferences.annotations.PreferenceType;
import com.mb.android.preferences.domain.Config;

public class GeneralConfig extends Config {

	public GeneralConfig() {
		super.setId("general");
	}

	@ConfigMetadata(id = "downloadLocation", required = true, type = PreferenceType.String)
	@ConfigDescription(title = "Download Location", description = "Location to download .nzb files")
	private String downloadLocation = "";

	// PREMIUM_START
	@ConfigMetadata(id = "showSabDownloadActivity", required = true, type = PreferenceType.Boolean)
	@ConfigDescription(title = "Always show downloads", description = "Show SABNzbd+ downloads page when you select a new download")
	private Boolean showSabDownloadActivity = true;
	// PREMIUM_END

	@ConfigMetadata(id = "startupScreen", required = true, type = PreferenceType.StringList)
	@ConfigDescription(title = "Startup Screen", description = "Which screen should be shown when NZBAir startsup?")
	@ConfigOptions(values = { @ConfigOption(title = "Home", value = "home"),
			// PREMIUM_START
			@ConfigOption(title = "SABNzbd Downloads", value = "sabnzbdownloads"), @ConfigOption(title = "SABNzbd Status", value = "sabnzbstatus"),
			// PREMIUM_END
			@ConfigOption(title = "Favourites", value = "savedfavourites"), @ConfigOption(title = "Searches", value = "savedsearches") })
	private String startupScreen = "home";

	public String getStartupScreen() {
		return startupScreen;
	}

	public String getDownloadLocation() {
		return downloadLocation;
	}

	// PREMIUM_START
	public Boolean shouldShowSabDownloadActivity() {
		return showSabDownloadActivity;
	}

	// PREMIUM_END

	@Override
	public String getName() {
		return "General Configuration";
	}

}
