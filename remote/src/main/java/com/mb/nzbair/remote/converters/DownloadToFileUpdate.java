
package com.mb.nzbair.remote.converters;

import com.mb.nzbair.remote.domain.DownloadMetadata;

public interface DownloadToFileUpdate {

	void onDownloadPending(DownloadMetadata metadata);
}
