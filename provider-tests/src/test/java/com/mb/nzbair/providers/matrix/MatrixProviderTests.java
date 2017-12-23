package com.mb.nzbair.providers.matrix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import android.util.Log;

import com.mb.nzbair.providers.base.BaseProvider.NoSuchCategoryException;
import com.mb.nzbair.providers.domain.category.Category;

@RunWith(PowerMockRunner.class)
@PrepareForTest(android.util.Log.class)
public class MatrixProviderTests {
	private MatrixProvider provider;

	@Before
	public void setup() {
		PowerMockito.mockStatic(Log.class);
		provider = new MatrixProvider("test", "test", true, "fe0d5ded9d7044957d2fb788c7eb1002", "xabre6000", true, true, 1);
	}

	@Test
	public void testGetAsCategory() {
		Category providerCategory = provider.getProviderCategory();
		assertTrue(providerCategory.hasChildren());
	}

	@Test
	public void testGetCategoryById() throws NoSuchCategoryException {
		Category cat = provider.getCategory("20");
		assertEquals("Apps : Linux", cat.getTitle());
	}

	@Test(expected = NoSuchCategoryException.class)
	public void testGetInvalidCategory() throws NoSuchCategoryException {
		provider.getCategory("asdasd");
	}

	@Test
	public void testGetCategories() throws NoSuchCategoryException {
		assert (provider.getCategories().size() > 0);
	}
}
