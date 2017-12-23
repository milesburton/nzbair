
package com.mb.android.nzbAirPremium.download;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.mb.android.nzbAirPremium.app.Air;
import com.mb.android.nzbAirPremium.preferences.domain.GeneralConfig;
import com.mb.nzbair.remote.HttpGetter;
import com.mb.nzbair.remote.HttpRequestCompleteCallback;
import com.mb.nzbair.remote.converters.DownloadToFileConverter;
import com.mb.nzbair.remote.converters.DownloadToFileUpdate;
import com.mb.nzbair.remote.converters.HttpResponseConverter;
import com.mb.nzbair.remote.domain.DownloadMetadata;
import com.mb.nzbair.remote.domain.HttpRequestComplete;
import com.mb.nzbair.remote.domain.RequestFor;
import com.mb.nzbair.remote.domain.WithCallback;

public class DownloadService extends Service implements HttpRequestCompleteCallback, DownloadToFileUpdate {

	private static final String TAG = "DownloadManagerService";

	private final ExecutorService downloader;
	private IDownloadManagerCallback callback;

	private final ConcurrentHashMap<String, AirDownloadMetadata> activeDownloads = new ConcurrentHashMap<String, AirDownloadMetadata>();
	private GeneralConfig generalConfig = null;

	public DownloadService() {
		downloader = Executors.newSingleThreadExecutor();
		this.generalConfig = Air.get().getGeneralConfig();
	}

	private void startTask(String id, DownloadMetadata payload) {

		if (generalConfig == null) {
			return;
		}

		final HttpResponseConverter<DownloadMetadata> strat = new DownloadToFileConverter(this, payload, generalConfig.getDownloadLocation());

		final RequestFor<DownloadMetadata> r = new RequestFor<DownloadMetadata>(payload.getDownloadUrl(), strat);
		final WithCallback c = new WithCallback(this, id).addPayload(payload);

		downloader.submit(new HttpGetter<DownloadMetadata>(r, c));
	}

	/**
	 * Prepare binder
	 */

	@Override
	public IBinder onBind(Intent intent) {

		/**
		 * Implement implicit download interface
		 */
		return new IDownloadManagerService.Stub() {

			/**
			 * Request a download
			 */

			@Override
			public void download(AirDownloadMetadata dm) throws RemoteException {

				dm.setGuid();

				if (dm.getDestinationFolder() == null || dm.getDestinationFolder().equals("")) {
					dm.setDestinationFolder(generalConfig.getDownloadLocation());
				}

				dm.setStatus(DownloadToFileConverter.PENDING);
				DownloadService.this.activeDownloads.put(dm.getUuid(), dm);

				if (DownloadService.this.callback != null) {
					DownloadService.this.callback.onDownloadAdded(dm.getUuid());
				}

				DownloadService.this.startTask(dm.getUuid(), dm);
			}

			@Override
			public AirDownloadMetadata getDownloadMetadata(String guid) throws RemoteException {
				try {
					return DownloadService.this.activeDownloads.get(guid);
				} catch (final Exception ex) {
					Log.e(TAG, ex.toString());
					return null;
				}
			}

			@Override
			public List<AirDownloadMetadata> getDownloads() throws RemoteException {

				return new ArrayList<AirDownloadMetadata>(DownloadService.this.activeDownloads.values());

			}

			@Override
			public void addListener(IDownloadManagerCallback callback) throws RemoteException {
				DownloadService.this.callback = callback;

			}

			@Override
			public void removeListener(IDownloadManagerCallback callback) throws RemoteException {

				DownloadService.this.callback = null;
			}

			@Override
			public void clearCompleted() throws RemoteException {
				for (final AirDownloadMetadata download : DownloadService.this.activeDownloads.values()) {
					if (download.getProgress() == 100) {
						removeDownload(download);
					}
				}

			}

			@Override
			public void removeDownload(AirDownloadMetadata downloadMetadata) throws RemoteException {
				DownloadService.this.activeDownloads.remove(downloadMetadata.getUuid());
				if (DownloadService.this.callback != null) {
					DownloadService.this.callback.onDownloadRemoved(downloadMetadata.getUuid());
				}
			}

		};
	}

	private void reloadDownloadMetaData(AirDownloadMetadata dm) {
		final AirDownloadMetadata _dm = activeDownloads.get(dm.getUuid());
		if (_dm == null) {
			return;
		}

		_dm.reload(dm);
	}

	@Override
	public void downloadComplete(HttpRequestComplete rc) {

		final AirDownloadMetadata dm = (AirDownloadMetadata) rc.getPayload();
		if (rc.hasError()) {
			dm.setStatus(DownloadToFileConverter.FAILED);
		}

		reloadDownloadMetaData(dm);
		if (callback == null) {
			return;
		}

		try {
			callback.onDownloadComplete(dm.getUuid());
		} catch (final RemoteException e) {
			Log.e(TAG, e.toString());
		}

	}

	@Override
	public void onDownloadPending(DownloadMetadata md) {

		reloadDownloadMetaData((AirDownloadMetadata) md);
		if (callback == null) {
			return;
		}
		try {
			callback.onDownloadUpdate(md.getUuid());
		} catch (final RemoteException e) {
			Log.e(TAG, e.toString());
		}

	}
}