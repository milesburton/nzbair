
package com.mb.android.nzbAirPremium.ui;

import android.os.Build;
import android.os.Bundle;

import com.actionbarsherlock.view.MenuItem;
import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.db.CategorySaveService;
import com.mb.android.nzbAirPremium.ui.fragments.BrowseMetadata;

public abstract class BasePosts extends BaseBrowse {

	CategorySaveService saveService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		saveService = new CategorySaveService(getApplicationContext());
	}

	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case R.id.bookmarkButton:
			final BrowseMetadata md = (BrowseMetadata) fragment;
			saveService.toggleSaveBookmark(md.getCategory(), md.getProvider().getId());
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		final MenuItem b = menu.add(android.view.Menu.NONE, R.id.bookmarkButton, android.view.Menu.NONE, "Bookmark").setIcon(android.R.drawable.btn_star);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			b.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		} else {
			b.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}

		return super.onCreateOptionsMenu(menu);

	}

}
