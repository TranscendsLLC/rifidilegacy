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
		System.out.println("JUnit Test " + this.getClass().getName() + " loaded.");
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("JUnit Test " + this.getClass().getName() + " unloaded.");
	}

	
	@Test
	public void testConnect(){
		ThingMagicConnectionInfo info = new ThingMagicConnectionInfo();
		info.setIPAddress("127.0.0.1");
		info.setPort(8080);
		

		ThingMagicReaderAdapter adapter = new ThingMagicReaderAdapter(info);
		
		System.out.println("test");
		
		Assert.assertTrue(adapter.connect());
		
		System.out.println("Connected.");
		
		List<TagRead> tagReads = adapter.getNextTags();
		Assert.assertNotNull(tagReads);
		
		for(TagRead tagRead : tagReads){
			System.out.println(tagRead.toXML());
		}
		
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
