package com.mb.nzbair.remote.converters;

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

@RunWith(PowerMockRunner.class)
@PrepareForTest(android.util.Log.class)
public class BooleanConverterTests {

	BooleanConverter converter;

	@Before
	public void setup() {
		converter = new BooleanConverter();
		PowerMockito.mockStatic(Log.class);
	}

	@Test
	public void testInvalidBoolean() throws Exception {
		String inputData = "blarg";
		InputStream stream = getInputStream(inputData);
		Boolean result = (Boolean) converter.parseStream(stream, null);
		assertTrue(!result);
	}

	@Test
	public void testValidFalseBoolean() throws Exception {
		String inputData = "false";
		InputStream stream = getInputStream(inputData);
		Boolean result = (Boolean) converter.parseStream(stream, null);
		assertTrue(!result);
	}

	@Test
	public void testValidTrueBoolean() throws Exception {
		String inputData = "true";
		InputStream stream = getInputStream(inputData);
		Boolean result = (Boolean) converter.parseStream(stream, null);
		assertTrue(result);
	}

	@Test
	public void testValidTrueBooleanWithNewLine() throws Exception {
		String inputData = "true\n";
		InputStream stream = getInputStream(inputData);
		Boolean result = (Boolean) converter.parseStream(stream, null);
		assertTrue(result);
	}

	private InputStream getInputStream(String jsonData) {
		InputStream is = new ByteArrayInputStream(jsonData.getBytes());
		return is;
	}

}
