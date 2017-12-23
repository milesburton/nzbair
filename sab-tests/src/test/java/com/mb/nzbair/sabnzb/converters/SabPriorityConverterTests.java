package com.mb.nzbair.sabnzb.converters;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import android.util.Log;

import com.mb.nzbair.sabnzb.SabException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(android.util.Log.class)
public class SabPriorityConverterTests {

	SabPriorityConverter converter;

	@Before
	public void setup() {
		converter = new SabPriorityConverter();
		PowerMockito.mockStatic(Log.class);
	}

	@Test(expected = SabException.class)
	public void testInvalidJson() throws Exception {
		String brokenJson = "{\"stat";
		getConverterResponse(brokenJson);
	}

	@Test
	public void testPriority() throws Exception {
		String jsonData = "{\"position\":1}";
		converter.setRequestedPriority(1);
		Boolean result = getConverterResponse(jsonData);
		assertTrue(result);
	}

	@Test
	public void testForcePrioritySpecialCase() throws Exception {
		String jsonData = "{\"position\":0}";
		converter.setRequestedPriority(2);
		Boolean result = getConverterResponse(jsonData);
		assertTrue(result);
	}

	private Boolean getConverterResponse(String jsonData) throws Exception {
		Boolean result = (Boolean) converter.parseStream(getInputStream(jsonData), null);
		return result;
	}

	private InputStream getInputStream(String jsonData) {
		InputStream is = new ByteArrayInputStream(jsonData.getBytes());
		return is;
	}

}
