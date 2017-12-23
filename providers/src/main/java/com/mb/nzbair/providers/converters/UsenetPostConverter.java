
package com.mb.nzbair.providers.converters;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import com.mb.nzbair.providers.domain.UsenetPost;
import com.mb.nzbair.remote.response.RestResponse;

public class UsenetPostConverter extends BaseConverter<UsenetPost> {

	private ObjectMapper objectMapper = null;
	private JsonFactory jsonFactory = null;
	private JsonParser jp = null;

	public UsenetPostConverter() {
		objectMapper = new ObjectMapper();
		jsonFactory = new JsonFactory();
	}

	@Override
	public UsenetPost convert(RestResponse rr) throws Exception {

		this.throwErrorIf(rr);

		final Reader r = new InputStreamReader(rr.getStream());
		final BufferedReader reader = new BufferedReader(r);
		jp = jsonFactory.createJsonParser(reader);
		final UsenetPost upr = objectMapper.readValue(jp, UsenetPost.class);
		return upr;
	}

}
