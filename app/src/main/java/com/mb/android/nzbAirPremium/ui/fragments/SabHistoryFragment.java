
package com.mb.android.nzbAirPremium.ui.fragments;

//PREMIUM_START
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.ui.listAdapters.SABHistoryAdapter;
import com.mb.android.ui.listeners.OnCustomClickListener;
import com.mb.nzbair.sabnzb.SabException;
import com.mb.nzbair.sabnzb.domain.HistoricalQueue;
import com.mb.nzbair.sabnzb.domain.HistoricalSlot;

public class SabHistoryFragment extends SabBase implements OnCreateContextMenuListener, OnCustomClickListener<HistoricalSlot>, FragmentMetadata {

	private static final String TAG = SabHistoryFragment.class.getName();
	private SABHistoryAdapter historyAdapter;

	private HistoricalSlot selectedItem;

	public static SabHistoryFragment getInstance(Activity mActivity) {
		final SabHistoryFragment frag = new SabHistoryFragment();
		frag.mActivity = mActivity;
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.layout_list_footer, container, false);
	}

	@Override
	public void onActivityCreated(Bundle icicle) {

		super.onActivityCreated(icicle);
		final ListView lv = (ListView) getView().findViewById(android.R.id.list);
		historyAdapter = new SABHistoryAdapter(getActivity(), this, this);
		lv.setAdapter(historyAdapter);
		refresh();
	}

	@Override
	public void doRefresh() {

		if (canRefresh()) {
			sabProxy.requestHistory(0, 50);
		}
	}

	@Override
	public void onResponseHistory(final HistoricalQueue hQueue, final Throwable error) {

		guiThread.post(new Runnable() {

			@Override
			public void run() {
				try {
					if (error != null) {
						throw error;
					}
					setFooterText("Showing " + hQueue.getSlots().size());
					historyAdapter.setModel(hQueue.getSlots());
				} catch (final SabException ex) {
					setFooterText(ex.getMessage());
				} catch (final IOException ex) {
					setFooterText("Connection error");
				} catch (final Throwable ex) {
					setFooterText("Unknown error");
				}
			}
		});

	}

	@Override
	public void onResponseDeleteHistoryJob(final Boolean ok, final Throwable error) {
		guiThread.post(new Runnable() {

			@Override
			public void run() {
				showBooleanResponse(ok, "Deleted", "Failed to delete job", error);

				// Request update
				doRefresh();
			}
		});

	}

	@Override
	public void OnClick(View aView, int position, HistoricalSlot slot) {

		try {
			switch (aView.getId()) {
			case R.id.DeleteItem:
				final List<HistoricalSlot> jobs = new ArrayList<HistoricalSlot>();
				jobs.add(slot);
				sabProxy.requestDeleteHistoricalJob(jobs, false);
				historyAdapter.remove(slot);

				// Inform user we've completed a delete
				Toast.makeText(getActivity().getApplicationContext(), "Deleting...", Toast.LENGTH_SHORT).show();

				break;
			}
		} catch (final Exception e) {
			Log.e(TAG, "Onclick error: " + e.toString());
			e.printStackTrace();
		}
	}

	@Override
	public void OnLongClick(View aView, int position, HistoricalSlot payload) {

	}

	@Override
	public void OnTouch(View aView, int position, HistoricalSlot item) {
		this.selectedItem = item;
	}

	private static final int ContextMenuRetryJob = 1;

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

		try {
			menu.add(0, ContextMenuRetryJob, 0, "Retry");
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

		switch (item.getItemId()) {
		case ContextMenuRetryJob:
			sabProxy.requestRetryJob(selectedItem);
			return true;
		}

		return true;
	}

	@Override
	public String getTitle() {
		return "SAB History";
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

}
// PREMIUM_END
