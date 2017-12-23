
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

public class NZBRusProvider extends BaseProvider {

	private static final String TAG = NewznabProvider.class.getName();

	private final Map<String, String> browseParams = new Hashtable<String, String>();
	private final Map<String, String> params = new Hashtable<String, String>();

	public NZBRusProvider(int version, String id, String name, String uid, String apikey) {
		super(id, name);

		externaliseMe();
		configureParams(uid, apikey);
		browseParams.put("cat", "");

		setBrowseService(new RemoteBrowseProxy("nzbrus", browseParams, version));
		((RemoteBrowseProxy) getBrowseService()).setCategoryParamField("cat");

	}

	public void configure(String name, String uid, String apikey) {

		try {
			setName(name);
			configureParams(uid, apikey);
			((RemoteBrowseProxy) getBrowseService()).configure(browseParams);
		} catch (final Exception ex) {
			Log.w(TAG, "Cannot update provider services");
		}
	}

	private void configureParams(String username, String password) {

		params.put("uid", username);
		params.put("apikey", password);
		browseParams.putAll(params);
	}

	private void externaliseMe() {
		categories = new ArrayList<Category>();

		final RootCategory all = new RootCategory("0", "All Categories");
		final RootCategory anime = new RootCategory("3", "Anime");
		final RootCategory applications = new RootCategory("1s", "Applications");
		final RootCategory console = new RootCategory("2s", "Console");
		final RootCategory ebooks = new RootCategory("15", "Ebooks");
		final RootCategory games = new RootCategory("3s", "Games");
		final RootCategory handheld = new RootCategory("7s", "Hangheld");
		final RootCategory movies = new RootCategory("9s", "Movies");
		final RootCategory music = new RootCategory("14s", "Music");
		final RootCategory tv = new RootCategory("20s", "TV");

		// Set siblings
		all.setSiblings(categories);
		anime.setSiblings(categories);
		applications.setSiblings(categories);
		console.setSiblings(categories);
		ebooks.setSiblings(categories);
		games.setSiblings(categories);
		handheld.setSiblings(categories);
		movies.setSiblings(categories);
		music.setSiblings(categories);
		tv.setSiblings(categories);

		categories.add(all);
		categories.add(anime);
		categories.add(applications);
		categories.add(console);
		categories.add(games);
		categories.add(handheld);
		categories.add(movies);
		categories.add(music);
		categories.add(tv);

		applications.addChild(new ParentCategory());
		applications.addChild(new Category("6", "0-Day"));
		applications.addChild(new Category("93", "Mac"));
		applications.addChild(new Category("9", "Misc"));
		applications.addChild(new Category("12", "PC ISO"));

		console.addChild(new ParentCategory());
		console.addChild(new Category("87", "0-Day"));
		console.addChild(new Category("30", "PlayStation 2"));
		console.addChild(new Category("89", "PlayStation 3"));
		console.addChild(new Category("92", "Wii"));
		console.addChild(new Category("39", "Xbox"));
		console.addChild(new Category("88", "Xbox 360"));

		handheld.addChild(new ParentCategory());
		handheld.addChild(new Category("21", "DS"));
		handheld.addChild(new Category("33", "PSP"));

		movies.addChild(new ParentCategory());
		movies.addChild(new Category("45", "DVDr"));
		movies.addChild(new Category("90", "HD"));
		movies.addChild(new Category("48", "Misc"));
		movies.addChild(new Category("51", "XviD"));

		music.addChild(new ParentCategory());
		music.addChild(new Category("54", "MP3"));
		music.addChild(new Category("60", "Video"));
		music.addChild(new Category("99", "Phone"));

		tv.addChild(new ParentCategory());
		tv.addChild(new Category("69", "DVDr"));
		tv.addChild(new Category("91", "HD"));
		tv.addChild(new Category("72", "Misc"));
		tv.addChild(new Category("75", "XviD"));

		// set provider
		normaliseProviderIdInCategories(categories);

	}
}
