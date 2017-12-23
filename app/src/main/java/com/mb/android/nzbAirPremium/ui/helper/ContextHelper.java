
package com.mb.android.nzbAirPremium.ui.helper;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

public class ContextHelper {

	public static Context getContext(String tag, Activity a) {
		if (a == null) {
			Log.w(tag, "Activity is null ignoring action");
			return null;
		}

		final Context con = a.getApplicationContext();
		if (con == null) {
			Log.w(tag, "Context is null ignoring action");
		}

		return con;
	}

	public static Activity getActivity(String tag, Fragment con) {
		if (con == null || con.getActivity() == null) {
			Log.w(tag, "Activity is null ignoring action");
		}

		return con.getActivity();
	}

	public static View getView(String TAG, Fragment frag) {

		if (frag == null || frag.getView() == null) {
			Log.w(TAG, "View is null ignoring action");
			return null;
		}

		return frag.getView();
	}
}