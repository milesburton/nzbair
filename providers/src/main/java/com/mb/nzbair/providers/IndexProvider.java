
package com.mb.nzbair.providers;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import android.util.Log;

import com.mb.nzbair.providers.base.BaseProvider;
import com.mb.nzbair.providers.base.RemoteBrowseProxy;
import com.mb.nzbair.providers.domain.category.Category;

public class IndexProvider extends BaseProvider {

	private static final String TAG = IndexProvider.class.getName();

	private String forceSSLKey;
	private String allowInvalidSSLKey;

	Map<String, String> params = new Hashtable<String, String>();

	public IndexProvider(int appVersion) {
		super("nzbindex", "NZBIndex");

		externaliseMe();
		setBrowseService(new RemoteBrowseProxy("nzbindexProvider", params, appVersion));
	}

	private void externaliseMe() {

		categories = new ArrayList<Category>();
		categories.add(new Category("Latest"));

		normaliseProviderIdInCategories(categories);
	}

	public void configure() {

		try {
			((RemoteBrowseProxy) getBrowseService()).configure(params);
		} catch (final Exception ex) {
			Log.w(TAG, "Could not load provider services. " + ex.toString());
		}

	}

	public String getForceSSLKey() {
		return forceSSLKey;
	}

	public void setForceSSLKey(String forceSSLKey) {
		this.forceSSLKey = forceSSLKey;
	}

	public String getAllowInvalidSSLKey() {
		return allowInvalidSSLKey;
	}

	public void setAllowInvalidSSLKey(String allowInvalidSSLKey) {
		this.allowInvalidSSLKey = allowInvalidSSLKey;
	}

}
