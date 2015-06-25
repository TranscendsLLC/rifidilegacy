/*******************************************************************************
 * Copyright (c) 2014 Transcends, LLC.
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU 
 * General Public License as published by the Free Software Foundation; either version 2 of the 
 * License, or (at your option) any later version. This program is distributed in the hope that it will 
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You 
 * should have received a copy of the GNU General Public License along with this program; if not, 
 * write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, 
 * USA. 
 * http://www.gnu.org/licenses/gpl-2.0.html
 *******************************************************************************/
/**
 * 
 */
package org.rifidi.edge.services;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.edge.sensors.NotSubscribedException;
import org.rifidi.edge.sensors.Sensor;
import org.rifidi.edge.util.RifidiEventInterface;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.stream.input.InputHandler;

/**
 * Receive and handle ReadCycles from readers. Publish them to Esper.
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class EsperReceiver implements Runnable {
	/** Logger for this class. */
	private static final Log logger = LogFactory.getLog(EsperReceiver.class);
	/** Set containing the sensors the receiver currently handles. */
	private final Set<Sensor> sensors;
	/** The esper runtime. */
	//private final EPRuntime runtime;
	
	private final SiddhiManagementService siddhiManagementService;
	
	//private Map<String, InputHandler> handlerMap;
	
	/**
	 * Constructor.
	 * 
	 * @param runtime
	 */
	//public EsperReceiver(final EPRuntime runtime, final SiddhiManager manager) {
	public EsperReceiver(final SiddhiManagementService siddhiManagementService) {
		sensors = new CopyOnWriteArraySet<Sensor>();
		//FIXME SIDDHI 
		//this.runtime = runtime;
		System.out.println("TESTSIDDHI.EsperReceiver.constructor: Setting siddhiManagementService: " + siddhiManagementService);
		this.siddhiManagementService = siddhiManagementService;
		//this.handlerMap = new LinkedHashMap<String, InputHandler>();
	}

	/**
	 * Add a new sensor to the receiver.
	 * 
	 * @param sensor
	 */
	public void addSensor(final Sensor sensor) {
		sensors.add(sensor);
	}

	/**
	 * Remove a sensor from the receiver.
	 * 
	 * @param sensor
	 */
	public void removeSensor(final Sensor sensor) {
		sensors.add(sensor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		System.out.println("TESTSIDDHI.EsperReceiver.run.1()");
		while (!Thread.currentThread().isInterrupted()) {
			for (Sensor sensor : sensors) {
				//FIXME SIDDHI
				
				try {
					
					EsperEventContainer container = sensor.receive(this);
					
					//FIXME SIDDHI
					//runtime.sendEvent(container.getReadCycle());
					
					try {
						
						//System.out.println("TESTSIDDHI.EsperReceiver.before 'for'");
						for (RifidiEventInterface event : container.getOtherEvents()) {
							//runtime.sendEvent(event);
							System.out.println("TESTSIDDHI.EsperReceiver.run2");
							siddhiManagementService.sendEvent(event);
						}
						
						
						for (RifidiEventInterface rifidiEvent : container.getOtherEvents()) {
							System.out.println("TESTSIDDHI.EsperReceiver.run3");
							siddhiManagementService.sendEvent(rifidiEvent);
						}
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					
				} catch (NotSubscribedException e) {
					throw new RuntimeException(e);
				}
				
				// when a service becomes unavailable the proxy throws a runtime
				// exception
				catch (RuntimeException re) {
					logger.debug("A sensor went away. " + re);
					sensors.remove(sensor);
				}
				
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			}
		}
	}

}
