
package com.mb.nzbair.providers;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import android.util.Log;

import com.mb.nzbair.providers.base.BaseProvider;
import com.mb.nzbair.providers.base.RemoteBrowseProxy;
import com.mb.nzbair.providers.domain.category.Category;

public class ClubProvider extends BaseProvider {

	private static final String TAG = ClubProvider.class.getName();

	private String forceSSLKey;
	private String allowInvalidSSLKey;

	Map<String, String> params = new Hashtable<String, String>();

	public ClubProvider(int appVersion) {

		super("nzbclub", "NZBClub");

		externaliseMe();
		setBrowseService(new RemoteBrowseProxy("nzbclubProvider", params, appVersion));
	}

	private void externaliseMe() {
		categories = new ArrayList<Category>();
		categories.add(new Category("3869", "alt.binaries"));

		// set provider
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
