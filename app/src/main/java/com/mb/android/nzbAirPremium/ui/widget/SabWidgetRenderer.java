
package com.mb.android.nzbAirPremium.ui.widget;

//PREMIUM_START
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.ui.DownloadsActivity;
import com.mb.android.nzbAirPremium.ui.fragments.SabDownloadsFragment;
import com.mb.nzbair.sabnzb.domain.Queue;
import com.mb.nzbair.sabnzb.domain.Slot;

public class SabWidgetRenderer {

	public static String TAG = SabWidgetRenderer.class.getName();

	public void renderLowPowerView(Context context, RemoteViews updateViews) {
		updateViews.setTextViewText(R.id.currentdownload, "NZBAir Disabled");
		updateViews.setTextViewText(R.id.status, "Battery Low");
		updateViews.setTextViewText(R.id.summary, "");
	}

	public void renderQueue(Context context, RemoteViews updateViews, Queue newQueue) {
		try {

			String summary = "";
			String status = "";
			String title = "";

			// Grab latest download if any
			if (newQueue.getNoOfSlots() > 0) {

				// Current download
				final Slot aSlot = newQueue.getSlots().get(0);
				String filename = aSlot.getFilename();
				if (filename.length() >= 25) {
					filename = filename.substring(0, 23) + "...";
				}

				title = filename;

				final String percentage = "(" + aSlot.getPercentage() + "%)";

				summary = String.format("%1$s / %2$sM %3$s", (int) (aSlot.getMb() - aSlot.getMbLeft()), aSlot.getMb(), percentage);

				if (newQueue.getStatus().equals("Idle") || newQueue.getStatus().equals("Paused")) {
					status = "Paused. Queue: " + newQueue.getNoOfSlots();
				} else {
					status = String.format("%1$s @ %2$s k", newQueue.getTimeLeft(), newQueue.getKbPerSec());
				}
			} else {
				title = "SABNzbd+ is " + newQueue.getStatus();
				summary = String.format("Disk Space: %1$s", newQueue.getDiskSpace1());
				status = String.format("%1$s - %2$s", newQueue.getLoadAvg(), newQueue.getUptime());

			}

			updateViews.setTextViewText(R.id.currentdownload, title);
			updateViews.setTextViewText(R.id.status, status);
			updateViews.setTextViewText(R.id.summary, summary);

			attachMainButtonListeners(context, updateViews);

		} catch (final Exception ex) {
			Log.e(TAG, "Error rendering widget: " + ex.toString());
			ex.printStackTrace();
		}
	}

	private void attachMainButtonListeners(Context con, RemoteViews updateViews) {
		final PendingIntent widgetUpdateIntent = PendingIntent.getBroadcast(con, 0, new Intent().setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE), PendingIntent.FLAG_UPDATE_CURRENT);

		// SABNZB View
		final Intent sabnzbIntent = new Intent(con, DownloadsActivity.class).putExtra(DownloadsActivity.STARTUP_CLASS, SabDownloadsFragment.class.getCanonicalName()).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		final PendingIntent sabnzbPendingIntent = PendingIntent.getActivity(con, 0, sabnzbIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		updateViews.setOnClickPendingIntent(R.id.sabnzbButton, sabnzbPendingIntent);

		// Refresh
		updateViews.setOnClickPendingIntent(R.id.sab_widget, widgetUpdateIntent);
		updateViews.setOnClickPendingIntent(R.id.currentdownload, widgetUpdateIntent);
		updateViews.setOnClickPendingIntent(R.id.status, widgetUpdateIntent);
		updateViews.setOnClickPendingIntent(R.id.summary, widgetUpdateIntent);

	}

}
// PREMIUM_END