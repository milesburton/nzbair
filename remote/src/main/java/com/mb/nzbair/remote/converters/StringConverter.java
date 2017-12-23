
package com.mb.nzbair.remote.converters;

import java.io.IOException;

import com.mb.nzbair.remote.response.RestResponse;

public class StringConverter implements HttpResponseConverter<String> {

	@Override
	public String convert(RestResponse r) throws IOException {

		return r.getBodyAsString();
	}

}
