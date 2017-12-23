
package com.mb.android.nzbAirPremium.ui.helper;

import android.content.Context;
import android.widget.Toast;

import com.mb.nzbair.sabnzb.SabException;
import com.mb.nzbair.seh.NZBAirServiceException;
import com.mb.nzbair.seh.ProviderException;

public class ThrowableHelper {

	public static void showError(Context con, Throwable error) {

		if (con == null) {
			return;
		}

		String text = "";
		if (error instanceof ProviderException) {
			final ProviderException ex = (ProviderException) error;
			text = ex.getProviderErrorString();
			if ("".equals(text)) {
				text = ex.getErrorString();
			}
		} else if (error instanceof NZBAirServiceException) {
			final NZBAirServiceException ex = (NZBAirServiceException) error;
			text = ex.getErrorString();
		} else if (error instanceof SabException) {
			final SabException ex = (SabException) error;
			text = ex.getMessage();
		}

		Toast.makeText(con, text, Toast.LENGTH_SHORT).show();
	}
}
