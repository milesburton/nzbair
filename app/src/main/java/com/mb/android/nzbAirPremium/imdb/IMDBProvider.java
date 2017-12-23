
package com.mb.android.nzbAirPremium.imdb;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.mb.nzbair.remote.HttpGetter;
import com.mb.nzbair.remote.HttpRequestCompleteCallback;
import com.mb.nzbair.remote.converters.HttpResponseConverter;
import com.mb.nzbair.remote.converters.StringConverter;
import com.mb.nzbair.remote.domain.HttpRequestComplete;
import com.mb.nzbair.remote.domain.RequestFor;
import com.mb.nzbair.remote.domain.WithCallback;

public class IMDBProvider implements IImdbProvider, HttpRequestCompleteCallback {

	private final ExecutorService downloader;

	String apiUrl = "http://www.imdbapi.com/";
	private final List<IImdbCallbackProvider> callbacks = new ArrayList<IImdbCallbackProvider>();

	public IMDBProvider() {
		downloader = Executors.newSingleThreadExecutor();
	}

	@Override
	public void addListener(IImdbCallbackProvider callback) {

		this.callbacks.add(callback);
	}

	@Override
	public void removeListener(IImdbCallbackProvider callback) {

		this.callbacks.remove(callback);
	}

	@Override
	public void requestImdbPost(String id) {
		startTask("", new IMDBConverter(new StringConverter()), apiUrl, getParams(id, ""), id);
	}

	private Map<String, String> getParams(String id, String term) {
		final Map<String, String> params = new Hashtable<String, String>();
		params.put("i", "tt" + id);
		params.put("t", term);

		return params;
	}

	private void startTask(String request, HttpResponseConverter<Imdb> strat, String url, Map<String, String> params, String payload) {

		final RequestFor<Imdb> r = new RequestFor<Imdb>(url, strat).addParam(params);
		final WithCallback c = new WithCallback(this, request.toString()).addPayload(payload);

		downloader.submit(new HttpGetter<Imdb>(r, c));
	}

	@Override
	public void downloadComplete(HttpRequestComplete rc) {

		final Imdb aPost = (Imdb) rc.getResponse();
		aPost.setId((String) rc.getPayload());
		for (final IImdbCallbackProvider callback : callbacks) {
			callback.responseImdbPost(aPost, rc.getError());
		}

	}

	@Override
	public void requestImdbPostFromTerm(String term) {
		startTask("", new IMDBConverter(new StringConverter()), apiUrl, getParams("", term), term);

	}

}
