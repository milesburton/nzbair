package com.mb.nzbair.sabnzb.service.abilities.request;


public interface SabRequestPause {
	
	void requestPause();

	void requestPause(Integer minutes);
}
