package com.mb.nzbair.providers.newzbin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import android.util.Log;

import com.mb.nzbair.providers.base.FavouritesProxy;
import com.mb.nzbair.providers.domain.UsenetPost;
import com.mb.nzbair.providers.domain.UsenetPostResult;
import com.mb.nzbair.providers.interfaces.FavouritesCallback;

@RunWith(PowerMockRunner.class)
@PrepareForTest(android.util.Log.class)
public class NewzbinFavouritesTests {

	private NewzbinProvider provider;

	@Before
	public void setup() {
		PowerMockito.mockStatic(Log.class);
		provider = new NewzbinProvider("test", "test", false, "10907816", "xabre5000", true, 1);
	}

	@Test
	public void testGetBrowseProvider() {
		FavouritesProxy details = (FavouritesProxy) provider.getFavouritesService();
		assertEquals("xabre5000", details.getParams().get("username"));
		assertEquals("10907816", details.getParams().get("password"));
	}

	@Test
	public void testAddFavourite() throws InterruptedException {
		FavouritesProxy details = (FavouritesProxy) provider.getFavouritesService();
		UsenetPost post = new UsenetPost();
		post.setId("6191900");
		details.addListener(callback);
		details.requestAddFavourite(post);

		Thread.sleep(2000);
		assertTrue(response);
	}

	@Test
	public void testRequestFavourites() throws InterruptedException {
		FavouritesProxy details = (FavouritesProxy) provider.getFavouritesService();
		details.addListener(callback);
		details.requestFavourites(0, 50);

		Thread.sleep(2000);
		assertTrue(callbackPost.getTotalresults() > 0);
	}

	@Test
	public void testRemoveFavourite() throws InterruptedException {
		FavouritesProxy details = (FavouritesProxy) provider.getFavouritesService();
		UsenetPost post = new UsenetPost();
		post.setId("6191900");
		details.addListener(callback);
		details.requestRemoveFavourite(post);

		Thread.sleep(2000);
		assertTrue(response);
	}

	//	@Test
	//	public void testAddInvalidFavourite() throws InterruptedException {
	//		FavouritesProxy details = (FavouritesProxy) provider.getFavouritesService();
	//		UsenetPost post = new UsenetPost();
	//		post.setId("asdasdasd");
	//		details.addListener(callback);
	//		details.requestAddFavourite(post);
	//
	//		Thread.sleep(2000);
	//		assertFalse(response);
	//	}

	@Test
	public void testRemoveInvalidFavourite() throws InterruptedException {
		FavouritesProxy details = (FavouritesProxy) provider.getFavouritesService();
		UsenetPost post = new UsenetPost();
		post.setId("dsfsdfsdfds");
		details.addListener(callback);
		details.requestRemoveFavourite(post);

		Thread.sleep(2000);
		assertTrue(response);
	}

	private UsenetPostResult callbackPost;
	private Throwable callbackError;
	private boolean response;

	private final FavouritesCallback callback = new FavouritesCallback() {

		@Override
		public void onResponseAddFavourite(UsenetPost post, Boolean response, Throwable error) {
			NewzbinFavouritesTests.this.response = response;
			callbackError = error;
		}

		@Override
		public void onResponseFavourites(UsenetPostResult posts, Throwable error) {
			callbackPost = posts;
			callbackError = error;
		}

		@Override
		public void onResponseRemoveFavourite(UsenetPost post, Boolean response, Throwable error) {
			NewzbinFavouritesTests.this.response = response;
			callbackError = error;
		}

	};

}
