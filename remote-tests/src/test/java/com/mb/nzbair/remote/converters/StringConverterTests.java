package com.mb.nzbair.remote.converters;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import android.util.Log;

@RunWith(PowerMockRunner.class)
@PrepareForTest(android.util.Log.class)
public class StringConverterTests {

	StringConverter converter;

	@Before
	public void setup() {
		converter = new StringConverter();
		PowerMockito.mockStatic(Log.class);
	}

	@Test
	public void testEmptyString() throws Exception {
		String inputData = "";
		InputStream stream = getInputStream(inputData);
		String result = (String) converter.parseStream(stream, null);
		assertEquals("", result);
	}

	@Test
	public void testValidString() throws Exception {
		String inputData = "test";
		InputStream stream = getInputStream(inputData);
		String result = (String) converter.parseStream(stream, null);
		assertEquals("test", result);
	}

	private InputStream getInputStream(String jsonData) {
		InputStream is = new ByteArrayInputStream(jsonData.getBytes());
		return is;
	}

}
