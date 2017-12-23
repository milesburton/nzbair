
package com.mb.android.nzbAirPremium.ui.widget;

//PREMIUM_START
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;

public class SabAlarmManager {

	public void setRepeating(Context appContext, int refreshInterval) {

		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, 10);
		final AlarmManager alarmManager = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);
		alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), refreshInterval * 1000, withIntent(appContext));

	}

	public void removeAlarm(Context appContext) {
		final AlarmManager alarmManager = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(withIntent(appContext));
	}

	private PendingIntent withIntent(Context con) {

		return PendingIntent.getBroadcast(con, 0, new Intent().setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE), PendingIntent.FLAG_UPDATE_CURRENT);
	}
}
// PREMIUM_END