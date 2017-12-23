
package com.mb.nzbair.providers.domain.category;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mb.nzbair.providers.base.BaseProvider.NoSuchCategoryException;

public class Category implements Serializable {

	private static final long serialVersionUID = 6513029351037588483L;
	private String catid = "";
	private String title = "";
	private Category parent = null;
	private List<Category> children = new ArrayList<Category>();
	private List<Category> siblings = new ArrayList<Category>();
	private String providerId;

	public Category() {
	}

	public Category(String subcatid, String title) {
		this.catid = subcatid;
		this.title = title;
	}

	public boolean hasChildren() {
		return this.children != null && this.children.size() > 0;
	}

	public List<Category> getChildren() {
		return children;
	}

	public Category addChild(Category child) {
		children.add(child);
		child.setParent(this);
		child.setSiblings(children);
		child.setProvider(this.providerId);
		return this;
	}

	public void setChildren(List<Category> children) {
		this.children = children;
	}

	public String generateCategoryIdsAsCSV() {
		if (this instanceof BranchCategory) {

			if (siblings == null || siblings.size() == 0) {
				return this.getId();
			} else {
				return getAsCSV(siblings);
			}
		}

		if (children == null || children.size() == 0) {
			return this.getId();
		}

		return getAsCSV(children);
	}

	private String getAsCSV(List<Category> categories) {
		final StringBuffer sb = new StringBuffer();
		for (final Category subcat : categories) {

			if (subcat instanceof BranchCategory || subcat instanceof ParentCategory) {
				continue;
			}

			sb.append(subcat.getId() + ",");
		}

		return sb.toString().substring(0, sb.length() - 1);
	}

	public boolean hasChild(String id) {
		if (this.hasChildren()) {
			for (final Category cat : children) {
				if (cat.getId().equals(id) || cat.hasChild(id)) {
					return true;
				}
			}
		}
		return false;
	}

	public Category getChild(String id) throws NoSuchCategoryException {
		if (this.hasChildren()) {
			for (final Category cat : children) {
				if (cat.getId().equals(id)) {
					return cat;
				} else if (cat.hasChild(id)) {
					return cat.getChild(id);
				}

			}
		}
		throw new NoSuchCategoryException();
	}

	public Category(String title) {
		this.title = title;
	}

	public String getId() {
		return catid;
	}

	public void setSubcatid(String subcatid) {
		this.catid = subcatid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setParent(Category cat) {
		this.parent = cat;
	}

	public Category getParent() {
		return this.parent;
	}

	public boolean hasParent() {
		return this.parent != null;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProvider(String payload) {
		this.providerId = payload;
	}

	public List<Category> getSiblings() {
		return siblings;
	}

	public void setSiblings(List<Category> siblings) {
		this.siblings = siblings;
	}
}
