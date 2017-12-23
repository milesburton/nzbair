
package com.mb.android.nzbAirPremium.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mb.android.nzbAirPremium.R;
import com.mb.android.nzbAirPremium.app.Air;
import com.mb.android.nzbAirPremium.download.AirDownloadMetadata;
import com.mb.android.nzbAirPremium.download.IDownloadManagerService;
import com.mb.android.nzbAirPremium.ui.fragments.BrowseMetadata;
import com.mb.android.nzbAirPremium.ui.fragments.ImdbFragment;
import com.mb.android.nzbAirPremium.ui.fragments.LocalDownloadFragment;
import com.mb.android.nzbAirPremium.ui.fragments.PostDetailsFragment;
import com.mb.android.nzbAirPremium.ui.fragments.PostImageFragment;
import com.mb.android.nzbAirPremium.ui.fragments.Refreshable;
import com.mb.android.nzbAirPremium.ui.fragments.WebViewFragment;
import com.mb.android.nzbAirPremium.ui.fragments.adapters.PageAdapter;
import com.mb.android.nzbAirPremium.ui.helper.SABHelper;
import com.mb.android.nzbAirPremium.ui.helper.ThrowableHelper;
import com.mb.nzbair.providers.Provider;
import com.mb.nzbair.providers.ProviderFactory;
import com.mb.nzbair.providers.ProviderFactory.InvalidProviderException;
import com.mb.nzbair.providers.domain.UsenetPost;
import com.mb.nzbair.providers.domain.UsenetPostResult;
import com.mb.nzbair.providers.domain.category.Category;
import com.mb.nzbair.providers.interfaces.DetailsCallback;
import com.mb.nzbair.providers.interfaces.FavouritesCallback;
import com.mb.nzbair.seh.ProviderException;
import com.viewpagerindicator.TitlePageIndicator;

public class DetailsActivity extends BaseBrowse implements DetailsCallback, BrowseMetadata, Refreshable, OnClickListener, FavouritesCallback, OnPageChangeListener {

	final String TAG = DetailsActivity.class.getName();

	public static final String EXTRA_POST = "post";
	public static final String EXTRA_PROVIDER_ID = "provider";

	private static final String S_BOOKMARKED = "isBookmarked";
	private static final String S_FRAGMENT_POSITION = "fragmentPosition";

	private Handler guiThread;

	private UsenetPost model;
	private Boolean isBookmarked = false;
	private Provider provider;

	private PageAdapter mAdapter;
	private ViewPager mPager;

	private SABHelper sabHelper;
	private int selectedPage = 0;

	private TitlePageIndicator indicator;

	private final Intent svc = new Intent(IDownloadManagerService.class.getName());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);

		// Prepare threads
		guiThread = new Handler();
		sabHelper = new SABHelper(this, guiThread);

		/* Setup pager */
		mAdapter = new PageAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		/* Setup titles */
		indicator = (TitlePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(mPager);
		indicator.setOnPageChangeListener(this);

		try {
			populateStateFromIntent();

			// Attach default fragment
			fragment = PostDetailsFragment.getInstance(model);
			mAdapter.add(fragment);

			/* Request core post data */
			if (!resultsAreCached(savedInstanceState)) {
				// Fetch enriched
				provider.getDetailsService().requestPostDetails(model);
			} else {
				// If we've got data. Just restore from cache
				onRestoreInstanceState(savedInstanceState);
				addCapabilitiesFor(model);
			}

			indicator.setCurrentItem(selectedPage);
			mPager.setCurrentItem(selectedPage);
			attachBookmarkListener();
			attachButtonListeners();
		} catch (final Exception ex) {
			Log.e(TAG, "Failed to start UsenetPostActivity: ");
			toast("Cannot show post details - sorry");
			ex.printStackTrace();
			finish();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		startService(svc);
		bindService(svc, svcConn, Context.BIND_AUTO_CREATE);

		this.invalidateOptionsMenu();
	}

	@Override
	public void onStop() {
		super.onStop();
		unbindService(svcConn);
		stopService(svc);
	}

	private void populateStateFromIntent() {
		final Intent intent = getIntent();
		model = (UsenetPost) intent.getSerializableExtra(EXTRA_POST);
		try {
			provider = ProviderFactory.getInstance().getProvider(intent.getStringExtra(EXTRA_PROVIDER_ID));
		} catch (final InvalidProviderException e) {
			Log.e(TAG, "Failed to get provider");
		}

		getSupportActionBar().setTitle(model.getTitle());

		/* Request core post data */
		if (provider != null && model.getId() != null) {
			provider.getDetailsService().addListener(this);
		} else {
			toast("Post could not be found");
		}

	}

	private void attachBookmarkListener() {
		// Bookmarks: Add listener or remove button
		if (provider.getFavouritesService() != null) {
			provider.getFavouritesService().addListener(this);
		} else {
			final View tView = findViewById(R.id.postBookmarkCol);
			((ViewGroup) tView.getParent()).removeView(tView);
		}
	}

	private void attachButtonListeners() {

		if (findViewById(R.id.bookmarkButton) != null) {
			findViewById(R.id.bookmarkButton).setOnClickListener(this);
		}

		if (findViewById(R.id.downloadButton) != null) {
			findViewById(R.id.downloadButton).setOnClickListener(this);
		}

		if (findViewById(R.id.sabnzbButton) != null) {
			findViewById(R.id.sabnzbButton).setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.downloadButton:
			requestLocalDownload();
			break;
		case R.id.bookmarkButton:
			requestBookmark();
			break;
		case R.id.sabnzbButton:
			sabHelper.addByUrlRequest(model);
			break;
		}
	}

	private void requestBookmark() {
		String toastText = "";
		try {
			if (isBookmarked) {
				provider.getFavouritesService().requestRemoveFavourite(model);
				toastText = "Requesting Bookmark Removal...";
			} else {
				provider.getFavouritesService().requestAddFavourite(model);
				toastText = "Request Bookmark...";
			}
		} catch (final Exception ex) {
			Log.e(TAG, ex.toString());
			ex.printStackTrace();
			toastText = "Failed to request bookmark";
		}
		if (!"".equals(toastText)) { // When clicked, show a toast with the
			Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
		}
	}

	private void requestLocalDownload() {
		if (Air.get().getGeneralConfig() == null) {
			toast("Please configure your download location");
			return;
		}

		try {
			final AirDownloadMetadata dm = new AirDownloadMetadata(model.getNzbDownloadUrl(), Air.get().getGeneralConfig().getDownloadLocation(), model.getTitle());
			if (dm.getDownloadUrl().equalsIgnoreCase("")) {
				toast("Invalid download URL, that's odd");
			} else {
				service.download(dm);
				startActivity(new Intent(this, DownloadsActivity.class).putExtra(DownloadsActivity.STARTUP_CLASS, LocalDownloadFragment.class.getCanonicalName()));
				finish();
			}
		} catch (final Exception ex) {
			Log.e(TAG, ex.toString());
			ex.printStackTrace();
			toast("Failed to download");
		}
	}

	private void toast(String text) {
		if (!"".equals(text)) {
			Toast.makeText(DetailsActivity.this, text, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void responseDetails(final UsenetPost post, final Throwable error) {
		guiThread.post(new Runnable() {

			@Override
			public void run() {
				if (error != null) {
					ThrowableHelper.showError(DetailsActivity.this, error);
				} else {
					DetailsActivity.this.model = post;
					addCapabilitiesFor(post);

					indicator.setViewPager(mPager);
					indicator.setCurrentItem(selectedPage);
					mPager.setCurrentItem(selectedPage);
					DetailsActivity.this.invalidateOptionsMenu();
				}
			}
		});

	}

	private void addCapabilitiesFor(UsenetPost post) {
		if (post == null) {
			return;
		}

		((PostDetailsFragment) fragment).responseDetails(post, null);

		if (ImdbFragment.supports(post)) {
			mAdapter.add(ImdbFragment.getInstance(post));
		}

		if (PostImageFragment.supports(post)) {
			mAdapter.add(PostImageFragment.getInstance(post));
		}

		if (WebViewFragment.supports(post, WebViewFragment.PageType.COMMENTS)) {
			mAdapter.add(WebViewFragment.getInstance(post, WebViewFragment.PageType.COMMENTS));
		}

		if (WebViewFragment.supports(post, WebViewFragment.PageType.NFO)) {
			mAdapter.add(WebViewFragment.getInstance(post, WebViewFragment.PageType.NFO));
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle inState) {
		if (resultsAreCached(inState)) {
			model = (UsenetPost) inState.getSerializable(EXTRA_POST);
			isBookmarked = inState.getBoolean(S_BOOKMARKED);
			selectedPage = inState.getInt(S_FRAGMENT_POSITION);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (model != null) {
			outState.putSerializable(EXTRA_POST, model);
		}
		outState.putInt(S_FRAGMENT_POSITION, selectedPage);
		outState.putBoolean(S_BOOKMARKED, isBookmarked);

		super.onSaveInstanceState(outState);
	}

	protected boolean resultsAreCached(Bundle inState) {
		return (inState != null && inState.containsKey(EXTRA_POST));
	}

	private IDownloadManagerService service = null;
	private final ServiceConnection svcConn = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder binder) {
			service = IDownloadManagerService.Stub.asInterface(binder);
			Log.i(TAG, "Service connected");
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			service = null;
			Log.i(TAG, "Service disconnected");
		}
	};

	@Override
	public Category getCategory() {
		return null;
	}

	@Override
	public Provider getProvider() {
		return provider;
	}

	@Override
	public void refresh() {
		try {
			// Todo: improve me
			provider.getDetailsService().requestPostDetails(model);
		} catch (final Exception ex) {
			Log.e(TAG, "Couldn't refresh");
			ex.printStackTrace();
		}

	}

	@Override
	public UsenetPost getPost() {
		return model;
	}

	@Override
	public void onResponseAddFavourite(UsenetPost post, final Boolean response, final Throwable error) {
		isBookmarked = response;
		favouriteResponse(response, "Bookmark added", "Already added", error);
	}

	@Override
	public void onResponseFavourites(UsenetPostResult posts, Throwable error) {
	}

	@Override
	public void onResponseRemoveFavourite(UsenetPost post, Boolean response, Throwable error) {
		isBookmarked = !response;
		favouriteResponse(response, "Bookmark removed", "Already removed", error);
	}

	private void favouriteResponse(final boolean response, final String positive, final String negative, final Throwable error) {
		guiThread.post(new Runnable() {

			@Override
			public void run() {
				if (error != null) {
					if (error instanceof ProviderException) {
						toast(((ProviderException) error).getProviderErrorString());
					} else {
						toast("Couldn't get a response from the provider");
					}
				} else {
					if (response) {
						toast(positive);
					} else {
						toast(negative);
					}
				}
			}
		});
	}

	@Override
	public String getKeywords() {
		return null;
	}

	@Override
	public void onPageSelected(int position) {
		selectedPage = position;
		invalidateOptionsMenu();
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}
}