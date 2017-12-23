
package com.mb.nzbair.providers.converters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;

import com.mb.nzbair.remote.converters.HttpResponseConverter;
import com.mb.nzbair.remote.response.RestResponse;
import com.mb.nzbair.seh.ProviderException;

public abstract class BaseConverter<T> implements HttpResponseConverter<T> {

	private ObjectMapper objectMapper = null;
	private JsonFactory jsonFactory = null;
	private JsonParser jp = null;

	public BaseConverter() {
		final ObjectMapper om = new ObjectMapper();
		objectMapper = om.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		jsonFactory = new JsonFactory();
	}

	protected void throwErrorIf(RestResponse rr) throws ProviderException, IOException {

		if (rr.getStatus() != 203 && rr.getStatus() != 500) {
			return;
		}

		final Reader r = new InputStreamReader(rr.getStream());
		final BufferedReader reader = new BufferedReader(r);
		jp = jsonFactory.createJsonParser(reader);
		final ProviderException e = objectMapper.readValue(jp, ProviderException.class);
		throw e;
	}
}
