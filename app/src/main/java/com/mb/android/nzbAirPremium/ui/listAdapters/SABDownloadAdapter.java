
package com.mb.android.nzbAirPremium.ui.listAdapters;

//PREMIUM_START
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.ui.helper.FormatHelper;
import com.mb.android.ui.listeners.CustomClickListener;
import com.mb.android.ui.listeners.OnCustomClickListener;
import com.mb.nzbair.sabnzb.domain.Slot;

public class SABDownloadAdapter extends BaseAdapter {

	private List<Slot> aModel;
	private final LayoutInflater mInflater;
	private final OnCreateContextMenuListener contextCallback;
	private final OnCustomClickListener<Slot> customCallback;

	public SABDownloadAdapter(Context context, OnCreateContextMenuListener contextCallback, OnCustomClickListener<Slot> customCallback) {

		aModel = new ArrayList<Slot>();

		mInflater = LayoutInflater.from(context);
		this.contextCallback = contextCallback;
		this.customCallback = customCallback;

	}

	public void setModel(List<Slot> model) {
		aModel = model;
	}

	public List<Slot> getModel() {
		return aModel;
	}

	public void add(Slot anItem) {
		aModel.add(anItem);
		this.notifyDataSetChanged();
	}

	public Slot getItem(String guid) {
		for (final Slot anItem : aModel) {
			if (anItem.getNzoId().equals(guid)) {
				return anItem;
			}
		}

		return null;
	}

	@Override
	public int getCount() {

		return aModel.size();
	}

	@Override
	public Object getItem(int arg0) {

		return aModel.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {

		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder view = new ViewHolder();

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listview_row_pendingdownload, null);
			convertView.setOnCreateContextMenuListener(contextCallback);

			final ProgressBar pb = (ProgressBar) convertView.findViewById(R.id.UpdateProgress);
			final TextView tvDownloadTitle = (TextView) convertView.findViewById(R.id.DownloadTitle);
			final TextView tvUpdateProgress = (TextView) convertView.findViewById(R.id.UpdateStatusText);

			final TextView tvTimeLeft = (TextView) convertView.findViewById(R.id.UpdateTimeLeft);
			final TextView tvPriority = (TextView) convertView.findViewById(R.id.UpdatePriority);
			final ImageButton deleteButton = (ImageButton) convertView.findViewById(R.id.DeleteItem);

			view.DownloadTitle = tvDownloadTitle;
			view.UpdateProgress = pb;
			view.UpdateStatusText = tvUpdateProgress;
			view.UpdateTimeLeft = tvTimeLeft;
			view.UpdatePriority = tvPriority;
			view.deleteButton = deleteButton;
			convertView.setTag(view);

		} else {
			view = (ViewHolder) convertView.getTag();
		}

		final Slot item = aModel.get(position);

		final CustomClickListener<Slot> listener = new CustomClickListener<Slot>(customCallback, position, item);
		convertView.setOnTouchListener(listener);

		view.deleteButton.setOnClickListener(listener);
		view.DownloadTitle.setText(item.getFilename());
		view.UpdateProgress.setProgress(item.getPercentage());
		view.UpdateStatusText.setText(FormatHelper.formatFileSize(item.getMb() - item.getMbLeft()) + " of " + FormatHelper.formatFileSize(item.getMb()) + " (" + item.getStatus() + ")");

		view.UpdatePriority.setText("Priority: " + item.getPriority());
		view.UpdateTimeLeft.setText("Time Left: " + item.getTimeLeft());

		return convertView;
	}

	private static class ViewHolder {

		private TextView DownloadTitle;
		private TextView UpdateStatusText;
		private ProgressBar UpdateProgress;
		private TextView UpdateTimeLeft;
		private TextView UpdatePriority;
		private ImageButton deleteButton;
	}

}
// PREMIUM_END