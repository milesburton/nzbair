package com.mb.nzbair.sabnzb.service.abilities.response;


public interface SabAutoShutdownResponse {
	void onResponseAutoShutdownOn(Boolean ok, Throwable error);

	void onResponseAutoShutdownOff(Boolean ok, Throwable error);
}
