package com.mb.android.nzbAirPremium.download;

import com.mb.android.nzbAirPremium.download.IDownloadManagerCallback;
import com.mb.android.nzbAirPremium.download.AirDownloadMetadata;

interface IDownloadManagerService {   

    void download(in AirDownloadMetadata downloadMetadata);
    void addListener(IDownloadManagerCallback callback);
    void removeListener(IDownloadManagerCallback callback);
    AirDownloadMetadata getDownloadMetadata(in String guid);
    List<AirDownloadMetadata> getDownloads();
    void clearCompleted();
    void removeDownload(in AirDownloadMetadata downloadMetadata);
    
}