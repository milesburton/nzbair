
package com.mb.nzbair.seh;

public class InvalidProviderException extends ProviderException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5968770742750387610L;
	private final String providerName;

	public InvalidProviderException(String name, Throwable e) {
		super(500, "Invalid BaseProvider", e);
		this.providerName = name;
	}

	public String getProviderName() {
		return providerName;
	}
}
