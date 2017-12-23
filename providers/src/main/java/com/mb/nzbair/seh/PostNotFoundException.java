
package com.mb.nzbair.seh;

/**
 * Created by IntelliJ IDEA. User: miles Date: 8/27/11 Time: 9:28 PM To change
 * this template use File | Settings | File Templates.
 */
public class PostNotFoundException extends ProviderException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1277078646479072585L;

	public PostNotFoundException(String postId) {
		super(404, "Post not found: " + postId);
	}
}
