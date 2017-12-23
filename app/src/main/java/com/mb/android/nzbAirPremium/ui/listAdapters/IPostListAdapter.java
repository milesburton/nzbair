
package com.mb.android.nzbAirPremium.ui.listAdapters;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.mb.nzbair.providers.domain.UsenetPost;

public interface IPostListAdapter extends ListAdapter {

	public abstract void setModel(List<UsenetPost> model);

	public abstract List<UsenetPost> getModel();

	@Override
	public abstract int getCount();

	@Override
	public abstract Object getItem(int arg0);

	@Override
	public abstract long getItemId(int arg0);

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);

	public abstract void notifyDataSetChanged();
}