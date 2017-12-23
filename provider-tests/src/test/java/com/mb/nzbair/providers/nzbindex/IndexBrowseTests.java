package com.mb.nzbair.providers.nzbindex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
public class IndexBrowseTests {

	private IndexProvider provider;

	@Before
	public void setup() {
		PowerMockito.mockStatic(Log.class);
		provider = new IndexProvider(false, true, 1);
	}

	@Test
	public void testGetBrowseProvider() {

		assertNotNull(provider.getBrowseService());
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
		browse.browse(new RootCategory("", ""), 0, 50);

		Thread.sleep(2000);
		assertEquals(50, callbackResult.getPosts().size());
	}

	@Test
	public void testSearchWithCategory() throws InterruptedException {
		RemoteBrowseProxy browse = (RemoteBrowseProxy) provider.getBrowseService();
		browse.addListener(callback);
		browse.search("Call", new RootCategory("", ""), 0, 50);

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
