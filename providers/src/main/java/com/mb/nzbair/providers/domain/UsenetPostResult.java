/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mb.nzbair.providers.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author miles
 */
public class UsenetPostResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1098321532067588920L;
	private List<UsenetPost> posts;
	private Integer offset;
	private Integer limit;
	private Integer totalresults;

	public UsenetPostResult() {
		posts = new ArrayList<UsenetPost>();
		offset = 0;
		limit = 50;
		totalresults = 0;

	}

	public List<UsenetPost> getPosts() {
		return posts;
	}

	public void setPosts(List<UsenetPost> posts) {
		this.posts = posts;
	}

	public int getOffset() {
		if (offset == null) {
			offset = 0;
		}
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		if (limit == null) {
			limit = 0;
		}
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getTotalresults() {
		if (totalresults == null) {
			totalresults = 0;
		}
		return totalresults;
	}

	public void setTotalresults(int totalresults) {
		this.totalresults = totalresults;
	}
}
