
package com.mb.android.nzbAirPremium.favourites;

import java.util.Comparator;

import com.mb.nzbair.providers.domain.category.Category;

public class CategoryComparer implements Comparator<Category> {

	@Override
	public int compare(Category s1, Category s2) {
		return s1.getTitle().compareToIgnoreCase(s2.getTitle());
	}
}