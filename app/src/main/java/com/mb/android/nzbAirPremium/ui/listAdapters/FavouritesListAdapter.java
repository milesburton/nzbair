
package com.mb.android.nzbAirPremium.ui.listAdapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.ui.helper.FormatHelper;
import com.mb.android.ui.listeners.CustomClickListener;
import com.mb.android.ui.listeners.OnCustomClickListener;
import com.mb.nzbair.providers.domain.UsenetPost;

public class FavouritesListAdapter extends BaseAdapter implements IPostListAdapter {

	private static final String TAG = FavouritesListAdapter.class.getName();
	private List<UsenetPost> aModel = new ArrayList<UsenetPost>();
	private final LayoutInflater mInflater;
	private final OnCustomClickListener<UsenetPost> customTouchCallback;

	public FavouritesListAdapter(Context context, OnCustomClickListener<UsenetPost> customTouchCallback) {
		mInflater = LayoutInflater.from(context);
		this.customTouchCallback = customTouchCallback;
	}

	@Override
	public void setModel(List<UsenetPost> model) {
		aModel = model;
	}

	@Override
	public List<UsenetPost> getModel() {
		return aModel;
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
			convertView = mInflater.inflate(R.layout.listview_row_favourites, null);
			final TextView tvTitle = (TextView) convertView.findViewById(R.id.title);
			final TextView tvSize = (TextView) convertView.findViewById(R.id.size);
			final ImageButton deleteButton = (ImageButton) convertView.findViewById(R.id.DeleteItem);

			view.title = tvTitle;
			view.deleteButton = deleteButton;
			view.size = tvSize;
			convertView.setTag(view);

		} else {
			view = (ViewHolder) convertView.getTag();
		}

		try {
			final UsenetPost item = aModel.get(position);
			view.title.setText(item.getTitle());
			view.size.setText(FormatHelper.formatFileSizeBytes(item.getFilesize()));
			view.deleteButton.setTag(position);

			final CustomClickListener<UsenetPost> handler = new CustomClickListener<UsenetPost>(this.customTouchCallback, position, item);
			view.deleteButton.setOnClickListener(handler);
			convertView.setOnClickListener(handler);
		} catch (final Exception ex) {
			Log.e(TAG, "Populating list error, sectionsAdapter: " + ex.toString());
		}

		return convertView;

	}

	private static class ViewHolder {

		private TextView title;
		private TextView size;
		private ImageButton deleteButton;
	}

}
