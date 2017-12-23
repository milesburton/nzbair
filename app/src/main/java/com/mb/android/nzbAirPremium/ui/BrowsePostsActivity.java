
package com.mb.android.nzbAirPremium.ui;

import android.os.Bundle;

import com.mb.android.nzbAirPremium.R;

public class BrowsePostsActivity extends BasePosts {

	final String TAG = BrowsePostsActivity.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse_posts);
	}

	@Override
	protected void onStart() {
		fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_browse_posts);
		super.onStart();
	}

}