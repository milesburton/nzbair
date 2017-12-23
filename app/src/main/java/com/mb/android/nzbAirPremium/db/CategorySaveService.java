
package com.mb.android.nzbAirPremium.db;

import android.content.Context;
import android.widget.Toast;

import com.mb.android.nzbAirPremium.favourites.SaveFavouritesProvider;
import com.mb.android.nzbAirPremium.favourites.SaveSearchProvider;
import com.mb.nzbair.providers.domain.category.Category;

public class CategorySaveService extends SaveService {

	public CategorySaveService(Context context) {
		super(context);
	}

	/**
	 * Save or delete a bookmark for the current activity
	 */
	public void toggleSaveBookmark(Category category, String providerId) {
		final DbEntry db = getEntryForCategory(category, providerId);
		toggleSave(db);
	}

	public void toggleSaveSearch(Category category, String providerId, String keywords) {
		final DbEntry db = getEntryForSaveSearch(category, providerId, keywords);
		toggleSave(db);
	}

	DbEntry getEntryForSaveSearch(Category category, String providerId, String keywords) {
		// Setup variables
		String key;
		String value;
		String bucket;

		// Probably a search
		key = keywords;
		if (category != null && !category.getId().equals("")) {
			value = category.getId();
		} else {
			value = "";
		}
		bucket = SaveSearchProvider.BUCKET_NAME;

		final DbEntry db = new DbEntry();
		db.setBucket(bucket);
		db.setKey(key);
		db.setValue(value);
		db.setExtra(providerId);
		return db;
	}

	DbEntry getEntryForCategory(Category category, String providerId) {
		// Setup variables
		String key;
		String value;

		String bucket;
		// Probably a category
		key = category.generateCategoryIdsAsCSV();
		bucket = SaveFavouritesProvider.BUCKET_NAME;
		value = category.getTitle();

		final DbEntry db = new DbEntry();
		db.setBucket(bucket);
		db.setKey(key);
		db.setValue(value);
		db.setExtra(providerId);
		return db;
	}

	private void toggleSave(DbEntry db) {
		
		String toastText;
		boolean actionResult = false;
		//
		if (bookmarkExists(db)) {
			actionResult = delete(db.getBucket(), db.getKey(), db.getValue(), db.getExtra());
			toastText = "Removed...";

		} else {
			actionResult = create(db.getBucket(), db.getKey(), db.getValue(), db.getExtra());

			toastText = "Saved...";
		}

		if (!actionResult) {
			toastText = "Failed to update";
		}

		Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
	}

	public boolean bookmarkExists(DbEntry db) {
		return exists(db.getBucket(), db.getKey(), db.getValue(), db.getExtra());
	}

}
