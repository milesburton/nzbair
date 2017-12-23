
package com.mb.nzbair.providers.converters;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import com.mb.nzbair.providers.domain.UsenetPostResult;
import com.mb.nzbair.remote.response.RestResponse;

public class UsenetPostResultConverter extends BaseConverter<UsenetPostResult> {

	private ObjectMapper objectMapper = null;
	private JsonFactory jsonFactory = null;
	private JsonParser jp = null;

	public UsenetPostResultConverter() {
		objectMapper = new ObjectMapper();
		jsonFactory = new JsonFactory();
	}

	@Override
	public UsenetPostResult convert(RestResponse rr) throws Exception {

		this.throwErrorIf(rr);
		final Reader r = new InputStreamReader(rr.getStream());
		final BufferedReader reader = new BufferedReader(r);
		jp = jsonFactory.createJsonParser(reader);
		final UsenetPostResult upr = objectMapper.readValue(jp, UsenetPostResult.class);

		return upr;
	}

}
