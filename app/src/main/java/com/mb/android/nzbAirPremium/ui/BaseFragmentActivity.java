
package com.mb.android.nzbAirPremium.ui;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.SearchView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.ui.helper.MenuHelper;

public abstract class BaseFragmentActivity extends SherlockFragmentActivity {

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {

		ifHoneycombImplementSearch(menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return MenuHelper.onOptionsItemSelected(this, this, item);
	}

	@TargetApi(11)
	void ifHoneycombImplementSearch(com.actionbarsherlock.view.Menu menu) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
			final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
			searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
			searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

				@Override
				public boolean onQueryTextChange(String newText) {
					return false;
				}

				@Override
				public boolean onQueryTextSubmit(String query) {
					final Intent i = new Intent(BaseFragmentActivity.this, SearchPostsActivity.class);
					i.putExtra(SearchManager.APP_DATA, getSearchBundleForContext());
					i.putExtra(SearchManager.QUERY, query);
					startActivity(i);
					return true;
				}
			});
		}
	}

	@Override
	public boolean onSearchRequested() {
		startSearch(null, false, getSearchBundleForContext(), false);
		return true;
	}

	protected Bundle getSearchBundleForContext() {
		return new Bundle();
	}
}
