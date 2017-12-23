package com.mb.nzbair.providers;

import com.mb.nzbair.providers.base.DetailsProxy;
import com.mb.nzbair.providers.domain.category.Category;
import com.mb.nzbair.providers.interfaces.BrowseService;
import com.mb.nzbair.providers.interfaces.FavouritesService;
import org.junit.Test;

import java.util.List;

import static com.mb.nzbair.providers.ProviderFactory.InvalidProviderException;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class ProviderFactoryTests {

	@Test
	public void testFactoryInstance() {
		ProviderFactory factory = ProviderFactory.getInstance();
		assertNotNull(factory);
	}

	@Test
	public void testAddProvider() {
		ProviderFactory factory = new ProviderFactory();
		factory.addProvider(mockProvider);
		assertEquals(1, factory.getProviders().size());
	}

	@Test
	public void testRemoveProvider() {
		ProviderFactory factory = new ProviderFactory();
		factory.addProvider(mockProvider);
		factory.removeProvider(mockProvider.getId());
		assertEquals(0, factory.getProviders().size());
	}

	@Test
	public void testRemoveInvalidProvider() {
		ProviderFactory factory = new ProviderFactory();
		factory.removeProvider("asdasd");
		assertEquals(0, factory.getProviders().size());
	}

	@Test
	public void testGetProviderId() throws InvalidProviderException {
		ProviderFactory factory = new ProviderFactory();
		factory.addProvider(mockProvider);
		assertEquals(mockProvider, factory.getProvider("test"));
	}

	@Test(expected = InvalidProviderException.class)
	public void testGetInvalidProvider() throws InvalidProviderException {
		ProviderFactory factory = new ProviderFactory();
		factory.getProvider("test");
	}

	@Test
	public void testHasProvider() throws InvalidProviderException {
		ProviderFactory factory = new ProviderFactory();
		factory.addProvider(mockProvider);
		assertTrue(factory.hasProvider("test"));
	}

	@Test
	public void testHasNoProvider() throws InvalidProviderException {
		ProviderFactory factory = new ProviderFactory();
		assertFalse(factory.hasProvider("test"));
	}



    @Test(expected = InvalidProviderException.class)
	public void testGetProviderWithInvalidId() throws InvalidProviderException {
		ProviderFactory factory = new ProviderFactory();
		factory.getProvider("");
	}

	@Test(expected = InvalidProviderException.class)
	public void testGetProviderWithNullId() throws InvalidProviderException {
		ProviderFactory factory = new ProviderFactory();
		factory.getProvider(null);
	}

	@Test(expected = InvalidProviderException.class)
	public void testGetDefaultProviderWithNoneSet() throws InvalidProviderException {
		ProviderFactory factory = new ProviderFactory();
		factory.getDefaultProvider();
	}

	@Test
	public void testGetDefaultProvider() throws InvalidProviderException {
		ProviderFactory factory = new ProviderFactory();
		factory.setDefaultSearchProvider("test");
		factory.addProvider(mockProvider);
		assertEquals(mockProvider, factory.getDefaultProvider());
	}

	@Test
	public void testGetProvidersAsCategory() {
		ProviderFactory factory = new ProviderFactory();
		factory.addProvider(mockProvider);
		assertEquals(1, factory.getProvidersAsCategories().size());
		assertEquals("test", factory.getProvidersAsCategories().get(0).getTitle());
		assertEquals("test", factory.getProvidersAsCategories().get(0).getId());
	}

	private final Provider mockProvider = new Provider() {

		@Override
		public BrowseService getBrowseService() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<Category> getCategories() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Category getCategory(String id) {
			return null;
		}

		@Override
		public DetailsProxy getDetailsService() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public FavouritesService getFavouritesService() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getId() {
			// TODO Auto-generated method stub
			return "test";
		}

		@Override
		public Category getProviderCategory() {
			return new Category("test", "test");
		}

		@Override
		public String getName() {
			return "";
		}
	};
}
