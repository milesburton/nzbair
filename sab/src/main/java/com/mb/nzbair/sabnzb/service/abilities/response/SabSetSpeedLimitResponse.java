package com.mb.nzbair.sabnzb.service.abilities.response;


public interface SabSetSpeedLimitResponse {
	void onResponseSetSpeedLimit(Boolean ok, Throwable error);
}
