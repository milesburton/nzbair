
package com.mb.nzbair.providers.interfaces;

import com.mb.nzbair.providers.domain.UsenetPost;
import com.mb.nzbair.providers.domain.UsenetPostResult;

public interface FavouritesCallback {

	void onResponseAddFavourite(UsenetPost post, Boolean response, Throwable error);

	void onResponseRemoveFavourite(UsenetPost post, Boolean response, Throwable error);

	void onResponseFavourites(UsenetPostResult posts, Throwable error);
}
