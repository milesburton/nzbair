package com.mb.nzbair.providers.matrix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import android.util.Log;

import com.mb.nzbair.providers.base.RemoteDetailsProxy;
import com.mb.nzbair.providers.domain.UsenetPost;
import com.mb.nzbair.providers.interfaces.DetailsCallback;

@RunWith(PowerMockRunner.class)
@PrepareForTest(android.util.Log.class)
public class MatrixDetailsTests {

	private MatrixProvider provider;

	@Before
	public void setup() {
		PowerMockito.mockStatic(Log.class);
		provider = new MatrixProvider("test", "test", false, "fe0d5ded9d7044957d2fb788c7eb1002", "xabre6000", true, true, 1);
	}

	@Test
	public void testGetBrowseProvider() {
		RemoteDetailsProxy details = (RemoteDetailsProxy) provider.getDetailsService();
		assertEquals("fe0d5ded9d7044957d2fb788c7eb1002", details.getParams().get("apikey"));
		assertEquals("xabre6000", details.getParams().get("username"));
	}

	@Test
	public void testGetDetails() throws InterruptedException {
		RemoteDetailsProxy details = (RemoteDetailsProxy) provider.getDetailsService();
		UsenetPost post = new UsenetPost();
		post.setId("1167706");
		details.addListener(callback);
		details.requestPostDetails(post);

		Thread.sleep(2000);
		assertEquals("1167706", callbackPost.getId());
	}

	@Test
	public void testGetInvalidDetails() throws InterruptedException {
		RemoteDetailsProxy details = (RemoteDetailsProxy) provider.getDetailsService();
		UsenetPost post = new UsenetPost();
		post.setId("asdasdsad");
		details.addListener(callback);
		details.requestPostDetails(post);

		Thread.sleep(2000);
		assertNull(callbackPost);
		assertNotNull(callbackError);
	}

	private UsenetPost callbackPost;
	private Throwable callbackError;

	private final DetailsCallback callback = new DetailsCallback() {

		@Override
		public void responseDetails(UsenetPost post, Throwable error) {
			callbackPost = post;
			callbackError = error;
		}
	};

}
