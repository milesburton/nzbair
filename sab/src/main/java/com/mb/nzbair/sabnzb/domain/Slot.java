
package com.mb.nzbair.sabnzb.domain;

import com.mb.nzbair.sabnzb.service.SabState;

public class Slot {

	private String status;
	private int index;
	private String eta;
	private String timeLeft;
	private String avgAge;
	private String script;
	private String msgId;
	private String verbosity;
	private double mb;
	private double mbLeft;
	private String filename;
	private String priority;
	private String cat;
	private int percentage;
	private String nzoId;
	private int unpackOpts;
	private String size;

	public Slot() {
	}

	public String getStatus() {
		return status;
	}

	public SabState getSabStatus() {
		try {
			return SabState.valueOf(status.toUpperCase());
		} catch (final Exception ex) {
			return SabState.UNKNOWN;
		}
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getEta() {
		return eta;
	}

	public void setEta(String eta) {
		this.eta = eta;
	}

	public String getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(String timeLeft) {
		this.timeLeft = timeLeft;
	}

	public String getAvgAge() {
		return avgAge;
	}

	public void setAvgAge(String avgAge) {
		this.avgAge = avgAge;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getVerbosity() {
		return verbosity;
	}

	public void setVerbosity(String verbosity) {
		this.verbosity = verbosity;
	}

	public double getMb() {
		return mb;
	}

	public void setMb(double mb) {
		this.mb = mb;
	}

	public double getMbLeft() {
		return mbLeft;
	}

	public void setMbLeft(double mbLeft) {
		this.mbLeft = mbLeft;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getCat() {
		return cat;
	}

	public void setCat(String cat) {
		this.cat = cat;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

	public String getNzoId() {
		return nzoId;
	}

	public void setNzoId(String nzoId) {
		this.nzoId = nzoId;
	}

	public int getUnpackOpts() {
		return unpackOpts;
	}

	public void setUnpackOpts(int unpackOpts) {
		this.unpackOpts = unpackOpts;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
}