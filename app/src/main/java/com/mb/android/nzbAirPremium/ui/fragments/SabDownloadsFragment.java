
package com.mb.android.nzbAirPremium.ui.fragments;

//PREMIUM_START
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.ui.listAdapters.SABDownloadAdapter;
import com.mb.android.ui.listeners.OnCustomClickListener;
import com.mb.nzbair.sabnzb.SabException;
import com.mb.nzbair.sabnzb.domain.Queue;
import com.mb.nzbair.sabnzb.domain.Slot;

public class SabDownloadsFragment extends SabBase implements OnCreateContextMenuListener, OnCustomClickListener<Slot> {

	private static final String TAG = SabDownloadsFragment.class.getName();

	private SABDownloadAdapter queuedDownloadsAdapter;
	private int selectedPos;

	public static SabDownloadsFragment getInstance(Activity mActivity) {
		final SabDownloadsFragment frag = new SabDownloadsFragment();
		frag.mActivity = mActivity;
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.layout_list_footer, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		if (mActivity == null) {
			mActivity = this.getActivity();
		}

		super.onActivityCreated(savedInstanceState);
		queuedDownloadsAdapter = new SABDownloadAdapter(mActivity, this, this);
		final ListView lv = (ListView) getView().findViewById(android.R.id.list);
		lv.setAdapter(queuedDownloadsAdapter);
		refresh();
	}

	private static final int ContextMenuPause = 1;
	private static final int ContextMenuResume = 2;
	private static final int ContextMenuRename = 3;
	private static final int ContextMenuRaisePriority = 4;
	private static final int ContextMenuLowerPriority = 5;
	private static final int ContextMenuForce = 6;

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

		try {

			final Slot aJob = (Slot) queuedDownloadsAdapter.getItem(selectedPos);

			menu.add(0, ContextMenuRename, 0, "Rename");

			switch (aJob.getSabStatus()) {
			case PAUSED:
				menu.add(0, ContextMenuResume, 0, "Resume");
				break;
			case DOWNLOADING:
			case IDLE:
				menu.add(0, ContextMenuPause, 0, "Pause");
				break;
			}

			if (queuedDownloadsAdapter.getModel().size() > 1) {
				if (selectedPos > 0) {
					menu.add(0, ContextMenuForce, 0, "Download Now (Force)");
					menu.add(0, ContextMenuRaisePriority, 0, "Raise Priority");
				}

				if (queuedDownloadsAdapter.getModel().size() > 0 && selectedPos < (queuedDownloadsAdapter.getModel().size() - 1)) {
					menu.add(0, ContextMenuLowerPriority, 0, "Lower Priority");
				}
			}
		} catch (final Exception ex) {
			Log.e(TAG, "Context menu failed to populate. " + ex.toString());
			ex.printStackTrace();
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		
		if (!getUserVisibleHint()) {
			return false;
		}

		final Slot aJob = (Slot) queuedDownloadsAdapter.getItem(selectedPos);
		switch (item.getItemId()) {
		case ContextMenuRename:
			// Popup a text input window
			renameJob(aJob);
			return true;

		case ContextMenuPause:
			sabProxy.requestPauseJob(aJob);
			return true;
		case ContextMenuResume:
			sabProxy.requestResumeJob(aJob);
			return true;
		case ContextMenuForce:
			sabProxy.requestPriority(aJob, 2);
			return true;
		case ContextMenuRaisePriority: {
			if (selectedPos > 0) {
				final Slot switchWith = (Slot) queuedDownloadsAdapter.getItem(selectedPos - 1);
				sabProxy.requestSwapJob(aJob, switchWith);
			}
			return true;
		}

		case ContextMenuLowerPriority: {
			if (selectedPos < queuedDownloadsAdapter.getModel().size()) {
				final Slot switchWith = (Slot) queuedDownloadsAdapter.getItem(selectedPos + 1);
				sabProxy.requestSwapJob(aJob, switchWith);
			}
			return true;
		}

		default:
			return super.onContextItemSelected(item);
		}
	}

	/**
	 * Pops up a temporary window to get user input. On successful input
	 * triggers the sabProxy to rename option
	 * 
	 * @param aJob
	 */
	private void renameJob(final Slot aJob) {
		final Context con = mActivity;
		if (con == null) {
			return;
		}

		// Required for popup box
		final FrameLayout fl = new FrameLayout(con);
		final EditText input = new EditText(con);

		// Add popup box view
		fl.addView(input, new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		// Set current name
		input.setText(aJob.getFilename());

		// Prepare and launch popup
		new AlertDialog.Builder(con).setView(fl).setTitle("Rename NZB filename").setPositiveButton("Set", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface d, int which) {
				d.dismiss();
				try {
					SabDownloadsFragment.this.sabProxy.requestRenameJob(aJob, input.getText().toString());

				} catch (final Exception ex) {
					ex.printStackTrace();
					SabDownloadsFragment.this.setFooterText("Failed to rename");
				}
			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface d, int which) {
				d.dismiss();
			}
		}).create().show();
	}

	@Override
	public void onResponseDeleteJobs(final List<Slot> jobs, final Boolean ok, final Throwable error) {

		guiThread.post(new Runnable() {

			@Override
			public void run() {
				showBooleanResponse(ok, "Deleted", "Failed to delete", error);
				doRefresh();
			}
		});

	}

	@Override
	public void onResponseQueueStatus(final Queue queue, final Throwable error) {
		super.onResponseQueueStatus(queue, error);

		guiThread.post(new Runnable() {

			@Override
			public void run() {
				try {
					if (error != null) {
						throw error;
					}

					queuedDownloadsAdapter.setModel(queue.getSlots());
					queuedDownloadsAdapter.notifyDataSetChanged();
					setFooterText("Queue: " + queue.getNoOfSlots() + " - Finished: " + queue.getFinished() + " - " + queue.getKbPerSec() + "K/s (" + queue.getSpeedLimit() + ") ");
				} catch (final SabException ex) {
					setFooterText(ex.getMessage());
				} catch (final IOException ex) {
					toast(ex.getMessage());
					setFooterText("Connection error");
				} catch (final Throwable ex) {
					toast(ex.getMessage());
					setFooterText("Unknown error");
				}
			}
		});

	}

	@Override
	public void OnTouch(View aView, int position, Slot item) {
		this.selectedPos = position;

	}

	@Override
	public void OnClick(View aView, int position, Slot job) {

		try {
			switch (aView.getId()) {
			case R.id.DeleteItem:
				Log.d(TAG, "Item #" + selectedPos);

				final List<Slot> jobs = new ArrayList<Slot>();
				jobs.add(job);
				sabProxy.requestDeleteJobs(jobs, false);

				toast("Deleting...");

				break;
			}
		} catch (final Exception ex) {
			Log.e(TAG, "Onclick error: " + ex.toString());
			ex.printStackTrace();
		}
	}

	@Override
	public String getTitle() {
		return "SAB Downloads";
	}

	@Override
	public void OnLongClick(View aView, int position, Slot payload) {

	}

	@Override
	public void onPollingPaused(String reason) {

		guiThread.post(new Runnable() {

			@Override
			public void run() {
				setFooterText("Polling has been paused");
			}
		});
	}

	@Override
	public void doRefresh() {

		if (canRefresh()) {
			sabProxy.requestQueueStatus();
		}
	}

}
// PREMIUM_END
