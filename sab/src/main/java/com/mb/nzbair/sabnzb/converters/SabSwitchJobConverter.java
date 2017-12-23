
package com.mb.nzbair.sabnzb.converters;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.mb.nzbair.remote.converters.HttpResponseConverter;
import com.mb.nzbair.remote.response.RestResponse;

public class SabSwitchJobConverter implements HttpResponseConverter<Boolean> {

	private static String TAG = SabSwitchJobConverter.class.getName();

	@Override
	public Boolean convert(RestResponse rr) throws Exception {

		final String json = rr.getBodyAsString();

		SabErrorScanner.throwErrorIf(json);

		try {
			final JSONObject obj = new JSONObject(json);

			if (obj.getJSONObject("result").optInt("position") > -1) {
				return true;
			} else {
				return false;
			}
		} catch (final JSONException e) {
			Log.e(TAG, "JSON Parse Exception: " + e.toString());
			return false;
		}

	}

}
