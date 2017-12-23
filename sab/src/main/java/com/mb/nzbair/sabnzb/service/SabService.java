
package com.mb.nzbair.sabnzb.service;

import java.io.File;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import com.mb.nzbair.remote.AsyncDownloadTask;
import com.mb.nzbair.remote.HttpGetter;
import com.mb.nzbair.remote.HttpPoster;
import com.mb.nzbair.remote.HttpRequestCompleteCallback;
import com.mb.nzbair.remote.ThreadedDownloadTask;
import com.mb.nzbair.remote.converters.StringConverter;
import com.mb.nzbair.remote.domain.HttpRequestComplete;
import com.mb.nzbair.remote.domain.HttpRequestCompleteWithError;
import com.mb.nzbair.remote.domain.RequestFor;
import com.mb.nzbair.remote.domain.WithCallback;
import com.mb.nzbair.sabnzb.converters.BooleanResponseConverter;
import com.mb.nzbair.sabnzb.converters.CategoryConverter;
import com.mb.nzbair.sabnzb.converters.HistoricalQueueConverter;
import com.mb.nzbair.sabnzb.converters.QueueStatusConverter;
import com.mb.nzbair.sabnzb.converters.SabPriorityConverter;
import com.mb.nzbair.sabnzb.converters.SabSwitchJobConverter;
import com.mb.nzbair.sabnzb.domain.HistoricalQueue;
import com.mb.nzbair.sabnzb.domain.HistoricalSlot;
import com.mb.nzbair.sabnzb.domain.Queue;
import com.mb.nzbair.sabnzb.domain.Slot;

public class SabService implements Sab, HttpRequestCompleteCallback {

	private AsyncDownloadTask downloader;
	private SabRequestCallback callback;

	private String id;
	private String sabUrl;
	private String sabAPi;
	private int errorCount = 0;
	private boolean pollingPaused = false;

	public SabService(String id, String sabUrl, String sabApi) {

		downloader = new ThreadedDownloadTask(Executors.newSingleThreadExecutor());
		configure(id, sabUrl, sabApi);
	}

	public void setDownloadTaskStratergy(AsyncDownloadTask taskStratergy) {
		this.downloader = taskStratergy;
	}

	@Override
	public void configure(String id, String sabUrl, String sabApi) {
		this.sabUrl = sabUrl;
		this.sabAPi = sabApi;
		this.id = id;
	}

	@Override
	public void requestCategoryList() {

		final Map<String, String> params = new Hashtable<String, String>();
		params.put("mode", "get_cats");
		setCommonParams(params);

		final RequestFor<List<String>> r = new RequestFor<List<String>>(sabUrl, new CategoryConverter()).addParam(params);
		final WithCallback c = new WithCallback(this, SabRequest.CategoryList.toString());

		downloader.request(new HttpGetter<List<String>>(r, c));
	}

	@Override
	public void requestPause() {

		final Map<String, String> params = new Hashtable<String, String>();
		params.put("mode", "pause");
		setCommonParams(params);

		requestBoolean(SabRequest.Pause, params);
	}

	@Override
	public void requestPause(Integer minutes) {

		final Map<String, String> params = new Hashtable<String, String>();
		params.put("mode", "config");
		params.put("name", "set_pause");
		params.put("value", minutes.toString());
		setCommonParams(params);

		requestBoolean(SabRequest.PauseTemporary, params);
	}

	@Override
	public void requestSetSpeedLimit(int limit) {

		final Map<String, String> params = new Hashtable<String, String>();
		params.put("mode", "config");
		params.put("name", "speedlimit");
		params.put("value", ((Integer) limit).toString());
		setCommonParams(params);

		requestBoolean(SabRequest.SetSpeedLimit, params);
	}

	@Override
	public void requestShutdown() {

		final Map<String, String> params = new Hashtable<String, String>();
		params.put("mode", "shutdown");
		setCommonParams(params);

		requestBoolean(SabRequest.Shutdown, params);
	}

	@Override
	public void requestResume() {

		final Map<String, String> params = new Hashtable<String, String>();
		params.put("mode", "resume");
		setCommonParams(params);

		requestBoolean(SabRequest.Resume, params);
	}

	@Override
	public void requestVersion() {

		final Map<String, String> params = new Hashtable<String, String>();
		params.put("mode", "version");
		setCommonParams(params);

		requestString(SabRequest.Version, params);
	}

	@Override
	public void requestDeleteJobs(List<Slot> slots, boolean deleteAll) {

		final Map<String, String> params = new Hashtable<String, String>();
		params.put("mode", "queue");
		params.put("name", "delete");
		setCommonParams(params);

		if (deleteAll) {
			params.put("value", "all");
		} else {
			final StringBuilder sb = new StringBuilder();
			for (final Slot job : slots) {
				sb.append(job.getNzoId() + ",");
			}
			params.put("value", sb.toString());
		}

		requestBoolean(SabRequest.DeleteJob, params, slots);

	}

	@Override
	public void requestMoveJob(Slot from, int position) {
		final Map<String, String> params = new Hashtable<String, String>();
		params.put("mode", "switch");
		params.put("value", from.getNzoId());
		params.put("value2", ((Integer) position).toString());
		setCommonParams(params);

		requestSwitch(SabRequest.MoveJob, params);

	}

	@Override
	public void requestPauseJob(Slot job) {
		final Map<String, String> params = new Hashtable<String, String>();
		params.put("mode", "queue");
		params.put("name", "pause");
		params.put("value", job.getNzoId());
		setCommonParams(params);

		requestBoolean(SabRequest.PauseJob, params);

	}

	@Override
	public void requestRenameJob(Slot job, String filename) {
		if (filename.equals("")) {
			return;
		}

		final Map<String, String> params = new Hashtable<String, String>();
		params.put("mode", "queue");
		params.put("name", "rename");
		params.put("value", job.getNzoId());
		params.put("value2", URLEncoder.encode(filename));
		setCommonParams(params);

		requestBoolean(SabRequest.RenameJob, params, job);

	}

	@Override
	public void requestResumeJob(Slot job) {

		final Map<String, String> params = new Hashtable<String, String>();
		params.put("mode", "queue");
		params.put("name", "resume");
		params.put("value", job.getNzoId());
		setCommonParams(params);

		requestBoolean(SabRequest.ResumeJob, params, job);

	}

	@Override
	public void requestRetryJob(HistoricalSlot job) {
		try {
			final Map<String, String> params = new Hashtable<String, String>();
			params.put("mode", "retry");
			params.put("name", job.getId());
			setCommonParams(params);

			requestBoolean(SabRequest.RetryJob, params, job);
		} catch (final NullPointerException ex) {
			downloadComplete(new HttpRequestCompleteWithError(ex).addRequestId(SabRequest.RetryJob.toString()));
		}
	}

	@Override
	public void requestSwapJob(Slot from, Slot to) {

		final Map<String, String> params = new Hashtable<String, String>();
		params.put("mode", "switch");
		params.put("value", from.getNzoId());
		params.put("value2", to.getNzoId());
		setCommonParams(params);

		requestSwitch(SabRequest.SwapJob, params, from);

	}

	@Override
	public void requestAddByUrl(String url, String category) {

		if (url == null) {
			url = "";
		}

		if (category == null) {
			category = "";
		}

		final Map<String, String> params = new Hashtable<String, String>();
		params.put("mode", "addurl");
		params.put("name", url);
		params.put("cat", category);
		setCommonParams(params);

		requestBoolean(SabRequest.AddByUrl, params);

	}

	@Override
	public void requestHistory(int offset, int limit) {

		final Map<String, String> params = new Hashtable<String, String>();
		params.put("mode", "history");
		params.put("start", ((Integer) offset).toString());
		params.put("limit", ((Integer) limit).toString());
		setCommonParams(params);

		final RequestFor<HistoricalQueue> r = new RequestFor<HistoricalQueue>(sabUrl, new HistoricalQueueConverter()).addParam(params);
		final WithCallback c = new WithCallback(this, SabRequest.History.toString());

		downloader.request(new HttpGetter<HistoricalQueue>(r, c));

	}

	@Override
	public void requestQueueStatus() {

		final Map<String, String> params = new Hashtable<String, String>();
		params.put("mode", "queue");
		setCommonParams(params);

		final RequestFor<Queue> r = new RequestFor<Queue>(sabUrl, new QueueStatusConverter()).addParam(params);
		final WithCallback c = new WithCallback(this, SabRequest.Queue.toString());

		downloader.request(new HttpGetter<Queue>(r, c));

	}

	@Override
	public void requestDeleteHistoricalJob(List<HistoricalSlot> slots, boolean deleteAll) {
		final Map<String, String> params = new Hashtable<String, String>();
		params.put("mode", "history");
		params.put("name", "delete");
		setCommonParams(params);

		if (deleteAll) {
			params.put("value", "all");
		} else {
			final StringBuilder sb = new StringBuilder();
			for (final HistoricalSlot job : slots) {
				sb.append(job.getNzo_id() + ",");
			}
			params.put("value", sb.toString());
		}

		requestBoolean(SabRequest.DeleteHistoricalJob, params, slots);
	}

	@Override
	public void requestPriority(Slot job, Integer priority) {

		final Map<String, String> params = new Hashtable<String, String>();
		params.put("mode", "queue");
		params.put("name", "priority");
		params.put("value", job.getNzoId());
		params.put("value2", priority.toString());
		setCommonParams(params);

		final SabPriorityConverter con = new SabPriorityConverter();
		con.setRequestedPriority(priority);

		final RequestFor<Boolean> r = new RequestFor<Boolean>(sabUrl, con).addParam(params);
		final WithCallback c = new WithCallback(this, SabRequest.Priority.toString());

		downloader.request(new HttpGetter<Boolean>(r, c));
	}

	// Convert passed string to a qualified URL
	void setCommonParams(Map<String, String> params) {
		params.put("apikey", sabAPi);
		params.put("output", "json");
	}

	@Override
	public void removeListener(SabRequestCallback callback) {
		this.callback = null;
	}

	@Override
	public void addListener(SabRequestCallback callback) {
		this.callback = callback;
	}

	@Override
	public boolean hasValidSetup() {
		if (sabUrl == null || "".equals(sabUrl)) {
			return false;
		}

		if (sabAPi == null || "".equals(sabAPi)) {
			return false;
		}

		return true;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void requestWarningsList() {

	}

	@Override
	public void requestScriptList() {

	}

	@Override
	public void requestSetEmptyQueueAction(String action) {

	}

	@Override
	public void requestAutoShutdownOff() {

	}

	@Override
	public void requestAutoShutdownOn() {

	}

	@Override
	public boolean isPollingPaused() {

		return pollingPaused;
	}

	@Override
	public void resumePolling() {

		errorCount = 0;
		pollingPaused = false;
	}

	@Override
	public void pausePolling(String reason) {

		pollingPaused = true;
		if (callback != null) {
			callback.onPollingPaused(reason);
		}
	}

	@Override
	public void requestUpload(File f) {

		final Map<String, String> params = new Hashtable<String, String>();
		setCommonParams(params);

		requestBoolean(SabRequest.UploadFile, params);

		final RequestFor<Boolean> r = new RequestFor<Boolean>(sabUrl, new BooleanResponseConverter()).addParam(params);
		final WithCallback c = new WithCallback(this, SabRequest.UploadFile.toString());

		downloader.request(new HttpPoster<Boolean>(r, c, new SabNzbFileBody(f)));

	}

	private void requestBoolean(SabRequest request, Map<String, String> params, Object payload) {

		final RequestFor<Boolean> r = new RequestFor<Boolean>(sabUrl, new BooleanResponseConverter()).addParam(params);
		final WithCallback c = new WithCallback(this, request.toString()).addPayload(payload);

		downloader.request(new HttpGetter<Boolean>(r, c));
	}

	private void requestBoolean(SabRequest request, Map<String, String> params) {

		final RequestFor<Boolean> r = new RequestFor<Boolean>(sabUrl, new BooleanResponseConverter()).addParam(params);
		final WithCallback c = new WithCallback(this, request.toString());

		downloader.request(new HttpGetter<Boolean>(r, c));
	}

	private void requestString(SabRequest request, Map<String, String> params) {

		final RequestFor<String> r = new RequestFor<String>(sabUrl, new StringConverter()).addParam(params);
		final WithCallback c = new WithCallback(this, request.toString());

		downloader.request(new HttpGetter<String>(r, c));
	}

	private void requestSwitch(SabRequest request, Map<String, String> params) {

		final RequestFor<Boolean> r = new RequestFor<Boolean>(sabUrl, new SabSwitchJobConverter()).addParam(params);
		final WithCallback c = new WithCallback(this, request.toString());

		downloader.request(new HttpGetter<Boolean>(r, c));
	}

	private void requestSwitch(SabRequest request, Map<String, String> params, Object payload) {

		final RequestFor<Boolean> r = new RequestFor<Boolean>(sabUrl, new SabSwitchJobConverter()).addParam(params);
		final WithCallback c = new WithCallback(this, request.toString());
		c.addPayload(payload);

		downloader.request(new HttpGetter<Boolean>(r, c));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void downloadComplete(HttpRequestComplete rc) {

		switch (SabRequest.valueOf(rc.getRequestId())) {
		case CategoryList:
			if (callback != null) {
				callback.onResponseCategoryList((List<String>) rc.getResponse(), rc.getError());
			}
			break;
		case SetSpeedLimit:
			if (callback != null) {
				callback.onResponseSetSpeedLimit((Boolean) rc.getResponse(), rc.getError());
			}
			break;
		case Shutdown:
			if (callback != null) {
				callback.onResponseShutdown((Boolean) rc.getResponse(), rc.getError());
			}
			break;
		case DeleteJob:
			if (callback != null) {
				callback.onResponseDeleteJobs((List<Slot>) rc.getPayload(), (Boolean) rc.getResponse(), rc.getError());
			}
			break;
		case MoveJob:
			if (callback != null) {
				callback.onResponseMoveJob((Slot) rc.getPayload(), (Boolean) rc.getResponse(), rc.getError());
			}
			break;
		case RenameJob:
			if (callback != null) {
				callback.onResponseRenameJob((Slot) rc.getPayload(), (Boolean) rc.getResponse(), rc.getError());
			}
			break;
		case PauseJob:
			if (callback != null) {
				callback.onResponsePauseJob((Slot) rc.getPayload(), (Boolean) rc.getResponse(), rc.getError());
			}
			break;
		case PauseTemporary:
		case Pause:
			if (callback != null) {
				callback.onResponsePause((Boolean) rc.getResponse(), rc.getError());
			}
			break;
		case Resume:
			if (callback != null) {
				callback.onResponseResume((Boolean) rc.getResponse(), rc.getError());
			}
			break;
		case SwapJob:
			if (callback != null) {
				callback.onResponseSwapJob((Slot) rc.getPayload(), (Boolean) rc.getResponse(), rc.getError());
			}
			break;
		case Priority:
			if (callback != null) {
				callback.onResponsePriority((Slot) rc.getPayload(), (Boolean) rc.getResponse(), rc.getError());
			}
			break;
		case Version:
			if (callback != null) {
				callback.onResponseVersion((String) rc.getResponse(), rc.getError());
			}
			break;

		case AddByUrl:
			if (callback != null) {
				callback.onResponseAddByUrl((Boolean) rc.getResponse(), rc.getError());
			}
			break;
		case Queue:
			if (callback != null) {
				callback.onResponseQueueStatus((Queue) rc.getResponse(), rc.getError());
			}
			break;
		case History:
			if (callback != null) {
				callback.onResponseHistory((HistoricalQueue) rc.getResponse(), rc.getError());
			}
			break;
		case DeleteHistoricalJob:
			if (callback != null) {
				callback.onResponseDeleteHistoryJob((Boolean) rc.getResponse(), rc.getError());
			}
			break;
		case RetryJob:
			if (callback != null) {
				callback.onResponseRetryJob((Boolean) rc.getResponse(), rc.getError());
			}
			break;
		case UploadFile:
			if (callback != null) {
				callback.onFileUploaded((Boolean) rc.getResponse(), rc.getError());
			}

		}

		if (rc.hasError()) {
			errorCount++;
			if (errorCount > 5) {
				pausePolling("Too many errors");
			}
		} else if (errorCount > 0) {
			errorCount--;
		}

	}

}
