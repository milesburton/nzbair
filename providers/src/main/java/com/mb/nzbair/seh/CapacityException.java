
package com.mb.nzbair.seh;

/**
 * Created by IntelliJ IDEA. User: miles Date: 29/08/11 Time: 00:39 To change
 * this template use File | Settings | File Templates.
 */
public class CapacityException extends ProviderException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CapacityException() {
		super(450, "Capacity exception. BaseProvider is overloaded, please try later");
	}

}
