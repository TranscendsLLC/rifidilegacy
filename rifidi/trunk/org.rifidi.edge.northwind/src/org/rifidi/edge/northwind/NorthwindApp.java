/*
 *  NorthwindApp.java
 *
 *  Created:	Aug 9, 2010
 *  Project:	Rifidi Edge Server - A middleware platform for RFID applications
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	GNU Public License (GPL)
 *  				http://www.opensource.org/licenses/gpl-3.0.html
 */
package org.rifidi.edge.northwind;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.rifidi.edge.core.app.api.AbstractRifidiApp;
import org.rifidi.edge.core.app.api.service.EsperUtil;
import org.rifidi.edge.core.app.api.service.tagmonitor.ReadZone;
import org.rifidi.edge.core.app.api.service.tagmonitor.ReadZoneMonitoringService;
import org.rifidi.edge.core.app.api.service.tagmonitor.ReadZoneSubscriber;
import org.rifidi.edge.core.services.notification.data.TagReadEvent;
import org.rifidi.edge.northwind.events.DockDoorArrivedEvent;
import org.rifidi.edge.northwind.events.DockDoorDepartedEvent;
import org.rifidi.edge.northwind.events.WeighStationArrivedEvent;
import org.rifidi.edge.northwind.events.WeighStationDepartedEvent;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.StatementAwareUpdateListener;

/**
 * This application is a sample displaying how a Rifidi Edge application is put
 * together. It monitors 2 read zones, the "dock door" and the "weigh station".
 * In a normal workflow, a tag will appear and disappear at the dock door, and
 * then appear and disappear at the weigh station.  
 * 
 * @author Matthew Dean - matt@pramari.com
 */
public class NorthwindApp extends AbstractRifidiApp {

	// The names of the readers.
	private String dockdoor_reader = "Alien_1";
	private String weighstation_reader = "Alien_2";

	// How long a tag can not be seen by a reader before the reader assumes the
	// tag has left the field of view, in seconds.
	private Float dockdoor_timeout = 2.0f;
	private Float weighstation_timeout = 2.0f;

	// How long a tag can remain on either the dockdoor or the weigh station
	// before an alert is sent.
	private String dockdoor_dwelltime = "120 sec";
	private String weighstation_dwelltime = "120 sec";

	// The maximum amount of time it should take before the tag makes its way
	// from the dock door to the weigh station.
	private String dockdoor_to_weighstation_timer = "10 min";

	// The maximum amount of time a tag should be held once seen on the weigh
	// station to make sure it doesn't go backwards. For example, if the tag is
	// seen on the weigh station, and then 8 minutes later it is seen on the
	// dock door, an alert will be fired.
	private String weighstation_to_dockdoor_timer = "10 min";

	private List<ReadZoneSubscriber> subscriberList = new ArrayList<ReadZoneSubscriber>();

	/** The service for monitoring arrival and departure events */
	private ReadZoneMonitoringService readZoneMonitoringService;

	/**
	 * 
	 * 
	 * @param group
	 * @param name
	 */
	public NorthwindApp(String group, String name) {
		super(group, name);
	}

	/**
	 * Called at startup
	 */
	@Override
	public void _start() {
		// The _start method is called after the initialize method. In this
		// method you want to define your ReadZones and define your subscribers.
		// This is also a good place to create your esper statements and
		// listeners.
		super._start();

		/* These statements register the events we will use */
		addEventType(DockDoorArrivedEvent.class);
		addEventType(DockDoorDepartedEvent.class);
		addEventType(WeighStationDepartedEvent.class);
		addEventType(WeighStationArrivedEvent.class);

		// Create the read zones. Currently the only read zones are the Dock
		// Door and the Weigh Station.
		ReadZone dock_door = new ReadZone(dockdoor_reader);
		ReadZone weigh_station = new ReadZone(weighstation_reader);

		// Create the subscribers, which will monitor the defined read zones and
		// send out events based on certain criteria.
		NorthwindDockDoorReadZoneSubscriber dock_door_subscriber = new NorthwindDockDoorReadZoneSubscriber(
				this, "dock_door");
		NorthwindWeighStationReadZoneSubscriber weigh_station_subscriber = new NorthwindWeighStationReadZoneSubscriber(
				this, "weigh_station");
		// Add the subscribers to the subscriber list. This allows the
		// subscribers to be deleted when needed.
		this.subscriberList.add(dock_door_subscriber);
		this.subscriberList.add(weigh_station_subscriber);

		// Start listening to the subscribers we have created. All tags sent
		// back by the readers are now being monitored, and all events generated
		// from those tags will go into Esper.
		this.readZoneMonitoringService.subscribe(dock_door_subscriber,
				dock_door, this.dockdoor_timeout, TimeUnit.SECONDS);
		this.readZoneMonitoringService.subscribe(weigh_station_subscriber,
				weigh_station, this.weighstation_timeout, TimeUnit.SECONDS);

		// These statements insert all TagReadEvents from the given ReadZones
		// into esper.
		String insert_dockdoor_all = EsperUtil.buildInsertStatement(
				"dock_door", dock_door);
		String insert_weighstation_all = EsperUtil.buildInsertStatement(
				"weigh_station", weigh_station);
		addStatement(insert_dockdoor_all);
		addStatement(insert_weighstation_all);

		// These statements monitor the dock door and weigh station and makes
		// sure tags don't stay on too long. The amount of time a tag can be
		// seen before generating an event are controlled by the
		// "dockdoor_dwelltime" and "weighstation_dwelltime" variables.
		StatementAwareUpdateListener dockDoorTagOnTooLongListener = new StatementAwareUpdateListener() {
			@Override
			public void update(EventBean[] arg0, EventBean[] arg1,
					EPStatement arg2, EPServiceProvider arg3) {
				if (arg0 != null) {
					TagReadEvent tag = (TagReadEvent) arg0[0]
							.get("dockarrived.tag");
					System.out.println("TAG SEEN TOO LONG ON DOCK DOOR: "
							+ tag.getTag().getID());
				}

			}
		};
		addStatement("select dockarrived.tag from pattern "
				+ "[every dockarrived=DockDoorArrivedEvent-> "
				+ "timer:interval(" + this.dockdoor_dwelltime
				+ ") and not DockDoorDepartedEvent"
				+ "(tag.tag.ID=dockarrived.tag.tag.ID)]",
				dockDoorTagOnTooLongListener);
		StatementAwareUpdateListener weighStationTagOnTooLongListener = new StatementAwareUpdateListener() {
			@Override
			public void update(EventBean[] arg0, EventBean[] arg1,
					EPStatement arg2, EPServiceProvider arg3) {
				if (arg0 != null) {
					TagReadEvent tag = (TagReadEvent) arg0[0]
							.get("weigharrived.tag");
					System.out.println("TAG SEEN TOO LONG ON WEIGH STATION: "
							+ tag.getTag().getID());
				}

			}
		};
		addStatement("select weigharrived.tag from pattern "
				+ "[every weigharrived=WeighStationArrivedEvent-> "
				+ "timer:interval(" + this.weighstation_dwelltime
				+ ") and not WeighStationDepartedEvent"
				+ "(tag.tag.ID=weigharrived.tag.tag.ID)]",
				weighStationTagOnTooLongListener);

		// This monitor detects when a tag moves in the correct order: from dock
		// door to weigh station. The amount of time it would take these
		// statements to time out is given by the
		// "dockdoor_to_weighstation_timer" variables and the
		// "weighstation_to_dockdoor_timer" variables.
		StatementAwareUpdateListener dockDoorToWeighStationListener = new StatementAwareUpdateListener() {
			@Override
			public void update(EventBean[] arg0, EventBean[] arg1,
					EPStatement arg2, EPServiceProvider arg3) {
				if (arg0 != null) {
					TagReadEvent tag = (TagReadEvent) arg0[0]
							.get("dock2weigh.tag");
					System.out.println("Tag moved from dock door to weigh "
							+ "station: " + tag.getTag().getFormattedID());
				}
			}
		};
		// Dock door followed by weigh station (what should happen)
		addStatement("select dock2weigh.tag from pattern "
				+ "[every dock2weigh=DockDoorArrivedEvent-> "
				+ "WeighStationArrivedEvent"
				+ "(tag.tag.ID=dock2weigh.tag.tag.ID) " + "where timer:within("
				+ this.dockdoor_to_weighstation_timer + ")]",
				dockDoorToWeighStationListener);

		StatementAwareUpdateListener backwardsListener = new StatementAwareUpdateListener() {
			@Override
			public void update(EventBean[] arg0, EventBean[] arg1,
					EPStatement arg2, EPServiceProvider arg3) {
				if (arg0 != null) {
					TagReadEvent tag = (TagReadEvent) arg0[0]
							.get("weigh2dock.tag");
					System.out.println("Tag moved backwards: "
							+ tag.getTag().getFormattedID());
				}
			}
		};
		// Weigh station followed by dock door (going backwards, should not
		// happen)
		addStatement("select weigh2dock.tag from pattern "
				+ "[every weigh2dock=WeighStationArrivedEvent-> "
				+ "DockDoorArrivedEvent"
				+ "(tag.tag.ID=weigh2dock.tag.tag.ID) " + "where timer:within("
				+ this.weighstation_to_dockdoor_timer + ")]", backwardsListener);

		// This listener will happen if a tag arrives at the weigh station which
		// has not been seen at the dock door (or it took too long getting from
		// the weigh station to the dock door and the statement timed out).
		StatementAwareUpdateListener dockDoorSkipListener = new StatementAwareUpdateListener() {
			@Override
			public void update(EventBean[] arg0, EventBean[] arg1,
					EPStatement arg2, EPServiceProvider arg3) {
				if (arg0 != null) {
					TagReadEvent tag = (TagReadEvent) arg0[0]
							.get("skipdocktag.tag");
					System.out.println("Tag skipped dock: "
							+ tag.getTag().getFormattedID());
				}
			}
		};
		addStatement("insert into dockdoorpre select * from DockDoorArrivedEvent");
		addStatement(
				"select skipdocktag.tag from WeighStationArrivedEvent as skipdocktag "
						+ "where not exists (select * from dockdoorpre.std:unique(tag) "
						+ "as d where skipdocktag.tag.tag.ID = d.tag.tag.ID)",
				dockDoorSkipListener);

	}

	/**
	 * Called when stopping
	 */
	@Override
	public void _stop() {
		for (ReadZoneSubscriber e : this.subscriberList) {
			this.readZoneMonitoringService.unsubscribe(e);
		}
	}

	/**
	 * Put an event on Esper.
	 * 
	 * @param event
	 */
	public void sendNorthwindEvent(Object event) {
		this.sendEvent(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.app.api.AbstractRifidiApp#initialize()
	 */
	@Override
	public void initialize() {
		// The initialize method is called first when the plugin is starting up.
		// This is a good time to load in any variables you will need from
		// external sources like properties files.

		this.dockdoor_reader = getProperty("DockDoorReader", null);
		this.weighstation_reader = getProperty("WeighStationReader", null);
		this.dockdoor_dwelltime = getProperty("DockDoorDwellTime", null);
		this.weighstation_dwelltime = getProperty("WeighStationDwellTime", null);
		this.dockdoor_timeout = Float.parseFloat(getProperty("DockDoorTimeout",
				null));
		this.weighstation_timeout = Float.parseFloat(getProperty(
				"WeighStationTimeout", null));
		this.dockdoor_to_weighstation_timer = getProperty(
				"DockDoorToWeighStationTimer", null);
		this.weighstation_to_dockdoor_timer = getProperty(
				"WeighStationToDockDoorTimer", null);
	}

	/**
	 * Called by spring
	 * 
	 * @param rzms
	 */
	public void setReadZoneMonitoringService(ReadZoneMonitoringService rzms) {
		this.readZoneMonitoringService = rzms;
	}

}