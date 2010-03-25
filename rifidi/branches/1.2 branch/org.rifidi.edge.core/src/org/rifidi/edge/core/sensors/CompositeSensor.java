/*
 * 
 * CompositeSensor.java
 *  
 * Created:     July 8th, 2009
 * Project:       Rifidi Edge Server - A middleware platform for RFID applications
 *                   http://www.rifidi.org
 *                   http://rifidi.sourceforge.net
 * Copyright:   Pramari LLC and the Rifidi Project
 * License:      The software in this package is published under the terms of the GPL License
 *                   A copy of the license is included in this distribution under RifidiEdge-License.txt 
 */
package org.rifidi.edge.core.sensors;

import java.util.Collection;
import java.util.Set;

import org.rifidi.edge.core.sensors.exceptions.ImmutableException;
import org.rifidi.edge.core.sensors.exceptions.InUseException;

public interface CompositeSensor extends Sensor{

	/**
	 * Get the names of child sensors this sensor has.
	 * 
	 * @return
	 */
	Set<String> getChildren();
	
	/**
	 * Add a new child sensor.
	 * 
	 * @param child
	 * @throws ImmutableException
	 * @throws InUseException
	 */
	void addChild(final Sensor child) throws ImmutableException,
			InUseException;

	/**
	 * Remove a child sensor.
	 * 
	 * @param child
	 * @throws ImmutableException
	 * @throws InUseException
	 */
	void removeChild(final Sensor child) throws ImmutableException,
			InUseException;

	/**
	 * Remove several child sensors.
	 * 
	 * @param children
	 * @throws ImmutableException
	 * @throws InUseException
	 */
	void removeChildren(final Collection<Sensor> children)
			throws ImmutableException, InUseException;

}