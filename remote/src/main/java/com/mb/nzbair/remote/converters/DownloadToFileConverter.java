
package com.mb.nzbair.remote.converters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import org.apache.http.Header;

import android.util.Log;

import com.mb.nzbair.remote.domain.DownloadMetadata;
import com.mb.nzbair.remote.response.RestResponse;
import com.mb.nzbair.remote.utils.FormatHelper;

public class DownloadToFileConverter implements HttpResponseConverter<DownloadMetadata> {

	private static final String TAG = "DownloadFileTask";

	public static final int DOWNLOADING = 0;
	public static final int COMPLETE = 1;
	public static final int PENDING = 3;
	public static final int FAILED = 4;

	private final DownloadToFileUpdate callback;
	private final DownloadMetadata dm;
	private File destinationFile;

	private FileOutputStream f;
	private final String downloadLocation;

	public DownloadToFileConverter(DownloadToFileUpdate callback, DownloadMetadata dm, String downloadLocation) {
		this.dm = dm;
		this.callback = callback;
		this.downloadLocation = downloadLocation;
	}

	@Override
	public DownloadMetadata convert(RestResponse r) throws IOException {

		final Header[] contentDispositionHeaders = r.getResponse().getHeaders("Content-Disposition");
		final String contentDisposition = contentDispositionHeaders != null && contentDispositionHeaders.length > 0 ? contentDispositionHeaders[0].getValue() : "";
		// Prepare file
		// Attempt to get filename
		final String filename = FilenameHelpers.getFilename(dm.getDownloadUrl(), dm.getDestinationFilename(), contentDisposition);

		destinationFile = new File(downloadLocation, filename);
		if (!destinationFile.exists()) {
			destinationFile.createNewFile();
		}
		f = new FileOutputStream(destinationFile);

		dm.setStatus(DOWNLOADING);
		dm.setEta("Unknown");

		Log.i(TAG, "Downloading web resource: " + dm.getDownloadUrl());

		// Grabfilesize
		final long fileSize = r.getResponse().getEntity().getContentLength();
		final InputStream stream = r.getStream();
		dm.setFilesize(fileSize);

		final byte[] buffer = new byte[1024];
		int len1 = 0, downloaded = 0;
		long startTime = Calendar.getInstance().getTimeInMillis();
		while ((len1 = stream.read(buffer)) > 0) {
			downloaded += len1;
			f.write(buffer, 0, len1);

			if (downloaded % 10240 == 0) {
				dm.setFileDownloaded(downloaded);

				double timeTakenToDownloadTenKb = Calendar.getInstance().getTimeInMillis() - startTime;
				timeTakenToDownloadTenKb /= 1000; // Seconds
				timeTakenToDownloadTenKb /= 10; // Time to download 1k
				timeTakenToDownloadTenKb = 1 / timeTakenToDownloadTenKb; // time
				// to
				// download
				// 1k
				dm.setEta(FormatHelper.getNumberFormatter().format(timeTakenToDownloadTenKb) + "kb/sec");
				startTime = Calendar.getInstance().getTimeInMillis();

				// Update details
				if (fileSize > 0) {
					final double progress = ((double) downloaded / (double) fileSize) * 100;
					dm.setProgress((int) progress);
				}

				callback.onDownloadPending(dm);
				// Take difference between the last time we did a loop
				// and now to get speed
			}
		}

		if (destinationFile.length() > 0) {
			dm.setFilesize(destinationFile.length()); // Get
			dm.setFileDownloaded(destinationFile.length());
		}
		// total
		// filesize
		// in
		// bytes
		if (downloaded <= 0) {
			dm.setStatus(FAILED);
		} else {
			dm.setStatus(COMPLETE);
		}

		dm.setProgress(100);
		return dm;
	}

}
