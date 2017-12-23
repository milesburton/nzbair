
package com.mb.nzbair.sabnzb.domain;

import java.util.List;

import com.mb.nzbair.sabnzb.service.SabState;

public class Queue {

	private int cache_limit;
	private String[] categories;
	private String[] scripts;
	private boolean paused;
	private String new_rel_url;
	private boolean restart_req;
	private List<Slot> slots;
	private String helpuri;
	private String uptime;
	private String refreshRate;
	private boolean isVerbose;
	private int start;
	private String version;
	private double diskSpaceTotal2;
	private String colorScheme;
	private boolean darwin;
	private boolean nt;
	private String status;
	private String lastWarning;
	private int haveWarnings;
	private int cacheArt;
	private String finishAction;
	private int noOfSlots;
	private String cacheSize;
	private int finished;
	private String newReleases;
	private String pauseInt; // interval
	private double mbLeft;
	private double diskSpace2;
	private double diskSpace1;
	private double diskSpaceTotal1;
	private String timeLeft;
	private double mb;
	private String eta;
	private String nzb_quota;
	private String loadAvg;
	private int limit;
	private String kbPerSec;
	private int speedLimit;
	private String webDir;
	private int queueDetails;

	public Queue() {
	}

	public int getCache_limit() {
		return cache_limit;
	}

	public void setCache_limit(int cacheLimit) {
		cache_limit = cacheLimit;
	}

	public String[] getCategories() {
		return categories;
	}

	public void setCategories(String[] categories) {
		this.categories = categories;
	}

	public String[] getScripts() {
		return scripts;
	}

	public void setScripts(String[] scripts) {
		this.scripts = scripts;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public String getNew_rel_url() {
		return new_rel_url;
	}

	public void setNew_rel_url(String newRelUrl) {
		new_rel_url = newRelUrl;
	}

	public boolean isRestart_req() {
		return restart_req;
	}

	public void setRestart_req(boolean restartReq) {
		restart_req = restartReq;
	}

	public List<Slot> getSlots() {
		return slots;
	}

	public void setSlots(List<Slot> slots) {
		this.slots = slots;
	}

	public String getHelpuri() {
		return helpuri;
	}

	public void setHelpuri(String helpuri) {
		this.helpuri = helpuri;
	}

	public String getUptime() {
		return uptime;
	}

	public void setUptime(String uptime) {
		this.uptime = uptime;
	}

	public String getRefreshRate() {
		return refreshRate;
	}

	public void setRefreshRate(String refreshRate) {
		this.refreshRate = refreshRate;
	}

	public boolean isVerbose() {
		return isVerbose;
	}

	public void setVerbose(boolean isVerbose) {
		this.isVerbose = isVerbose;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public double getDiskSpaceTotal2() {
		return diskSpaceTotal2;
	}

	public void setDiskSpaceTotal2(double diskSpaceTotal2) {
		this.diskSpaceTotal2 = diskSpaceTotal2;
	}

	public String getColorScheme() {
		return colorScheme;
	}

	public void setColorScheme(String colorScheme) {
		this.colorScheme = colorScheme;
	}

	public boolean isDarwin() {
		return darwin;
	}

	public void setDarwin(boolean darwin) {
		this.darwin = darwin;
	}

	public boolean isNt() {
		return nt;
	}

	public void setNt(boolean nt) {
		this.nt = nt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLastWarning() {
		return lastWarning;
	}

	public void setLastWarning(String lastWarning) {
		this.lastWarning = lastWarning;
	}

	public int getHaveWarnings() {
		return haveWarnings;
	}

	public void setHaveWarnings(int haveWarnings) {
		this.haveWarnings = haveWarnings;
	}

	public int getCacheArt() {
		return cacheArt;
	}

	public void setCacheArt(int cacheArt) {
		this.cacheArt = cacheArt;
	}

	public String getFinishAction() {
		return finishAction;
	}

	public void setFinishAction(String finishAction) {
		this.finishAction = finishAction;
	}

	public int getNoOfSlots() {
		return noOfSlots;
	}

	public void setNoOfSlots(int noOfSlots) {
		this.noOfSlots = noOfSlots;
	}

	public String getCacheSize() {
		return cacheSize;
	}

	public void setCacheSize(String cacheSize) {
		this.cacheSize = cacheSize;
	}

	public int getFinished() {
		return finished;
	}

	public void setFinished(int finished) {
		this.finished = finished;
	}

	public String getNewReleases() {
		return newReleases;
	}

	public void setNewReleases(String newReleases) {
		this.newReleases = newReleases;
	}

	public String getPauseInt() {
		return pauseInt;
	}

	public void setPauseInt(String pauseInt) {
		this.pauseInt = pauseInt;
	}

	public double getMbLeft() {
		return mbLeft;
	}

	public void setMbLeft(double mbLeft) {
		this.mbLeft = mbLeft;
	}

	public double getDiskSpace2() {
		return diskSpace2;
	}

	public void setDiskSpace2(double diskSpace2) {
		this.diskSpace2 = diskSpace2;
	}

	public double getDiskSpace1() {
		return diskSpace1;
	}

	public void setDiskSpace1(double diskSpace1) {
		this.diskSpace1 = diskSpace1;
	}

	public double getDiskSpaceTotal1() {
		return diskSpaceTotal1;
	}

	public void setDiskSpaceTotal1(double diskdiskSpaceTotal1) {
		this.diskSpaceTotal1 = diskdiskSpaceTotal1;
	}

	public String getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(String timeLeft) {
		this.timeLeft = timeLeft;
	}

	public double getMb() {
		return mb;
	}

	public void setMb(double mb) {
		this.mb = mb;
	}

	public String getEta() {
		return eta;
	}

	public void setEta(String eta) {
		this.eta = eta;
	}

	public String getNzb_quota() {
		return nzb_quota;
	}

	public void setNzb_quota(String nzbQuota) {
		nzb_quota = nzbQuota;
	}

	public String getLoadAvg() {
		return loadAvg;
	}

	public void setLoadAvg(String loadAvg) {
		this.loadAvg = loadAvg;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getKbPerSec() {
		return kbPerSec;
	}

	public void setKbPerSec(String kbPerSec) {
		this.kbPerSec = kbPerSec;
	}

	public int getSpeedLimit() {
		return speedLimit;
	}

	public void setSpeedLimit(int speedLimit) {
		this.speedLimit = speedLimit;
	}

	public String getWebDir() {
		return webDir;
	}

	public void setWebDir(String webDir) {
		this.webDir = webDir;
	}

	public int getQueueDetails() {
		return queueDetails;
	}

	public void setQueueDetails(int queueDetails) {
		this.queueDetails = queueDetails;
	}

	public SabState getSabStatus() {
		return SabState.valueOf(status.toUpperCase());
	}

	public int getDisk1SpaceRemaining() {
		if (getDiskSpaceTotal1() <= 0) {
			return 0;
		}
		final double diskSpaceUsed = getDiskSpaceTotal1() - getDiskSpace1();

		final int progress = (int) ((diskSpaceUsed / getDiskSpaceTotal1()) * 100);

		return progress;
	}

	public int getDisk2SpaceRemaining() {
		if (getDiskSpaceTotal2() <= 0) {
			return 0;
		}
		final double diskSpaceUsed = getDiskSpaceTotal2() - getDiskSpace2();

		final int progress = (int) ((diskSpaceUsed / getDiskSpaceTotal2()) * 100);

		return progress;
	}

	public int getDownloadProgress() {
		if (mb <= 0) {
			return 0;
		}

		return (int) (((mb - mbLeft) / mb) * 100);
	}

}