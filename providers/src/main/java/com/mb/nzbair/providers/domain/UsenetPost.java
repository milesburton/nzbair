
package com.mb.nzbair.providers.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UsenetPost implements Serializable {

	private static final long serialVersionUID = -3200475053383680637L;
	private String id;
	private String title;
	private Long filesize;
	private String categoryText;
	private String categoryId;
	private String provider;
	private Long unixTimeAdded;
	private String nzbDownloadUrl;

	private HashMap<String, String> metadata = new HashMap<String, String>();

	private List<String> groups = new ArrayList<String>();

	private List<String> images = new ArrayList<String>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getFilesize() {
		return filesize;
	}

	public void setFilesize(Long filesize) {
		this.filesize = filesize;
	}

	public String getCategoryText() {
		return categoryText;
	}

	public void setCategoryText(String categoryText) {
		this.categoryText = categoryText;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public HashMap<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(HashMap<String, String> metadata) {
		this.metadata = metadata;
	}

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public Long getUnixTimeAdded() {
		return unixTimeAdded;
	}

	public void setUnixTimeAdded(Long unixTimeAdded) {
		this.unixTimeAdded = unixTimeAdded;
	}

	public String getNzbDownloadUrl() {
		return nzbDownloadUrl;
	}

	public void setNzbDownloadUrl(String nzbDownloadUrl) {
		this.nzbDownloadUrl = nzbDownloadUrl;
	}
}
