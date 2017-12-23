
package com.mb.nzbair.providers.interfaces;

import com.mb.nzbair.providers.domain.UsenetPost;

public interface FavouritesService {

	void requestFavourites(int offset, int limit);

	void requestAddFavourite(UsenetPost post);

	void requestRemoveFavourite(UsenetPost post);

	void addListener(FavouritesCallback callback);

	void removeListener(FavouritesCallback callback);
}
