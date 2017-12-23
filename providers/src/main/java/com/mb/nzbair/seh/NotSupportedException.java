
package com.mb.nzbair.seh;

/**
 * Created by IntelliJ IDEA. User: miles Date: 8/27/11 Time: 11:43 PM To change
 * this template use File | Settings | File Templates.
 */
public class NotSupportedException extends ProviderException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotSupportedException() {
		super(500, "Action not supported by provider");
	}
}
