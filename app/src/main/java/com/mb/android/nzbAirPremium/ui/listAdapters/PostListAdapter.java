
package com.mb.android.nzbAirPremium.ui.listAdapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.ui.helper.FormatHelper;
import com.mb.android.ui.listeners.CustomClickListener;
import com.mb.android.ui.listeners.OnCustomClickListener;
import com.mb.nzbair.providers.domain.UsenetPost;

public class PostListAdapter extends BaseAdapter implements IPostListAdapter {

	private List<UsenetPost> aModel = new ArrayList<UsenetPost>();
	private final LayoutInflater mInflater;

	private final OnCustomClickListener<UsenetPost> customCallback;

	public PostListAdapter(Context context, OnCustomClickListener<UsenetPost> customCallback) {
		this.customCallback = customCallback;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public void setModel(List<UsenetPost> model) {
		aModel = model;
	}

	@Override
	public List<UsenetPost> getModel() {
		return aModel;
	}

	public void add(UsenetPost anItem) {
		aModel.add(anItem);
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {

		if (aModel == null) {
			return 0;
		}
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
			convertView = mInflater.inflate(R.layout.listview_row_post, null);

			final TextView tvTitle = (TextView) convertView.findViewById(R.id.title);
			final TextView tvSize = (TextView) convertView.findViewById(R.id.size);

			view.title = tvTitle;
			view.size = tvSize;
			convertView.setTag(view);

		} else {
			view = (ViewHolder) convertView.getTag();
		}

		final UsenetPost item = aModel.get(position);

		view.title.setText(item.getTitle());
		view.size.setText(FormatHelper.formatFileSizeBytes(item.getFilesize()));

		final CustomClickListener<UsenetPost> handler = new CustomClickListener<UsenetPost>(customCallback, position, item);

		convertView.setOnLongClickListener(handler);
		convertView.setOnClickListener(handler);

		return convertView;
	}

	private static class ViewHolder {

		private TextView title;
		private TextView size;
	}

}
