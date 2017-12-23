
package com.mb.nzbair.remote.converters;

/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.SystemClock;

/**
 * Some helper functions for the download manager
 */
public class FilenameHelpers {

	// private static String TAG = FilenameHelpers.class.getName();

	public static Random sRandom = new Random(SystemClock.uptimeMillis());

	/** Regex used to parse content-disposition headers */
	private static final Pattern CONTENT_DISPOSITION_PATTERN = Pattern.compile("attachment;\\s*filename\\s*=\\s*\"([^\"]*)\"");

	private FilenameHelpers() {
	}

	/*
	 * Parse the Content-Disposition HTTP Header. The format of the header is
	 * defined here: http://www.w3.org/Protocols/rfc2616/rfc2616-sec19.html This
	 * header provides a filename for content that is going to be downloaded to
	 * the file system. We only support the attachment type.
	 */
	public static String parseContentDisposition(String contentDisposition) {
		if (contentDisposition == null) {
			return null;
		}
		try {
			final Matcher m = CONTENT_DISPOSITION_PATTERN.matcher(contentDisposition);
			if (m.find()) {
				return m.group(1);
			}
		} catch (final IllegalStateException ex) {
			// This function is defined as returning null when it can't parse
			// the header
		}
		return null;
	}

	private static String getFilenameFromUrlString(String url) {
		return url.substring(url.lastIndexOf("/") + 1);
	}

	public static String getFilename(String url, String fileTitle, String cd) {
		try {
			String filename;
			if ((filename = parseContentDisposition(cd)) == null) {
				if (fileTitle != null && !fileTitle.equals("")) {
					filename = fileTitle + ".nzb"; //
				} else {
					filename = getFilenameFromUrlString(url);
				}
			}

			return filename;
		} catch (final Exception ex) {
			return null;
		}

	}

}
