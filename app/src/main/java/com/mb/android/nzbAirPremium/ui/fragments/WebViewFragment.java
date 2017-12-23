
package com.mb.android.nzbAirPremium.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.ui.helper.ContextHelper;
import com.mb.nzbair.providers.domain.UsenetPost;

public class WebViewFragment extends Fragment implements FragmentMetadata {

	private static final String TAG = WebViewFragment.class.getName();

	protected String url = "";
	protected String title = "";

	WebView view = null;

	public enum PageType {
		NFO, COMMENTS, GENERIC

	}

	public static WebViewFragment getInstance(String url) {
		final WebViewFragment frag = new WebViewFragment();
		frag.url = url;
		frag.title = "Web";
		return frag;
	}

	public static WebViewFragment getInstance(UsenetPost post, PageType type) {
		final WebViewFragment frag = new WebViewFragment();

		switch (type) {
		case COMMENTS:
			frag.title = "Comments";
			if (post.getProvider().equals("nzbmatrix")) {
				frag.url = "https://nzbmatrix.com/nzb-details.php?id=" + post.getId() + "#startcomments";
			} else if (post.getProvider().equals("nzbsu")) {
				frag.url = "https://nzb.su/details/" + post.getId();
			} else if (post.getProvider().equals("newzbin")) {
				frag.url = "http://www.newzbin.com/browse/post/" + post.getId() + "/#CommentsPH";
			}
			break;
		case NFO:
			frag.title = "NFO";
			if (post.getProvider().equals("nzbmatrix")) {
				frag.url = "https://nzbmatrix.com/nzb-details.php?id=" + post.getId() + "&type=nfo";
			} else if (post.getProvider().equals("newzbin")) {
				frag.url = "http://www.newzbin.com/nfo/view/txt/" + post.getId();
			}
			break;
		}

		if ("".equals(frag.url)) {
			frag.url = "http://www.nzbair.com";
			frag.title = "WebView";
		}
		return frag;
	}

	public static boolean supports(UsenetPost post, PageType type) {
		if (post.getProvider() != null) {
			if (post.getProvider().equals("nzbmatrix") || post.getProvider().equals("newzbin")) {
				if (type == PageType.COMMENTS) {
					return true;
				} else if (type == PageType.NFO && post.getMetadata().containsKey("Has NFO")) {
					return true;
				}
			} else if (post.getProvider().equals("nzbsu")) {
				if (type == PageType.COMMENTS) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		// Setup view
		final View v = inflater.inflate(R.layout.fragment_web_view, container, false);
		view = (WebView) v.findViewById(R.id.webview);
		view.setWebViewClient(client);
		final WebSettings webSettings = view.getSettings();
		webSettings.setJavaScriptEnabled(true);
		view.loadUrl(url);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		final View v = ContextHelper.getView(TAG, this);
		if (v == null) {
			return;
		}

		view.saveState(outState);
		super.onSaveInstanceState(outState);
	}

	protected void onRestoreInstanceState(Bundle state) {
		final View v = ContextHelper.getView(TAG, this);
		if (v == null) {
			return;
		}

		view.restoreState(state);
	}

	@Override
	public String getTitle() {
		return title;
	}

	WebViewClient client = new WebViewClient() {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	};
}
