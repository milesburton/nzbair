
package com.mb.android.nzbAirPremium.ui.listAdapters;

import java.util.Hashtable;
import java.util.Map.Entry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mb.android.nzbAirPremium.R;

public class KeyValueAdapter extends BaseAdapter {

	private Hashtable<String, String> list;
	private final LayoutInflater mInflater;

	public KeyValueAdapter(Context context) {
		mInflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		if (list == null) {
			return 0;
		}

		final int size = list.size();
		return size;
	}

	@Override
	public Object getItem(int arg0) {
		if (list == null) {
			return null;
		}

		return list.entrySet().toArray()[arg0];
	}

	@Override
	public long getItemId(int arg0) {

		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listview_row_keyvalue, null);
			holder = new ViewHolder();
			holder.key = (TextView) convertView.findViewById(R.id.key);
			holder.value = (TextView) convertView.findViewById(R.id.value);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Entry<String, String> kvp = (Entry<String, String>) getItem(position);
		holder.key.setText(kvp.getKey() + ":");
		holder.value.setText(kvp.getValue());

		return convertView;
	}

	public void setList(Hashtable<String, String> list) {
		this.list = list;
	}

	private static class ViewHolder {

		private TextView key;
		private TextView value;
	}

}
