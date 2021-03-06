/*
 * RS_CreateReader.java
 * 
 * Created:     July 8th, 2009
 * Project:       Rifidi Edge Server - A middleware platform for RFID applications
 *                    http://www.rifidi.org
 *                    http://rifidi.sourceforge.net
 * Copyright:   Pramari LLC and the Rifidi Project
 * License:      The software in this package is published under the terms of the EPL License
 *                    A copy of the license is included in this distribution under Rifidi-License.txt 
 */
package org.rifidi.edge.core.rmi.client.readerconfigurationstub;

import javax.management.AttributeList;

import org.rifidi.edge.api.SensorManagerService;
import org.rifidi.rmi.proxycache.cache.AbstractRMICommandObject;

/**
 * This call creates a new Reader on the server.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 */
public class RS_CreateReader extends
		AbstractRMICommandObject<String, RuntimeException> {

	/** The supplied readerConfigurationID to use */
	private String readerFactoryID;
	/** The attributes to set on the new reader configuration */
	private AttributeList attributes;

	/**
	 * Constructor
	 * 
	 * @param serverDescription
	 *            The server description to use
	 * @param readerFactoryID
	 *            The ID of the ReaderFactory to use (i.e. the kind of reader to
	 *            create)
	 * @param readerProperties
	 *            the properties to set on the new reader. Properties of the
	 *            reader not included in this list will have their default
	 *            values. reader Properties may contains no Attributes inside,
	 *            but it may not be null
	 */
	public RS_CreateReader(RS_ServerDescription serverDescription,
			String readerFactoryID, AttributeList readerProperties) {
		super(serverDescription);
		this.readerFactoryID = readerFactoryID;
		this.attributes = readerProperties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.rmi.proxycache.cache.AbstractRMICommandObject#performRemoteCall
	 * (java.lang.Object)
	 */
	@Override
	protected String performRemoteCall(Object remoteObject)
			throws RuntimeException {
		SensorManagerService stub = (SensorManagerService) remoteObject;
		stub.createReader(readerFactoryID, attributes);
		//TODO: this might not be clean
		return "";
	}

}
