
package com.mb.nzbair.providers.base;

import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.Map;

import com.mb.nzbair.providers.converters.UsenetPostResultConverter;
import com.mb.nzbair.providers.domain.UsenetPostResult;
import com.mb.nzbair.providers.domain.category.Category;
import com.mb.nzbair.providers.interfaces.BrowseCallback;
import com.mb.nzbair.providers.interfaces.BrowseService;
import com.mb.nzbair.remote.HttpGetter;
import com.mb.nzbair.remote.domain.HttpRequestComplete;
import com.mb.nzbair.remote.domain.RequestFor;
import com.mb.nzbair.remote.domain.WithCallback;

public class RemoteBrowseProxy extends BaseProxy<BrowseCallback> implements BrowseService {

	private final Map<String, String> params = new Hashtable<String, String>();
	private String categoryParamField = "cat";

	public RemoteBrowseProxy(String providerId, Map<String, String> params, int appVersion) {

		super(providerId, "browse", appVersion);
		params.put("version", ((Integer) appVersion).toString());
		configure(params);
	}

	public void configure(Map<String, String> params) {

		this.params.putAll(params);
	}

	@Override
	public void browse(Category category, int offset, int limit) {

		final RequestFor<UsenetPostResult> r = new RequestFor<UsenetPostResult>(withApiUrl(offset, limit), new UsenetPostResultConverter()).addParam(buildParams(category));

		final WithCallback c = new WithCallback(this, Request.Category.toString());

		startTask(new HttpGetter<UsenetPostResult>(r, c));
	}

	@Override
	public void search(String keywords, Category category, int offset, int limit) {

		final RequestFor<UsenetPostResult> r = new RequestFor<UsenetPostResult>(withSearchUrl(keywords, offset, limit), new UsenetPostResultConverter()).addParam(buildParams(category));

		final WithCallback c = new WithCallback(this, Request.Search.toString());

		startTask(new HttpGetter<UsenetPostResult>(r, c));
	}

	private Map<String, String> buildParams(Category category) {
		final Map<String, String> requestParams = new Hashtable<String, String>();
		requestParams.putAll(params);

		if (category != null) {
			requestParams.put(categoryParamField, category.generateCategoryIdsAsCSV());
		}

		return requestParams;
	}

	private String withSearchUrl(String term, Integer offset, Integer limit) {
		return super.withApiUrl() + URLEncoder.encode(term) + "/" + offset.toString() + "/" + limit.toString();

	}

	@Override
	public void downloadComplete(HttpRequestComplete rc) {

		switch (Request.valueOf(rc.getRequestId())) {
		case Category:
			for (final BrowseCallback callback : getListeners()) {
				callback.onResponseCategory((UsenetPostResult) rc.getResponse(), rc.getError());
			}

			break;
		case Search:
			for (final BrowseCallback callback : getListeners()) {
				callback.onResponseSearch((UsenetPostResult) rc.getResponse(), rc.getError());
			}

			break;
		}

	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setCategoryParamField(String categoryParamField) {
		this.categoryParamField = categoryParamField;
	}

	private enum Request {
		Search, Category
	}

}
