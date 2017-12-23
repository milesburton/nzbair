
package com.mb.nzbair.providers;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import android.util.Log;

import com.mb.nzbair.providers.base.BaseProvider;
import com.mb.nzbair.providers.base.RemoteBrowseProxy;
import com.mb.nzbair.providers.domain.category.Category;
import com.mb.nzbair.providers.domain.category.ParentCategory;
import com.mb.nzbair.providers.domain.category.RootCategory;

public class NewznabProvider extends BaseProvider {

	private static final String TAG = NewznabProvider.class.getName();

	private final Map<String, String> browseParams = new Hashtable<String, String>();
	private final Map<String, String> params = new Hashtable<String, String>();

	public NewznabProvider(String id, String name, int appVersion, String apiKey, String apiUrl) {
		super(id, name);

		externaliseMe();
		configureParams(apiKey, apiUrl);
		browseParams.put("cat", "");

		setBrowseService(new RemoteBrowseProxy("newznab", browseParams, appVersion));
		((RemoteBrowseProxy) getBrowseService()).setCategoryParamField("cat");
	}

	public void configure(String name, String apiKey, String apiUrl) {
		try {
			setName(name);
			configureParams(apiKey, apiUrl);
			((RemoteBrowseProxy) getBrowseService()).configure(browseParams);
		} catch (final Exception ex) {
			Log.w(TAG, "Cannot update provider services");
		}
	}

	private void configureParams(String apiKey, String apiUrl) {
		params.put("apikey", apiKey);
		params.put("apiUrl", apiUrl);
		browseParams.putAll(params);
	}

	private void externaliseMe() {
		categories = new ArrayList<Category>();

		final Category Latest = new Category("-1", "Latest");
		final RootCategory Console = new RootCategory("1000", "Console");
		final RootCategory Movies = new RootCategory("2000", "Movies");
		final RootCategory Audio = new RootCategory("3000", "Audio");
		final RootCategory PC = new RootCategory("4000", "PC");
		final RootCategory TV = new RootCategory("5000", "TV");
		final RootCategory XXX = new RootCategory("6000", "XXX");
		final RootCategory Other = new RootCategory("2000", "Other");

		Latest.setSiblings(categories);
		Console.setSiblings(categories);
		Movies.setSiblings(categories);
		Audio.setSiblings(categories);
		PC.setSiblings(categories);
		TV.setSiblings(categories);
		XXX.setSiblings(categories);
		Other.setSiblings(categories);

		categories.add(Latest);
		categories.add(Console);
		categories.add(Movies);
		categories.add(Audio);
		categories.add(PC);
		categories.add(TV);
		categories.add(XXX);

		Console.addChild(new ParentCategory());
		Console.addChild(new Category("1000", "All"));
		Console.addChild(new Category("1010", "NDS"));
		Console.addChild(new Category("1020", "PSP"));
		Console.addChild(new Category("1030", "Wii"));
		Console.addChild(new Category("1040", "Xbox"));
		Console.addChild(new Category("1050", "Xbox 360"));
		Console.addChild(new Category("1060", "WiiWare"));
		Console.addChild(new Category("1070", "XBOX 360 DLC"));
		Console.addChild(new Category("1080", "PS3"));

		Movies.addChild(new ParentCategory());
		Movies.addChild(new Category("2000", "All"));
		Movies.addChild(new Category("2010", "Foreign"));
		Movies.addChild(new Category("2020", "Other"));
		Movies.addChild(new Category("2030", "SD"));
		Movies.addChild(new Category("2040", "HD"));
		Movies.addChild(new Category("2045", "Foreign"));

		Audio.addChild(new ParentCategory());
		Audio.addChild(new Category("3000", "All"));
		Audio.addChild(new Category("3010", "MP3"));
		Audio.addChild(new Category("3020", "Video"));
		Audio.addChild(new Category("3030", "Audiobook"));
		Audio.addChild(new Category("3040", "Lossless"));

		PC.addChild(new ParentCategory());
		PC.addChild(new Category("4000", "All"));
		PC.addChild(new Category("4010", "0day"));
		PC.addChild(new Category("4050", "Games"));
		PC.addChild(new Category("4020", "ISO"));
		PC.addChild(new Category("4030", "Mac"));
		PC.addChild(new Category("4040", "Phone"));

		TV.addChild(new ParentCategory());
		TV.addChild(new Category("5000", "All"));
		TV.addChild(new Category("5070", "Anime"));
		TV.addChild(new Category("5020", "Foreign"));
		TV.addChild(new Category("5040", "HD"));
		TV.addChild(new Category("5050", "Other"));
		TV.addChild(new Category("5030", "SD"));
		TV.addChild(new Category("5060", "Sport"));

		XXX.addChild(new ParentCategory());
		XXX.addChild(new Category("6000", "All"));
		XXX.addChild(new Category("6010", "DVD"));
		XXX.addChild(new Category("6020", "WMV"));
		XXX.addChild(new Category("6030", "XviD"));
		XXX.addChild(new Category("6040", "x264"));

		Other.addChild(new ParentCategory());
		Other.addChild(new Category("2000", "All"));
		Other.addChild(new Category("7030", "Comics"));
		Other.addChild(new Category("7020", "Ebook"));
		Other.addChild(new Category("7010", "Misc"));

		// set provider
		normaliseProviderIdInCategories(categories);
	}

}
