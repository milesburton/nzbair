
package com.mb.nzbair.remote.converters;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mb.nzbair.remote.response.RestResponse;

public class ImageResponseConverter implements HttpResponseConverter<Bitmap> {

	@Override
	public Bitmap convert(RestResponse r) throws IOException {

		return BitmapFactory.decodeStream(r.getStream());
	}

}
