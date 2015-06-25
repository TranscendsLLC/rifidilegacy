/**
 * 
 */
package org.rifidi.edge.services;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.edge.notification.DatacontainerEvent;
import org.rifidi.edge.notification.EPCGeneration1Event;
import org.rifidi.edge.notification.EPCGeneration2Event;
import org.rifidi.edge.notification.GPIEvent;
import org.rifidi.edge.notification.GPOEvent;
import org.rifidi.edge.notification.ReadCycle;
import org.rifidi.edge.notification.SensorConnectedEvent;
import org.rifidi.edge.notification.SensorDisconnectedEvent;
import org.rifidi.edge.notification.SensorStatusEvent;
import org.rifidi.edge.notification.TagReadEvent;
import org.rifidi.edge.util.RifidiEventInterface;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.stream.input.InputHandler;

/**
 * Service for handling Siddhi instances.
 * 
 * @author Matthew Dean
 */
public class SiddhiManagementServiceImpl implements SiddhiManagementService {

	private SiddhiManager manager;
	private Map<String, InputHandler> handlerMap;

	/** Logger for this class. */
	private static final Log logger = LogFactory
			.getLog(EsperManagementServiceImpl.class);

	public SiddhiManagementServiceImpl() {
		logger.info("SiddhiManagementServiceImpl created.");
		manager = new SiddhiManager();
		handlerMap = new LinkedHashMap<String, InputHandler>();
		
		//Add streams for non tag events
		
		//GPIEvent
		String gpiEventClassName = GPIEvent.class.getSimpleName();
		manager.defineStream( "define stream " + gpiEventClassName + " ( readerId string, port int, state int )" );//state boolean
		handlerMap.put(gpiEventClassName, manager.getInputHandler(gpiEventClassName) );
		
		//GPOEvent
		String gpoEventClassName = GPOEvent.class.getSimpleName();
		manager.defineStream("define stream " + gpoEventClassName + " ( readerId string, port int, state int )");//state boolean
		handlerMap.put(gpoEventClassName, manager.getInputHandler( gpoEventClassName) );
	
		//StartEvent
		String startEventClassName = StartEvent.class.getSimpleName();
		manager.defineStream("define stream " + startEventClassName + " ( name string, trigger string )");
		handlerMap.put(startEventClassName, manager.getInputHandler( startEventClassName ) );
		
		//DestroyEvent
		String destroyEventClassName = DestroyEvent.class.getSimpleName();
		manager.defineStream("define stream " + destroyEventClassName + " ( name string )");
		handlerMap.put(destroyEventClassName, manager.getInputHandler( destroyEventClassName) );
		
		//DatacontainerEvent ???
		//manager.defineStream("define stream " + DatacontainerEvent.class.getName() + " ( name string )");
		//handlerMap.put(DatacontainerEvent.class.getName(), manager.getInputHandler( DatacontainerEvent.class.getName()) );
						
		//EPCGeneration1Event
		//manager.defineStream("define stream " + EPCGeneration1Event.class.getName() + " ( memory int, length int )");
		//handlerMap.put(EPCGeneration1Event.class.getName(), manager.getInputHandler( EPCGeneration1Event.class.getName()) );

		//EPCGeneration2Event
		//manager.defineStream("define stream " + EPCGeneration2Event.class.getName() + " ( memory int, length int )");
		//handlerMap.put(EPCGeneration2Event.class.getName(), manager.getInputHandler( EPCGeneration2Event.class.getName()) );
		
		//ReadCycle
		//manager.defineStream("define stream " + ReadCycle.class.getName() + " ( tags string, readerID string, eventTimestamp long )");
		//handlerMap.put(ReadCycle.class.getName(), manager.getInputHandler( ReadCycle.class.getName()) );
				
		//SensorStatusEvent
		String sensorStatusEventClassName = SensorStatusEvent.class.getSimpleName();
		manager.defineStream("define stream " + sensorStatusEventClassName + " ( readerID string, timestamp long )");
		handlerMap.put(sensorStatusEventClassName, manager.getInputHandler( sensorStatusEventClassName) );
		
		//SensorDisconnectedEvent
		String sensorDisconnectedEventClassName = SensorDisconnectedEvent.class.getSimpleName();
		manager.defineStream("define stream " + sensorDisconnectedEventClassName + " ( sensorID string, timestamp long, sessionID string)");
		handlerMap.put(sensorDisconnectedEventClassName, manager.getInputHandler( sensorDisconnectedEventClassName) );
		
		//SensorConnectedEvent
		String sensorConnectedEventEventClassName = SensorConnectedEvent.class.getSimpleName();
		manager.defineStream("define stream " + sensorConnectedEventEventClassName + " ( sensorID string, timestamp long, sessionID string)");
		handlerMap.put(sensorConnectedEventEventClassName, manager.getInputHandler( sensorConnectedEventEventClassName) );
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.services.SiddhiManagementService#getManager()
	 */
	
	@Override
	public SiddhiManager getManager() {
		return this.manager;
	}
	

	public Map<String, InputHandler> getHandlerMap() {
		return handlerMap;
	}

	/**
	 * Adds a handler to handler map
	 * @param key the handler key to reference inside map
	 * @param handler the handler to add
	 */
	public void addHandler(String key, InputHandler handler){
		handlerMap.put(key, handler);
	}
	
	public void sendEvent(RifidiEventInterface event)
			throws InterruptedException {
		
		System.out.println("SiddhiManagementServiceImpl.sendEvent(): " + event);
		
		handlerMap.get(event.getEventName()).send( event.getEventAttributes() );
		
	}
	
	

}
