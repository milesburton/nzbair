package com.mb.nzbair.sabnzb.service.abilities.response;


public interface SabPaused {
	
	void onResponsePause(Boolean ok, Throwable error);
}
