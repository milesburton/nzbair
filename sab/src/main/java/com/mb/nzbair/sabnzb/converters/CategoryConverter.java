
package com.mb.nzbair.sabnzb.converters;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.mb.nzbair.remote.converters.HttpResponseConverter;
import com.mb.nzbair.remote.response.RestResponse;
import com.mb.nzbair.sabnzb.SabException;

public class CategoryConverter implements HttpResponseConverter<List<String>> {

	private static String TAG = CategoryConverter.class.toString();

	@Override
	public List<String> convert(RestResponse rr) throws Exception {

		final String json = rr.getBodyAsString();

		SabErrorScanner.throwErrorIf(json);

		try {
			final JSONObject obj = new JSONObject(json);
			final JSONArray arr = obj.getJSONArray("categories");
			final List<String> cats = new ArrayList<String>();
			for (int i = 0; i < arr.length(); ++i) {
				cats.add(arr.getString(i));
			}
			return cats;
		} catch (final JSONException e) {
			Log.e(TAG, "JSON Parse Exception: " + e.toString());
			throw new SabException("Could not parse remote response");
		}
	}
}