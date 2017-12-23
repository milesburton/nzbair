
package com.mb.nzbair.seh;

import org.codehaus.jackson.annotate.JsonCreator;

public class ProviderException extends NZBAirServiceException {

	private static final long serialVersionUID = 6217436816562444523L;

	@JsonCreator
	public static ProviderException create(String t) {
		return new ProviderException();
	}

	public ProviderException() {
		super();
	}

	public ProviderException(int providerErrorCode, String providerErrorString, Throwable cause) {
		super(100, "BaseProvider Exception", cause);
		this.providerErrorCode = providerErrorCode;
		this.providerErrorString = providerErrorString;
	}

	public ProviderException(int providerErrorCode, String providerErrorString) {
		super(100, "BaseProvider Exception");
		this.providerErrorCode = providerErrorCode;
		this.providerErrorString = providerErrorString;
	}

	private int providerErrorCode = 0;
	private String providerErrorString = "";

	public int getProviderErrorCode() {
		return providerErrorCode;
	}

	public void setProviderErrorCode(int providerErrorCode) {
		this.providerErrorCode = providerErrorCode;
	}

	public String getProviderErrorString() {
		return providerErrorString;
	}

	public void setProviderErrorString(String providerErrorString) {
		this.providerErrorString = providerErrorString;
	}
}
