
package com.mb.android.nzbAirPremium.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.ui.fragments.LocalDownloadFragment;
import com.mb.android.nzbAirPremium.ui.fragments.Refreshable;
import com.mb.android.nzbAirPremium.ui.fragments.SabDetailsFragment;
import com.mb.android.nzbAirPremium.ui.fragments.SabDownloadsFragment;
import com.mb.android.nzbAirPremium.ui.fragments.SabHistoryFragment;
import com.mb.android.nzbAirPremium.ui.fragments.adapters.PageAdapter;
import com.viewpagerindicator.TitlePageIndicator;

public class DownloadsActivity extends BaseFragmentActivity implements OnPageChangeListener {

	static final String TAG = DownloadsActivity.class.getName();

	PageAdapter mAdapter;
	ViewPager mPager;
	TitlePageIndicator indicator;

	public static String STARTUP_CLASS = "startupClass";

	int selectedPage = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_downloads);

		// /* Setup pager */
		mAdapter = new PageAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		// /* Setup titles */
		indicator = (TitlePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(mPager);
		indicator.setOnPageChangeListener(this);

		// PREMIUM_START
		mAdapter.add(SabDetailsFragment.getInstance(this));
		mAdapter.add(SabDownloadsFragment.getInstance(this));
		mAdapter.add(SabHistoryFragment.getInstance(this));
		// PREMIUM_END
		mAdapter.add(LocalDownloadFragment.getInstance(this));

	}

	@Override
	protected void onStart() {

		super.onStart();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		if (getIntent().hasExtra(STARTUP_CLASS)) {
			final String startupClass = getIntent().getStringExtra(STARTUP_CLASS);
			final Integer position = mAdapter.contains(startupClass);
			if (position == null) {
				Log.w(TAG, "Could not find " + startupClass);

			}
			indicator.setCurrentItem(position);
			mPager.setCurrentItem(position);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		final SherlockFragment f = getFragmentByPage();

		// Pass to sab base to handle pausing/resume etc
		if (f != null) {
			final boolean hasHandledMenuQuery = f.onOptionsItemSelected(item);
			if (hasHandledMenuQuery) {
				return true;
			}
		}

		switch (item.getItemId()) {
		case R.id.action_refresh:
			if (f instanceof Refreshable) {
				((Refreshable) f).refresh();
			}
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getSupportMenuInflater().inflate(R.menu.browse_menu, menu);
		final SherlockFragment f = getFragmentByPage();

		if (f != null) {
			f.onCreateOptionsMenu(menu, getSupportMenuInflater());
		}

		return super.onCreateOptionsMenu(menu);
	}

	private SherlockFragment getFragmentByPage() {

		SherlockFragment f = null;
		if (selectedPage > -1) {
			f = (SherlockFragment) mAdapter.getItem(selectedPage);
		}
		return f;
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {

		selectedPage = position;
		invalidateOptionsMenu();
	}

}
