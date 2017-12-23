
package com.mb.android.nzbAirPremium.ui;

import android.os.Bundle;

import com.mb.android.nzbAirPremium.R;

public class FavouritesActivity extends BaseBrowse {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favourites);
	}

	@Override
	protected void onStart() {
		fragment = this.getSupportFragmentManager().findFragmentById(R.id.fragment_favourites);
		super.onStart();
	}
}