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
import com.mb.nzbair.sabnzb.converters.QueueStatusConverter;
import com.mb.nzbair.sabnzb.domain.Queue;

@RunWith(PowerMockRunner.class)
@PrepareForTest(android.util.Log.class)
public class QueueStatusConverterTests {

	QueueStatusConverter converter;

	@Before
	public void setup() {
		converter = new QueueStatusConverter();
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
		Queue result = getConverterResponse(validJson);

		//	assertEquals("en", result.getActive_lang());
		//	assertEquals("07c1a69a430cf59cf08bc5fc6d42c3a6", result.getSession());
		//	assertEquals("0 ", result.getSpeed());
		//	assertEquals("0 B", result.getSize());

		assert (1369.45 == result.getDiskSpaceTotal2());
		assertEquals("warning", result.getLastWarning());
		assert (20 == result.getHaveWarnings());
		assert (0 == result.getPauseInt());
		assert (1369.45 == result.getDiskSpaceTotal1());
		assert (0 == result.getMb());
		assertEquals("0.00 | 0.01 | 0.05", result.getLoadAvg());
		//assertEquals("0", result.getCacheMax());
		assert (0 == result.getSpeedLimit());

		assertEquals("20h", result.getUptime());
		assertEquals("10", result.getRefreshRate());
		assertEquals("0.6.8", result.getVersion());
		assertEquals("white", result.getColorScheme());
		//assertEquals("", result.getNewRelease());
		assertEquals("Idle", result.getStatus());
		assert (0 == result.getCacheArt());
		//assertEquals("0 B", result.getSizeLeft());
		assertEquals("0 B", result.getCacheSize());
		assert (0 == result.getMbLeft());
		assert (742.45 == result.getDiskSpace2());
		assert (742.45 == result.getDiskSpace1());
		assertEquals("0:00:00", result.getTimeLeft());
		assertEquals("", result.getNzb_quota());
		assertEquals("unknown", result.getEta());
		assertEquals("0.00", result.getKbPerSec());
		assertEquals("", result.getNew_rel_url());
		assert (0 == result.getQueueDetails());

		assertEquals("Downloading", result.getSlots().get(0).getStatus());
		assertEquals("20:41 Sat 26 Nov", result.getSlots().get(0).getEta());
		assertEquals("7d", result.getSlots().get(0).getAvgAge());
		assertEquals("None", result.getSlots().get(0).getScript());
		assertEquals("", result.getSlots().get(0).getMsgId());
		assertEquals("", result.getSlots().get(0).getVerbosity());
		assert (4859.13 == result.getSlots().get(0).getMb());
		assertEquals("The.Snow.Devils-1967", result.getSlots().get(0).getFilename());
		assertEquals("Normal", result.getSlots().get(0).getPriority());
		assertEquals("games", result.getSlots().get(0).getCat());
		assert (4481.66 == result.getSlots().get(0).getMbLeft());
		assertEquals("0:15:02", result.getSlots().get(0).getTimeLeft());
		assert (7 == result.getSlots().get(0).getPercentage());
		assertEquals("SABnzbd_nzo_OAkuHm", result.getSlots().get(0).getNzoId());
		assert (3 == result.getSlots().get(0).getUnpackOpts());
		assertEquals("4.7 GB", result.getSlots().get(0).getSize());
	}

	private Queue getConverterResponse(String jsonData) throws Exception {
		Queue result = (Queue) converter.parseStream(getInputStream(jsonData), null);
		return result;
	}

	private InputStream getInputStream(String jsonData) {
		InputStream is = new ByteArrayInputStream(jsonData.getBytes());
		return is;
	}

	String validJson = "{   \"queue\":{ \"categories\":[\"test\"], \"scripts\":[],    \"active_lang\":\"en\",      \"session\":\"07c1a69a430cf59cf08bc5fc6d42c3a6\",      \"speed\":\"0  \",      \"size\":\"0 B\",      \"limit\":0,      \"start\":0,      \"diskspacetotal2\":\"1369.45\",      \"darwin\":false,      \"last_warning\":\"warning\",      \"have_warnings\":\"20\",      \"noofslots\":1,      \"pause_int\":\"0\",      \"diskspacetotal1\":\"1369.45\",      \"mb\":\"0.00\",      \"loadavg\":\"0.00 | 0.01 | 0.05\",      \"cache_max\":\"0\",      \"speedlimit\":\"\",      \"webdir\":\"/usr/share/sabnzbdplus/interfaces/smpl/templates\",      \"paused\":false,      \"isverbose\":false,      \"restart_req\":false,      \"power_options\":true,      \"helpuri\":\"http://wiki.sabnzbd.org/\",      \"uptime\":\"20h\",      \"refresh_rate\":\"10\",      \"version\":\"0.6.8\",      \"color_scheme\":\"white\",      \"new_release\":\"\",      \"nt\":false,      \"status\":\"Idle\",      \"finish\":0,      \"cache_art\":\"0\",      \"paused_all\":false,      \"finishaction\":null,      \"sizeleft\":\"0 B\",      \"cache_size\":\"0 B\",      \"mbleft\":\"0.00\",      \"diskspace2\":\"742.45\",      \"diskspace1\":\"742.45\",      \"timeleft\":\"0:00:00\",      \"nzb_quota\":\"\",      \"eta\":\"unknown\",      \"kbpersec\":\"0.00\",      \"new_rel_url\":\"\",      \"queue_details\":\"0\", \"slots\":[         {            \"status\":\"Downloading\",            \"index\":0,            \"eta\":\"20:41 Sat 26 Nov\",            \"missing\":0,            \"avg_age\":\"7d\",            \"script\":\"None\",            \"msgid\":\"\",            \"verbosity\":\"\",            \"mb\":\"4859.13\",            \"sizeleft\":\"4.4 GB\",            \"filename\":\"The.Snow.Devils-1967\",            \"priority\":\"Normal\",            \"cat\":\"games\",            \"mbleft\":\"4481.66\",            \"timeleft\":\"0:15:02\",            \"percentage\":\"7\",            \"nzo_id\":\"SABnzbd_nzo_OAkuHm\",            \"unpackopts\":\"3\",            \"size\":\"4.7 GB\"         }	]   }}";

}
