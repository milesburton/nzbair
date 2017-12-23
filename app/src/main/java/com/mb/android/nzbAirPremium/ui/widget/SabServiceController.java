
package com.mb.android.nzbAirPremium.ui.widget;

//PREMIUM_START
import java.util.List;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import com.mb.android.nzbAirPremium.R;
import com.mb.nzbair.sabnzb.domain.HistoricalQueue;
import com.mb.nzbair.sabnzb.domain.Queue;
import com.mb.nzbair.sabnzb.domain.Slot;
import com.mb.nzbair.sabnzb.service.SabProxy;
import com.mb.nzbair.sabnzb.service.SabRequestCallback;

public class SabServiceController {

	public static String TAG = SabServiceController.class.getCanonicalName();
	private Context context;
	private int[] widgetIds;

	public void update(Context context, int[] widgetIds) {

		this.context = context;
		this.widgetIds = widgetIds;

		if (!SabProxy.getProxy().hasActiveService() || SabProxy.getProxy().isPollingPaused()) {
			Log.i(TAG, "No active sab service");
		}

		if (context == null) {
			Log.i(TAG, "no valid context");
		}

		if (widgetIds == null) {
			Log.w(TAG, "No Widgets to update. Aborting...");
			return;
		}

		SabProxy.getProxy().addListener(sabCallback);
		SabProxy.getProxy().requestQueueStatus();
	}

	public void setLowPowerView() {
		final RemoteViews updateViews = createRemoteView();
		new SabWidgetRenderer().renderLowPowerView(context, updateViews);
		updateWidgets(context, updateViews);
	}

	private void updateWidgets(Context con, RemoteViews updateViews) {
		AppWidgetManager.getInstance(con).updateAppWidget(widgetIds, updateViews);
	}

	private RemoteViews createRemoteView() {
		final RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget_sab);
		return updateViews;
	}

	private final SabRequestCallback sabCallback = new SabRequestCallback() {

		private final String TAG = SabServiceController.class.getCanonicalName();

		@Override
		public void onResponseAddByUrl(Boolean ok, Throwable error) {

		}

		@Override
		public void onResponseAutoShutdownOff(Boolean ok, Throwable error) {

		}

		@Override
		public void onResponseAutoShutdownOn(Boolean ok, Throwable error) {

		}

		@Override
		public void onResponseCategoryList(List<String> categories, Throwable error) {

		}

		@Override
		public void onResponseDeleteHistoryJob(Boolean ok, Throwable error) {

		}

		@Override
		public void onResponseDeleteJobs(List<Slot> jobs, Boolean ok, Throwable error) {

		}

		@Override
		public void onResponseHistory(HistoricalQueue hQueue, Throwable error) {

		}

		@Override
		public void onResponseMoveJob(Slot from, Boolean ok, Throwable error) {

		}

		@Override
		public void onResponsePause(Boolean ok, Throwable error) {

		}

		@Override
		public void onResponsePauseJob(Slot job, Boolean ok, Throwable error) {

		}

		@Override
		public void onResponseQueueStatus(final Queue queue, Throwable error) {

			SabProxy.getProxy().removeListener(this);

			if (error == null) {

				try {
					final RemoteViews updateViews = createRemoteView();
					new SabWidgetRenderer().renderQueue(context, updateViews, queue);
					updateWidgets(context, updateViews);
				} catch (final Exception ex) {
					Log.e(TAG, "Failed to render queue status");
				}
			}

		}

		@Override
		public void onResponseRenameJob(Slot job, Boolean ok, Throwable error) {

		}

		@Override
		public void onResponseResume(Boolean ok, Throwable error) {

		}

		@Override
		public void onResponseResumeJob(Slot job, Boolean ok, Throwable error) {

		}

		@Override
		public void onResponseRetryJob(Boolean ok, Throwable error) {

		}

		@Override
		public void onResponseScriptList(Throwable error) {

		}

		@Override
		public void onResponseSetEmptyQueueAction(String action, Throwable error) {

		}

		@Override
		public void onResponseSetSpeedLimit(Boolean ok, Throwable error) {

		}

		@Override
		public void onResponseShutdown(Boolean ok, Throwable error) {

		}

		@Override
		public void onResponseSwapJob(Slot from, Boolean ok, Throwable error) {

		}

		@Override
		public void onResponseVersion(String version, Throwable error) {

		}

		@Override
		public void onResponseWarningsList(Throwable error) {

		}

		@Override
		public void onResponsePriority(Slot job, Boolean ok, Throwable error) {

		}

		@Override
		public void onPollingPaused(String reason) {

		}

		@Override
		public void onFileUploaded(Boolean ok, Throwable error) {
		}

	};
}
// PREMIUM_END