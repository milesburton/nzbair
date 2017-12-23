package com.mb.nzbair.remote.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.mb.nzbair.remote.utils.QueryStringBuilder;

public class QueryStringBuilderTests {

	private List<String> defaultFields;

	@Before
	public void setup() {
		defaultFields = new ArrayList<String>();
		defaultFields.add("test");
		defaultFields.add("foo");
		defaultFields.add("bar");
	}

	@Test
	public void testMergeBasicParams() throws Exception {

		Map<String, String> defaults = new HashMap<String, String>();
		defaults.put("test", "testTest");
		defaults.put("foo", "testFoo");
		defaults.put("bar", "testBar");

		Map<String, String> queryString = new HashMap<String, String>();
		queryString.put("test", "qsTest");
		queryString.put("bar", "qsBar");

		Map<String, String> result = QueryStringBuilder.mergeParams(defaultFields, defaults, queryString);

		assertEquals("qsTest", result.get("test"));
		assertEquals("qsBar", result.get("bar"));
		assertEquals("testFoo", result.get("foo"));
	}

	@Test
	public void testMergeDefaultsWithUnneededParams() throws Exception {

		Map<String, String> defaults = new HashMap<String, String>();
		defaults.put("test", "testTest");
		defaults.put("foo", "testFoo");
		defaults.put("bar", "testBar");

		Map<String, String> queryString = new HashMap<String, String>();
		queryString.put("test", "qsTest");
		queryString.put("fizz", "buzz");

		Map<String, String> result = QueryStringBuilder.mergeParams(defaultFields, defaults, queryString);

		assertEquals("qsTest", result.get("test"));
		assertEquals("testBar", result.get("bar"));
		assertNull(result.get("fizz"));
	}

	@Test
	public void testMergeDefaultsWithNoDefaults() throws Exception {

		Map<String, String> defaults = new HashMap<String, String>();

		Map<String, String> queryString = new HashMap<String, String>();
		queryString.put("test", "qsTest");
		queryString.put("bar", "qsFizz");

		Map<String, String> result = QueryStringBuilder.mergeParams(defaultFields, defaults, queryString);

		assertEquals("qsTest", result.get("test"));
		assertEquals("qsFizz", result.get("bar"));
		assertNull(result.get("testFoo"));
	}

	@Test
	public void testBuildParams() throws Exception {

		Map<String, String> fields = new HashMap<String, String>();
		fields.put("test", "testTest");
		fields.put("foo", "testFoo");
		fields.put("bar", "testBar");

		String qs = QueryStringBuilder.buildQueryString(fields);

		assertEquals("test=testTest&foo=testFoo&bar=testBar&", qs);

	}

	@Test
	public void testBuildParamsHtmlEncode() throws Exception {

		Map<String, String> fields = new HashMap<String, String>();
		fields.put("test", "testTest");
		fields.put("foo", "char & ");
		fields.put("bar", "foo bar");

		String qs = QueryStringBuilder.buildQueryString(fields);

		assertEquals("test=testTest&foo=char+%26+&bar=foo+bar&", qs);

	}
}
