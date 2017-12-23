package com.mb.nzbair.sabnzb.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.mb.nzbair.sabnzb.domain.HistoricalSlot;
import com.mb.nzbair.sabnzb.domain.Slot;

public class SabProxyTests {

	private SabProxy proxy;
	private final int TIMEOUT = 100;

	private Map<SabRequest, Integer> requestMap;

	@Before
	public void setup() {

		proxy = new SabProxy();
		proxy.addService(mockSab);
		proxy.setTimeout(TIMEOUT);

		requestMap = new HashMap<SabRequest, Integer>();
	}

	@Test
	public void testDefaultService() {
		proxy = new SabProxy();
		proxy.addService(mockSab);
		assertTrue(proxy.isReady());
	}

	@Test
	public void testRemoveService() {
		proxy = new SabProxy();
		proxy.addService(mockSab);
		proxy.removeService(mockSab.getId());
		assertFalse(proxy.isReady());
	}

	@Test
	public void testGetService() {
		proxy = new SabProxy();
		proxy.addService(mockSab);
		assertEquals(proxy.getService("cake"), mockSab);
	}

	@Test
	public void testHasService() {
		proxy = new SabProxy();
		proxy.addService(mockSab);
		assertTrue(proxy.hasService("cake"));
	}

	@Test
	public void testHasServiceNo() {
		proxy = new SabProxy();
		assertFalse(proxy.hasService("cake"));
	}

	@Test
	public void testInvalidDefaultService() {
		proxy = new SabProxy();
		assertFalse(proxy.isReady());
	}

	@Test
	public void testValidSetupWithService() {
		assertTrue(proxy.hasValidSetup());
	}

	@Test
	public void testValidSetupWithNoActiveService() {
		proxy = new SabProxy();
		assertFalse(proxy.hasValidSetup());
	}

	@Test
	public void testTwoAddByUrlWithinTimeout() {
		SabRequest request = SabRequest.AddByUrl;
		shouldBeZero(request);
		proxy.requestAddByUrl("", "");
		shouldBeOne(request);
		proxy.requestAddByUrl("", "");
		shouldBeOne(request);
	}

	@Test
	public void testTwoAddByUrlExpiredTimeout() throws InterruptedException {
		SabRequest request = SabRequest.AddByUrl;
		shouldBeZero(request);
		proxy.requestAddByUrl("", "");
		shouldBeOne(request);
		waitTillTimeoutExpired();
		proxy.requestAddByUrl("", "");
		shouldBeTwo(request);
	}

	@Test
	public void testTwoAutoShutDownOffWithinTimeout() {
		SabRequest request = SabRequest.AutoShutDownOff;
		shouldBeZero(request);
		proxy.requestAutoShutdownOff();
		shouldBeOne(request);
		proxy.requestAutoShutdownOff();
		shouldBeOne(request);
	}

	@Test
	public void testTwoAutoShutDownOnExpiredTimeout() throws InterruptedException {
		SabRequest request = SabRequest.AutoShutDownOn;
		shouldBeZero(request);
		proxy.requestAutoShutdownOn();
		shouldBeOne(request);
		waitTillTimeoutExpired();
		proxy.requestAutoShutdownOn();
		shouldBeTwo(request);
	}

	@Test
	public void testTwoAutoShutDownOnWithinTimeout() {
		SabRequest request = SabRequest.AutoShutDownOn;
		shouldBeZero(request);
		proxy.requestAutoShutdownOn();
		shouldBeOne(request);
		proxy.requestAutoShutdownOn();
		shouldBeOne(request);
	}

	@Test
	public void testTwoAutoShutDownOffExpiredTimeout() throws InterruptedException {
		SabRequest request = SabRequest.AutoShutDownOff;
		shouldBeZero(request);
		proxy.requestAutoShutdownOff();
		shouldBeOne(request);
		waitTillTimeoutExpired();
		proxy.requestAutoShutdownOff();
		shouldBeTwo(request);
	}

	@Test
	public void testTwoCategoryListWithinTimeout() {
		SabRequest request = SabRequest.CategoryList;
		shouldBeZero(request);
		proxy.requestCategoryList();
		shouldBeOne(request);
		proxy.requestCategoryList();
		shouldBeOne(request);
	}

	@Test
	public void testTwoCategoryListExpiredTimeout() throws InterruptedException {
		SabRequest request = SabRequest.CategoryList;
		shouldBeZero(request);
		proxy.requestCategoryList();
		shouldBeOne(request);
		waitTillTimeoutExpired();
		proxy.requestCategoryList();
		shouldBeTwo(request);
	}

	@Test
	public void testTwoDeleteHistoricalJobWithinTimeout() {
		SabRequest request = SabRequest.DeleteHistoricalJob;
		shouldBeZero(request);
		proxy.requestDeleteHistoricalJob(null, true);
		shouldBeOne(request);
		proxy.requestDeleteHistoricalJob(null, true);
		shouldBeOne(request);
	}

	@Test
	public void testTwoDeleteHistoricalJobExpiredTimeout() throws InterruptedException {
		SabRequest request = SabRequest.DeleteHistoricalJob;
		shouldBeZero(request);
		proxy.requestDeleteHistoricalJob(null, true);
		shouldBeOne(request);
		waitTillTimeoutExpired();
		proxy.requestDeleteHistoricalJob(null, true);
		shouldBeTwo(request);
	}

	@Test
	public void testTwoDeleteJobsWithinTimeout() {
		SabRequest request = SabRequest.DeleteJob;
		shouldBeZero(request);
		proxy.requestDeleteJobs(null, true);
		shouldBeOne(request);
		proxy.requestDeleteJobs(null, true);
		shouldBeOne(request);
	}

	@Test
	public void testTwoDeleteJobsExpiredTimeout() throws InterruptedException {
		SabRequest request = SabRequest.DeleteJob;
		shouldBeZero(request);
		proxy.requestDeleteJobs(null, true);
		shouldBeOne(request);
		waitTillTimeoutExpired();
		proxy.requestDeleteJobs(null, true);
		shouldBeTwo(request);
	}

	@Test
	public void testTwoHistoryWithinTimeout() {
		SabRequest request = SabRequest.History;
		shouldBeZero(request);
		proxy.requestHistory(0, 0);
		shouldBeOne(request);
		proxy.requestHistory(0, 0);
		shouldBeOne(request);
	}

	@Test
	public void testTwoHistoryExpiredTimeout() throws InterruptedException {
		SabRequest request = SabRequest.History;
		shouldBeZero(request);
		proxy.requestHistory(0, 0);
		shouldBeOne(request);
		waitTillTimeoutExpired();
		proxy.requestHistory(0, 0);
		shouldBeTwo(request);
	}

	@Test
	public void testTwoMoveJobWithinTimeout() {
		SabRequest request = SabRequest.MoveJob;
		shouldBeZero(request);
		proxy.requestMoveJob(null, 0);
		shouldBeOne(request);
		proxy.requestMoveJob(null, 0);
		shouldBeOne(request);
	}

	@Test
	public void testTwoMoveJobExpiredTimeout() throws InterruptedException {
		SabRequest request = SabRequest.MoveJob;
		shouldBeZero(request);
		proxy.requestMoveJob(null, 0);
		shouldBeOne(request);
		waitTillTimeoutExpired();
		proxy.requestMoveJob(null, 0);
		shouldBeTwo(request);
	}

	@Test
	public void testTwoPausesWithinTimeout() {
		SabRequest request = SabRequest.Pause;
		shouldBeZero(request);
		proxy.requestPause();
		shouldBeOne(request);
		proxy.requestPause();
		shouldBeOne(request);
	}

	@Test
	public void testTwoPausesExpiredTimeout() throws InterruptedException {
		SabRequest request = SabRequest.Pause;
		shouldBeZero(request);
		proxy.requestPause();
		shouldBeOne(request);
		waitTillTimeoutExpired();
		proxy.requestPause();
		shouldBeTwo(request);
	}

	@Test
	public void testTwoTemporaryPausesWithinTimeout() {
		SabRequest request = SabRequest.PauseTemporary;
		shouldBeZero(request);
		proxy.requestPause(10);
		shouldBeOne(request);
		proxy.requestPause(10);
		shouldBeOne(request);
	}

	@Test
	public void testTwoTemporaryPausesExpiredTimeout() throws InterruptedException {
		SabRequest request = SabRequest.PauseTemporary;
		shouldBeZero(request);
		proxy.requestPause(10);
		shouldBeOne(request);
		waitTillTimeoutExpired();
		proxy.requestPause(10);
		shouldBeTwo(request);
	}

	@Test
	public void testTwoPauseJobWithinTimeout() {
		SabRequest request = SabRequest.PauseJob;
		shouldBeZero(request);
		proxy.requestPauseJob(null);
		shouldBeOne(request);
		proxy.requestPauseJob(null);
		shouldBeOne(request);
	}

	@Test
	public void testTwoPauseJobExpiredTimeout() throws InterruptedException {
		SabRequest request = SabRequest.PauseJob;
		shouldBeZero(request);
		proxy.requestPauseJob(null);
		shouldBeOne(request);
		waitTillTimeoutExpired();
		proxy.requestPauseJob(null);
		shouldBeTwo(request);
	}

	@Test
	public void testTwoQueueWithinTimeout() {
		SabRequest request = SabRequest.Queue;
		shouldBeZero(request);
		proxy.requestQueueStatus();
		shouldBeOne(request);
		proxy.requestQueueStatus();
		shouldBeOne(request);
	}

	@Test
	public void testTwQueueExpiredTimeout() throws InterruptedException {
		SabRequest request = SabRequest.Queue;
		shouldBeZero(request);
		proxy.requestQueueStatus();
		shouldBeOne(request);
		waitTillTimeoutExpired();
		proxy.requestQueueStatus();
		shouldBeTwo(request);
	}

	@Test
	public void testTwoRenameJobWithinTimeout() {
		SabRequest request = SabRequest.RenameJob;
		shouldBeZero(request);
		proxy.requestRenameJob(null, null);
		shouldBeOne(request);
		proxy.requestRenameJob(null, null);
		shouldBeOne(request);
	}

	@Test
	public void testTwoRenameJobExpiredTimeout() throws InterruptedException {
		SabRequest request = SabRequest.RenameJob;
		shouldBeZero(request);
		proxy.requestRenameJob(null, null);
		shouldBeOne(request);
		waitTillTimeoutExpired();
		proxy.requestRenameJob(null, null);
		shouldBeTwo(request);
	}

	@Test
	public void testTwoResumes() {
		SabRequest request = SabRequest.Resume;
		shouldBeZero(request);
		proxy.requestResume();
		shouldBeOne(request);
		proxy.requestResume();
		shouldBeOne(request);
	}

	@Test
	public void testTwoResumesExpiredTimeout() throws InterruptedException {
		SabRequest request = SabRequest.Resume;
		shouldBeZero(request);
		proxy.requestResume();
		shouldBeOne(request);
		waitTillTimeoutExpired();
		proxy.requestResume();
		shouldBeTwo(request);
	}

	@Test
	public void testTwoResumeJobs() {
		SabRequest request = SabRequest.ResumeJob;
		shouldBeZero(request);
		proxy.requestResumeJob(null);
		shouldBeOne(request);
		proxy.requestResumeJob(null);
		shouldBeOne(request);
	}

	@Test
	public void testTwoResumeJobsExpiredTimeout() throws InterruptedException {
		SabRequest request = SabRequest.ResumeJob;
		shouldBeZero(request);
		proxy.requestResumeJob(null);
		shouldBeOne(request);
		waitTillTimeoutExpired();
		proxy.requestResumeJob(null);
		shouldBeTwo(request);
	}

	@Test
	public void testTwoRetryJob() {
		SabRequest request = SabRequest.RetryJob;
		shouldBeZero(request);
		proxy.requestRetryJob(null);
		shouldBeOne(request);
		proxy.requestRetryJob(null);
		shouldBeOne(request);
	}

	@Test
	public void testTwoRetryJobExpiredTimeout() throws InterruptedException {
		SabRequest request = SabRequest.RetryJob;
		shouldBeZero(request);
		proxy.requestRetryJob(null);
		shouldBeOne(request);
		waitTillTimeoutExpired();
		proxy.requestRetryJob(null);
		shouldBeTwo(request);
	}

	@Test
	public void testRequestScriptList() {
		SabRequest request = SabRequest.ScriptList;
		shouldBeZero(request);
		proxy.requestScriptList();
		shouldBeOne(request);
		proxy.requestScriptList();
		shouldBeOne(request);
	}

	@Test
	public void testRequestScriptListExpiredTimeout() throws InterruptedException {
		SabRequest request = SabRequest.ScriptList;
		shouldBeZero(request);
		proxy.requestScriptList();
		shouldBeOne(request);
		waitTillTimeoutExpired();
		proxy.requestScriptList();
		shouldBeTwo(request);
	}

	@Test
	public void testRequestSetEmptyQueueAction() {
		SabRequest request = SabRequest.EmptyQueueAction;
		shouldBeZero(request);
		proxy.requestSetEmptyQueueAction(null);
		shouldBeOne(request);
		proxy.requestSetEmptyQueueAction(null);
		shouldBeOne(request);
	}

	@Test
	public void testRequestSetEmptyQueueActionExpiredTimeout() throws InterruptedException {
		SabRequest request = SabRequest.EmptyQueueAction;
		shouldBeZero(request);
		proxy.requestSetEmptyQueueAction(null);
		shouldBeOne(request);
		waitTillTimeoutExpired();
		proxy.requestSetEmptyQueueAction(null);
		shouldBeTwo(request);
	}

	@Test
	public void testRequestSetSpeedLimit() {
		SabRequest request = SabRequest.SetSpeedLimit;
		shouldBeZero(request);
		proxy.requestSetSpeedLimit(0);
		shouldBeOne(request);
		proxy.requestSetSpeedLimit(0);
		shouldBeOne(request);
	}

	@Test
	public void testRequestSetSpeedLimitTimeout() throws InterruptedException {
		SabRequest request = SabRequest.SetSpeedLimit;
		shouldBeZero(request);
		proxy.requestSetSpeedLimit(0);
		shouldBeOne(request);
		waitTillTimeoutExpired();
		proxy.requestSetSpeedLimit(0);
		shouldBeTwo(request);
	}

	@Test
	public void testRequestShutdown() {
		SabRequest request = SabRequest.Shutdown;
		shouldBeZero(request);
		proxy.requestShutdown();
		shouldBeOne(request);
		proxy.requestShutdown();
		shouldBeOne(request);
	}

	@Test
	public void testRequestShutdownTimeout() throws InterruptedException {
		SabRequest request = SabRequest.Shutdown;
		shouldBeZero(request);
		proxy.requestShutdown();
		shouldBeOne(request);
		waitTillTimeoutExpired();
		proxy.requestShutdown();
		shouldBeTwo(request);
	}

	@Test
	public void testRequestSwapJob() {
		SabRequest request = SabRequest.SwapJob;
		shouldBeZero(request);
		proxy.requestSwapJob(null, null);
		shouldBeOne(request);
		proxy.requestSwapJob(null, null);
		shouldBeOne(request);
	}

	@Test
	public void testRequestSwapJobTimeout() throws InterruptedException {
		SabRequest request = SabRequest.SwapJob;
		shouldBeZero(request);
		proxy.requestSwapJob(null, null);
		shouldBeOne(request);
		waitTillTimeoutExpired();
		proxy.requestSwapJob(null, null);
		shouldBeTwo(request);
	}

	@Test
	public void testRequestVersion() {
		SabRequest request = SabRequest.Version;
		shouldBeZero(request);
		proxy.requestVersion();
		shouldBeOne(request);
		proxy.requestVersion();
		shouldBeOne(request);
	}

	@Test
	public void testRequestVersionTimeout() throws InterruptedException {
		SabRequest request = SabRequest.Version;
		shouldBeZero(request);
		proxy.requestVersion();
		shouldBeOne(request);
		waitTillTimeoutExpired();
		proxy.requestVersion();
		shouldBeTwo(request);
	}

	@Test
	public void testRequestPriority() {
		SabRequest request = SabRequest.Priority;
		shouldBeZero(request);
		proxy.requestPriority(null, -1);
		shouldBeOne(request);
		proxy.requestPriority(null, -1);
		shouldBeOne(request);
	}

	@Test
	public void testRequestPriorityTimeout() throws InterruptedException {
		SabRequest request = SabRequest.Priority;
		shouldBeZero(request);
		proxy.requestPriority(null, -1);
		shouldBeOne(request);
		waitTillTimeoutExpired();
		proxy.requestPriority(null, -1);
		shouldBeTwo(request);
	}

	private void waitTillTimeoutExpired() throws InterruptedException {
		Thread.sleep((long) (TIMEOUT * 1.1));
	}

	private void shouldBeOne(SabRequest request) {
		assertEquals(request.toString() + " should be one", 1, getCounter(request));
	}

	private void shouldBeTwo(SabRequest request) {
		assertEquals(request.toString() + " should be two", 2, getCounter(request));
	}

	private void shouldBeZero(SabRequest request) {
		assertEquals(request.toString() + " should be zero", 0, getCounter(request));
	}

	private void incrementCounter(SabRequest request) {

		int count = getCounter(request);
		count++;
		requestMap.put(request, count);
	}

	private int getCounter(SabRequest request) {
		Integer count = 0;
		if (requestMap.containsKey(request)) {
			count = requestMap.get(request);
		}

		return count;
	}

	private final ISab mockSab = new ISab() {

		@Override
		public void addListener(ISabRequestCallback callback) {

		}

		@Override
		public boolean hasValidSetup() {

			return true;
		}

		@Override
		public void removeListener(ISabRequestCallback callback) {

		}

		@Override
		public void requestAddByUrl(String url, String category) {
			incrementCounter(SabRequest.AddByUrl);
		}

		@Override
		public void requestAutoShutdownOff() {
			incrementCounter(SabRequest.AutoShutDownOff);
		}

		@Override
		public void requestAutoShutdownOn() {
			incrementCounter(SabRequest.AutoShutDownOn);
		}

		@Override
		public void requestCategoryList() {
			incrementCounter(SabRequest.CategoryList);
		}

		@Override
		public void requestDeleteHistoricalJob(List<HistoricalSlot> slots, boolean deleteAll) {
			incrementCounter(SabRequest.DeleteHistoricalJob);
		}

		@Override
		public void requestDeleteJobs(List<Slot> slots, boolean deleteAll) {
			incrementCounter(SabRequest.DeleteJob);
		}

		@Override
		public void requestHistory(int offset, int limit) {
			incrementCounter(SabRequest.History);
		}

		@Override
		public void requestMoveJob(Slot from, int position) {
			incrementCounter(SabRequest.MoveJob);
		}

		@Override
		public void requestPause() {
			incrementCounter(SabRequest.Pause);
		}

		@Override
		public void requestPause(Integer minutes) {
			incrementCounter(SabRequest.PauseTemporary);
		}

		@Override
		public void requestPauseJob(Slot job) {
			incrementCounter(SabRequest.PauseJob);
		}

		@Override
		public void requestQueueStatus() {
			incrementCounter(SabRequest.Queue);
		}

		@Override
		public void requestRenameJob(Slot job, String filename) {
			incrementCounter(SabRequest.RenameJob);
		}

		@Override
		public void requestResume() {
			incrementCounter(SabRequest.Resume);
		}

		@Override
		public void requestResumeJob(Slot job) {
			incrementCounter(SabRequest.ResumeJob);
		}

		@Override
		public void requestRetryJob(HistoricalSlot from) {
			incrementCounter(SabRequest.RetryJob);
		}

		@Override
		public void requestScriptList() {
			incrementCounter(SabRequest.ScriptList);
		}

		@Override
		public void requestSetEmptyQueueAction(String action) {
			incrementCounter(SabRequest.EmptyQueueAction);
		}

		@Override
		public void requestSetSpeedLimit(int limit) {
			incrementCounter(SabRequest.SetSpeedLimit);
		}

		@Override
		public void requestShutdown() {
			incrementCounter(SabRequest.Shutdown);
		}

		@Override
		public void requestSwapJob(Slot from, Slot to) {
			incrementCounter(SabRequest.SwapJob);
		}

		@Override
		public void requestVersion() {
			incrementCounter(SabRequest.Version);
		}

		@Override
		public void requestPriority(Slot job, Integer priority) {
			incrementCounter(SabRequest.Priority);
		}

		@Override
		public void requestWarningsList() {

		}

		@Override
		public void configure(String id, String sabUrl, String sabApi, boolean allowInvalidCerts) {

		}

		@Override
		public String getId() {
			// TODO Auto-generated method stub
			return "cake";
		}

	};
}
