
package com.mb.android.nzbAirPremium.search;

/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.mb.nzbair.remote.RestClient;

/**
 * Provides access to the dictionary database.
 */
public class PostHintProvider extends ContentProvider {

	String TAG = PostHintProvider.class.getName();

	public static String AUTHORITY = PostHintProvider.class.getName();
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/keywords");

	String[] cols = new String[3];
	String[] row = new String[3];

	String apiUrl = "http://www.nzbair.com/providers/nzbair/hint/";

	public PostHintProvider() {
		// Setup cols
		cols[0] = BaseColumns._ID;
		cols[1] = SearchManager.SUGGEST_COLUMN_TEXT_1;
		cols[2] = SearchManager.SUGGEST_COLUMN_QUERY;
	}

	@Override
	public boolean onCreate() {
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		final String query = uri.getLastPathSegment().toLowerCase();

		// Prepare stock cursor
		final MatrixCursor c = new MatrixCursor(cols);

		if (!query.equals(SearchManager.SUGGEST_URI_PATH_QUERY)) {
			final Map<String, String> params = new Hashtable<String, String>();
			params.put("keyword", query);
			try {
				String csv = new RestClient(new DefaultHttpClient()).get(apiUrl, new HashMap<String, String>(), new ArrayList<Header>()).getBodyAsString();
				csv = csv.replace("\n", "");
				final String[] hints = csv.split(",");
				for (final String hint : hints) {

					if (hint.equals("")) {
						continue;
					}
					row[0] = "0";
					row[1] = hint;
					row[2] = hint;
					c.addRow(row);
				}
			} catch (final Exception e) {

			}

		}

		return c;

	}

	/**
	 * This method is required in order to query the supported types. It's also
	 * useful in our own query() method to determine the type of Uri received.
	 */

	@Override
	public String getType(Uri uri) {
		return "Meh";
	}

	// Other required implementations...

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		throw new UnsupportedOperationException();
	}

}