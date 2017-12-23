
package com.mb.android.nzbAirPremium.ui.listAdapters;

import java.text.NumberFormat;
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
import com.mb.android.nzbAirPremium.download.AirDownloadMetadata;
import com.mb.android.nzbAirPremium.ui.helper.FormatHelper;
import com.mb.android.ui.listeners.CustomClickListener;
import com.mb.android.ui.listeners.OnCustomClickListener;

public class DownloadListAdapter extends BaseAdapter {

	private List<AirDownloadMetadata> model = new ArrayList<AirDownloadMetadata>();
	private final LayoutInflater mInflater;

	private final OnCustomClickListener<AirDownloadMetadata> onCustomClickListener;

	private final OnCreateContextMenuListener onCreateContextMenuListener;

	public DownloadListAdapter(Context context, OnCreateContextMenuListener onCreateContextMenuListener, OnCustomClickListener<AirDownloadMetadata> onCustomClickListener) {
		mInflater = LayoutInflater.from(context);

		this.onCreateContextMenuListener = onCreateContextMenuListener;
		this.onCustomClickListener = onCustomClickListener;
	}

	public void add(AirDownloadMetadata anItem) {
		model.add(anItem);
		this.notifyDataSetChanged();
	}

	public void remove(AirDownloadMetadata anItem) {
		model.remove(anItem);
		this.notifyDataSetChanged();
	}

	public void setModel(List<AirDownloadMetadata> model) {
		this.model = model;
	}

	public AirDownloadMetadata getItem(String guid) {
		for (final AirDownloadMetadata anItem : model) {
			if (anItem.getUuid().equals(guid)) {
				return anItem;
			}
		}

		return null;
	}

	@Override
	public int getCount() {

		return model.size();
	}

	@Override
	public Object getItem(int arg0) {

		return model.get(arg0);
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
			convertView.setOnCreateContextMenuListener(onCreateContextMenuListener);

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
		final AirDownloadMetadata item = model.get(position);
		view.deleteButton.setTag(position);
		view.DownloadTitle.setText(item.getDestinationFilename());
		view.UpdateProgress.setProgress(item.getProgress());
		view.UpdateStatusText.setText(formatBytes(item.getfileDownloaded()) + " of " + formatBytes(item.getFilesize()));
		view.UpdateTimeLeft.setText("Speed: " + item.getEta());
		view.UpdatePriority.setText("Status: " + item.getStatusAsText());

		final CustomClickListener<AirDownloadMetadata> customClickListener = new CustomClickListener<AirDownloadMetadata>(onCustomClickListener, position, item);
		view.deleteButton.setOnClickListener(customClickListener);
		convertView.setOnTouchListener(customClickListener);

		return convertView;
	}

	private static String formatBytes(double sizeInBytes) {
		if (sizeInBytes <= 0) {
			return "0 K";
		}

		sizeInBytes /= 1024;

		if (sizeInBytes > 1024) {
			sizeInBytes /= 1024;
			return FormatHelper.formatFileSize(sizeInBytes);
		} else {
			final NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(2);
			return nf.format(sizeInBytes) + " K";
		}

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
