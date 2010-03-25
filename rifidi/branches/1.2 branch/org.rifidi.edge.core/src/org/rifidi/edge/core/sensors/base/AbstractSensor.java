/*
 * 
 * AbstractSensor.java
 *  
 * Created:     July 8th, 2009
 * Project:       Rifidi Edge Server - A middleware platform for RFID applications
 *                   http://www.rifidi.org
 *                   http://rifidi.sourceforge.net
 * Copyright:   Pramari LLC and the Rifidi Project
 * License:      The software in this package is published under the terms of the GPL License
 *                   A copy of the license is included in this distribution under RifidiEdge-License.txt 
 */
/**
 * 
 */
package org.rifidi.edge.core.sensors.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.AttributeList;

import org.osgi.framework.BundleContext;
import org.rifidi.edge.api.rmi.dto.ReaderDTO;
import org.rifidi.edge.api.rmi.dto.SessionDTO;
import org.rifidi.edge.core.configuration.Configuration;
import org.rifidi.edge.core.configuration.ConfigurationType;
import org.rifidi.edge.core.configuration.services.RifidiService;
import org.rifidi.edge.core.exceptions.CannotCreateSessionException;
import org.rifidi.edge.core.sensors.Sensor;
import org.rifidi.edge.core.sensors.SensorSession;
import org.rifidi.edge.core.sensors.commands.AbstractCommandConfiguration;
import org.rifidi.edge.core.sensors.exceptions.CannotDestroySensorException;
import org.rifidi.edge.core.sensors.exceptions.DuplicateSubscriptionException;
import org.rifidi.edge.core.sensors.exceptions.ImmutableException;
import org.rifidi.edge.core.sensors.exceptions.InUseException;
import org.rifidi.edge.core.sensors.exceptions.NotSubscribedException;
import org.rifidi.edge.core.sensors.impl.SensorImpl;
import org.rifidi.edge.core.services.esper.internal.EsperEventContainer;
import org.rifidi.edge.core.services.notification.data.ReadCycle;

/**
 * A reader creates and manages instances of sessions. The reader itself holds
 * all configuration parameters and creates the sessions according to these. The
 * returned sensorSession objects are immutable and if some parameters of the
 * factory change after a session has been created, the created session will
 * retain its configuration until it is destroyed and a new one is created
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public abstract class AbstractSensor<T extends SensorSession> extends
		RifidiService implements Sensor {

	private final SensorImpl sensorImpl;

	/**
	 * This constructor is only for CGLIB. DO NOT OVERWRITE!
	 */
	public AbstractSensor() {
		super();
		sensorImpl = new SensorImpl(getID(), true);
	}

	/**
	 * Create a new sensor session.
	 * 
	 * @return id of the created session
	 * @exception CannotCreateSessionException
	 *                - if the session cannot be created
	 */
	abstract public String createSensorSession()
			throws CannotCreateSessionException;

	/**
	 * This method is called when a sensor session is being created from a DTO,
	 * such as restoring the session from persistance.
	 * 
	 * @param sessionDTO
	 * @return the ID of the session
	 * @exception CannotCreateSessionException
	 *                if the session cannot be created
	 */
	abstract public String createSensorSession(SessionDTO sessionDTO)
			throws CannotCreateSessionException;

	/**
	 * Get all currently created reader sessions. The Key is the ID of the
	 * session, and the value is the actual session
	 * 
	 * @return
	 */
	abstract public Map<String, SensorSession> getSensorSessions();

	/**
	 * Destroy a sesnor session.
	 * 
	 * @param session
	 */
	abstract public void destroySensorSession(String id)
			throws CannotDestroySensorException;

	/**
	 * Send properties that have been modified to the physical reader
	 */
	abstract public void applyPropertyChanges();

	/**
	 * This method returns a display name for clients to use. This way readers
	 * can have user-friendly names (such as "Dock Door") in a client.
	 * 
	 * @return The display name of the Sensor
	 */
	abstract protected String getDisplayName();

	/**
	 * Notifier the sensor that a command configuration has disappeared.
	 * 
	 * @param commandConfiguration
	 * @param properties
	 */
	abstract public void unbindCommandConfiguration(
			AbstractCommandConfiguration<?> commandConfiguration,
			Map<?, ?> properties);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.sensors.Sensor#addReceiver(org.rifidi.edge.core.
	 * sensors.Sensor)
	 */
	@Override
	public void addReceiver(Sensor receiver) {
		sensorImpl.addReceiver(receiver);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.sensors.Sensor#getName()
	 */
	@Override
	public String getName() {
		return sensorImpl.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.sensors.Sensor#isImmutable()
	 */
	@Override
	public Boolean isImmutable() {
		return sensorImpl.isImmutable();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.sensors.Sensor#isInUse()
	 */
	@Override
	public Boolean isInUse() {
		return sensorImpl.isInUse();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.sensors.Sensor#receive(java.lang.Object)
	 */
	@Override
	public EsperEventContainer receive(Object object)
			throws NotSubscribedException {
		return sensorImpl.receive(object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.sensors.Sensor#removeReceiver(org.rifidi.edge.core
	 * .sensors.Sensor)
	 */
	@Override
	public void removeReceiver(Sensor receiver) {
		sensorImpl.removeReceiver(receiver);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.sensors.Sensor#send(org.rifidi.edge.core.services
	 * .notification.data.ReadCycle)
	 */
	@Override
	public void send(ReadCycle cycle) {
		sensorImpl.send(cycle);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.sensors.Sensor#sendEvent(java.lang.Object)
	 */
	@Override
	public void sendEvent(Object event) {
		sensorImpl.sendEvent(event);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.sensors.Sensor#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) throws ImmutableException, InUseException {
		sensorImpl.setName(name);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.sensors.Sensor#subscribe(java.lang.Object)
	 */
	@Override
	public void subscribe(Object object) throws DuplicateSubscriptionException {
		sensorImpl.subscribe(object);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.sensors.Sensor#unsubscribe(java.lang.Object)
	 */
	@Override
	public void unsubscribe(Object object) throws NotSubscribedException {
		sensorImpl.unsubscribe(object);

	}

	/***
	 * This method returns the Data Transfer Object for this Reader
	 * 
	 * @param config
	 *            The Configuration Object for this AbstractSensor
	 * @return A data transfer object for this reader
	 */
	public ReaderDTO getDTO(final Configuration config) {
		String readerID = config.getServiceID();
		String factoryID = config.getFactoryID();
		AttributeList attrs = config.getAttributes(config.getAttributeNames());
		List<SessionDTO> sessionDTOs = new ArrayList<SessionDTO>();
		for (SensorSession s : this.getSensorSessions().values()) {
			sessionDTOs.add(s.getDTO());
		}
		ReaderDTO dto = new ReaderDTO(readerID, factoryID, attrs, sessionDTOs,
				getDisplayName());
		return dto;
	}

	/**
	 * Register the reader to OSGi.
	 * 
	 * Registers the service with the following params:
	 * 
	 * <pre>
	 * type - "reader"
	 * reader - the reader type supplied as an argument
	 * serviceid - the service ID of the reader
	 * </pre>
	 * 
	 * @param context
	 *            The Bundlecontext to use
	 * @param readerType
	 *            The Type of reader to register it as
	 */
	public void register(BundleContext context, String readerType) {
		register(context, readerType, new HashMap<String, String>());
	}

	/**
	 * Register the reader to OSGi.
	 * 
	 * Registers the service with the following params:
	 * 
	 * <pre>
	 * type - "reader"
	 * reader - the reader type supplied as an argument
	 * serviceid - the service ID of the reader
	 * </pre>
	 * 
	 * @param context
	 *            The Bundlecontext to use
	 * @param readerType
	 *            The Type of reader to register it as
	 * @param filterParams
	 *            Any additional OSGi filter params to use when registering the
	 *            service
	 */
	public void register(BundleContext context, String readerType,
			Map<String, String> filterParams) {
		Map<String, String> parms = new HashMap<String, String>();
		parms.put("type", ConfigurationType.READER.toString());
		parms.put("reader", readerType);
		parms.put("serviceid", getID());
		if (filterParams != null)
			parms.putAll(filterParams);
		Set<String> interfaces = new HashSet<String>();
		interfaces.add(AbstractSensor.class.getName());
		register(context, interfaces, parms);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.configuration.RifidiService#destroy()
	 */
	@Override
	protected void destroy() {
		unregister();
		sensorImpl.destroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Sensor: " + getID();
	}
}
