
package com.mb.nzbair.sabnzb.service;

import java.io.File;
import java.util.List;


import com.mb.nzbair.sabnzb.domain.HistoricalSlot;
import com.mb.nzbair.sabnzb.domain.Slot;
import com.mb.nzbair.sabnzb.service.abilities.request.SabRequestAutoShutdown;
import com.mb.nzbair.sabnzb.service.abilities.request.SabRequestPause;
import com.mb.nzbair.sabnzb.service.abilities.request.SabRequestQueue;
import com.mb.nzbair.sabnzb.service.abilities.request.SabRequestResume;
import com.mb.nzbair.sabnzb.service.abilities.request.SabRequestSetSpeedLimit;
import com.mb.nzbair.sabnzb.service.abilities.request.SabRequestShutdown;

public interface Sab extends SabRequestPause, SabRequestResume, SabRequestShutdown, SabRequestAutoShutdown, SabRequestQueue, SabRequestSetSpeedLimit {

	boolean hasValidSetup();

	boolean isPollingPaused();

	void resumePolling();

	void pausePolling(String reason);

	String getId();

	void configure(String id, String sabUrl, String sabApi);

	void addListener(SabRequestCallback callback);

	void removeListener(SabRequestCallback callback);

	void requestSetEmptyQueueAction(String action);

	void requestWarningsList();

	void requestCategoryList();

	void requestScriptList();

	void requestVersion();

	void requestDeleteJobs(List<Slot> slots, boolean deleteAll);

	void requestSwapJob(Slot from, Slot to);

	void requestMoveJob(Slot from, int position);

	void requestRetryJob(HistoricalSlot from);

	void requestPauseJob(Slot job);

	void requestResumeJob(Slot job);

	void requestRenameJob(Slot job, String filename);

	void requestAddByUrl(String url, String category);

	void requestHistory(int offset, int limit);

	void requestDeleteHistoricalJob(List<HistoricalSlot> slots, boolean deleteAll);

	void requestPriority(Slot job, Integer priority);

	void requestUpload(File f);
}
