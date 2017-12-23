package com.mb.nzbair.remote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.InputStream;
import java.net.URLConnection;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import android.util.Log;

@RunWith(PowerMockRunner.class)
@PrepareForTest(android.util.Log.class)
public class DownloadWebResourceTests {

	String callbackId;
	Object callbackResponse;
	Object callbackPayload;
	Throwable callbackError;

	IDownloadWebResourceCallback mockCallback = new IDownloadWebResourceCallback() {

		@Override
		public void downloadComplete(String id, Object response, Object payload, Throwable error) {
			DownloadWebResourceTests.this.callbackId = id;
			DownloadWebResourceTests.this.callbackResponse = response;
			DownloadWebResourceTests.this.callbackPayload = payload;
			DownloadWebResourceTests.this.callbackError = error;
		}

		@Override
		public void downloadUpdate(String id, Object response, Object payload) {

		}
	};

	IParseStrategy mockParseStrategy = new IParseStrategy() {

		@Override
		public Object parseStream(InputStream stream, URLConnection con) throws Exception {
			return true;
		}
	};

	IParseStrategy mockErrorParseStrategy = new IParseStrategy() {

		@Override
		public Object parseStream(InputStream stream, URLConnection con) throws Exception {
			throw new Exception("a test error");
		}
	};

	private final String mockUrl = "http://google.com";
	private final String mockId = "test";
	private final Boolean mockPayload = true;

	@Before
	public void setup() {
		PowerMockito.mockStatic(Log.class);
		PowerMockito.mockStatic(DownloadHelper.class);
	}

	@Test
	public void testRequest() {
		DownloadWebResource asyncDownloader = new DownloadWebResource(mockCallback, mockUrl, new HashMap<String, String>(), true, mockId, mockParseStrategy, mockPayload);
		asyncDownloader.run();
		assertEquals(mockId, callbackId);
		assertEquals(mockPayload, callbackPayload);
		assertEquals(true, callbackResponse);
		assertNull(callbackError);
	}

	@Test()
	public void testErrorRequest() {
		DownloadWebResource asyncDownloader = new DownloadWebResource(mockCallback, mockUrl, new HashMap<String, String>(), true, mockId, mockErrorParseStrategy, mockPayload);
		asyncDownloader.run();
		assertEquals(mockId, callbackId);
		assertEquals(mockPayload, callbackPayload);
		assertNull(callbackResponse);
		assertNotNull(callbackError);
	}
}
