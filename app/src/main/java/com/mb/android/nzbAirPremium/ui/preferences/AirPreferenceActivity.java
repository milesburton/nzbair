
package com.mb.android.nzbAirPremium.ui.preferences;

import com.actionbarsherlock.view.MenuItem;
import com.mb.android.nzbAirPremium.app.Air;
import com.mb.android.nzbAirPremium.ui.helper.MenuHelper;
import com.mb.android.preferences.ui.GenericPreferenceActivity;

public class AirPreferenceActivity extends GenericPreferenceActivity {

	public AirPreferenceActivity() {
		super(Air.get().getConfigManager());
	}

	@Override
	public void onStart() {
		super.onStart();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		MenuHelper.onOptionsItemSelected(this, this, item);
		return super.onOptionsItemSelected(item);
	}

}
