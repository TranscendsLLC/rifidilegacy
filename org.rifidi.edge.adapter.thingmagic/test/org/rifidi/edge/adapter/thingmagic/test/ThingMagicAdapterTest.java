package org.rifidi.edge.adapter.thingmagic.test;

import static org.junit.Assert.*;

import java.util.List;

import javax.jms.ConnectionFactory;



import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.rifidi.edge.adapter.thingmagic.ThingMagicConnectionInfo;
import org.rifidi.edge.adapter.thingmagic.ThingMagicReaderAdapter;
import org.rifidi.edge.core.tag.TagRead;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

public class ThingMagicAdapterTest {

	private ConnectionFactory connectionFactory;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ServiceRegistry.getInstance().service(this);
	}

	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public void testConnect(){
		ThingMagicConnectionInfo info = new ThingMagicConnectionInfo();
		info.setIPAddress("172.0.0.1");
		info.setPort(8080);
		
		ThingMagicReaderAdapter adapter = new ThingMagicReaderAdapter(info);
		
		Assert.assertTrue(adapter.connect());
		
		List<TagRead> tagReads = adapter.getNextTags();
		Assert.assertNotNull(tagReads);
		
		//TODO print off tagReads
		
		
		Assert.assertTrue(adapter.disconnect());
	}
	
	/*@Test
	public void testTagRead(){
		//TODO: implement this test.
	}*/
	
	@Inject
	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
}
