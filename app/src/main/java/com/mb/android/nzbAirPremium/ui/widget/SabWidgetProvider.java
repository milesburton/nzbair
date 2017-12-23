
package com.mb.android.nzbAirPremium.ui.widget;

//PREMIUM_START
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SabWidgetProvider extends AppWidgetProvider {

	public static String TAG = SabWidgetProvider.class.getCanonicalName();

	private static final int REFRESH_SEC_ON_POWER = 30;
	private static final int REFRESH_SEC_ON_BATTERY = 180;
	private static final int REFRESH_SEC_ON_UNKNOWN = 180;
	private static final int REFRESH_SEC_ON_BATTERYLOW = 0;

	@Override
	public void onReceive(Context context, Intent intent) {

		super.onReceive(context, intent);

		try {
			handleDisableIntent(context, intent);
			handleEnableIntent(context, intent);
			handlePowerStatusChangedIntent(context, intent);
			handleUpdateIntent(context, intent);
		} catch (final Exception ex) {
			Log.e(TAG, "Failed to onReceive: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	private void handleEnableIntent(Context context, Intent intent) {

		if (!intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_ENABLED)) {
			return;
		}

		try {
			new SabAlarmManager().setRepeating(context, REFRESH_SEC_ON_UNKNOWN);
		} catch (final Exception ex) {
			Log.e(TAG, "Failed to onEnabled: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	private void handleDisableIntent(Context context, Intent intent) {

		if (!intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_DISABLED)) {
			return;
		}

		try {
			new SabAlarmManager().removeAlarm(context);
		} catch (final Exception ex) {
			Log.e(TAG, "Failed to onDisable: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	private void handleUpdateIntent(Context context, Intent intent) {
		if (isWidgetUpdateRequest(intent)) {
			new SabServiceController().update(context, getWidgetIds(context));
		}
	}

	private void handlePowerStatusChangedIntent(Context context, Intent intent) {

		if (isLowPower(intent)) {
			new SabAlarmManager().setRepeating(context, REFRESH_SEC_ON_BATTERYLOW);
			new SabServiceController().setLowPowerView();
		} else if (isPowerConnected(intent)) {
			new SabAlarmManager().setRepeating(context, REFRESH_SEC_ON_POWER);
		} else if (isRunningOnBatteries(intent)) {
			new SabAlarmManager().setRepeating(context, REFRESH_SEC_ON_BATTERY);
		}
	}

	private int[] getWidgetIds(Context context) {

		final ComponentName thisAppWidget = new ComponentName(context.getPackageName(), SabWidgetProvider.class.getName());
		final int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(thisAppWidget);

		return appWidgetIds;
	}

	private boolean isWidgetUpdateRequest(Intent intent) {

		return AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction());
	}

	private boolean isRunningOnBatteries(Intent intent) {

		return (Intent.ACTION_POWER_DISCONNECTED.equals(intent.getAction()) || Intent.ACTION_BATTERY_OKAY.equals(intent.getAction()));
	}

	private boolean isPowerConnected(Intent intent) {

		return Intent.ACTION_POWER_CONNECTED.equals(intent.getAction());
	}

	private boolean isLowPower(Intent intent) {
		return Intent.ACTION_BATTERY_LOW.equals(intent.getAction());
	}

}
// PREMIUM_END