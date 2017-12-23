
package com.mb.android.nzbAirPremium.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.view.Menu;
import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.app.Air;
import com.mb.android.nzbAirPremium.ui.preferences.AirPreferenceLauncherActivity;
import com.mb.android.nzbAirPremium.ui.preferences.OneTimeSetupActivity;

public class MainMenuActivity extends BaseFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		showFirstRunDialog();
	}

	private void showFirstRunDialog() {

		if (!Air.get().isFirstRun()) {
			return;
		}

		final String title = "Welcome to NZBAir";
		final String message = "This appears to be your first run would you like to configure NZBAir now?";
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message).setTitle(title).setCancelable(true);

		builder.setPositiveButton("One Time Setup", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				final Intent settingsIntent = new Intent(MainMenuActivity.this, OneTimeSetupActivity.class);
				MainMenuActivity.this.startActivity(settingsIntent);
			}
		});

		builder.setNegativeButton("Advanced", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				final Intent settingsIntent = new Intent(MainMenuActivity.this, AirPreferenceLauncherActivity.class);
				MainMenuActivity.this.startActivity(settingsIntent);
			}
		});

		builder.create().show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.core_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

}
