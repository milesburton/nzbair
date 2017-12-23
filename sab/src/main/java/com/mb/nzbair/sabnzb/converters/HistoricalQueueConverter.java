
package com.mb.nzbair.sabnzb.converters;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.mb.nzbair.remote.converters.HttpResponseConverter;
import com.mb.nzbair.remote.response.RestResponse;
import com.mb.nzbair.sabnzb.domain.HistoricalQueue;
import com.mb.nzbair.sabnzb.domain.HistoricalSlot;

public class HistoricalQueueConverter implements HttpResponseConverter<HistoricalQueue> {

	private static String TAG = HistoricalQueueConverter.class.toString();

	@Override
	public HistoricalQueue convert(RestResponse rr) throws Exception {

		final String json = rr.getBodyAsString();

		SabErrorScanner.throwErrorIf(json);

		try {
			final JSONObject obj = new JSONObject(json);
			final HistoricalQueue status = convert(obj.getJSONObject("history"));
			return status;
		} catch (final JSONException e) {
			Log.e(TAG, "JSON Parse Exception: " + e.toString());
			return null;
		}
	}

	private static HistoricalQueue convert(JSONObject obj) throws JSONException {
		final HistoricalQueue hq = new HistoricalQueue();

		if (!obj.isNull("active_lang")) {
			hq.setActive_lang(obj.optString("active_lang", ""));
		}
		if (!obj.isNull("session")) {
			hq.setSession(obj.optString("session", ""));
		}
		if (!obj.isNull("speed")) {
			hq.setSpeed(obj.optString("speed", ""));
		}
		if (!obj.isNull("size")) {
			hq.setSize(obj.optString("size", ""));
		}
		if (!obj.isNull("diskspacetotal2")) {
			hq.setDiskspacetotal2(obj.optString("diskspacetotal2", ""));
		}
		if (!obj.isNull("darwin")) {
			hq.setDarwin(obj.optString("darwin", ""));
		}
		if (!obj.isNull("last_warning")) {
			hq.setLast_warning(obj.optString("last_warning", ""));
		}
		if (!obj.isNull("have_warnings")) {
			hq.setHave_warnings(obj.optString("have_warnings", ""));
		}
		if (!obj.isNull("noofslots")) {
			hq.setNoofslots(obj.optString("noofslots", ""));
		}
		if (!obj.isNull("pause_int")) {
			hq.setPause_int(obj.optString("pause_int", ""));
		}
		if (!obj.isNull("diskspacetotal1")) {
			hq.setDiskspacetotal1(obj.optString("diskspacetotal1", ""));
		}
		if (!obj.isNull("mb")) {
			hq.setMb(obj.optString("mb", ""));
		}
		if (!obj.isNull("loadavg")) {
			hq.setLoadavg(obj.optString("loadavg", ""));
		}
		if (!obj.isNull("cache_max")) {
			hq.setCache_max(obj.optString("cache_max", ""));
		}
		if (!obj.isNull("speedlimit")) {
			hq.setSpeedlimit(obj.optString("speedlimit", ""));
		}
		if (!obj.isNull("webdir")) {
			hq.setWebdir(obj.optString("webdir", ""));
		}
		if (!obj.isNull("paused")) {
			hq.setPaused(obj.optString("paused", ""));
		}
		if (!obj.isNull("restart_req")) {
			hq.setRestart_req(obj.optString("restart_req", ""));
		}
		if (!obj.isNull("power_options")) {
			hq.setPower_options(obj.optString("power_options", ""));
		}
		if (!obj.isNull("helpuri")) {
			hq.setHelpuri(obj.optString("helpuri", ""));
		}
		if (!obj.isNull("uptime")) {
			hq.setUptime(obj.optString("uptime", ""));
		}
		if (!obj.isNull("total_size")) {
			hq.setTotal_size(obj.optString("total_size", ""));
		}
		if (!obj.isNull("month_size")) {
			hq.setMonth_size(obj.optString("month_size", ""));
		}
		if (!obj.isNull("week_size")) {
			hq.setWeek_size(obj.optString("week_size", ""));
		}
		if (!obj.isNull("version")) {
			hq.setVersion(obj.optString("version", ""));
		}
		if (!obj.isNull("color_scheme")) {
			hq.setColor_scheme(obj.optString("color_scheme", ""));
		}
		if (!obj.isNull("new_release")) {
			hq.setNew_release(obj.optString("new_release", ""));
		}
		if (!obj.isNull("nt")) {
			hq.setNt(obj.optString("nt", ""));
		}
		if (!obj.isNull("status")) {
			hq.setStatus(obj.optString("status", ""));
		}
		if (!obj.isNull("cache_art")) {
			hq.setCache_art(obj.optString("cache_art", ""));
		}
		if (!obj.isNull("paused_all")) {
			hq.setPaused_all(obj.optString("paused_all", ""));
		}
		if (!obj.isNull("finishaction")) {
			hq.setFinishaction(obj.optString("finishaction", ""));
		}
		if (!obj.isNull("sizeleft")) {
			hq.setSizeleft(obj.optString("sizeleft", ""));
		}
		if (!obj.isNull("cache_size")) {
			hq.setCache_size(obj.optString("cache_size", ""));
		}
		if (!obj.isNull("mbleft")) {
			hq.setMbleft(obj.optString("mbleft", ""));
		}
		if (!obj.isNull("diskspace2")) {
			hq.setDiskspace2(obj.optString("diskspace2", ""));
		}
		if (!obj.isNull("diskspace1")) {
			hq.setDiskspace1(obj.optString("diskspace1", ""));
		}
		if (!obj.isNull("timeleft")) {
			hq.setTimeleft(obj.optString("timeleft", ""));
		}
		if (!obj.isNull("nzb_quota")) {
			hq.setNzb_quota(obj.optString("nzb_quota", ""));
		}
		if (!obj.isNull("eta")) {
			hq.setEta(obj.optString("eta", ""));
		}
		if (!obj.isNull("kbpersec")) {
			hq.setKbpersec(obj.optString("kbpersec", ""));
		}
		if (!obj.isNull("new_rel_url")) {
			hq.setNew_rel_url(obj.optString("new_rel_url", ""));
		}

		if (hq.getNoofslots() != null || !hq.getNoofslots().equals("")) {
			hq.setSlots(getJobs(obj.getJSONArray("slots")));
		}

		return hq;
	}

	private static List<HistoricalSlot> getJobs(JSONArray arr) throws JSONException {
		final List<HistoricalSlot> jobs = new ArrayList<HistoricalSlot>();

		for (int i = 0; i < arr.length(); i++) {
			jobs.add(getJob(arr.getJSONObject(i)));
		}

		return jobs;
	}

	private static HistoricalSlot getJob(JSONObject obj) throws JSONException {
		final HistoricalSlot hq = new HistoricalSlot();

		if (!obj.isNull("action_line")) {
			hq.setAction_line(obj.optString("action_line", ""));
		}
		if (!obj.isNull("show_details")) {
			hq.setShow_details(obj.optString("show_details", ""));
		}
		if (!obj.isNull("script_log")) {
			hq.setScript_log(obj.optString("script_log", ""));
		}
		if (!obj.isNull("meta")) {
			hq.setMeta(obj.optString("meta", ""));
		}
		if (!obj.isNull("fail_message")) {
			hq.setFail_message(obj.optString("fail_message", ""));
		}
		if (!obj.isNull("loaded")) {
			hq.setLoaded(obj.optString("loaded", ""));
		}
		if (!obj.isNull("id")) {
			hq.setId(obj.optString("id", ""));
		}
		if (!obj.isNull("size")) {
			hq.setSize(obj.optString("size", ""));
		}
		if (!obj.isNull("category")) {
			hq.setCategory(obj.optString("category", ""));
		}
		if (!obj.isNull("pp")) {
			hq.setPp(obj.optString("pp", ""));
		}
		if (!obj.isNull("completeness")) {
			hq.setCompleteness(obj.optString("completeness", ""));
		}
		if (!obj.isNull("script")) {
			hq.setScript(obj.optString("script", ""));
		}
		if (!obj.isNull("nzb_name")) {
			hq.setNzb_name(obj.optString("nzb_name", ""));
		}
		if (!obj.isNull("download_time")) {
			hq.setDownload_time(obj.optString("download_time", ""));
		}
		if (!obj.isNull("storage")) {
			hq.setStorage(obj.optString("storage", ""));
		}
		if (!obj.isNull("status")) {
			hq.setStatus(obj.optString("status", ""));
		}
		if (!obj.isNull("script_line")) {
			hq.setScript_line(obj.optString("script_line", ""));
		}
		if (!obj.isNull("completed")) {
			hq.setCompleted(obj.optString("completed", ""));
		}
		if (!obj.isNull("nzo_id")) {
			hq.setNzo_id(obj.optString("nzo_id", ""));
		}
		if (!obj.isNull("downloaded")) {
			hq.setDownloaded(obj.optString("downloaded", ""));
		}
		if (!obj.isNull("report")) {
			hq.setReport(obj.optString("report", ""));
		}
		if (!obj.isNull("path")) {
			hq.setPath(obj.optString("path", ""));
		}
		if (!obj.isNull("postproc_time")) {
			hq.setPostproc_time(obj.optString("postproc_time", ""));
		}
		if (!obj.isNull("name")) {
			hq.setName(obj.optString("name", ""));
		}
		if (!obj.isNull("url")) {
			hq.setUrl(obj.optString("url", ""));
		}
		if (!obj.isNull("bytes")) {
			hq.setBytes(obj.optString("bytes", ""));
		}
		if (!obj.isNull("url_info")) {
			hq.setUrl_info(obj.optString("url_info", ""));
		}
		if (!obj.isNull("stage_log")) {
			hq.setStage_log(obj.optString("stage_log", ""));
		}

		return hq;
	}

}
