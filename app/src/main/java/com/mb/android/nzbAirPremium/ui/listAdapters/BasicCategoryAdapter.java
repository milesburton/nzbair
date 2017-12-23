
package com.mb.android.nzbAirPremium.ui.listAdapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mb.android.nzbAirPremium.R;
import com.mb.android.ui.listeners.CustomClickListener;
import com.mb.android.ui.listeners.OnCustomClickListener;
import com.mb.nzbair.providers.domain.category.Category;

public class BasicCategoryAdapter extends BaseAdapter {

	private List<Category> aModel = new ArrayList<Category>();
	private final LayoutInflater mInflater;
	private final OnCustomClickListener<Category> callback;
	private final OnCreateContextMenuListener contextCallback;

	public BasicCategoryAdapter(Context context, OnCustomClickListener<Category> callback, OnCreateContextMenuListener contextCallback) {
		mInflater = LayoutInflater.from(context);
		this.callback = callback;
		this.contextCallback = contextCallback;
	}

	public List<Category> getModel() {
		return aModel;
	}

	public List<Category> setModel(List<Category> model) {
		this.aModel = model;
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
		
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listview_row_simple, null);
			
			if(contextCallback!= null)
				convertView.setOnCreateContextMenuListener(contextCallback);

			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.title.setText(aModel.get(position).getTitle());

		final CustomClickListener<Category> handler = new CustomClickListener<Category>(callback, position, aModel.get(position));

		convertView.setOnClickListener(handler);
		convertView.setOnTouchListener(handler);

		return convertView;
	}

	private static class ViewHolder {

		private TextView title;
	}

}
