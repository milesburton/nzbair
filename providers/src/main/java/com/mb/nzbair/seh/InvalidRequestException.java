
package com.mb.nzbair.seh;

public class InvalidRequestException extends ProviderException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7121484108205465299L;

	public InvalidRequestException() {
		super(500, "Invalid Request");
	}
}
