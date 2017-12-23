
package com.mb.android.nzbAirPremium.ui.helper;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.mb.nzbair.providers.domain.UsenetPost;

/**
 * controls default GUI responses from bookmark request provider
 * 
 * @author miles
 * 
 */
public class BookmarkHelper {

	public static void addBookmark(Handler guiThread, final Context context, final UsenetPost post) {
		guiThread.post(new Runnable() {

			@Override
			public void run() {
				// When clicked, show a toast with the TextView text
				Toast.makeText(context, "Deleting " + post.getTitle(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	public static void removeBookmark(Handler guiThread, final Context context, final UsenetPost post) {
		guiThread.post(new Runnable() {

			@Override
			public void run() {
				// When clicked, show a toast with the TextView text
				Toast.makeText(context, "Deleting " + post.getTitle(), Toast.LENGTH_SHORT).show();
			}
		});
	}
}
