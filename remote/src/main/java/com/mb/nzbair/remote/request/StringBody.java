
package com.mb.nzbair.remote.request;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

class StringBody implements Body {

	private final String body;

	public StringBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return body;
	}

	@Override
	public HttpEntity getHttpEntity() {
		try {
			return new StringEntity(body);
		} catch (final UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof StringBody && obj.toString() == this.toString();
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
}
