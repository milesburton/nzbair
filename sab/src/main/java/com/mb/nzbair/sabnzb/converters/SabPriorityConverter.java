
package com.mb.nzbair.sabnzb.converters;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.mb.nzbair.remote.converters.HttpResponseConverter;
import com.mb.nzbair.remote.response.RestResponse;

public class SabPriorityConverter implements HttpResponseConverter<Boolean> {

	private static String TAG = SabPriorityConverter.class.getName();

	Integer requestedPriority = -2;

	@Override
	public Boolean convert(RestResponse rr) throws Exception {

		final String json = rr.getBodyAsString();

		SabErrorScanner.throwErrorIf(json);

		try {
			final JSONObject obj = new JSONObject(json);
			final int newPos = obj.getInt("position");
			if (requestedPriority == 2 && newPos == 0) {
				return true;
			} else {
				return newPos == requestedPriority;
			}

		} catch (final JSONException e) {
			Log.e(TAG, "JSON Parse Exception: " + e.toString());
			return false;
		}

	}

	public void setRequestedPriority(int i) {
		requestedPriority = i;
	}

}
