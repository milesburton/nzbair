package com.mb.nzbair.providers.nzbsu;

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
import com.mb.nzbair.providers.domain.category.BranchCategory;
import com.mb.nzbair.providers.domain.category.Category;
import com.mb.nzbair.providers.domain.category.ParentCategory;
import com.mb.nzbair.providers.domain.category.RootCategory;
import com.mb.nzbair.providers.interfaces.BrowseCallback;
import com.mb.nzbair.providers.newznab.NewznabProvider;

@RunWith(PowerMockRunner.class)
@PrepareForTest(android.util.Log.class)
public class SuBrowseTests {

	private NewznabProvider provider;

	@Before
	public void setup() {
		PowerMockito.mockStatic(Log.class);
		provider = new NewznabProvider("nzbzu", "NZBSu", false, true, 1, "610fda1af16adbe270cb5096328c6ab2", "http://nzb.su/api");
	}

	@Test
	public void testGetBrowseProvider() {

		RemoteBrowseProxy proxy = (RemoteBrowseProxy) provider.getBrowseService();
		assertNotNull(proxy);
		assertEquals("610fda1af16adbe270cb5096328c6ab2", proxy.getParams().get("apikey"));
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
		browse.browse(new Category("1010", "NDS"), 0, 50);

		Thread.sleep(2000);
		assertEquals(50, callbackResult.getPosts().size());
	}

	@Test
	public void testSearchWithCategory() throws InterruptedException {
		RemoteBrowseProxy browse = (RemoteBrowseProxy) provider.getBrowseService();
		browse.addListener(callback);
		browse.search("Call", new Category("2000", "All"), 0, 50);

		Thread.sleep(2000);
		assertEquals(50, callbackResult.getPosts().size());
	}

	@Test
	public void testBrowseWithMultipleCategory() throws InterruptedException {
		RemoteBrowseProxy browse = (RemoteBrowseProxy) provider.getBrowseService();
		browse.addListener(callback);

		RootCategory Console = new RootCategory("2000", "Console");
		Console.addChild(new ParentCategory());
		Console.addChild(new BranchCategory("2000", "All"));
		Console.addChild(new Category("1010", "NDS"));
		Console.addChild(new Category("1020", "PSP"));
		Console.addChild(new Category("1030", "Wii"));
		Console.addChild(new Category("1040", "Xbox"));
		Console.addChild(new Category("1050", "Xbox 360"));
		Console.addChild(new Category("1060", "WiiWare"));
		Console.addChild(new Category("1070", "XBOX 360 DLC"));
		Console.addChild(new Category("1080", "PS3"));

		browse.browse(Console, 0, 50);

		Thread.sleep(2000);
		assertEquals(50, callbackResult.getPosts().size());
	}

	@Test
	public void testSearchWithMultipleCategory() throws InterruptedException {
		RemoteBrowseProxy browse = (RemoteBrowseProxy) provider.getBrowseService();
		browse.addListener(callback);

		RootCategory Console = new RootCategory("2000", "Console");
		Console.addChild(new ParentCategory());
		Console.addChild(new BranchCategory("2000", "All"));
		Console.addChild(new Category("1010", "NDS"));
		Console.addChild(new Category("1020", "PSP"));
		Console.addChild(new Category("1030", "Wii"));
		Console.addChild(new Category("1040", "Xbox"));
		Console.addChild(new Category("1050", "Xbox 360"));
		Console.addChild(new Category("1060", "WiiWare"));
		Console.addChild(new Category("1070", "XBOX 360 DLC"));
		Console.addChild(new Category("1080", "PS3"));

		browse.search("house", Console, 0, 50);

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
