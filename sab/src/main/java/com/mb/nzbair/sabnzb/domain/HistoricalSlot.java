
package com.mb.nzbair.sabnzb.domain;

import com.mb.nzbair.sabnzb.service.SabState;

public class HistoricalSlot {

	private String action_line;
	private String show_details;
	private String script_log;
	private String meta;
	private String fail_message;
	private String loaded;
	private String id;
	private String size;
	private String category;
	private String pp;
	private String completeness;
	private String script;
	private String nzb_name;
	private String download_time;
	private String storage;
	private String status;
	private String script_line;
	private String completed;
	private String nzo_id;
	private String downloaded;
	private String report;
	private String path;
	private String postproc_time;
	private String name;
	private String url;
	private String bytes;
	private String url_info;
	private String stage_log;

	public SabState getSabStatus() {
		try {
			return SabState.valueOf(status.toUpperCase());
		} catch (final Exception ex) {
			return SabState.UNKNOWN;
		}
	}

	public String getAction_line() {
		return action_line;
	}

	public void setAction_line(String actionLine) {
		action_line = actionLine;
	}

	public String getShow_details() {
		return show_details;
	}

	public void setShow_details(String showDetails) {
		show_details = showDetails;
	}

	public String getScript_log() {
		return script_log;
	}

	public void setScript_log(String scriptLog) {
		script_log = scriptLog;
	}

	public String getMeta() {
		return meta;
	}

	public void setMeta(String meta) {
		this.meta = meta;
	}

	public String getFail_message() {
		return fail_message;
	}

	public void setFail_message(String failMessage) {
		fail_message = failMessage;
	}

	public String getLoaded() {
		return loaded;
	}

	public void setLoaded(String loaded) {
		this.loaded = loaded;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPp() {
		return pp;
	}

	public void setPp(String pp) {
		this.pp = pp;
	}

	public String getCompleteness() {
		return completeness;
	}

	public void setCompleteness(String completeness) {
		this.completeness = completeness;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getNzb_name() {
		return nzb_name;
	}

	public void setNzb_name(String nzbName) {
		nzb_name = nzbName;
	}

	public String getDownload_time() {
		return download_time;
	}

	public void setDownload_time(String downloadTime) {
		download_time = downloadTime;
	}

	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getScript_line() {
		return script_line;
	}

	public void setScript_line(String scriptLine) {
		script_line = scriptLine;
	}

	public String getCompleted() {
		return completed;
	}

	public void setCompleted(String completed) {
		this.completed = completed;
	}

	public String getNzo_id() {
		return nzo_id;
	}

	public void setNzo_id(String nzoId) {
		nzo_id = nzoId;
	}

	public String getDownloaded() {
		return downloaded;
	}

	public void setDownloaded(String downloaded) {
		this.downloaded = downloaded;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPostproc_time() {
		return postproc_time;
	}

	public void setPostproc_time(String postprocTime) {
		postproc_time = postprocTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBytes() {
		return bytes;
	}

	public void setBytes(String bytes) {
		this.bytes = bytes;
	}

	public String getUrl_info() {
		return url_info;
	}

	public void setUrl_info(String urlInfo) {
		url_info = urlInfo;
	}

	public String getStage_log() {
		return stage_log;
	}

	public void setStage_log(String stageLog) {
		stage_log = stageLog;
	}
}
