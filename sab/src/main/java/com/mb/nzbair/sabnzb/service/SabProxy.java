
package com.mb.nzbair.sabnzb.service;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mb.nzbair.sabnzb.domain.HistoricalQueue;
import com.mb.nzbair.sabnzb.domain.HistoricalSlot;
import com.mb.nzbair.sabnzb.domain.Queue;
import com.mb.nzbair.sabnzb.domain.Slot;

public class SabProxy implements Sab {

	private final Map<String, Sab> available = new Hashtable<String, Sab>();
	private Sab activeService = null;
	private final SabListener sabListener = new SabListener();
	private final Set<SabRequestCallback> listeners = Collections.synchronizedSet(new HashSet<SabRequestCallback>());
	private final Map<SabRequest, Long> mutex = new Hashtable<SabRequest, Long>();
	private int timeout = 2000;
	private static SabProxy instance = new SabProxy();

	public static SabProxy getProxy() {
		return instance;
	}

	@Override
	public boolean hasValidSetup() {

		if (hasActiveService()) {
			return activeService.hasValidSetup();
		}
		return false;
	}

	@Override
	public void configure(String id, String sabUrl, String sabApi) {
		if (hasActiveService()) {
			activeService.configure(id, sabUrl, sabApi);
		}
	}

	@Override
	public void requestAddByUrl(String url, String category) {
		final SabRequest request = SabRequest.AddByUrl;

		if (canRequest(request)) {
			recordRequest(request);
			activeService.requestAddByUrl(url, category);
		}
	}

	@Override
	public void requestAutoShutdownOff() {
		final SabRequest request = SabRequest.AutoShutDownOff;

		if (canRequest(request)) {
			recordRequest(request);
			activeService.requestAutoShutdownOff();
		}
	}

	@Override
	public void requestAutoShutdownOn() {
		final SabRequest request = SabRequest.AutoShutDownOn;

		if (canRequest(request)) {
			recordRequest(request);
			activeService.requestAutoShutdownOn();
		}
	}

	@Override
	public void requestCategoryList() {
		final SabRequest request = SabRequest.CategoryList;

		if (canRequest(request)) {
			recordRequest(request);
			activeService.requestCategoryList();
		}
	}

	@Override
	public void requestDeleteHistoricalJob(List<HistoricalSlot> slots, boolean deleteAll) {
		final SabRequest request = SabRequest.DeleteHistoricalJob;

		if (canRequest(request)) {
			recordRequest(request);
			activeService.requestDeleteHistoricalJob(slots, deleteAll);
		}
	}

	@Override
	public void requestDeleteJobs(List<Slot> slots, boolean deleteAll) {
		final SabRequest request = SabRequest.DeleteJob;

		if (canRequest(request)) {
			recordRequest(request);
			activeService.requestDeleteJobs(slots, deleteAll);
		}
	}

	@Override
	public void requestHistory(int offset, int limit) {
		final SabRequest request = SabRequest.History;

		if (canRequest(request)) {
			recordRequest(request);
			activeService.requestHistory(offset, limit);
		}
	}

	@Override
	public void requestMoveJob(Slot from, int position) {
		final SabRequest request = SabRequest.MoveJob;

		if (canRequest(request)) {
			recordRequest(request);
			activeService.requestMoveJob(from, position);
		}
	}

	@Override
	public void requestPause() {
		final SabRequest request = SabRequest.Pause;

		if (canRequest(request)) {
			recordRequest(request);
			activeService.requestPause();
		}
	}

	@Override
	public void requestPause(Integer minutes) {
		final SabRequest request = SabRequest.PauseTemporary;

		if (canRequest(request)) {
			recordRequest(request);
			activeService.requestPause(minutes);
		}
	}

	@Override
	public void requestPauseJob(Slot job) {
		final SabRequest request = SabRequest.PauseJob;

		if (canRequest(request)) {
			recordRequest(request);
			activeService.requestPauseJob(job);
		}
	}

	@Override
	public void requestQueueStatus() {
		final SabRequest request = SabRequest.Queue;

		if (canRequest(request)) {
			recordRequest(request);
			activeService.requestQueueStatus();
		}
	}

	@Override
	public void requestRenameJob(Slot job, String filename) {
		final SabRequest request = SabRequest.RenameJob;

		if (canRequest(request)) {
			recordRequest(request);
			activeService.requestRenameJob(job, filename);
		}
	}

	@Override
	public void requestResume() {
		final SabRequest request = SabRequest.Resume;

		if (canRequest(request)) {
			recordRequest(request);
			activeService.requestResume();
		}
	}

	@Override
	public void requestResumeJob(Slot job) {
		final SabRequest request = SabRequest.ResumeJob;

		if (canRequest(request)) {
			recordRequest(request);
			activeService.requestResumeJob(job);
		}
	}

	@Override
	public void requestRetryJob(HistoricalSlot from) {
		final SabRequest request = SabRequest.RetryJob;

		if (canRequest(request)) {
			recordRequest(request);
			activeService.requestRetryJob(from);
		}
	}

	@Override
	public void requestScriptList() {
		final SabRequest request = SabRequest.ScriptList;

		if (canRequest(request)) {
			recordRequest(request);
			activeService.requestScriptList();
		}
	}

	@Override
	public void requestSetEmptyQueueAction(String action) {
		final SabRequest request = SabRequest.EmptyQueueAction;

		if (canRequest(request)) {
			recordRequest(request);
			activeService.requestSetEmptyQueueAction(action);
		}
	}

	@Override
	public void requestSetSpeedLimit(int limit) {
		final SabRequest request = SabRequest.SetSpeedLimit;

		if (canRequest(request)) {
			recordRequest(request);
			activeService.requestSetSpeedLimit(limit);
		}
	}

	@Override
	public void requestShutdown() {
		final SabRequest request = SabRequest.Shutdown;

		if (canRequest(request)) {
			recordRequest(request);
			activeService.requestShutdown();
		}
	}

	@Override
	public void requestSwapJob(Slot from, Slot to) {
		final SabRequest request = SabRequest.SwapJob;

		if (canRequest(request)) {
			recordRequest(request);
			activeService.requestSwapJob(from, to);
		}
	}

	@Override
	public void requestVersion() {
		final SabRequest request = SabRequest.Version;

		if (canRequest(request)) {
			recordRequest(request);
			activeService.requestVersion();
		}
	}

	@Override
	public void requestWarningsList() {

	}

	private void recordRequest(SabRequest request) {
		mutex.put(request, new Date().getTime());
	}

	public void addService(Sab service) {
		available.put(service.getId(), service);
		if (!hasActiveService()) {
			enableService(service);
		}
	}

	public void removeService(String id) {

		if (available.get(id) != null && activeService == available.get(id)) {
			activeService = null;
		}
		available.remove(id);
	}

	private void detachListener() {
		activeService.removeListener(sabListener);
	}

	public void enableService(Sab service) {
		if (hasActiveService()) {
			detachListener();
		}

		activeService = service;
		activeService.addListener(sabListener);
	}

	@Override
	public void requestPriority(Slot job, Integer priority) {
		final SabRequest request = SabRequest.Priority;

		if (canRequest(request)) {
			recordRequest(request);
			activeService.requestPriority(job, priority);
		}
	}

	public boolean isReady() {
		return hasActiveService();
	}

	public int getTimeout() {
		return timeout;
	}

	private boolean canRequest(SabRequest request) {

		final long timeLastRun = getTimeLastRun(request);
		final long timeNow = new Date().getTime();

		if (hasActiveService() && !activeService.isPollingPaused() && (timeLastRun == 0 || (timeNow - timeLastRun) >= timeout)) {
			return true;
		}
		return false;
	}

	private long getTimeLastRun(SabRequest request) {
		if (mutex.containsKey(request)) {
			return mutex.get(request);
		}
		return 0;
	}

	public boolean hasActiveService() {
		return activeService != null;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	@Override
	public void removeListener(SabRequestCallback listener) {
		listeners.remove(listener);
	}

	@Override
	public void addListener(SabRequestCallback listener) {
		listeners.add(listener);
	}

	private class SabListener implements SabRequestCallback {

		@Override
		public void onResponseAddByUrl(Boolean ok, Throwable error) {

			synchronized (listeners) {
				for (final SabRequestCallback listener : listeners) {
					listener.onResponseAddByUrl(ok, error);
				}
			}
		}

		@Override
		public void onResponseAutoShutdownOff(Boolean ok, Throwable error) {

			synchronized (listeners) {
				for (final SabRequestCallback listener : listeners) {
					listener.onResponseAutoShutdownOff(ok, error);
				}
			}
		}

		@Override
		public void onResponseAutoShutdownOn(Boolean ok, Throwable error) {

			synchronized (listeners) {
				for (final SabRequestCallback listener : listeners) {
					listener.onResponseAutoShutdownOn(ok, error);
				}
			}
		}

		@Override
		public void onResponseCategoryList(List<String> categories, Throwable error) {

			synchronized (listeners) {
				for (final SabRequestCallback listener : listeners) {
					listener.onResponseCategoryList(categories, error);
				}
			}
		}

		@Override
		public void onResponseDeleteHistoryJob(Boolean ok, Throwable error) {

			synchronized (listeners) {
				for (final SabRequestCallback listener : listeners) {
					listener.onResponseDeleteHistoryJob(ok, error);
				}
			}
		}

		@Override
		public void onResponseDeleteJobs(List<Slot> jobs, Boolean ok, Throwable error) {

			synchronized (listeners) {
				for (final SabRequestCallback listener : listeners) {
					listener.onResponseDeleteJobs(jobs, ok, error);
				}
			}
		}

		@Override
		public void onResponseHistory(HistoricalQueue hQueue, Throwable error) {

			synchronized (listeners) {
				for (final SabRequestCallback listener : listeners) {
					listener.onResponseHistory(hQueue, error);
				}
			}
		}

		@Override
		public void onResponseMoveJob(Slot from, Boolean ok, Throwable error) {

			synchronized (listeners) {
				for (final SabRequestCallback listener : listeners) {
					listener.onResponseMoveJob(from, ok, error);
				}
			}
		}

		@Override
		public void onResponsePause(Boolean ok, Throwable error) {

			synchronized (listeners) {
				for (final SabRequestCallback listener : listeners) {
					listener.onResponsePause(ok, error);
				}
			}
		}

		@Override
		public void onResponsePauseJob(Slot job, Boolean ok, Throwable error) {

			synchronized (listeners) {
				for (final SabRequestCallback listener : listeners) {
					listener.onResponsePauseJob(job, ok, error);
				}
			}
		}

		@Override
		public void onResponseQueueStatus(Queue queue, Throwable error) {

			synchronized (listeners) {
				for (final SabRequestCallback listener : listeners) {
					listener.onResponseQueueStatus(queue, error);
				}
			}
		}

		@Override
		public void onResponseRenameJob(Slot job, Boolean ok, Throwable error) {

			synchronized (listeners) {
				for (final SabRequestCallback listener : listeners) {
					listener.onResponseRenameJob(job, ok, error);
				}
			}
		}

		@Override
		public void onResponseResume(Boolean ok, Throwable error) {

			synchronized (listeners) {
				for (final SabRequestCallback listener : listeners) {
					listener.onResponseResume(ok, error);
				}
			}
		}

		@Override
		public void onResponseResumeJob(Slot job, Boolean ok, Throwable error) {

			synchronized (listeners) {
				for (final SabRequestCallback listener : listeners) {
					listener.onResponseResumeJob(job, ok, error);
				}
			}
		}

		@Override
		public void onResponseRetryJob(Boolean ok, Throwable error) {

			synchronized (listeners) {
				for (final SabRequestCallback listener : listeners) {
					listener.onResponseRetryJob(ok, error);
				}
			}
		}

		@Override
		public void onResponseScriptList(Throwable error) {

			synchronized (listeners) {
				for (final SabRequestCallback listener : listeners) {
					listener.onResponseScriptList(error);
				}
			}
		}

		@Override
		public void onResponseSetEmptyQueueAction(String action, Throwable error) {

			synchronized (listeners) {
				for (final SabRequestCallback listener : listeners) {
					listener.onResponseSetEmptyQueueAction(action, error);
				}
			}
		}

		@Override
		public void onResponseSetSpeedLimit(Boolean ok, Throwable error) {

			synchronized (listeners) {
				for (final SabRequestCallback listener : listeners) {
					listener.onResponseSetSpeedLimit(ok, error);
				}
			}
		}

		@Override
		public void onResponseShutdown(Boolean ok, Throwable error) {

			synchronized (listeners) {
				for (final SabRequestCallback listener : listeners) {
					listener.onResponseShutdown(ok, error);
				}
			}
		}

		@Override
		public void onResponseSwapJob(Slot from, Boolean ok, Throwable error) {

			synchronized (listeners) {
				for (final SabRequestCallback listener : listeners) {
					listener.onResponseSwapJob(from, ok, error);
				}
			}
		}

		@Override
		public void onResponseVersion(String version, Throwable error) {

			synchronized (listeners) {
				for (final SabRequestCallback listener : listeners) {
					listener.onResponseVersion(version, error);
				}
			}
		}

		@Override
		public void onResponseWarningsList(Throwable error) {

			synchronized (listeners) {
				for (final SabRequestCallback listener : listeners) {
					listener.onResponseWarningsList(error);
				}
			}
		}

		@Override
		public void onResponsePriority(Slot job, Boolean ok, Throwable error) {

			synchronized (listeners) {
				for (final SabRequestCallback listener : listeners) {
					listener.onResponsePriority(job, ok, error);
				}
			}
		}

		@Override
		public void onPollingPaused(String reason) {

			synchronized (listeners) {
				for (final SabRequestCallback listener : listeners) {
					listener.onPollingPaused(reason);
				}
			}

		}

		@Override
		public void onFileUploaded(Boolean ok, Throwable error) {

			synchronized (listeners) {
				for (final SabRequestCallback listener : listeners) {
					listener.onFileUploaded(ok, error);
				}
			}
		}

	}

	@Override
	public String getId() {
		if (hasActiveService()) {
			return activeService.getId();
		}
		return null;
	}

	public Sab getService(String id) {
		return available.get(id);
	}

	public boolean hasService(String id) {
		return getService(id) != null;
	}

	@Override
	public boolean isPollingPaused() {
		if (hasActiveService()) {
			return activeService.isPollingPaused();
		}
		return false;
	}

	@Override
	public void resumePolling() {

		if (hasActiveService()) {
			activeService.resumePolling();
		}

	}

	@Override
	public void pausePolling(String reason) {

		if (hasActiveService()) {
			activeService.pausePolling(reason);
		}
	}

	@Override
	public void requestUpload(File f) {

		if (hasActiveService()) {
			activeService.requestUpload(f);
		}
	}

}
