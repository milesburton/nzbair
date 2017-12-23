
package com.mb.android.nzbAirPremium.ui.fragments;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.download.AirDownloadMetadata;
import com.mb.android.nzbAirPremium.download.IDownloadManagerCallback;
import com.mb.android.nzbAirPremium.download.IDownloadManagerService;
import com.mb.android.nzbAirPremium.ui.helper.ContextHelper;
import com.mb.android.nzbAirPremium.ui.listAdapters.DownloadListAdapter;
import com.mb.android.ui.listeners.OnCustomClickListener;
import com.mb.nzbair.remote.domain.DownloadMetadata;

public class LocalDownloadFragment extends SherlockFragment implements OnCreateContextMenuListener, OnCustomClickListener<AirDownloadMetadata>, FragmentMetadata {

	private static final String TAG = LocalDownloadFragment.class.getName();

	private AirDownloadMetadata selectedItem;
	protected Handler guiThread;
	private DownloadListAdapter listAdapter;

	private final Intent svc = new Intent(IDownloadManagerService.class.getName());

	public static LocalDownloadFragment getInstance(Activity mActivity) {
		final LocalDownloadFragment frag = new LocalDownloadFragment();
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		guiThread = new Handler();
		final View v = inflater.inflate(R.layout.layout_list_footer, container, false);
		listAdapter = new DownloadListAdapter(getActivity(), this, this);
		final ListView lv = (ListView) v.findViewById(android.R.id.list);
		lv.setAdapter(listAdapter);
		return v;
	}

	private void setFooterText(String text) {
		final View v = ContextHelper.getView(TAG, this);
		if (v == null) {
			return;
		}
		final TextView footer = (TextView) v.findViewById(R.id.footer);
		footer.setText(text);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

		this.getActivity().getMenuInflater().inflate(R.menu.local_downloads_context_menu, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		
		if (!getUserVisibleHint()) {
			return false;
		}
		
		switch (item.getItemId()) {
		case R.id.action_retry:

			try {
				listAdapter.remove(selectedItem);
				service.download(selectedItem);
			} catch (final RemoteException e) {
				e.printStackTrace();
			}
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private final IDownloadManagerCallback.Stub callback = new IDownloadManagerCallback.Stub() {

		@Override
		public void onDownloadComplete(String uuid) throws RemoteException {
			LocalDownloadFragment.this.updateModel(uuid);
		}

		@Override
		public void onDownloadUpdate(String uuid) throws RemoteException {
			LocalDownloadFragment.this.updateModel(uuid);
		}

		@Override
		public void onDownloadAdded(String uuid) throws RemoteException {
			final AirDownloadMetadata data = service.getDownloadMetadata(uuid);
			if (data == null) {
				return;
			}

			Log.i(LocalDownloadFragment.TAG, "onDownloadAdded");

			listAdapter.add(data);
			listAdapter.notifyDataSetChanged();
		}

		@Override
		public void onDownloadRemoved(String uuid) throws RemoteException {

			listAdapter.remove(listAdapter.getItem(uuid));
			listAdapter.notifyDataSetChanged();

			guiThread.post(new Runnable() {

				@Override
				public void run() {
					updateFooter();
				}
			});
		}
	};

	/**
	 * Update progress of a single item on the listadapter
	 */
	void updateModel(String uuid) {
		try {
			final DownloadMetadata data = service.getDownloadMetadata(uuid);

			if (data == null) {
				return;
			}

			guiThread.post(new Runnable() {

				@Override
				public void run() {
					Log.i(LocalDownloadFragment.TAG, "Activity callback, got update");

					final DownloadMetadata anItem = listAdapter.getItem(data.getUuid());
					if (anItem == null) {
						return;
					}
					anItem.setProgress(data.getProgress());
					anItem.setFilesize(data.getFilesize());
					anItem.setStatus(data.getStatus());
					listAdapter.notifyDataSetChanged();
					updateFooter();
				}
			});

		} catch (final RemoteException e) {
			e.printStackTrace();
		}
	}

	private void updateFooter() {
		List<AirDownloadMetadata> downloads;
		try {
			downloads = service.getDownloads();
			setFooterText("Downloads: " + downloads.size());
		} catch (final RemoteException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.add(android.view.Menu.NONE, R.id.action_clear_complete, android.view.Menu.NONE, "Clear Complete").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_clear_complete:
			try {
				service.clearCompleted();
				return true;
			} catch (final RemoteException e) {
				Log.e(TAG, e.toString());
			}
		}
		return false;
	}

	@Override
	public void onPause() {
		super.onPause();

		if (service != null) {
			try {
				service.removeListener(callback);
			} catch (final RemoteException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		if (service != null) {
			try {
				service.addListener(callback);
			} catch (final RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		final Activity a = ContextHelper.getActivity(TAG, this);
		if (a == null) {
			return;
		}
		a.startService(svc);
		a.bindService(svc, svcConn, Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onStop() {
		super.onStop();
		final Activity a = ContextHelper.getActivity(TAG, this);
		if (a == null) {
			return;
		}
		a.unbindService(svcConn);
		a.stopService(svc);
	}

	@Override
	public void OnTouch(View aView, int position, AirDownloadMetadata item) {
		this.selectedItem = item;
	}

	@Override
	public void OnClick(View aView, int position, AirDownloadMetadata item) {
		try {
			service.removeDownload(item);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void OnLongClick(View aView, int position, AirDownloadMetadata payload) {

	}

	private IDownloadManagerService service = null;
	private final ServiceConnection svcConn = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder binder) {
			service = IDownloadManagerService.Stub.asInterface(binder);
			Log.i(TAG, "Service connected");

			try {
				service.addListener(callback);
				listAdapter.setModel(service.getDownloads());
				updateFooter();
			} catch (final RemoteException e) {
				e.printStackTrace();
				setFooterText("Failed");
			}

		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			service = null;
			Log.i(TAG, "Service disconnected");
			setFooterText("Service disconnected");
		}
	};

	@Override
	public String getTitle() {
		return "Local Downloads";
	}

}
