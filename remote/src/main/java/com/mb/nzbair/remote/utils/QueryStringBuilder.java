
package com.mb.nzbair.remote.utils;

import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class QueryStringBuilder {

	public static Map<String, String> mergeParams(List<String> fields, Map<String, String> defaults, Map<String, String> queryString) {

		final Map<String, String> result = new Hashtable<String, String>();
		for (final String key : fields) {
			if (queryString.get(key) == null && defaults.get(key) != null) {
				result.put(key, defaults.get(key));
			} else if (queryString.get(key) != null) {
				result.put(key, queryString.get(key));
			}
		}

		return result;
	}

	public static String buildQueryString(Map<String, String> fields) {
		final StringBuffer sb = new StringBuffer();

		for (final Map.Entry<String, String> kv : fields.entrySet()) {

			sb.append(kv.getKey() + "=" + URLEncoder.encode(kv.getValue()));
			sb.append("&");
		}

		return sb.toString();
	}

}
