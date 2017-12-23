/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mb.nzbair.seh;

/**
 * @author miles
 */
public class ConversionErrorException extends ProviderException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConversionErrorException(Throwable cause) {
		super(500, "Failed to translate response from web mSabService", cause);
	}

	public ConversionErrorException() {
		super(500, "Failed to translate response from web mSabService");
	}

}
