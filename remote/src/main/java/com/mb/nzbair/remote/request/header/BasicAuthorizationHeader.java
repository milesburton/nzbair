
package com.mb.nzbair.remote.request.header;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;

import com.mb.nzbair.remote.utils.Base64;

public class BasicAuthorizationHeader implements Header {

	private final String base64;

	public BasicAuthorizationHeader(String username, String password) {

		final String keyValue = username + ":" + password;
		base64 = Base64.encodeBytes(keyValue.getBytes());
	}

	@Override
	public HeaderElement[] getElements() throws ParseException {
		return null;
	}

	@Override
	public String getName() {
		return "Authorization";
	}

	@Override
	public String getValue() {
		return "Basic " + base64;
	}

	public static class EncodeException extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7220187831630423078L;

	}
}
