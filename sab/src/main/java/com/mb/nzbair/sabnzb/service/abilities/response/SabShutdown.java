package com.mb.nzbair.sabnzb.service.abilities.response;


public interface SabShutdown {
	void onResponseShutdown(Boolean ok, Throwable error);
}
