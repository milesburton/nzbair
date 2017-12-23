package com.mb.nzbair.providers.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.mb.nzbair.providers.domain.UsenetPost;
import com.mb.nzbair.providers.interfaces.DetailsCallback;

public class DetailsProxyTests {

	DetailsProxy proxy;

	@Before
	public void setup() {
		proxy = new DetailsProxy("test", "", false, false, 1);
	}

	@Test
	public void testReturnsImmediately() {
		UsenetPost mockPost = new UsenetPost();
		proxy.addListener(mockCallback);
		proxy.requestPostDetails(mockPost);
		assertEquals(mockPost, callbackPost);
		assertNull(callbackError);
	}

	UsenetPost callbackPost;
	Throwable callbackError;

	private final DetailsCallback mockCallback = new DetailsCallback() {

		@Override
		public void responseDetails(UsenetPost post, Throwable error) {
			callbackPost = post;
			callbackError = error;

		}
	};
}
