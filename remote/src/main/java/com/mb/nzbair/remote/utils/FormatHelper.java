
package com.mb.nzbair.remote.utils;

import java.text.NumberFormat;

public class FormatHelper {

	private static NumberFormat nf = NumberFormat.getInstance();

	public static String formatFileSize(double sizeInMb) {
		if (sizeInMb <= 0) {
			return "0 MB";
		}

		final double r = sizeInMb / 1024;
		String qual = " MB";

		if (r >= 1) {
			sizeInMb = r;
			qual = " GB";
		}

		nf.setMaximumFractionDigits(2);
		return nf.format(sizeInMb) + qual;
	}

	public static String formatFileSizeBytes(double sizeInBytes) {
		String sizeType = " B";
		if (sizeInBytes > 0) {
			sizeInBytes = sizeInBytes / 1024;// Kilobytes
			if (sizeInBytes > 1024) // Is greater than 1MB
			{

				sizeInBytes /= 1024; // In megabytes
				if (sizeInBytes > 1024) { // Is greater than 1GB
					// Gigabytes
					sizeType = " GB";
					sizeInBytes /= 1024; // In megabytes
				} else {
					sizeType = " MB";
				}
			} else {
				sizeType = " KB";
			}
		}

		final NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		return nf.format(sizeInBytes) + sizeType;
	}

	public static NumberFormat getNumberFormatter() {
		nf.setMaximumFractionDigits(2);
		return nf;
	}
}
