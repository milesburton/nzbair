
package com.mb.android.nzbAirPremium.imdb;

public interface IImdbProvider {

	void requestImdbPost(String id);

	void requestImdbPostFromTerm(String term);

	void addListener(IImdbCallbackProvider callback);

	void removeListener(IImdbCallbackProvider callback);
}
