
package com.mb.nzbair.remote.converters;

import com.mb.nzbair.remote.response.RestResponse;

public class BooleanConverter implements HttpResponseConverter<Boolean> {

	@Override
	public Boolean convert(RestResponse r) throws Exception {

		final String response = r.getBodyAsString().replace('\n', ' ').trim();
		return Boolean.parseBoolean(response);
	}

}
