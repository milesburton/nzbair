
package com.mb.nzbair.sabnzb.service;

import java.util.List;

import com.mb.nzbair.sabnzb.domain.HistoricalQueue;
import com.mb.nzbair.sabnzb.domain.Slot;
import com.mb.nzbair.sabnzb.service.abilities.response.SabAutoShutdownResponse;
import com.mb.nzbair.sabnzb.service.abilities.response.SabPaused;
import com.mb.nzbair.sabnzb.service.abilities.response.SabQueueResponse;
import com.mb.nzbair.sabnzb.service.abilities.response.SabResumed;
import com.mb.nzbair.sabnzb.service.abilities.response.SabSetSpeedLimitResponse;
import com.mb.nzbair.sabnzb.service.abilities.response.SabShutdown;

public interface SabRequestCallback extends SabPaused, SabResumed, SabShutdown, SabAutoShutdownResponse, SabQueueResponse, SabSetSpeedLimitResponse {

	void onPollingPaused(String reason);

	void onResponseSetEmptyQueueAction(String action, Throwable error);

	void onResponseWarningsList(Throwable error);

	void onResponseCategoryList(List<String> categories, Throwable error);

	void onResponseScriptList(Throwable error);

	void onResponseVersion(String version, Throwable error);

	void onResponseDeleteJobs(List<Slot> jobs, Boolean ok, Throwable error);

	void onResponseSwapJob(Slot from, Boolean ok, Throwable error);

	void onResponseMoveJob(Slot from, Boolean ok, Throwable error);

	void onResponseRetryJob(Boolean ok, Throwable error);

	void onResponsePauseJob(Slot job, Boolean ok, Throwable error);

	void onResponseResumeJob(Slot job, Boolean ok, Throwable error);

	void onResponseRenameJob(Slot job, Boolean ok, Throwable error);

	void onResponseAddByUrl(Boolean ok, Throwable error);

	void onResponseHistory(HistoricalQueue hQueue, Throwable error);

	void onResponseDeleteHistoryJob(Boolean ok, Throwable error);

	void onResponsePriority(Slot job, Boolean ok, Throwable error);

	void onFileUploaded(Boolean ok, Throwable error);
}
