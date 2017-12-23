package com.mb.nzbair.providers.matrix;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import android.util.Log;

import com.mb.nzbair.providers.base.RemoteBrowseProxy;
import com.mb.nzbair.providers.domain.UsenetPostResult;
import com.mb.nzbair.providers.domain.category.Category;
import com.mb.nzbair.providers.domain.category.RootCategory;
import com.mb.nzbair.providers.interfaces.BrowseCallback;

@RunWith(PowerMockRunner.class)
@PrepareForTest(android.util.Log.class)
public class MatrixBrowseTests {

	private MatrixProvider provider;

	@Before
	public void setup() {
		PowerMockito.mockStatic(Log.class);
		provider = new MatrixProvider("test", "test", false, "fe0d5ded9d7044957d2fb788c7eb1002", "xabre6000", true, true, 1);
	}

	@Test
	public void testConfigure() {
		provider.configure("test", false, "", "", false, false);
		assertEquals("false", provider.getBrowseParams().get("isEnglish"));
		assertEquals("", provider.getBrowseParams().get("apikey"));
		assertEquals("", provider.getBrowseParams().get("username"));
		assertEquals("false", provider.getBrowseParams().get("isEnglish"));
	}

	@Test
	public void testGetBrowseProvider() {
		RemoteBrowseProxy service = (RemoteBrowseProxy) provider.getBrowseService();
		assertEquals("fe0d5ded9d7044957d2fb788c7eb1002", service.getParams().get("apikey"));
		assertEquals("xabre6000", service.getParams().get("username"));
	}

	@Test
	public void testBrowse() throws InterruptedException {
		RemoteBrowseProxy browse = (RemoteBrowseProxy) provider.getBrowseService();
		browse.addListener(callback);
		browse.browse(null, 0, 50);

		Thread.sleep(2000);
		assertEquals(50, callbackResult.getPosts().size());
	}

	@Test
	public void testSearch() throws InterruptedException {
		RemoteBrowseProxy browse = (RemoteBrowseProxy) provider.getBrowseService();
		browse.addListener(callback);
		browse.search("house", null, 0, 50);

		Thread.sleep(2000);
		assertEquals(50, callbackResult.getPosts().size());
	}

	@Test
	public void testSearchWithSpace() throws InterruptedException {
		RemoteBrowseProxy browse = (RemoteBrowseProxy) provider.getBrowseService();
		browse.addListener(callback);
		browse.search("the simpsons", null, 0, 50);

		Thread.sleep(2000);
		assertEquals(50, callbackResult.getPosts().size());
	}

	@Test
	public void testBrowseWithCategory() throws InterruptedException {
		RemoteBrowseProxy browse = (RemoteBrowseProxy) provider.getBrowseService();
		browse.addListener(callback);
		browse.browse(new Category("20", "Apps : Linux"), 0, 50);

		Thread.sleep(2000);
		assertEquals(50, callbackResult.getPosts().size());
	}

	@Test
	public void testSearchWithCategory() throws InterruptedException {
		RemoteBrowseProxy browse = (RemoteBrowseProxy) provider.getBrowseService();
		browse.addListener(callback);
		browse.search("linux", new Category("20", "Apps : Linux"), 0, 50);

		Thread.sleep(2000);
		assertEquals(50, callbackResult.getPosts().size());
	}

	@Test
	public void testBrowseWithMultipleCategory() throws InterruptedException {
		RemoteBrowseProxy browse = (RemoteBrowseProxy) provider.getBrowseService();
		browse.addListener(callback);

		RootCategory Apps = new RootCategory("101", "Apps");
		Apps.addChild(new Category("20", "Apps : Linux"));
		Apps.addChild(new Category("19", "Apps : Mac"));
		Apps.addChild(new Category("21", "Apps : Other"));
		Apps.addChild(new Category("18", "Apps : PC"));
		Apps.addChild(new Category("55", "Apps : Phone"));

		browse.browse(Apps, 0, 50);

		Thread.sleep(2000);
		assertEquals(50, callbackResult.getPosts().size());
	}

	@Test
	public void testSearchWithMultipleCategory() throws InterruptedException {
		RemoteBrowseProxy browse = (RemoteBrowseProxy) provider.getBrowseService();
		browse.addListener(callback);

		RootCategory Apps = new RootCategory("101", "Apps");
		Apps.addChild(new Category("20", "Apps : Linux"));
		Apps.addChild(new Category("19", "Apps : Mac"));
		Apps.addChild(new Category("21", "Apps : Other"));
		Apps.addChild(new Category("18", "Apps : PC"));
		Apps.addChild(new Category("55", "Apps : Phone"));

		browse.search("house", Apps, 0, 50);

		Thread.sleep(2000);
		assertEquals(50, callbackResult.getPosts().size());
	}

	private UsenetPostResult callbackResult;
	private Throwable callbackError;

	private final BrowseCallback callback = new BrowseCallback() {

		@Override
		public void onResponseCategory(UsenetPostResult postResult, Throwable error) {
			callbackResult = postResult;
			callbackError = error;

		}

		@Override
		public void onResponseSearch(UsenetPostResult postResult, Throwable error) {
			callbackResult = postResult;
			callbackError = error;

		}
	};

}
