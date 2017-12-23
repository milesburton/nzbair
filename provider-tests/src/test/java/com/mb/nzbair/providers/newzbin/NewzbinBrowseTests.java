package com.mb.nzbair.providers.newzbin;

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
import com.mb.nzbair.providers.domain.category.RootCategory;
import com.mb.nzbair.providers.interfaces.BrowseCallback;

@RunWith(PowerMockRunner.class)
@PrepareForTest(android.util.Log.class)
public class NewzbinBrowseTests {

	private NewzbinProvider provider;

	@Before
	public void setup() {
		PowerMockito.mockStatic(Log.class);
		provider = new NewzbinProvider("test", "test", false, "10907816", "xabre5000", true, 1);
	}

	@Test
	public void testGetBrowseProvider() {
		RemoteBrowseProxy service = (RemoteBrowseProxy) provider.getBrowseService();
		assertEquals("xabre5000", service.getParams().get("username"));
		assertEquals("10907816", service.getParams().get("password"));
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
		browse.browse(new RootCategory("4", "Games"), 0, 50);

		Thread.sleep(2000);
		assertEquals(50, callbackResult.getPosts().size());
	}

	@Test
	public void testSearchWithCategory() throws InterruptedException {
		RemoteBrowseProxy browse = (RemoteBrowseProxy) provider.getBrowseService();
		browse.addListener(callback);
		browse.search("Call", new RootCategory("4", "Games"), 0, 50);

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
