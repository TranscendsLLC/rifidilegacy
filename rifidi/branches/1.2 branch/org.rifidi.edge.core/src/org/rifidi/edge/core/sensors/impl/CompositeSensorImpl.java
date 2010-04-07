/*
 * 
 * SensorImpl.java
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
package org.rifidi.edge.core.sensors.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.edge.core.sensors.CompositeSensor;
import org.rifidi.edge.core.sensors.Sensor;
import org.rifidi.edge.core.sensors.exceptions.ImmutableException;
import org.rifidi.edge.core.sensors.exceptions.InUseException;

/**
 * @author Jochen Mader - jochen@pramari.com
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class CompositeSensorImpl extends SensorImpl implements CompositeSensor {

	/** Children of this node. */
	private final Set<Sensor> childNodes;
	private final static Log logger = LogFactory
			.getLog(CompositeSensorImpl.class);

	/**
	 * @param name
	 * @param childNodes
	 * @param immutable
	 */
	public CompositeSensorImpl(final String name,
			final Collection<Sensor> childNodes, final Boolean immutable) {
		super(name, immutable);
		this.childNodes = new CopyOnWriteArraySet<Sensor>();
		for (Sensor childSensor : childNodes) {
			try {
				addChild(childSensor);
			} catch (ImmutableException e) {
				logger.error(e.getMessage());
			} catch (InUseException e) {
				logger.error(e.getMessage());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wtf.Sensor#getChildren()
	 */
	public Set<String> getChildren() {
		Set<String> ret = new HashSet<String>();
		for (Sensor sensor : childNodes) {
			ret.add(sensor.getName());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.sensors.SensorUpdate#addChild(wtf.impl.SensorImpl )
	 */
	public void addChild(final Sensor child) throws ImmutableException,
			InUseException {
		if (isImmutable()) {
			throw new ImmutableException(getName() + " is immutable.");
		}
		if (isInUse()) {
			throw new InUseException(getName() + " is in use.");
		}
		childNodes.add(child);
		// receive updates (e.g. tag events) from this sensor
		child.addReceiver(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.sensors.CompositeSensorUpdate#removeChild(org
	 * .rifidi.edge.core.sensors.UpdateableSensor)
	 */
	@Override
	public void removeChild(Sensor child) throws ImmutableException,
			InUseException {
		if (isImmutable()) {
			throw new ImmutableException(getName() + " is immutable.");
		}
		if (isInUse()) {
			throw new InUseException(getName() + " is in use.");
		}
		// stop listening for tag events
		child.removeReceiver(this);
		childNodes.remove(child);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.sensors.SensorUpdate#removeChildren(java.util
	 * .Collection)
	 */
	public void removeChildren(final Collection<Sensor> children)
			throws ImmutableException, InUseException {
		if (isImmutable()) {
			throw new ImmutableException(getName() + " is immutable.");
		}
		if (isInUse()) {
			throw new InUseException(getName() + " is in use.");
		}
		for (Sensor childSensor : children) {
			//stop listening for tag events
			childSensor.removeReceiver(this);
			childNodes.remove(childSensor);
		}
	}

}
