
package com.mb.nzbair.seh;

/**
 * Created by IntelliJ IDEA. User: miles Date: 8/27/11 Time: 9:30 PM To change
 * this template use File | Settings | File Templates.
 */
public class NotAuthorisedException extends ProviderException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotAuthorisedException() {
		super(403, "Invalid Credentials");
	}
}
