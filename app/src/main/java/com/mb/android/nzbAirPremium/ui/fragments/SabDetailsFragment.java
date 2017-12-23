
package com.mb.android.nzbAirPremium.ui.fragments;

//PREMIUM_START
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.ui.helper.ContextHelper;
import com.mb.android.nzbAirPremium.ui.helper.FormatHelper;
import com.mb.nzbair.sabnzb.domain.Queue;

public class SabDetailsFragment extends SabBase {

	private static final String TAG = SabDetailsFragment.class.getName();

	public static SabDetailsFragment getInstance(Activity mActivity) {
		final SabDetailsFragment frag = new SabDetailsFragment();
		frag.mActivity = mActivity;
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.layout_sab_details, container, false);
	}

	@Override
	public void updateQueueDetails(final Queue queue, Throwable error) {
		super.updateQueueDetails(queue, error);
		try {
			if (getView() == null || error != null) {
				return;
			}

			setText(R.id.nzbQueueActive, queue.getNoOfSlots() + "");
			setText(R.id.nzbQueueComplete, queue.getFinished() + "");

			setText(R.id.nzbProgressComplete, FormatHelper.formatFileSize((queue.getMb() - queue.getMbLeft())));
			setText(R.id.nzbProgressTotal, FormatHelper.formatFileSize(queue.getMb()));

			setText(R.id.nzbEta, queue.getEta());
			setText(R.id.nzbState, queue.getStatus());
			setText(R.id.nzbSpeed, queue.getKbPerSec() + " (" + queue.getSpeedLimit() + ")");
			setText(R.id.nzbLoad, queue.getLoadAvg());
			setText(R.id.nzbUptime, queue.getUptime());

			setText(R.id.nzbWarnings, queue.getHaveWarnings() + "");

			setText(R.id.nzbDiskspace1, (queue.getDiskSpace1() + "GB"));
			setText(R.id.nzbDiskspace2, (queue.getDiskSpace2() + "GB"));

			((ProgressBar) getView().findViewById(R.id.diskspace1)).setProgress(queue.getDisk1SpaceRemaining());

			((ProgressBar) getView().findViewById(R.id.diskspace2)).setProgress(queue.getDisk2SpaceRemaining());

			((ProgressBar) getView().findViewById(R.id.progress)).setProgress(queue.getDownloadProgress());

		} catch (final Exception ex) {
			Log.e(TAG, ex.toString());
			final String err = "Error, check SAB settings";
			setText(R.id.nzbState, err);
			setText(R.id.nzbQueueComplete, err);
			ex.printStackTrace();
		}
	}

	private void setText(int viewId, String value) {
		final View v = ContextHelper.getView(TAG, this);
		if (v == null) {
			return;
		}
		((TextView) v.findViewById(viewId)).setText(value);
	}

	@Override
	public String getTitle() {
		return "SAB Details";
	}

	@Override
	public void doRefresh() {

		if (canRefresh()) {
			sabProxy.requestQueueStatus();
		}
	}

}
// PREMIUM_END
