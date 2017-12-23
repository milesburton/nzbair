
package com.mb.android.nzbAirPremium.preferences;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;

import com.mb.android.nzbAirPremium.preferences.domain.ClubConfig;
import com.mb.android.nzbAirPremium.preferences.domain.GeneralConfig;
import com.mb.android.nzbAirPremium.preferences.domain.IndexConfig;
import com.mb.android.nzbAirPremium.preferences.domain.NewznabConfig;
import com.mb.android.nzbAirPremium.preferences.domain.RusConfig;
import com.mb.android.nzbAirPremium.preferences.domain.SABConfig;
import com.mb.android.nzbAirPremium.preferences.domain.SuConfig;
import com.mb.android.preferences.persistance.ConfigDeserialiser;
import com.mb.android.preferences.persistance.SharedPrefenceConfigDeserialiser;
import com.mb.android.preferences.processor.ConfigProcessorStrategy;
import com.mb.android.preferences.processor.DefaultConfigDeserializerStrategy;

public class ConfigStrategyFactory {

	private final ConfigDeserialiser deserialiser;

	public ConfigStrategyFactory(SharedPreferences preferences) {
		this.deserialiser = new SharedPrefenceConfigDeserialiser(preferences);
	}

	public List<ConfigProcessorStrategy> getSupportedConfigStrategies() {

		final List<ConfigProcessorStrategy> configProcessorMap = new ArrayList<ConfigProcessorStrategy>();

		final IndexConfig indexConfig = new IndexConfig();
		configProcessorMap.add(new DefaultConfigDeserializerStrategy(deserialiser, indexConfig));
		final GeneralConfig generalConfig = new GeneralConfig();
		configProcessorMap.add(new DefaultConfigDeserializerStrategy(deserialiser, generalConfig));
		final SuConfig suConfig = new SuConfig();
		configProcessorMap.add(new DefaultConfigDeserializerStrategy(deserialiser, suConfig));
		final NewznabConfig newznabConfig = new NewznabConfig();
		configProcessorMap.add(new DefaultConfigDeserializerStrategy(deserialiser, newznabConfig));
		final RusConfig rusConfig = new RusConfig();
		configProcessorMap.add(new DefaultConfigDeserializerStrategy(deserialiser, rusConfig));
		final ClubConfig clubConfig = new ClubConfig();
		configProcessorMap.add(new DefaultConfigDeserializerStrategy(deserialiser, clubConfig));

		// PREMIUM_START
		final SABConfig sabConfig = new SABConfig();
		configProcessorMap.add(new DefaultConfigDeserializerStrategy(deserialiser, sabConfig));
		// PREMIUM_END

		return configProcessorMap;
	}

}
