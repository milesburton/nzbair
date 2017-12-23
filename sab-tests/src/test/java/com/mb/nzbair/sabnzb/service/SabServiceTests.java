package com.mb.nzbair.sabnzb.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.mb.nzbair.remote.AsyncDownloadTask;
import com.mb.nzbair.remote.DownloadWebResource;
import com.mb.nzbair.remote.converters.StringConverter;
import com.mb.nzbair.sabnzb.converters.BooleanResponseConverter;
import com.mb.nzbair.sabnzb.converters.CategoryConverter;
import com.mb.nzbair.sabnzb.converters.HistoricalQueueConverter;
import com.mb.nzbair.sabnzb.converters.QueueStatusConverter;
import com.mb.nzbair.sabnzb.converters.SabPriorityConverter;
import com.mb.nzbair.sabnzb.converters.SabSwitchJobConverter;
import com.mb.nzbair.sabnzb.domain.HistoricalQueue;
import com.mb.nzbair.sabnzb.domain.HistoricalSlot;
import com.mb.nzbair.sabnzb.domain.Queue;
import com.mb.nzbair.sabnzb.domain.Slot;

public class SabServiceTests {
	private DownloadWebResource task;

	private final SabService sab = new SabService("", "mock", "mock", false);

	private void assertCommonParams() {
		assertEquals("mock", task.getParams().get("apikey"));
		assertEquals("json", task.getParams().get("output"));
	}

	@Before
	public void setup() {
		sab.setDownloadTaskStratergy(mockDownloadTask);
		sab.addListener(mockSabCallback);
	}

	@Test
	public void testRequestCategoryList() {
		sab.requestCategoryList();
		assertEquals("get_cats", task.getParams().get("mode"));
		assertEquals(SabRequest.CategoryList.toString(), task.getId());
		assertEquals(CategoryConverter.class, task.getParseStratergy().getClass());
		assertEquals("mock", task.getUrl());
		assertNull(task.getPayload());
		assertCommonParams();
	}

	private List<String> categories;

	@Test
	public void testResponseCategoryList() {
		List<String> mockCategories = new ArrayList<String>();
		mockCategories.add("Movies");
		sab.downloadComplete(SabRequest.CategoryList.toString(), mockCategories, null, null);
		assertEquals(mockCategories, categories);
	}

	@Test
	public void testRequestPause() {
		sab.requestPause();
		assertEquals("pause", task.getParams().get("mode"));
		assertEquals(SabRequest.Pause.toString(), task.getId());
		assertEquals(BooleanResponseConverter.class, task.getParseStratergy().getClass());
		assertEquals("mock", task.getUrl());
		assertNull(task.getPayload());
		assertCommonParams();
	}

	private Boolean pausedState;

	@Test
	public void testResponsePause() {
		sab.downloadComplete(SabRequest.Pause.toString(), true, null, null);
		assertTrue(pausedState);
	}

	@Test
	public void testRequestSetSpeedLimit() {
		sab.requestSetSpeedLimit(10);
		assertEquals("config", task.getParams().get("mode"));
		assertEquals("speedlimit", task.getParams().get("name"));
		assertEquals("10", task.getParams().get("value"));

		assertEquals(SabRequest.SetSpeedLimit.toString(), task.getId());
		assertEquals(BooleanResponseConverter.class, task.getParseStratergy().getClass());
		assertEquals("mock", task.getUrl());
		assertNull(task.getPayload());
		assertCommonParams();
	}

	private Boolean speedLimitState;

	@Test
	public void testResponseSetSpeedLimit() {
		sab.downloadComplete(SabRequest.SetSpeedLimit.toString(), true, null, null);
		assertTrue(speedLimitState);
	}

	@Test
	public void testRequestShutdown() {
		sab.requestShutdown();
		assertEquals("shutdown", task.getParams().get("mode"));
		assertEquals(SabRequest.Shutdown.toString(), task.getId());
		assertEquals(BooleanResponseConverter.class, task.getParseStratergy().getClass());
		assertEquals("mock", task.getUrl());
		assertNull(task.getPayload());
		assertCommonParams();
	}

	private Boolean shutdownResponse;

	@Test
	public void testResponseShutdown() {
		sab.downloadComplete(SabRequest.Shutdown.toString(), true, null, null);
		assertTrue(shutdownResponse);
	}

	@Test
	public void testRequestResume() {
		sab.requestResume();
		assertEquals("resume", task.getParams().get("mode"));
		assertEquals(SabRequest.Resume.toString(), task.getId());
		assertEquals(BooleanResponseConverter.class, task.getParseStratergy().getClass());
		assertEquals("mock", task.getUrl());
		assertNull(task.getPayload());
		assertCommonParams();
	}

	private Boolean resumeResponse;

	@Test
	public void testResponseResume() {
		sab.downloadComplete(SabRequest.Resume.toString(), true, null, null);
		assertTrue(resumeResponse);
	}

	@Test
	public void testRequestVersion() {
		sab.requestVersion();
		assertEquals("version", task.getParams().get("mode"));
		assertEquals(SabRequest.Version.toString(), task.getId());
		assertEquals(StringConverter.class, task.getParseStratergy().getClass());
		assertEquals("mock", task.getUrl());
		assertNull(task.getPayload());
		assertCommonParams();
	}

	private String responseVersion;

	@Test
	public void testResponseVersion() {
		sab.downloadComplete(SabRequest.Version.toString(), "1", null, null);
		assertEquals("1", responseVersion);
	}

	@Test
	public void testRequestDeleteSingleJob() {
		List<Slot> slots = new ArrayList<Slot>();
		Slot slot = new Slot();
		slot.setNzoId("anIdea");
		slots.add(slot);

		sab.requestDeleteJobs(slots, false);
		assertEquals("queue", task.getParams().get("mode"));
		assertEquals("delete", task.getParams().get("name"));
		assertEquals("anIdea,", task.getParams().get("value"));
		assertEquals(SabRequest.DeleteJob.toString(), task.getId());
		assertEquals(BooleanResponseConverter.class, task.getParseStratergy().getClass());
		assertEquals("mock", task.getUrl());
		assertEquals(slots, task.getPayload());
		assertCommonParams();
	}

	@Test
	public void testRequestDeleteAllJob() {
		List<Slot> slots = new ArrayList<Slot>();
		Slot slot = new Slot();
		slot.setNzoId("anIdea");
		slots.add(slot);
		boolean deleteAll = true;

		sab.requestDeleteJobs(slots, deleteAll);
		assertEquals("queue", task.getParams().get("mode"));
		assertEquals("delete", task.getParams().get("name"));
		assertEquals("all", task.getParams().get("value"));
		assertEquals(SabRequest.DeleteJob.toString(), task.getId());
		assertEquals(BooleanResponseConverter.class, task.getParseStratergy().getClass());
		assertEquals("mock", task.getUrl());
		assertEquals(slots, task.getPayload());
		assertCommonParams();
	}

	private Boolean deleteJobsResponse;

	@Test
	public void testResponseDeleteJobs() {
		sab.downloadComplete(SabRequest.DeleteJob.toString(), true, null, null);
		assertTrue(deleteJobsResponse);
	}

	@Test
	public void testMoveJob() {

		Slot slot = new Slot();
		slot.setNzoId("anIdea");

		sab.requestMoveJob(slot, 1);
		assertEquals("switch", task.getParams().get("mode"));
		assertEquals(slot.getNzoId(), task.getParams().get("value"));
		assertEquals("1", task.getParams().get("value2"));
		assertEquals(SabRequest.MoveJob.toString(), task.getId());
		assertEquals(SabSwitchJobConverter.class, task.getParseStratergy().getClass());
		assertEquals("mock", task.getUrl());
		assertNull(task.getPayload());
		assertCommonParams();
	}

	private Boolean moveJobResponse;

	@Test
	public void testResponseMoveJob() {
		sab.downloadComplete(SabRequest.MoveJob.toString(), true, null, null);
		assertTrue(moveJobResponse);
	}

	@Test
	public void testPauseJob() {

		Slot slot = new Slot();
		slot.setNzoId("anIdea");

		sab.requestPauseJob(slot);
		assertEquals("queue", task.getParams().get("mode"));
		assertEquals("pause", task.getParams().get("name"));
		assertEquals(slot.getNzoId(), task.getParams().get("value"));

		assertEquals(SabRequest.PauseJob.toString(), task.getId());
		assertEquals(BooleanResponseConverter.class, task.getParseStratergy().getClass());
		assertEquals("mock", task.getUrl());
		assertNull(task.getPayload());
		assertCommonParams();
	}

	private Boolean pauseJobResponse;

	@Test
	public void testResponsePauseJob() {
		sab.downloadComplete(SabRequest.PauseJob.toString(), true, null, null);
		assertTrue(pauseJobResponse);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRequestRenameJob() {

		Slot slot = new Slot();
		slot.setNzoId("anIdea");

		String filename = "c:\bleh";

		sab.requestRenameJob(slot, filename);
		assertEquals("queue", task.getParams().get("mode"));
		assertEquals("rename", task.getParams().get("name"));
		assertEquals(slot.getNzoId(), task.getParams().get("value"));
		assertEquals(URLEncoder.encode(filename), task.getParams().get("value2"));

		assertEquals(SabRequest.RenameJob.toString(), task.getId());
		assertEquals(BooleanResponseConverter.class, task.getParseStratergy().getClass());
		assertEquals("mock", task.getUrl());
		assertEquals(slot, task.getPayload());
		assertCommonParams();
	}

	private Boolean renameJobResponse;

	@Test
	public void testResponseRenameJob() {
		sab.downloadComplete(SabRequest.RenameJob.toString(), true, null, null);
		assertTrue(renameJobResponse);
	}

	@Test
	public void testRequestRetryJob() {

		HistoricalSlot slot = new HistoricalSlot();
		slot.setId("anIdea");

		sab.requestRetryJob(slot);
		assertEquals("retry", task.getParams().get("mode"));
		assertEquals(slot.getId(), task.getParams().get("name"));

		assertEquals(SabRequest.RetryJob.toString(), task.getId());
		assertEquals(BooleanResponseConverter.class, task.getParseStratergy().getClass());
		assertEquals("mock", task.getUrl());
		assertEquals(slot, task.getPayload());
		assertCommonParams();
	}

	private Boolean retryJobResponse;

	@Test
	public void testResponseRetryJob() {
		sab.downloadComplete(SabRequest.RetryJob.toString(), true, null, null);
		assertTrue(retryJobResponse);
	}

	@Test
	public void testRequestSwapJob() {

		Slot slota = new Slot();
		slota.setNzoId("a");

		Slot slotb = new Slot();
		slotb.setNzoId("b");

		sab.requestSwapJob(slota, slotb);
		assertEquals("switch", task.getParams().get("mode"));
		assertEquals(slota.getNzoId(), task.getParams().get("value"));
		assertEquals(slotb.getNzoId(), task.getParams().get("value2"));

		assertEquals(SabRequest.SwapJob.toString(), task.getId());
		assertEquals(SabSwitchJobConverter.class, task.getParseStratergy().getClass());
		assertEquals("mock", task.getUrl());
		assertEquals(slota, task.getPayload());
		assertCommonParams();
	}

	private Boolean swapJobResponse;

	@Test
	public void testResponseSwapJob() {
		sab.downloadComplete(SabRequest.SwapJob.toString(), true, null, null);
		assertTrue(swapJobResponse);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRequestAddByUrl() {

		String url = "http://google.com";
		String category = "";
		// params are encoding by the remote layer NOT the sab layer
		sab.requestAddByUrl(url, category);
		assertEquals("addurl", task.getParams().get("mode"));
		assertEquals(url, task.getParams().get("name"));
		assertEquals(category, task.getParams().get("cat"));

		assertEquals(SabRequest.AddByUrl.toString(), task.getId());
		assertEquals(BooleanResponseConverter.class, task.getParseStratergy().getClass());
		assertEquals("mock", task.getUrl());
		assertCommonParams();
	}

	private Boolean responseAddByUrl;

	@Test
	public void testResponsAddByUrl() {
		sab.downloadComplete(SabRequest.AddByUrl.toString(), true, null, null);
		assertTrue(responseAddByUrl);
	}

	@Test
	public void testRequestHistory() {

		sab.requestHistory(1, 10);
		assertEquals("history", task.getParams().get("mode"));
		assertEquals("1", task.getParams().get("start"));
		assertEquals("10", task.getParams().get("limit"));

		assertEquals(SabRequest.History.toString(), task.getId());
		assertEquals(HistoricalQueueConverter.class, task.getParseStratergy().getClass());
		assertEquals("mock", task.getUrl());
		assertCommonParams();
	}

	private HistoricalQueue responseHistorySlot;

	@Test
	public void testResponseHistory() {
		HistoricalQueue slots = new HistoricalQueue();
		sab.downloadComplete(SabRequest.History.toString(), slots, null, null);
		assertEquals(slots, responseHistorySlot);
	}

	@Test
	public void testRequestQueueStatus() {
		sab.requestQueueStatus();
		assertEquals("queue", task.getParams().get("mode"));
		assertEquals(SabRequest.Queue.toString(), task.getId());
		assertEquals(QueueStatusConverter.class, task.getParseStratergy().getClass());
		assertEquals("mock", task.getUrl());
		assertCommonParams();
	}

	private Queue responseQueueStatus;

	@Test
	public void testResponseQueueStatus() {
		Queue slots = new Queue();
		sab.downloadComplete(SabRequest.Queue.toString(), slots, null, null);
		assertEquals(slots, responseQueueStatus);
	}

	@Test
	public void testRequestDelete() {
		HistoricalSlot slot = new HistoricalSlot();
		slot.setNzo_id("anIdea");
		List<HistoricalSlot> slots = new ArrayList<HistoricalSlot>();
		slots.add(slot);

		sab.requestDeleteHistoricalJob(slots, false);
		assertEquals("history", task.getParams().get("mode"));
		assertEquals("delete", task.getParams().get("name"));
		assertEquals("anIdea,", task.getParams().get("value"));
		assertEquals(SabRequest.DeleteHistoricalJob.toString(), task.getId());
		assertEquals(BooleanResponseConverter.class, task.getParseStratergy().getClass());
		assertEquals("mock", task.getUrl());
		assertCommonParams();
	}

	@Test
	public void testRequestDeleteAll() {

		sab.requestDeleteHistoricalJob(null, true);
		assertEquals("history", task.getParams().get("mode"));
		assertEquals("delete", task.getParams().get("name"));
		assertEquals("all", task.getParams().get("value"));
		assertEquals(SabRequest.DeleteHistoricalJob.toString(), task.getId());
		assertEquals(BooleanResponseConverter.class, task.getParseStratergy().getClass());
		assertEquals("mock", task.getUrl());
		assertCommonParams();
	}

	private Boolean responseDelete;

	@Test
	public void testResponseDelete() {
		sab.downloadComplete(SabRequest.DeleteHistoricalJob.toString(), true, null, null);
		assertTrue(responseDelete);
	}

	@Test
	public void testRequestPriority() {
		Slot slotb = new Slot();
		slotb.setNzoId("b");

		sab.requestPriority(slotb, -1);
		assertEquals("queue", task.getParams().get("mode"));
		assertEquals("priority", task.getParams().get("name"));
		assertEquals(slotb.getNzoId(), task.getParams().get("value"));
		assertEquals("-1", task.getParams().get("value2"));
		assertEquals(SabRequest.Priority.toString(), task.getId());
		assertEquals(SabPriorityConverter.class, task.getParseStratergy().getClass());
		assertEquals("mock", task.getUrl());
		assertCommonParams();
	}

	protected boolean responsePriority;

	@Test
	public void testResponsePriority() {
		sab.downloadComplete(SabRequest.Priority.toString(), true, null, null);
		assertTrue(responsePriority);
	}

	@Test
	public void testHasValidSetup() {
		assertTrue(sab.hasValidSetup());
	}

	@Test
	public void testHasInvalidSetup() {
		sab.configure("", null, null, false);
		assertFalse(sab.hasValidSetup());
	}

	AsyncDownloadTask mockDownloadTask = new AsyncDownloadTask() {

		@Override
		public void request(DownloadWebResource task) {
			SabServiceTests.this.task = task;
		}

	};

	ISabRequestCallback mockSabCallback = new ISabRequestCallback() {

		@Override
		public void onResponseCategoryList(List<String> categories, Throwable error) {
			SabServiceTests.this.categories = categories;
		}

		@Override
		public void onResponsePause(Boolean ok, Throwable error) {
			pausedState = ok;
		}

		@Override
		public void onResponseSetSpeedLimit(Boolean ok, Throwable error) {
			SabServiceTests.this.speedLimitState = ok;
		}

		@Override
		public void onResponseShutdown(Boolean ok, Throwable error) {
			SabServiceTests.this.shutdownResponse = ok;
		}

		@Override
		public void onResponseResume(Boolean ok, Throwable error) {
			SabServiceTests.this.resumeResponse = ok;
		}

		@Override
		public void onResponseVersion(String version, Throwable error) {
			SabServiceTests.this.responseVersion = version;
		}

		@Override
		public void onResponseDeleteJobs(List<Slot> jobs, Boolean ok, Throwable error) {
			SabServiceTests.this.deleteJobsResponse = ok;
		}

		@Override
		public void onResponseMoveJob(Slot from, Boolean ok, Throwable error) {
			SabServiceTests.this.moveJobResponse = ok;
		}

		@Override
		public void onResponsePauseJob(Slot job, Boolean ok, Throwable error) {
			SabServiceTests.this.pauseJobResponse = ok;
		}

		@Override
		public void onResponseResumeJob(Slot job, Boolean ok, Throwable error) {
			SabServiceTests.this.renameJobResponse = ok;
		}

		@Override
		public void onResponseRetryJob(Boolean ok, Throwable error) {
			SabServiceTests.this.retryJobResponse = ok;
		}

		@Override
		public void onResponseRenameJob(Slot job, Boolean ok, Throwable error) {
			SabServiceTests.this.renameJobResponse = ok;
		}

		@Override
		public void onResponseSwapJob(Slot from, Boolean ok, Throwable error) {
			SabServiceTests.this.swapJobResponse = ok;
		}

		@Override
		public void onResponseAddByUrl(Boolean ok, Throwable error) {
			SabServiceTests.this.responseAddByUrl = ok;
		}

		@Override
		public void onResponseHistory(HistoricalQueue hQueue, Throwable error) {
			SabServiceTests.this.responseHistorySlot = hQueue;
		}

		@Override
		public void onResponseQueueStatus(Queue queue, Throwable error) {
			SabServiceTests.this.responseQueueStatus = queue;
		}

		@Override
		public void onResponseDeleteHistoryJob(Boolean ok, Throwable error) {
			SabServiceTests.this.responseDelete = ok;
		}

		@Override
		public void onResponsePriority(Slot job, Boolean ok, Throwable error) {
			SabServiceTests.this.responsePriority = ok;
		}

		@Override
		public void onResponseAutoShutdownOff(Boolean ok, Throwable error) {

		}

		@Override
		public void onResponseAutoShutdownOn(Boolean ok, Throwable error) {

		}

		@Override
		public void onResponseScriptList(Throwable error) {

		}

		@Override
		public void onResponseSetEmptyQueueAction(String action, Throwable error) {

		}

		@Override
		public void onResponseWarningsList(Throwable error) {

		}

	};
}
