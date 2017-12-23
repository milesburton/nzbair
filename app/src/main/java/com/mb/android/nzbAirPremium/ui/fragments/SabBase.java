
package com.mb.android.nzbAirPremium.ui.fragments;

//PREMIUM_START
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.ui.helper.ContextHelper;
import com.mb.android.nzbAirPremium.ui.preferences.SabConfigListActivity;
import com.mb.nzbair.sabnzb.domain.HistoricalQueue;
import com.mb.nzbair.sabnzb.domain.Queue;
import com.mb.nzbair.sabnzb.domain.Slot;
import com.mb.nzbair.sabnzb.service.SabProxy;
import com.mb.nzbair.sabnzb.service.SabRequestCallback;
import com.mb.nzbair.sabnzb.service.SabState;

public abstract class SabBase extends SherlockFragment implements SabRequestCallback, Refreshable, FragmentMetadata {

	static final String TAG = SabBase.class.getName();

	static final String S_ACTIVITY = "activity";

	protected SabProxy sabProxy = SabProxy.getProxy();;
	protected Handler guiThread;
	protected Handler mHandler = new Handler();
	protected Activity mActivity = null;
	private Queue q = null;

	private static final int MENU_SWITCH_SAB = 5;
	private static final int MENU_PAUSE_SAB = 1;
	private static final int MENU_TEMP_PAUSE_SAB = 4;
	private static final int MENU_SPEED_LIMIT_SAB = 2;
	private static final int MENU_SHUTDOWN_SAB = 3;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sabProxy.addListener(this);

		// Prepare threads
		guiThread = new Handler();
	}

	private final Runnable mUpdateTimeTask = new Runnable() {

		@Override
		public void run() {
			guiThread.post(new Runnable() {

				@Override
				public void run() {
					doRefresh();
				}
			});

			mHandler.postDelayed(this, 15000);
		}
	};

	@Override
	public void refresh() {

		if (sabProxy.hasValidSetup()) {
			resumePollingIfRequired();
			doRefresh();

		} else {
			setFooterText("Please check your SAB settings");
		}
	}

	protected boolean canRefresh() {
		return sabProxy.hasValidSetup() && !sabProxy.isPollingPaused();
	}

	public abstract void doRefresh();

	protected void resumePollingIfRequired() {

		if (sabProxy.isPollingPaused()) {
			toast("Resuming SAB Polling...");
			sabProxy.resumePolling();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String text = "";

		switch (item.getItemId()) {
		case MENU_PAUSE_SAB:
			if (q.getSabStatus() == SabState.PAUSED) {

				sabProxy.requestResume();
				text = "Requesting Resume";
			} else {

				sabProxy.requestPause();
				text = "Requesting Pause";
			}

			toast(text);
			return true;
		case MENU_SPEED_LIMIT_SAB:
			popupSpeedOption();
			return true;
		case MENU_SHUTDOWN_SAB:
			sabProxy.requestShutdown();
			return true;
		case MENU_TEMP_PAUSE_SAB:

			if (q != null && sabProxy.hasValidSetup()) {
				if (q.getSabStatus() == SabState.PAUSED) {
					sabProxy.requestResume();
					text = "Requesting Resume";
				} else {
					if (item.getItemId() == 1) {
						sabProxy.requestPause();
						text = "Requesting Pause";
					} else {
						popupPauseOption();
					}
				}
			} else {
				text = "Check SABNzbd setup or wait a moment...";
			}
			if (text != null) {
				toast(text);
			}

			return true;
		case MENU_SWITCH_SAB:
			getActivity().startActivity(new Intent(getActivity(), SabConfigListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);

	}

	@Override
	public void onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
		menu.add(android.view.Menu.NONE, MENU_SWITCH_SAB, android.view.Menu.NONE, "Switch SABNzbd Server").setIcon(android.R.drawable.ic_menu_set_as);
		menu.add(android.view.Menu.NONE, MENU_PAUSE_SAB, android.view.Menu.NONE, "Pause/Resume").setIcon(android.R.drawable.ic_media_pause);
		menu.add(android.view.Menu.NONE, MENU_TEMP_PAUSE_SAB, android.view.Menu.NONE, "Temporary Pause/Resume").setIcon(android.R.drawable.ic_media_pause);
		menu.add(android.view.Menu.NONE, MENU_SPEED_LIMIT_SAB, android.view.Menu.NONE, "Speed limit").setIcon(android.R.drawable.ic_menu_sort_by_size);
		menu.add(android.view.Menu.NONE, MENU_SHUTDOWN_SAB, android.view.Menu.NONE, "Shutdown").setIcon(android.R.drawable.ic_lock_power_off);

		super.onCreateOptionsMenu(menu, inflater);
	}

	protected void setFooterText(String text) {
		final View v = ContextHelper.getView(TAG, this);
		if (v == null) {
			return;
		}
		final TextView footer = (TextView) v.findViewById(R.id.footer);
		if (footer != null) {
			footer.setText(text);
		}
	}

	private void popupSpeedOption() {
		final Activity con = mActivity;
		if (con == null) {
			return;
		}

		// Required for popup box
		final FrameLayout fl = new FrameLayout(con);
		final EditText input = new EditText(con);
		input.setInputType(InputType.TYPE_CLASS_NUMBER);

		// Add popup box view
		fl.addView(input, new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		// Prepare and launch popup
		new AlertDialog.Builder(con).setView(fl).setTitle("Throttle Download Speed").setPositiveButton("Set", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface d, int which) {
				d.dismiss();
				try {
					sabProxy.requestSetSpeedLimit(Integer.parseInt(input.getText().toString()));

				} catch (final Exception e) {
					Log.e(TAG, e.toString());
				}
			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface d, int which) {
				d.dismiss();
			}
		}).create().show();
	}

	/**
	 * Pops up a temporary window to get user input. On successful input
	 * triggers the sabProxy to rename option
	 * 
	 * @param aJob
	 */
	private void popupPauseOption() {
		final Activity con = mActivity;
		if (con == null) {
			return;
		}

		// Required for popup box
		final FrameLayout fl = new FrameLayout(con);
		final EditText input = new EditText(con);
		input.setInputType(InputType.TYPE_CLASS_NUMBER);

		// Add popup box view
		fl.addView(input, new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		final CharSequence[] items = { "5 Minutes", "30 Minutes", "1 Hour", "2 Hours", "8 Hours" };
		// Prepare and launch popup
		new AlertDialog.Builder(con).setView(fl).setTitle("Temporary Pause time (minutes)").setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					sabProxy.requestPause(5);
					break;
				case 1:
					sabProxy.requestPause(30);
					break;
				case 2:
					sabProxy.requestPause(60);
					break;
				case 3:
					sabProxy.requestPause(120);
					break;
				case 4:
					sabProxy.requestPause(480);
					break;
				default:
					sabProxy.requestPause(30);
					break;
				}

			}
		}).create().show();
	}

	@Override
	public void onPause() {
		try {
			sabProxy.removeListener(this);
			mHandler.removeCallbacks(mUpdateTimeTask);
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			super.onPause();
		}
	}

	@Override
	public void onResume() {
		try {
			sabProxy.addListener(this);
			mHandler.removeCallbacks(mUpdateTimeTask);
			mHandler.postDelayed(mUpdateTimeTask, 1);
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			super.onResume();
		}
	}

	protected void toast(String text) {

		final Context con = ContextHelper.getContext(TAG, mActivity);
		if (con == null) {
			return;
		}

		if (!"".equals(text)) {
			Toast.makeText(con, text, Toast.LENGTH_LONG).show();
		}
	}

	protected void showBooleanResponse(final Boolean result, final String success, final String fail, final Throwable error) {
		guiThread.post(new Runnable() {

			@Override
			public void run() {
				final Context con = ContextHelper.getContext(TAG, mActivity);
				if (con == null) {
					return;
				}
				if (error != null) {
					com.mb.android.nzbAirPremium.ui.helper.ThrowableHelper.showError(con, error);
					return;
				}
				String toastText;
				if (result != null && result) {
					toastText = success;
				} else {
					toastText = fail;
				}

				toast(toastText);

			}
		});
	}

	public void updateQueueDetails(Queue queue, Throwable error) {
		this.q = queue;
	}

	@Override
	public void onResponseQueueStatus(final Queue queue, final Throwable error) {

		guiThread.post(new Runnable() {

			@Override
			public void run() {
				updateQueueDetails(queue, error);
			}
		});

	}

	@Override
	public void onResponseSetSpeedLimit(Boolean ok, Throwable error) {
		showBooleanResponse(ok, "Speed limit set", "Could not set speed limit", error);
	}

	@Override
	public void onResponseShutdown(Boolean ok, Throwable error) {
		showBooleanResponse(ok, "Shutdown", "Could not shutdown", error);
	}

	@Override
	public void onResponsePause(Boolean ok, Throwable error) {
		showBooleanResponse(ok, "Paused", "Could not pause", error);
	}

	@Override
	public void onResponseResume(Boolean ok, Throwable error) {
		showBooleanResponse(ok, "Resumed", "Could not resume", error);
	}

	@Override
	public void onResponseMoveJob(Slot from, final Boolean ok, final Throwable error) {
		showBooleanResponse(ok, "Job Moved", "Failed to move job", error);
	}

	@Override
	public void onResponsePauseJob(Slot job, Boolean ok, final Throwable error) {
		showBooleanResponse(ok, "Paused", "Failed to pause", error);
	}

	@Override
	public void onResponseRenameJob(Slot job, final Boolean ok, final Throwable error) {
		showBooleanResponse(ok, "Renamed", "Couldnt rename", error);
	}

	@Override
	public void onResponseResumeJob(Slot job, Boolean ok, final Throwable error) {
		showBooleanResponse(ok, "Resumed", "Failed to resume", error);
	}

	@Override
	public void onResponseSwapJob(Slot from, final Boolean ok, final Throwable error) {
		showBooleanResponse(ok, "Job Moved", "Failed to move job", error);
	}

	@Override
	public void onResponseRetryJob(Boolean ok, final Throwable error) {
		showBooleanResponse(ok, "Job restarted", "Failed to restart job", error);
	}

	@Override
	public void onResponsePriority(Slot job, Boolean ok, Throwable error) {
		showBooleanResponse(ok, "Priority forced", "Failed to force priority", error);
	}

	@Override
	public void onResponseVersion(String version, Throwable error) {

	}

	@Override
	public void onResponseWarningsList(Throwable error) {

	}

	@Override
	public void onResponseDeleteHistoryJob(Boolean ok, Throwable error) {

	}

	@Override
	public void onResponseHistory(HistoricalQueue hQueue, Throwable error) {
	}

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
	public void onResponseDeleteJobs(List<Slot> jobs, Boolean ok, Throwable error) {

	}

	@Override
	public void onResponseScriptList(Throwable error) {

	}

	@Override
	public void onResponseSetEmptyQueueAction(String action, Throwable error) {

	}

	@Override
	public void onPollingPaused(String reason) {

		showBooleanResponse(true, "Polling has been paused. To resume touch refresh", "", null);
	}

	@Override
	public void onFileUploaded(Boolean ok, Throwable error) {

	}
}
// PREMIUM_END