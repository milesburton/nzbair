
package com.mb.nzbair.sabnzb;

public class SabException extends Exception {

	private static final long serialVersionUID = 1L;

	public SabException(String error) {
		super(error);
	}
}
