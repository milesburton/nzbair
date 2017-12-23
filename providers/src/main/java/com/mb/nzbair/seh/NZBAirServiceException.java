
package com.mb.nzbair.seh;

public class NZBAirServiceException extends Exception {

	private static final long serialVersionUID = -4306030059593408290L;

	public NZBAirServiceException(int errorCode, String errorString, Throwable cause) {
		super(errorString, cause);
	}

	public NZBAirServiceException(int errorCode, String errorString) {
		super(errorString);
		this.errorCode = errorCode;
		this.errorString = errorString;
	}

	public NZBAirServiceException() {
		super();
	}

	protected int errorCode = 0;

	protected String errorString = "";

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorString() {
		return errorString;
	}

	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	private String className = "";

}
