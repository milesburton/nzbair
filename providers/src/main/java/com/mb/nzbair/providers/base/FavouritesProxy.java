
package com.mb.nzbair.providers.base;

import java.util.Map;

import com.mb.nzbair.providers.converters.UsenetPostResultConverter;
import com.mb.nzbair.providers.domain.UsenetPost;
import com.mb.nzbair.providers.domain.UsenetPostResult;
import com.mb.nzbair.providers.interfaces.FavouritesCallback;
import com.mb.nzbair.providers.interfaces.FavouritesService;
import com.mb.nzbair.remote.HttpGetter;
import com.mb.nzbair.remote.converters.BooleanConverter;
import com.mb.nzbair.remote.domain.HttpRequestComplete;
import com.mb.nzbair.remote.domain.RequestFor;
import com.mb.nzbair.remote.domain.WithCallback;

public class FavouritesProxy extends BaseProxy<FavouritesCallback> implements FavouritesService {

	private Map<String, String> params;

	public FavouritesProxy(String providerId, Map<String, String> params, int appVersion) {

		super(providerId, "bookmarks", appVersion);
		params.put("version", ((Integer) appVersion).toString());
		configure(params);
	}

	public void configure(Map<String, String> params) {

		this.params = params;
	}

	@Override
	public void requestAddFavourite(UsenetPost post) {

		final RequestFor<Boolean> r = new RequestFor<Boolean>(withModifyBookmarksUrl("add", post.getId()), new BooleanConverter()).addParam(params);

		final WithCallback c = new WithCallback(this, FavouritesRequest.AddFavourite.toString());

		startTask(new HttpGetter<Boolean>(r, c));

	}

	@Override
	public void requestFavourites(int offset, int limit) {

		final RequestFor<UsenetPostResult> r = new RequestFor<UsenetPostResult>(withApiUrl(offset, limit), new UsenetPostResultConverter()).addParam(params);

		final WithCallback c = new WithCallback(this, FavouritesRequest.Favourites.toString());

		startTask(new HttpGetter<UsenetPostResult>(r, c));

	}

	@Override
	public void requestRemoveFavourite(UsenetPost post) {

		final RequestFor<Boolean> r = new RequestFor<Boolean>(withModifyBookmarksUrl("add", post.getId()), new BooleanConverter());

		final WithCallback c = new WithCallback(this, FavouritesRequest.RemoveFavourite.toString());

		startTask(new HttpGetter<Boolean>(r, c));
	}

	private String withModifyBookmarksUrl(String method, String postId) {
		return super.withApiUrl() + method + "/" + postId;
	}

	@Override
	public void downloadComplete(HttpRequestComplete rc) {

		switch (FavouritesRequest.valueOf(rc.getRequestId())) {
		case AddFavourite:

			for (final FavouritesCallback callback : getListeners()) {
				callback.onResponseAddFavourite(null, (Boolean) rc.getResponse(), rc.getError());
			}
			break;
		case RemoveFavourite:

			for (final FavouritesCallback callback : getListeners()) {
				callback.onResponseRemoveFavourite(null, (Boolean) rc.getResponse(), rc.getError());
			}
			break;
		case Favourites:
			for (final FavouritesCallback callback : getListeners()) {
				callback.onResponseFavourites((UsenetPostResult) rc.getResponse(), rc.getError());
			}
			break;
		}
	}

	private enum FavouritesRequest {
		AddFavourite, RemoveFavourite, Favourites
	}

	public Map<String, String> getParams() {
		return params;
	}

}
