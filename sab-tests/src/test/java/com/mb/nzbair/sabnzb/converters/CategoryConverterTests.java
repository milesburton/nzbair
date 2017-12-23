package com.mb.nzbair.sabnzb.converters;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import android.util.Log;

import com.mb.nzbair.sabnzb.SabException;
import com.mb.nzbair.sabnzb.converters.CategoryConverter;

@RunWith(PowerMockRunner.class)
@PrepareForTest(android.util.Log.class)
public class CategoryConverterTests {

	CategoryConverter converter;

	@Before
	public void setup() {
		converter = new CategoryConverter();
		PowerMockito.mockStatic(Log.class);
	}

	@Test(expected = SabException.class)
	public void testInvalidJson() throws Exception {
		String brokenJson = "{\"stat";
		getConverterResponse(brokenJson);
	}

	@Test
	public void testValidCategoryList() throws Exception {
		String jsonData = "{\"categories\":[\"test\"]}";
		List<String> result = getConverterResponse(jsonData);
		assertEquals("test", result.get(0));
		assertEquals(1, result.size());
	}

	private List<String> getConverterResponse(String jsonData) throws Exception {
		List<String> result = (List<String>) converter.parseStream(getInputStream(jsonData), null);
		return result;
	}

	private InputStream getInputStream(String jsonData) {
		InputStream is = new ByteArrayInputStream(jsonData.getBytes());
		return is;
	}

}
