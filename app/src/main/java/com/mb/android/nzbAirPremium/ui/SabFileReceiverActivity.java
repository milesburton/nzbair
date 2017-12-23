
package com.mb.android.nzbAirPremium.ui;

import java.io.File;
import java.net.URI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.mb.android.nzbAirPremium.ui.fragments.SabDownloadsFragment;
import com.mb.nzbair.sabnzb.service.SabProxy;

public class SabFileReceiverActivity extends FragmentActivity {

	public static final String TAG = SabFileReceiverActivity.class.getName();

	@Override
	public void onCreate(Bundle icicle) {

		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// PREMIUM_START
		processSabNzbdFile();
		// PREMIUM_END

		finish();
	}

	// PREMIUM_START
	private void processSabNzbdFile() {

		final Uri uri = getIntent().getData();

		if (uri == null) {
			return;
		}

		if (!SabProxy.getProxy().hasActiveService()) {

			final String msg = "Please setup a SAB server before you try and upload an NZB";
			toast(msg);
			return;
		}

		try {

			final String schema = uri.getScheme();
			if (schema == null) {

				final String msg = "NZBAir did not understand the file you wanted to upload";
				toast(msg);
				return;
			}

			if (schema.equals("file")) {

				SabProxy.getProxy().requestUpload(new File(new URI(getIntent().getData().toString())));

			} else if (schema.equals("http") || schema.equals("https")) {
				SabProxy.getProxy().requestAddByUrl(uri.toString(), "");
			}
			startSabDownloads();
			finish();
		} catch (final Exception e) {

			Log.e(TAG, "Failed to push NZB to SAB", e);
			toast("NZBAir could not upload that file");
		}

	}

	private void toast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	private void startSabDownloads() {
		startActivity(new Intent(this, DownloadsActivity.class).putExtra(DownloadsActivity.STARTUP_CLASS, SabDownloadsFragment.class.getCanonicalName()));
	}
	// PREMIUM_END
}
