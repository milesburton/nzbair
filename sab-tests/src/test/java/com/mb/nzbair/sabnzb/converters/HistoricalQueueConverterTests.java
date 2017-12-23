package com.mb.nzbair.sabnzb.converters;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import android.util.Log;

import com.mb.nzbair.sabnzb.SabException;
import com.mb.nzbair.sabnzb.converters.HistoricalQueueConverter;
import com.mb.nzbair.sabnzb.domain.HistoricalQueue;

@RunWith(PowerMockRunner.class)
@PrepareForTest(android.util.Log.class)
public class HistoricalQueueConverterTests {

	HistoricalQueueConverter converter;

	@Before
	public void setup() {
		converter = new HistoricalQueueConverter();
		PowerMockito.mockStatic(Log.class);
	}

	@Test(expected = SabException.class)
	public void testInvalidJson() throws Exception {
		String brokenJson = "{\"stat";
		getConverterResponse(brokenJson);
	}

	@Test
	public void testValidHistoricalQueue() throws Exception {
		System.out.println(validJson);
		HistoricalQueue result = getConverterResponse(validJson);
		assertEquals(1, result.getSlots().size());
		assertEquals("en", result.getActive_lang());
		assertEquals("07c1a69a430cf59cf08bc5fc6d42c3a6", result.getSession());
		assertEquals("0 ", result.getSpeed());
		assertEquals("0 B", result.getSize());
		assertEquals("1369.45", result.getDiskspacetotal2());
		assertEquals("0 ", result.getSpeed());
		assertEquals("0 B", result.getSize());
		assertEquals("1369.45", result.getDiskspacetotal2());
		assertEquals("warning", result.getLast_warning());
		assertEquals("20", result.getHave_warnings());
		assertEquals("0", result.getPause_int());
		assertEquals("1369.45", result.getDiskspacetotal1());
		assertEquals("0.00", result.getMb());
		assertEquals("0.00 | 0.01 | 0.05", result.getLoadavg());
		assertEquals("0", result.getCache_max());
		assertEquals("", result.getSpeedlimit());
		assertEquals("/usr/share/sabnzbdplus/interfaces/smpl/templates", result.getWebdir());
		assertEquals("http://wiki.sabnzbd.org/", result.getHelpuri());
		assertEquals("20h", result.getUptime());
		assertEquals("1.3 T", result.getTotal_size());
		assertEquals("322.7 G", result.getMonth_size());
		assertEquals("21.1 G", result.getWeek_size());
		assertEquals("0.6.8", result.getVersion());
		assertEquals("white", result.getColor_scheme());
		assertEquals("", result.getNew_release());
		assertEquals("Idle", result.getStatus());
		assertEquals("0", result.getCache_art());
		assertEquals("0 B", result.getSizeleft());
		assertEquals("0 B", result.getCache_size());
		assertEquals("0.00", result.getMbleft());
		assertEquals("742.45", result.getDiskspace2());
		assertEquals("742.45", result.getDiskspace1());
		assertEquals("0:00:00", result.getTimeleft());
		//assertEquals("253 M", result.getDay_size());
		assertEquals("", result.getNzb_quota());
		assertEquals("unknown", result.getEta());
		assertEquals("0.00", result.getKbpersec());
		assertEquals("", result.getNew_rel_url());

		assertEquals("", result.getSlots().get(0).getAction_line());
		assertEquals("True", result.getSlots().get(0).getShow_details());
		assertEquals("", result.getSlots().get(0).getScript_log());
		assertEquals("Download failed - Out of your server's retention?", result.getSlots().get(0).getFail_message());
		assertEquals("479 KB", result.getSlots().get(0).getSize());
		assertEquals("games", result.getSlots().get(0).getCategory());
		assertEquals("D", result.getSlots().get(0).getPp());
		assertEquals("None", result.getSlots().get(0).getScript());
		assertEquals("Sonic+Generations+%282011%29.nzb", result.getSlots().get(0).getNzb_name());
		assertEquals("/volumes/fastStorage/downloads/incomplete/Sonic+Generations+%282011%29", result.getSlots().get(0).getStorage());
		assertEquals("Failed", result.getSlots().get(0).getStatus());
		assertEquals("", result.getSlots().get(0).getScript_line());
		assertEquals("SABnzbd_nzo_PZL0lX", result.getSlots().get(0).getNzo_id());
		assertEquals("", result.getSlots().get(0).getReport());
		assertEquals("/volumes/fastStorage/downloads/incomplete/Sonic+Generations+%282011%29", result.getSlots().get(0).getPath());
		assertEquals("Sonic+Generations+%282011%29", result.getSlots().get(0).getName());
		assertEquals("", result.getSlots().get(0).getUrl());
		assertEquals("", result.getSlots().get(0).getUrl_info());
	}

	private HistoricalQueue getConverterResponse(String jsonData) throws Exception {
		HistoricalQueue result = (HistoricalQueue) converter.parseStream(getInputStream(jsonData), null);
		return result;
	}

	private InputStream getInputStream(String jsonData) {
		InputStream is = new ByteArrayInputStream(jsonData.getBytes());
		return is;
	}

	String validJson = "{\"history\": {\"active_lang\": \"en\",\"session\": \"07c1a69a430cf59cf08bc5fc6d42c3a6\",\"slots\": [{\"action_line\": \"\",\"show_details\": \"True\",\"script_log\": \"\",\"meta\": null,\"fail_message\": \"Download failed - Out of your server's retention?\",\"loaded\": false,\"id\": 19,\"size\": \"479 KB\",\"category\": \"games\",\"pp\": \"D\",\"retry\": 1,\"completeness\": 0,\"script\": \"None\",\"nzb_name\": \"Sonic+Generations+%282011%29.nzb\",\"download_time\": 1705,\"storage\": \"/volumes/fastStorage/downloads/incomplete/Sonic+Generations+%282011%29\",\"status\": \"Failed\",\"script_line\": \"\",\"completed\": 1322173420,\"nzo_id\": \"SABnzbd_nzo_PZL0lX\",\"downloaded\": 490518,\"report\": \"\",\"path\": \"/volumes/fastStorage/downloads/incomplete/Sonic+Generations+%282011%29\",\"postproc_time\": 0,\"name\": \"Sonic+Generations+%282011%29\",\"url\": \"\",\"bytes\": 490518,\"url_info\": \"\",\"stage_log\": [{\"name\": \"Download\",\"actions\": [\"Downloaded in 28 minutes 25 seconds at an average of 288 B/s<br/>11679 articles were missing\"]}]}],\"speed\": \"0 \",\"size\": \"0 B\",\"diskspacetotal2\": \"1369.45\",\"darwin\": false,\"last_warning\": \"warning\",\"have_warnings\": \"20\",\"noofslots\": 19,\"pause_int\": \"0\",\"diskspacetotal1\": \"1369.45\",\"mb\": \"0.00\",\"loadavg\": \"0.00 | 0.01 | 0.05\",\"cache_max\": \"0\",\"speedlimit\": \"\",\"webdir\": \"/usr/share/sabnzbdplus/interfaces/smpl/templates\",\"paused\": false,\"restart_req\": false,\"power_options\": true,\"helpuri\": \"http://wiki.sabnzbd.org/\",\"uptime\": \"20h\",\"total_size\": \"1.3 T\",\"month_size\": \"322.7 G\",\"week_size\": \"21.1 G\",\"version\": \"0.6.8\",\"color_scheme\": \"white\",\"new_release\": \"\",\"nt\": false,\"status\": \"Idle\",\"cache_art\": \"0\",\"paused_all\": false,\"finishaction\": null,\"sizeleft\": \"0 B\",\"cache_size\": \"0 B\",\"mbleft\": \"0.00\",\"diskspace2\": \"742.45\",\"diskspace1\": \"742.45\",\"timeleft\": \"0:00:00\",\"day_size\": \"253 M\",\"nzb_quota\": \"\",\"eta\": \"unknown\",\"kbpersec\": \"0.00\",\"new_rel_url\": \"\"}}";

}
