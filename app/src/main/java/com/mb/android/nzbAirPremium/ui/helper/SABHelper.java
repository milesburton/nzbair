
package com.mb.android.nzbAirPremium.ui.helper;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.mb.android.nzbAirPremium.app.Air;
import com.mb.android.nzbAirPremium.preferences.domain.GeneralConfig;
import com.mb.android.nzbAirPremium.ui.DownloadsActivity;
import com.mb.android.nzbAirPremium.ui.fragments.SabDownloadsFragment;
import com.mb.nzbair.providers.domain.UsenetPost;
import com.mb.nzbair.sabnzb.domain.HistoricalQueue;
import com.mb.nzbair.sabnzb.domain.Queue;
import com.mb.nzbair.sabnzb.domain.Slot;
import com.mb.nzbair.sabnzb.service.Sab;
import com.mb.nzbair.sabnzb.service.SabProxy;
import com.mb.nzbair.sabnzb.service.SabRequestCallback;

public class SABHelper implements SabRequestCallback {

	private Context context = null;
	private Handler guiThread = null;
	private final GeneralConfig generalConfig = Air.get().getGeneralConfig();
	// PREMIUM_START
	private final Sab sabProxy = SabProxy.getProxy();

	// PREMIUM_END

	public SABHelper(Context context, Handler guiThread) {
		this.context = context;
		this.guiThread = guiThread;
	}

	private void toast(String toastText) {
		// When clicked, show a toast with the TextView text
		Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
	}

	private void warnIfNotPremium() {
		if (!Air.isPremium()) {
			toast("Download NZBAir Premium now for SABNzbd support");
		}
	}

	public void addByUrlRequest(final UsenetPost model) {

		warnIfNotPremium();

		// PREMIUM_START
		// The anonymous class is used to capture the callback from SABNzb and
		// allow us to pass the model so we are able to add a post with the
		// resultant category

		// NB capture exception
		final SabRequestCallback callback = new SabRequestCallback() {

			@Override
			public void onResponseCategoryList(final List<String> categories, final Throwable error) {
				sabProxy.removeListener(this); // Remove temporary anonymous
				// class as listener
				// Add the SABHelper as a callback
				sabProxy.addListener(SABHelper.this);

				guiThread.post(new Runnable() {

					@Override
					public void run() {
						try {
							if (categories != null && categories.size() > 0) {
								final String[] tCats = new String[categories.size()];
								categories.toArray(tCats);
								final String[] cats = tCats;
								final AlertDialog.Builder builder = new AlertDialog.Builder(context);
								builder.setTitle("Category:");
								builder.setItems(cats, new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int item) {

										String cat = "";
										if (item < 0 || item > cats.length) {
											// If the item is invalid,
											// run a
											// best guess
											cat = model.getCategoryText();
										} else {
											cat = cats[item];
										}

										// Request the add
										if (model != null && model.getNzbDownloadUrl() != null) {
											sabProxy.requestAddByUrl(model.getNzbDownloadUrl(), cat);
										}
									}
								});

								final AlertDialog alert = builder.create();
								alert.show();
							} else {
								// Bypass category selection
								if (model != null && model.getNzbDownloadUrl() != null && !"".equals(model.getCategoryText())) {
									sabProxy.requestAddByUrl(model.getNzbDownloadUrl(), model.getCategoryText());
								}
							}

						} catch (final Exception ex) {
						}
					}
				});

			}

			@Override
			public void onResponseAddByUrl(Boolean ok, Throwable error) {

			}

			@Override
			public void onResponseAutoShutdownOff(Boolean ok, Throwable error) {

			}

			@Override
			public void onResponseAutoShutdownOn(Boolean ok, Throwable error) {

			}

			@Override
			public void onResponseDeleteHistoryJob(Boolean ok, Throwable error) {

			}

			@Override
			public void onResponseDeleteJobs(List<Slot> jobs, Boolean ok, Throwable error) {

			}

			@Override
			public void onResponseHistory(HistoricalQueue hQueue, Throwable error) {

			}

			@Override
			public void onResponseMoveJob(Slot from, Boolean ok, Throwable error) {

			}

			@Override
			public void onResponsePause(Boolean ok, Throwable error) {

			}

			@Override
			public void onResponsePauseJob(Slot job, Boolean ok, Throwable error) {

			}

			@Override
			public void onResponseQueueStatus(Queue queue, Throwable error) {

			}

			@Override
			public void onResponseRenameJob(Slot job, Boolean ok, Throwable error) {

			}

			@Override
			public void onResponseResume(Boolean ok, Throwable error) {

			}

			@Override
			public void onResponseResumeJob(Slot job, Boolean ok, Throwable error) {

			}

			@Override
			public void onResponseRetryJob(Boolean ok, Throwable error) {

			}

			@Override
			public void onResponseScriptList(Throwable error) {

			}

			@Override
			public void onResponseSetEmptyQueueAction(String action, Throwable error) {

			}

			@Override
			public void onResponseSetSpeedLimit(Boolean ok, Throwable error) {

			}

			@Override
			public void onResponseShutdown(Boolean ok, Throwable error) {

			}

			@Override
			public void onResponseSwapJob(Slot from, Boolean ok, Throwable error) {

			}

			@Override
			public void onResponseVersion(String version, Throwable error) {

			}

			@Override
			public void onResponseWarningsList(Throwable error) {

			}

			@Override
			public void onResponsePriority(Slot job, Boolean ok, Throwable error) {

			}

			@Override
			public void onPollingPaused(String reason) {
			}

			@Override
			public void onFileUploaded(Boolean ok, Throwable error) {

			}

		};

		if (!sabProxy.hasValidSetup()) {
			final String toastText = "Please check your SAB configuration";
			Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
		} else {
			sabProxy.addListener(callback);
			sabProxy.requestCategoryList();
		}

		// PREMIUM_END
	}

	@Override
	public void onResponseAddByUrl(final Boolean ok, final Throwable error) {

		warnIfNotPremium();
		// PREMIUM_START

		if (!sabProxy.hasValidSetup()) {
			toast("Please check your SAB configuration");
			return;
		}

		sabProxy.removeListener(this);
		guiThread.post(new Runnable() {

			@Override
			public void run() {
				String toastText;

				if (error == null && ok) {
					toastText = "Downloaded to SABnzb";
				} else {
					toastText = "Failed to add download. " + (error != null ? error.getMessage() : "");
				}

				// When clicked, show a toast with the TextView text
				toast(toastText);
				if (generalConfig != null && generalConfig.shouldShowSabDownloadActivity()) {
					context.startActivity(new Intent(context, DownloadsActivity.class).putExtra(DownloadsActivity.STARTUP_CLASS, SabDownloadsFragment.class.getCanonicalName()));
				}
			}
		});

		// PREMIUM_END

	}

	@Override
	public void onResponseAutoShutdownOff(Boolean ok, Throwable error) {

	}

	@Override
	public void onResponseAutoShutdownOn(Boolean ok, Throwable error) {

	}

	@Override
	public void onResponseCategoryList(List<String> categories, Throwable error) {

	}

	@Override
	public void onResponseDeleteHistoryJob(Boolean ok, Throwable error) {

	}

	@Override
	public void onResponseDeleteJobs(List<Slot> jobs, Boolean ok, Throwable error) {

	}

	@Override
	public void onResponseHistory(HistoricalQueue hQueue, Throwable error) {

	}

	@Override
	public void onResponseMoveJob(Slot from, Boolean ok, Throwable error) {

	}

	@Override
	public void onResponsePause(Boolean ok, Throwable error) {

	}

	@Override
	public void onResponsePauseJob(Slot job, Boolean ok, Throwable error) {

	}

	@Override
	public void onResponseQueueStatus(Queue queue, Throwable error) {

	}

	@Override
	public void onResponseRenameJob(Slot job, Boolean ok, Throwable error) {

	}

	@Override
	public void onResponseResume(Boolean ok, Throwable error) {

	}

	@Override
	public void onResponseResumeJob(Slot job, Boolean ok, Throwable error) {

	}

	@Override
	public void onResponseRetryJob(Boolean ok, Throwable error) {

	}

	@Override
	public void onResponseScriptList(Throwable error) {

	}

	@Override
	public void onResponseSetEmptyQueueAction(String action, Throwable error) {

	}

	@Override
	public void onResponseSetSpeedLimit(Boolean ok, Throwable error) {

	}

	@Override
	public void onResponseShutdown(Boolean ok, Throwable error) {

	}

	@Override
	public void onResponseSwapJob(Slot from, Boolean ok, Throwable error) {

	}

	@Override
	public void onResponseVersion(String version, Throwable error) {

	}

	@Override
	public void onResponseWarningsList(Throwable error) {

	}

	@Override
	public void onResponsePriority(Slot job, Boolean ok, Throwable error) {

	}

	@Override
	public void onPollingPaused(String reason) {

	}

	@Override
	public void onFileUploaded(Boolean ok, Throwable error) {

	}

}
