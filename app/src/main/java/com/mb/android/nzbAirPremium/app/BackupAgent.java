
package com.mb.android.nzbAirPremium.app;

import android.app.backup.BackupAgentHelper;
import android.app.backup.SharedPreferencesBackupHelper;

public class BackupAgent extends BackupAgentHelper {

	static final String PREFS_BACKUP_KEY = "prefs";

	@Override
	public void onCreate() {

		final SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, Air.get().getDefaultSharedPreferencesName());
		addHelper(PREFS_BACKUP_KEY, helper);
	}
}