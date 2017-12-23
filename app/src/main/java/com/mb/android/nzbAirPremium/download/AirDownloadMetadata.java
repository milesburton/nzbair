
package com.mb.android.nzbAirPremium.download;

import android.os.Parcel;
import android.os.Parcelable;

import com.mb.nzbair.remote.domain.DownloadMetadata;

public class AirDownloadMetadata extends DownloadMetadata {

	public AirDownloadMetadata(String dowloadUrl, String destinationFolder, String destinationFilename) {
		super(dowloadUrl, destinationFolder, destinationFilename);
	}

	protected AirDownloadMetadata(Parcel in) {
		super(in);
	}

	public static final Parcelable.Creator<AirDownloadMetadata> CREATOR = new Parcelable.Creator<AirDownloadMetadata>() {

		@Override
		public AirDownloadMetadata createFromParcel(Parcel in) {
			return new AirDownloadMetadata(in);
		}

		@Override
		public AirDownloadMetadata[] newArray(int size) {
			return new AirDownloadMetadata[size];
		}
	};

}