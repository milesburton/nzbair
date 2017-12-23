/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mb.nzbair.seh;

/**
 * @author miles
 */
public class ProviderDownException extends ProviderException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5289677431213790153L;

	public ProviderDownException(Throwable cause) {
		super(101, "BaseProvider is down", cause);
	}

	public ProviderDownException() {
		super(101, "BaseProvider is down");
	}

}
