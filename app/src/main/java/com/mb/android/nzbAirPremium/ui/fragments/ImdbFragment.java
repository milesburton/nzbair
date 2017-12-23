
package com.mb.android.nzbAirPremium.ui.fragments;

import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.imdb.IImdbCallbackProvider;
import com.mb.android.nzbAirPremium.imdb.IImdbProvider;
import com.mb.android.nzbAirPremium.imdb.IMDBProvider;
import com.mb.android.nzbAirPremium.imdb.Imdb;
import com.mb.android.nzbAirPremium.ui.helper.ContextHelper;
import com.mb.nzbair.providers.domain.UsenetPost;
import com.mb.nzbair.remote.HttpGetter;
import com.mb.nzbair.remote.HttpRequestCompleteCallback;
import com.mb.nzbair.remote.ThreadedDownloadTask;
import com.mb.nzbair.remote.converters.ImageResponseConverter;
import com.mb.nzbair.remote.domain.HttpRequestComplete;
import com.mb.nzbair.remote.domain.RequestFor;
import com.mb.nzbair.remote.domain.WithCallback;

public class ImdbFragment extends Fragment implements IImdbCallbackProvider, HttpRequestCompleteCallback, FragmentMetadata {

	private final String TAG = ImdbFragment.class.getName();

	public static final String S_IMDB_MODEL = "imdbmodel";
	public static final String S_POST_MODEL = "post";

	public static final String KEY_IMDB_ID = "imdb";

	private Imdb model = null;
	private IImdbProvider imdbProvider;
	private ThreadedDownloadTask downloader;

	protected Handler guiThread;
	UsenetPost post;

	public static Fragment getInstance(UsenetPost payload) {
		final ImdbFragment fragment = new ImdbFragment();
		fragment.post = payload;
		return fragment;
	}

	public static boolean supports(UsenetPost post) {
		return (post.getMetadata() != null && post.getMetadata().containsKey(KEY_IMDB_ID));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		final View v = inflater.inflate(R.layout.layout_imdb_details, container, false);
		// Prepare threads
		guiThread = new Handler();

		imdbProvider = new IMDBProvider();
		imdbProvider.addListener(this);

		downloader = new ThreadedDownloadTask(Executors.newSingleThreadExecutor());

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (resultsAreCached(savedInstanceState)) {
			this.model = (Imdb) savedInstanceState.getSerializable(S_IMDB_MODEL);
			this.post = (UsenetPost) savedInstanceState.getSerializable(S_POST_MODEL);
			responseImdbPost(this.model, null);
		} else {
			if (post.getMetadata().containsKey(KEY_IMDB_ID)) {
				imdbProvider.requestImdbPost(post.getMetadata().get(KEY_IMDB_ID));
				// Set loading
			}
		}
	}

	private void updateDetailsText(Imdb model) {
		try {
			if (NotNull(R.id.name, model.getTitle())) {
				setText(R.id.name, model.getTitle());
			}

			if (NotNull(R.id.yearRow, model.getYear())) {
				setText(R.id.year, model.getYear());
			}

			if (NotNull(R.id.DirectorRow, model.getDirector())) {
				setText(R.id.Director, model.getDirector());
			}

			if (NotNull(R.id.WriterRow, model.getWriter())) {
				setText(R.id.Writer, model.getWriter());
			}

			if (NotNull(R.id.ActorsRow, model.getActors())) {
				setText(R.id.Actors, model.getActors());
			}

			if (NotNull(R.id.PlotRow, model.getPlot())) {
				setText(R.id.Plot, model.getPlot());
			}

			if (NotNull(R.id.RuntimeRow, model.getRuntime())) {
				setText(R.id.Runtime, model.getRuntime());
			}

			if (NotNull(R.id.RatingRow, model.getRating())) {
				setText(R.id.Rating, model.getRating());
			}

			if (NotNull(R.id.VotesRow, model.getVotes())) {
				setText(R.id.Votes, model.getVotes());
			}

			if (NotNull(R.id.RatedRow, model.getYear())) {
				setText(R.id.Rated, model.getYear());
			}

			final TextView aUrlView = ((TextView) getView().findViewById(R.id.Link));
			aUrlView.setText(Html.fromHtml(model.getUrl()));
			aUrlView.setAutoLinkMask(Linkify.WEB_URLS);
			aUrlView.setLinksClickable(true);

			final View v = ContextHelper.getView(TAG, this);
			if (v != null) {
				((TextView) v.findViewById(R.id.Link)).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						final Pattern p = Pattern.compile(".*href=\"(.*?)\".*");
						final String subject = ImdbFragment.this.model.getUrl();
						final Matcher matcher = p.matcher(subject);
						if (matcher.matches()) {
							final Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(matcher.group(1))); // ImdbFragment.this.model.getUrl()

							startActivity(myIntent);
						}
					}
				});
			}
		} catch (final Exception ex) {
			Log.e(TAG, "Failed to write IMDB text");
			ex.printStackTrace();
		}
	}

	private void setText(int viewId, String value) {
		final View v = ContextHelper.getView(TAG, this);
		if (v == null) {
			return;
		}

		((TextView) v.findViewById(viewId)).setText(value);
	}

	private boolean NotNull(int viewId, String data) {
		if (data == null || data.equals("")) {
			final View tView = getView().findViewById(viewId);
			((ViewGroup) tView.getParent()).removeView(tView);
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void responseImdbPost(final Imdb post, final Throwable error) {
		guiThread.post(new Runnable() {

			@Override
			public void run() {
				final Context c = ContextHelper.getContext(TAG, ImdbFragment.this.getActivity());
				if (post != null && error == null) {
					ImdbFragment.this.model = post;
					updateDetailsText(post);

					if (post.getImage() != null && !post.getImage().equals("")) {

						final RequestFor<Bitmap> r = new RequestFor<Bitmap>(post.getImage(), new ImageResponseConverter());
						final WithCallback wc = new WithCallback(ImdbFragment.this, ((Integer) R.id.image).toString());
						final HttpGetter<Bitmap> g = new HttpGetter<Bitmap>(r, wc);
						downloader.request(g);
					}
				} else {
					if (c != null) {
						Toast.makeText(c, "IMDB details not available", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

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

				final View v = ContextHelper.getView(TAG, ImdbFragment.this);
				if (v == null) {
					return;
				}

				final Bitmap bm = (Bitmap) r.getResponse();
				final ImageView image = (ImageView) v.findViewById(Integer.parseInt(r.getRequestId()));
				image.setImageBitmap(bm);
			}
		});
	}

	protected boolean resultsAreCached(Bundle inState) {
		return !(inState == null || inState.getSerializable(S_IMDB_MODEL) == null || inState.getSerializable(S_POST_MODEL) == null);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		try {
			if (model != null) {
				outState.putSerializable(S_IMDB_MODEL, model);
			}
			if (post != null) {
				outState.putSerializable(S_POST_MODEL, post);
			}
		} catch (final Exception ex) {
			Log.e(TAG, ex.toString());
			ex.printStackTrace();
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	public String getTitle() {

		return "IMDB";
	}

}
