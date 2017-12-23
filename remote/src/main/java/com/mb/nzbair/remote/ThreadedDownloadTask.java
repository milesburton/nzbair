
package com.mb.nzbair.remote;

import java.util.concurrent.ExecutorService;

public class ThreadedDownloadTask implements AsyncDownloadTask {

	private final ExecutorService executor;

	public ThreadedDownloadTask(ExecutorService executor) {
		this.executor = executor;
	}

	@Override
	public void request(HttpClientAsync task) {
		executor.submit(task);
	}
}
