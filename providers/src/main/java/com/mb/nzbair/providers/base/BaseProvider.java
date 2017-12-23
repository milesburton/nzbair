
package com.mb.nzbair.providers.base;

import java.util.List;

import com.mb.nzbair.providers.Provider;
import com.mb.nzbair.providers.domain.category.Category;
import com.mb.nzbair.providers.interfaces.BrowseService;
import com.mb.nzbair.providers.interfaces.FavouritesService;

public abstract class BaseProvider implements Provider {

	private DetailsProxy detailsProvider;
	private BrowseService browseService;
	private FavouritesService favouritesService;

	private final String id;
	private String name;

	protected List<Category> categories = null;

	public BaseProvider(String id, String name) {
		this.id = id;
		this.name = name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Category getCategory(String id) throws NoSuchCategoryException {
		return getCategoryById(getCategories(), id);
	}

	protected void normaliseProviderIdInCategories(List<Category> categories) {

		for (final Category cat : categories) {
			cat.setProvider(id);
			if (cat.hasChildren()) {
				normaliseProviderIdInCategories(cat.getChildren());
			}
		}
	}

	private Category getCategoryById(List<Category> cats, String id) throws NoSuchCategoryException {
		for (final Category cat : cats) {
			if (cat.hasChild(id)) {
				return cat.getChild(id);
			}
		}

		throw new NoSuchCategoryException();
	}

	@Override
	public Category getProviderCategory() {
		final Category aCategory = new Category(getId(), name);
		aCategory.setProvider(getId());
		aCategory.setChildren(getCategories());

		return aCategory;
	}

	@Override
	public DetailsProxy getDetailsService() {
		if (detailsProvider == null) {
			detailsProvider = new DetailsProxy("", "", 0);
		}

		return detailsProvider;
	}

	@Override
	public String getId() {

		return id;
	}

	@Override
	public BrowseService getBrowseService() {

		return browseService;
	}

	@Override
	public FavouritesService getFavouritesService() {

		return favouritesService;
	}

	@Override
	public List<Category> getCategories() {

		return categories;
	}

	public static class NoSuchCategoryException extends Exception {

		private static final long serialVersionUID = 4073488322692249013L;
	}

	protected DetailsProxy getDetailsProvider() {
		return detailsProvider;
	}

	protected void setDetailsProvider(DetailsProxy detailsProvider) {
		this.detailsProvider = detailsProvider;
	}

	protected void setBrowseService(BrowseService browseService) {
		this.browseService = browseService;
	}

	protected void setFavouritesService(FavouritesService favouritesService) {
		this.favouritesService = favouritesService;
	};

}
