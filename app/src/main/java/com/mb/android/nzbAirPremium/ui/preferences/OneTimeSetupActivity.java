
package com.mb.android.nzbAirPremium.ui.preferences;

import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.setup.ISetupCallback;
import com.mb.android.nzbAirPremium.setup.SetupService;
import com.mb.android.nzbAirPremium.ui.helper.MenuHelper;

public class OneTimeSetupActivity extends SherlockFragmentActivity implements ISetupCallback, OnClickListener {

	private final String TAG = OneTimeSetupActivity.class.getName();
	private ProgressDialog loadingDialog;

	private SetupService service;
	private Handler guiThread;
	private String code;

	@Override
	public void onStart() {
		super.onStart();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		MenuHelper.onOptionsItemSelected(this, this, item);
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.layout_one_time_setup);

		guiThread = new Handler();
		try {
			findViewById(R.id.getSettings).setOnClickListener(this);

			if (service == null) {
				service = new SetupService(getApplicationContext());
			}

			if (!resultsAreCached(icicle)) {
				attachListeners();
				loadingDialog = ProgressDialog.show(this, "Fetching OTS Token", " Loading. Please wait ... ", true);
				service.requestToken();
			}

		} catch (final Exception ex) {
			ex.printStackTrace();
			loadingDialog.cancel();
			Log.e(TAG, ex.toString());
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		attachListeners();

	}

	private boolean resultsAreCached(Bundle icicle) {
		if (icicle == null) {
			return false;
		}
		return icicle.getString(EXTRA_SETUPCODE) != null;
	}

	private void setSetupToken(String _code) {
		code = _code;
		setText(R.id.code, code);
	}

	private void retryCode(String message) {
		final Context mContext = this;
		final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setMessage(message).setTitle("Error").setCancelable(true).setPositiveButton("Retry", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				service.requestToken();
			}
		}).setNegativeButton("Exit", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				finish();
			}
		});
		builder.create().show();
	}

	private void setText(int viewId, String value) {
		((TextView) findViewById(viewId)).setText(value);
	}

	private void updateSettings(final Map<String, String> configuration) {

		try {
			final Editor edit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

			for (final Map.Entry<String, String> entry : configuration.entrySet()) {
				if (entry.getValue().equals("true") || entry.getValue().equals("false")) {
					edit.putBoolean(entry.getKey(), Boolean.parseBoolean(entry.getValue()));
				} else {
					edit.putString(entry.getKey(), entry.getValue());
				}
			}

			if (edit.commit()) {
				Toast.makeText(getApplicationContext(), "Setting retrieved from server", Toast.LENGTH_SHORT).show();
				finish();
			} else {
				Toast.makeText(getApplicationContext(), "Error occured setting configuration", Toast.LENGTH_SHORT).show();
			}

		} catch (final Exception ex) {
			ex.printStackTrace();
			Log.e(TAG, ex.toString());
		}
	}

	@Override
	public void onClick(View buttonView) {

		final EditText editView = (EditText) findViewById(R.id.pin);
		if (code == null || code.length() == 0) {
			Toast.makeText(getApplicationContext(), "It looks like the token code hasn't loaded. Please request a token first.", Toast.LENGTH_SHORT).show();
			return;
		}

		if (editView.getText().length() == 0) {
			Toast.makeText(getApplicationContext(), "Please enter the pin code which you entered on the web interface", Toast.LENGTH_SHORT).show();
			return;
		}

		buttonView.setEnabled(false);
		service.requestSetupData(code, editView.getText().toString());

	}

	public static final String EXTRA_SETUPCODE = "setupCode";

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		try {
			if (this.code != null) {
				outState.putString(EXTRA_SETUPCODE, this.code);
			}
		} catch (final Exception ex) {
			Log.e(TAG, ex.toString());
			ex.printStackTrace();
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle inState) {
		if (resultsAreCached(inState)) {
			try {
				final String result = inState.getString(EXTRA_SETUPCODE);
				if (result != null && result != "") {
					setSetupToken(result);
				}

			} catch (final Exception ex) {
				ex.printStackTrace();
			}

		}
		super.onRestoreInstanceState(inState);
	}

	@Override
	public void onPause() {
		detachListeners();
		super.onPause();
	}

	protected void detachListeners() {
		if (service != null) {
			service.removeCallback(this);
		}
	}

	protected void attachListeners() {
		if (service != null) {
			service.addCallback(this);
		}
	}

	@Override
	public void responseToken(final String token, final Throwable error) {
		guiThread.post(new Runnable() {

			@Override
			public void run() {
				try {
					loadingDialog.cancel();
				} catch (final Exception ex) {
				}
				if (token == null) {
					retryCode("Please check your internet connetion");
				} else {
					setSetupToken(token);
				}
			}
		});
	}

	@Override
	public void responseConfiguration(final Map<String, String> configuration, final Throwable error) {
		guiThread.post(new Runnable() {

			@Override
			public void run() {
				findViewById(R.id.getSettings).setEnabled(true);

				if (error == null) {
					if ("failed".equals(configuration.get("result"))) {
						Toast.makeText(getApplicationContext(), "Could not fetch configuration. " + configuration.get("meesage"), Toast.LENGTH_SHORT).show();
					} else {
						updateSettings(configuration);
					}
				} else {
					Toast.makeText(getApplicationContext(), "Could not fetch configuration. Check your internet connection and OTS Pin", Toast.LENGTH_SHORT).show();
				}

			}
		});
	}
}
