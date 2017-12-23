
package com.mb.nzbair.sabnzb.converters;

import com.mb.nzbair.remote.converters.HttpResponseConverter;
import com.mb.nzbair.remote.response.RestResponse;

public class BooleanResponseConverter implements HttpResponseConverter<Boolean> {

	@Override
	public Boolean convert(RestResponse rr) throws Exception {

		final String response = rr.getBodyAsString();

		SabErrorScanner.throwErrorIf(response);

		return (response.contains("ok") || response.contains("{\"status\":true}"));
	}

}
