package com.mb.nzbair.remote;

import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.net.MalformedURLException;
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
public class DownloadHelperTests {

	private String invalidSecureUrl;
	private String nonsecureUrl;
	private String secureUrl;
	private DownloadHelper downloader;
	private IParseStrategy mockParseStratagy;

	@Before
	public void setup() {
		downloader = new DownloadHelper();
		nonsecureUrl = "http://demo.milesburton.com/test/nzbair/mock/providers/newzbin/reportfind/";
		secureUrl = "http://demo.milesburton.com/test/nzbair/mock/providers/newzbin/reportfind/";
		invalidSecureUrl = "https://demo.milesburton.com/test/nzbair/mock/providers/newzbin/reportfind/";
		mockParseStratagy = new IParseStrategy() {

			@Override
			public Object parseStream(InputStream stream, URLConnection con) throws Exception {
				return true;
			}
		};

		PowerMockito.mockStatic(Log.class);
	}

	@Test
	public void testGetUrl() throws Exception {

		boolean mockResponse = (Boolean) downloader.getUrl(nonsecureUrl, new HashMap<String, String>(), mockParseStratagy, false);
		assertTrue(mockResponse);
	}

	@Test
	public void testGetSecureUrl() throws Exception {

		boolean mockResponse = (Boolean) downloader.getUrl(secureUrl, new HashMap<String, String>(), mockParseStratagy, false);
		assertTrue(mockResponse);
	}

	@Test
	public void testGetInvalidSecureUrl() throws Exception {
		boolean mockResponse = (Boolean) downloader.getUrl(secureUrl, new HashMap<String, String>(), mockParseStratagy, true);
		assertTrue(mockResponse);
	}

	@Test(expected = MalformedURLException.class)
	public void testInvalidGetUrl() throws Exception {

		downloader.getUrl("", new HashMap<String, String>(), mockParseStratagy, true);

	}
}
