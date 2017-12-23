
package com.mb.nzbair.remote.domain;

import java.util.UUID;

import android.os.Parcel;
import android.os.Parcelable;

import com.mb.nzbair.remote.converters.DownloadToFileConverter;

public abstract class DownloadMetadata implements Parcelable {

	String downloadUrl, destinationFolder, destinationFilename, uuid;

	int progress, status;
	double fileSize = 0, fileDownloaded = 0;
	String eta = "Unknown";

	protected DownloadMetadata(Parcel in) {
		readFromParcel(in);
	}

	@Override
	public void writeToParcel(Parcel arg0, int flags) {

		arg0.writeString(downloadUrl);
		arg0.writeString(destinationFolder);
		arg0.writeString(destinationFilename);
		arg0.writeString(uuid);
		arg0.writeInt(progress);
		arg0.writeInt(status);
		arg0.writeDouble(fileSize);
		arg0.writeDouble(fileDownloaded);
	}

	public void readFromParcel(Parcel arg0) {

		downloadUrl = arg0.readString();
		destinationFolder = arg0.readString();
		destinationFilename = arg0.readString();
		uuid = arg0.readString();
		progress = arg0.readInt();
		status = arg0.readInt();
		fileSize = arg0.readDouble();
		fileDownloaded = arg0.readDouble();
	}

	public String getEta() {
		return eta;
	}

	public void setEta(String eta) {
		this.eta = eta;
	}

	public DownloadMetadata(String dowloadUrl, String destinationFolder, String destinationFilename) {
		this.downloadUrl = dowloadUrl;
		this.destinationFolder = destinationFolder;
		this.destinationFilename = destinationFilename;
	}

	public void reload(DownloadMetadata dm) {
		this.destinationFilename = dm.getDestinationFilename();
		this.destinationFolder = dm.getDestinationFolder();
		this.downloadUrl = dm.getDownloadUrl();
		this.fileDownloaded = dm.getfileDownloaded();
		this.fileSize = dm.getFilesize();
		this.progress = dm.getProgress();
		this.status = dm.getStatus();
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public String getDestinationFolder() {
		return destinationFolder;
	}

	public String getDestinationFilename() {
		return destinationFilename;
	}

	public String getUuid() {
		return uuid;
	}

	public double getFilesize() {
		return fileSize;
	}

	public double getfileDownloaded() {
		return fileDownloaded;
	}

	public void setFilesize(double filesize) {
		this.fileSize = filesize;
	}

	public void setFileDownloaded(double fileDownloaded) {
		this.fileDownloaded = fileDownloaded;
	}

	public void setDestinationFolder(String path) {
		this.destinationFolder = path;
	}

	public void setFilename(String name) {
		this.destinationFilename = name;
	}

	public void setGuid() {
		if (uuid == null) {
			uuid = UUID.randomUUID().toString();
		}
	}

	public String getStatusAsText() {
		switch (getStatus()) {
		case DownloadToFileConverter.DOWNLOADING:
			return "Downloading";

		case DownloadToFileConverter.COMPLETE:
			return "Complete";

		case DownloadToFileConverter.FAILED:
			return "Download Failed";
		case DownloadToFileConverter.PENDING:
			return "Pending";
		default:
			return "Unknown";

		}
	}

	@Override
	public int describeContents() {

		return 0;
	}

}
