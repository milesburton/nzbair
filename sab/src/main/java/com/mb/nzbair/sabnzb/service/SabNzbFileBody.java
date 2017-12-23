
package com.mb.nzbair.sabnzb.service;

import java.io.File;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;

import com.mb.nzbair.remote.request.Body;

public class SabNzbFileBody implements Body {

	private final File file;

	public SabNzbFileBody(File f) {

		this.file = f;
	}

	@Override
	public HttpEntity getHttpEntity() {

		final MultipartEntity me = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

		final FileBody fb = new FileBody(file);
		final FormBodyPart fbp = new FormBodyPart("name", fb);
		me.addPart(fbp);

		return me;
	}
}
