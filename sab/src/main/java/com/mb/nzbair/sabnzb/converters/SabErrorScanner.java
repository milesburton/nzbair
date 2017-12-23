
package com.mb.nzbair.sabnzb.converters;

import org.json.JSONException;
import org.json.JSONObject;

import com.mb.nzbair.sabnzb.SabException;

public class SabErrorScanner {

	public static void throwErrorIf(String str) throws Exception {

		try {
			final JSONObject obj = new JSONObject(str);
			if (obj.has("error")) {
				throw new SabException(obj.get("error").toString());
			}
		} catch (final JSONException ex) {
			throw new SabException(ex.getMessage());
		}

	}
}
