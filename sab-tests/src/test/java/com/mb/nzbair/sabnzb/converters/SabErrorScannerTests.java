package com.mb.nzbair.sabnzb.converters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import android.util.Log;

import com.mb.nzbair.sabnzb.SabException;
import com.mb.nzbair.sabnzb.converters.SabErrorScanner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(android.util.Log.class)
public class SabErrorScannerTests {

	@Before
	public void setup() {
		PowerMockito.mockStatic(Log.class);
	}

	@Test(expected = SabException.class)
	public void testInvalidJson() throws Exception {
		String brokenJson = "{\"stat";
		SabErrorScanner.throwErrorIf(brokenJson);
	}

	@Test(expected = SabException.class)
	public void testErrorJson() throws Exception {
		String brokenJson = "{\"error\":\"anError\"}";
		SabErrorScanner.throwErrorIf(brokenJson);
	}

	@Test
	public void testValidJson() throws Exception {
		String goodnJson = "{\"me\":\"you\"}";
		SabErrorScanner.throwErrorIf(goodnJson);
	}
}
