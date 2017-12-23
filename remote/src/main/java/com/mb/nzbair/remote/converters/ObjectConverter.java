
package com.mb.nzbair.remote.converters;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;

import com.mb.nzbair.remote.response.RestResponse;

public class ObjectConverter<T extends Object> implements HttpResponseConverter<T> {

	private ObjectMapper objectMapper = null;
	static private JsonFactory jsonFactory = null;
	private JsonParser jp = null;
	private Class<T> type = null;

	static {
		jsonFactory = new JsonFactory();
	}

	public ObjectConverter(Class<T> type) {
		final ObjectMapper om = new ObjectMapper();
		objectMapper = om.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.type = type;
	}

	@Override
	public T convert(RestResponse response) throws Exception {

		final Reader r = new InputStreamReader(response.getStream());
		final BufferedReader reader = new BufferedReader(r);
		jp = jsonFactory.createJsonParser(reader);
		return objectMapper.readValue(jp, type);
	}
}
