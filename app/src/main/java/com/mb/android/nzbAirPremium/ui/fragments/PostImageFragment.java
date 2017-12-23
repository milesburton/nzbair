
package com.mb.android.nzbAirPremium.ui.fragments;

import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mb.android.nzbAirPremium.R;
import com.mb.nzbair.providers.domain.UsenetPost;
import com.mb.nzbair.remote.HttpGetter;
import com.mb.nzbair.remote.HttpRequestCompleteCallback;
import com.mb.nzbair.remote.ThreadedDownloadTask;
import com.mb.nzbair.remote.converters.ImageResponseConverter;
import com.mb.nzbair.remote.domain.HttpRequestComplete;
import com.mb.nzbair.remote.domain.RequestFor;
import com.mb.nzbair.remote.domain.WithCallback;

public class PostImageFragment extends Fragment implements FragmentMetadata, HttpRequestCompleteCallback {

	private static final String TAG = PostImageFragment.class.getName();

	private ThreadedDownloadTask downloader;

	protected Handler guiThread;

	private static String EXTRA_IMAGE = "images";

	private UsenetPost post = null;

	public static Fragment getInstance(UsenetPost payload) {
		final PostImageFragment fragment = new PostImageFragment();
		fragment.post = payload;
		return fragment;
	}

	public static boolean supports(UsenetPost post) {
		return (post.getImages() != null && post.getImages().size() > 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Create view
		final View v = inflater.inflate(R.layout.fragment_post_image, container, false);

		// Prepare threads
		guiThread = new Handler();

		downloader = new ThreadedDownloadTask(Executors.newSingleThreadExecutor());

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (post != null) {

			final RequestFor<Bitmap> r = new RequestFor<Bitmap>(post.getImages().get(0), new ImageResponseConverter());
			final WithCallback c = new WithCallback(this, ((Integer) R.id.image_post).toString());
			final HttpGetter<Bitmap> g = new HttpGetter<Bitmap>(r, c);
			downloader.request(g);

		} else {
			onRestoreInstanceState(savedInstanceState);
		}
	}

	@Override
	public String getTitle() {

		return "Images";
	}

	Bitmap map = null;

	private void loadBitmap(final int id, final Bitmap bm) {
		try {
			final ImageView image = (ImageView) getActivity().findViewById(id);
			image.setImageBitmap(bm);
		} catch (final Exception ex) {
			Log.e(TAG, "Failed to load image");
			ex.printStackTrace();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		try {
			if (this.map != null) {
				outState.putParcelable(EXTRA_IMAGE, this.map);
			}

		} catch (final Exception ex) {
			Log.e(TAG, ex.toString());
			ex.printStackTrace();
		}
		super.onSaveInstanceState(outState);
	}

	public void onRestoreInstanceState(Bundle inState) {
		if (inState == null) {
			return;
		}

		if (inState.containsKey(EXTRA_IMAGE)) {
			map = inState.getParcelable(EXTRA_IMAGE);
			loadBitmap(R.id.image_post, map);
		}
	}

	@Override
	public void downloadComplete(final HttpRequestComplete r) {
		guiThread.post(new Runnable() {

			@Override
			public void run() {
				if (r.getResponse() == null) {
					Log.w(TAG, "Image was null. Giving up");
					return;
				}

				map = (Bitmap) r.getResponse();
				loadBitmap(Integer.parseInt(r.getRequestId()), map);
			}
		});
	}

}
