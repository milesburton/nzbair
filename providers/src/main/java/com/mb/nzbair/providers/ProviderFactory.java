
package com.mb.nzbair.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import com.mb.nzbair.providers.domain.category.Category;

public class ProviderFactory implements OnSharedPreferenceChangeListener {

	private final String searchEngineProviderKey = "";
	private final String searchEngineProviderDefault = "";

	private final Map<String, Provider> providers = new HashMap<String, Provider>();

	private String defaultProviderId;

	private static ProviderFactory instance;

	public ProviderFactory() {

	}

	public static ProviderFactory getInstance() {
		if (instance == null) {
			instance = new ProviderFactory();
		}
		return instance;
	}

	public List<Provider> getProviders() {
		return new ArrayList<Provider>(providers.values());
	}

	private Provider getProviderOrNull(String id) {
		if (id != null && id != "") {
			return providers.get(id);
		}

		return null;
	}

	public Provider getProvider(String id) {
		final Provider provider = getProviderOrNull(id);
		if (provider == null) {
			throw new InvalidProviderException();
		}
		return provider;
	}

	public boolean hasProvider(String id) {
		return getProviderOrNull(id) != null;
	}

	public Provider getDefaultProvider() {

		if (!hasProvider(defaultProviderId) && providers.size() > 0) {
			for (final Provider provider : providers.values()) {
				if (provider.getBrowseService() != null) {
					defaultProviderId = provider.getId();
				}
			}
		}

		return getProvider(defaultProviderId);
	}

	public void setDefaultSearchProvider(String id) {
		this.defaultProviderId = id;
	}

	public void addProvider(Provider provider) {
		this.providers.put(provider.getId(), provider);
	}

	public List<Category> getProvidersAsCategories() {
		final List<Category> categories = new ArrayList<Category>();
		for (final Provider aProvider : getProviders()) {
			categories.add(aProvider.getProviderCategory());
		}
		return categories;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		setDefaultSearchProvider(sharedPreferences.getString(getSearchEngineProviderKey(), getSearchEngineProviderDefault()));
	}

	private String getSearchEngineProviderDefault() {
		return searchEngineProviderDefault;
	}

	private String getSearchEngineProviderKey() {
		return searchEngineProviderKey;
	}

	public static class InvalidProviderException extends RuntimeException {

		private static final long serialVersionUID = 3260138327096329547L;
	}

	public void removeProvider(String id) {
		providers.remove(id);
	};

}
