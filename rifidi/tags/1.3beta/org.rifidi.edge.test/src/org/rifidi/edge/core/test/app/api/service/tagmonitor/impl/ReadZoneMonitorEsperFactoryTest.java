/**
 * 
 */
package org.rifidi.edge.core.test.app.api.service.tagmonitor.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.rifidi.edge.api.service.tagmonitor.ReadZone;
import org.rifidi.edge.api.service.tagmonitor.ReadZoneMonitorEsperFactory;

/**
 * @author manoj
 *
 */
public class ReadZoneMonitorEsperFactoryTest {
	ReadZone readZone;
	ReadZone readZone1;
	List<ReadZone> readZones;
	Integer windowID;
	Float departureWaitTime;
	TimeUnit timeUnit;
	ReadZoneMonitorEsperFactory readZoneMonitorEsperFactory;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		readZone = new ReadZone("readerID");
		readZone1 =new ReadZone("readerID1");
		readZones = new ArrayList<ReadZone>();
		readZones.add(readZone);
		readZones.add(readZone1);
		windowID = new Integer(5);
		departureWaitTime = new Float(10);
		timeUnit = TimeUnit.SECONDS;
		readZoneMonitorEsperFactory = new ReadZoneMonitorEsperFactory(readZones, windowID, departureWaitTime, timeUnit);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.rifidi.edge.api.service.tagmonitor.ReadZoneMonitorEsperFactory#createStatements()}.
	 */
	@Test
	public void testCreateStatements() {
		
	}

	/**
	 * Test method for {@link org.rifidi.edge.api.service.tagmonitor.ReadZoneMonitorEsperFactory#createQuery()}.
	 */
	@Test
	public void testCreateQuery() {
		assertEquals("select irstream * from tags_5", readZoneMonitorEsperFactory.createQuery());
	}

}
