
package com.mb.nzbair.sabnzb.converters;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.mb.nzbair.remote.converters.HttpResponseConverter;
import com.mb.nzbair.remote.response.RestResponse;
import com.mb.nzbair.sabnzb.SabException;
import com.mb.nzbair.sabnzb.domain.Queue;
import com.mb.nzbair.sabnzb.domain.Slot;

public class QueueStatusConverter implements HttpResponseConverter<Queue> {

	private static String TAG = QueueStatusConverter.class.toString();

	@Override
	public Queue convert(RestResponse rr) throws Exception {

		final String json = rr.getBodyAsString();
		SabErrorScanner.throwErrorIf(json);
		try {
			final JSONObject obj = new JSONObject(json);
			final Queue status = convert(obj.getJSONObject("queue"));
			return status;
		} catch (final JSONException e) {
			Log.e(TAG, "JSON Parse Exception: " + e.toString());
			throw new SabException(e.getMessage());
		}
	}

	private static Queue convert(JSONObject json) throws JSONException {
		final Queue queue = new Queue();

		queue.setCache_limit(json.getInt("cache_max"));
		queue.setCategories(getStringArray(json.getJSONArray("categories")));
		queue.setScripts(getStringArray(json.getJSONArray("scripts")));
		queue.setPaused(json.getBoolean("paused"));
		queue.setNew_rel_url(json.getString("new_rel_url"));
		queue.setRestart_req(json.getBoolean("restart_req"));

		queue.setVersion(json.getString("version"));
		queue.setHelpuri(json.getString("helpuri"));
		queue.setUptime(json.getString("uptime"));
		queue.setRefreshRate(json.getString("refresh_rate"));
		queue.setVerbose(json.getBoolean("isverbose"));
		queue.setColorScheme(json.getString("color_scheme"));
		queue.setDarwin(json.getBoolean("darwin"));
		queue.setNt(json.getBoolean("nt"));
		queue.setStatus(json.getString("status"));
		queue.setLastWarning(json.getString("last_warning"));
		queue.setHaveWarnings(json.getInt("have_warnings"));
		queue.setCacheArt(json.getInt("cache_art"));
		queue.setFinishAction(json.getString("finishaction"));
		queue.setNoOfSlots(json.getInt("noofslots"));
		queue.setCacheSize(json.getString("cache_size"));
		queue.setFinished(json.getInt("finish"));
		queue.setNewReleases(json.getString("new_release"));
		queue.setPauseInt(json.getString("pause_int"));
		queue.setMbLeft(json.getDouble("mbleft"));
		queue.setDiskSpace1(json.getDouble("diskspace1"));
		queue.setDiskSpace2(json.getDouble("diskspace2"));
		queue.setDiskSpaceTotal1(json.getDouble("diskspacetotal1"));
		queue.setDiskSpaceTotal2(json.getDouble("diskspacetotal2"));
		queue.setTimeLeft(json.getString("timeleft"));
		queue.setMb(json.getDouble("mb"));
		queue.setEta(json.getString("eta"));
		queue.setNzb_quota(json.getString("nzb_quota"));
		queue.setLoadAvg(json.getString("loadavg"));
		queue.setLimit(json.getInt("limit"));
		queue.setKbPerSec(json.getString("kbpersec"));

		if (!json.isNull("speedlimit")) {
			queue.setSpeedLimit(json.optInt("speedlimit", 0));
		}

		queue.setWebDir(json.getString("webdir"));
		queue.setQueueDetails(json.getInt("queue_details"));

		if (queue.getNoOfSlots() > 0) {
			queue.setSlots(getJobs(json.getJSONArray("slots")));
		} else {
			queue.setSlots(new ArrayList<Slot>());
		}

		return queue;
	}

	private static List<Slot> getJobs(JSONArray arr) throws JSONException {
		final List<Slot> jobs = new ArrayList<Slot>();

		for (int i = 0; i < arr.length(); i++) {
			jobs.add(getJob(arr.getJSONObject(i)));
		}

		return jobs;
	}

	private static Slot getJob(JSONObject json) throws JSONException {
		final Slot job = new Slot();

		job.setStatus(json.getString("status"));
		job.setIndex(json.getInt("index"));
		job.setEta(json.getString("eta"));
		job.setTimeLeft(json.getString("timeleft"));
		job.setAvgAge(json.getString("avg_age"));
		job.setScript(json.getString("script"));
		job.setMsgId(json.getString("msgid"));
		job.setVerbosity(json.getString("verbosity"));
		job.setMb(json.getDouble("mb"));
		job.setMbLeft(json.getDouble("mbleft"));
		job.setFilename(json.getString("filename"));
		job.setPriority(json.getString("priority"));
		job.setCat(json.getString("cat"));
		job.setPercentage(json.getInt("percentage"));
		job.setNzoId(json.getString("nzo_id"));
		try {
			job.setUnpackOpts(json.getInt("unpackopts"));
		} catch (final JSONException ex) {
			Log.e(TAG, "Cannot parse unpackopts. Expected. " + ex.toString());
		}
		job.setSize(json.getString("size"));

		return job;
	}

	private static String[] getStringArray(JSONArray arr) {
		final String[] items = new String[arr.length()];
		for (int i = 0; i < arr.length(); i++) {
			try {
				items[i] = arr.getString(i);
			} catch (final JSONException e) {
				Log.e(TAG, "Could not parse " + e.toString());
				e.printStackTrace();
			}
		}

		return items;

	}
}
