
package com.mb.android.nzbAirPremium.ui.fragments.adapters;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.mb.android.nzbAirPremium.ui.fragments.FragmentMetadata;

public class PageAdapter extends FragmentPagerAdapter {

	private final List<Fragment> fragments = new ArrayList<Fragment>();

	public PageAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public Fragment getItem(int position) {
		if (fragments.size() == position) {
			return null;
		}
		return fragments.get(position);
	}

	public void add(Fragment frag) {
		fragments.add(frag);
		this.notifyDataSetChanged();
	}

	public void restoreFragments(View v, int length) {
		for (int i = 0; i < length; i++) {
			fragments.add(null);
		}
	}

	/**
	 * Set position of class (NB: assumes no duplicate classes!)
	 * 
	 * @param canonicalClassName
	 * @return
	 */
	public Integer contains(String canonicalClassName) {
		int i = 0;
		for (final Fragment frag : fragments) {
			if (frag.getClass().getCanonicalName().equals(canonicalClassName)) {
				return i;
			}
			i++;
		}
		return i;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		try {
			return ((FragmentMetadata) getItem(position)).getTitle();
		} catch (final Exception ex) {
			return "Unknown Page";
		}
	}

	@Override
	public Object instantiateItem(View container, int position) {
		return super.instantiateItem(container, position);
	}

}
