package com.mb.nzbair.sabnzb.service.abilities.response;

import com.mb.nzbair.sabnzb.domain.Queue;


public interface SabQueueResponse {
	void onResponseQueueStatus(Queue queue, Throwable error);
}
