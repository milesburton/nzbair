
package com.mb.nzbair.remote.converters;

import com.mb.nzbair.remote.response.RestResponse;

public interface HttpResponseConverter<T> {

	T convert(RestResponse r) throws Exception;
}
