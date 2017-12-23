
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
import android.widget.TextView;

import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.ui.helper.FormatHelper;
import com.mb.android.ui.listeners.CustomClickListener;
import com.mb.android.ui.listeners.OnCustomClickListener;
import com.mb.nzbair.sabnzb.domain.HistoricalSlot;

public class SABHistoryAdapter extends BaseAdapter {

	private List<HistoricalSlot> aModel = new ArrayList<HistoricalSlot>();;
	private final LayoutInflater mInflater;
	private final OnCreateContextMenuListener contextCallback;
	private final OnCustomClickListener<HistoricalSlot> customCallback;

	public SABHistoryAdapter(Context context, OnCreateContextMenuListener contextCallback, OnCustomClickListener<HistoricalSlot> customCallback) {
		mInflater = LayoutInflater.from(context);
		this.contextCallback = contextCallback;
		this.customCallback = customCallback;
	}

	public void setModel(List<HistoricalSlot> model) {
		aModel = model;
		this.notifyDataSetChanged();
	}

	public void add(HistoricalSlot anItem) {
		aModel.add(anItem);
		this.notifyDataSetChanged();
	}

	public void remove(HistoricalSlot item) {
		aModel.remove(item);
		this.notifyDataSetChanged();
	}

	public HistoricalSlot getItem(String guid) {
		for (final HistoricalSlot anItem : aModel) {
			if (anItem.getNzo_id().equals(guid)) {
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
			convertView = mInflater.inflate(R.layout.listview_row_history, null);
			convertView.setOnCreateContextMenuListener(contextCallback);

			final TextView tvDownloadTitle = (TextView) convertView.findViewById(R.id.DownloadTitle);

			final TextView tvDownloadStatus = (TextView) convertView.findViewById(R.id.UpdateStatusText);

			final TextView tvUpdateSize = (TextView) convertView.findViewById(R.id.UpdateSize);

			final ImageButton deleteButton = (ImageButton) convertView.findViewById(R.id.DeleteItem);

			view.DownloadTitle = tvDownloadTitle;
			view.deleteButton = deleteButton;
			view.UpdateStatusText = tvDownloadStatus;
			view.UpdateSize = tvUpdateSize;
			convertView.setTag(view);

		} else {
			view = (ViewHolder) convertView.getTag();
		}

		final HistoricalSlot item = aModel.get(position);

		final CustomClickListener<HistoricalSlot> listener = new CustomClickListener<HistoricalSlot>(customCallback, position, item);

		convertView.setOnTouchListener(listener);
		view.deleteButton.setOnClickListener(listener);
		view.deleteButton.setTag(position);
		view.DownloadTitle.setText(item.getName());
		view.UpdateStatusText.setText("Status:" + item.getSabStatus().toString());
		try {
			view.UpdateSize.setText("Size: " + FormatHelper.formatFileSizeBytes(Double.parseDouble(item.getBytes())));
		} catch (final Exception ex) {
		}
		return convertView;
	}

	private static class ViewHolder {

		private TextView DownloadTitle;
		private TextView UpdateStatusText;
		private TextView UpdateSize;
		// TextView UpdateTimeLeft;
		// TextView UpdatePriority;
		private ImageButton deleteButton;
	}

}
// PREMIUM_END
