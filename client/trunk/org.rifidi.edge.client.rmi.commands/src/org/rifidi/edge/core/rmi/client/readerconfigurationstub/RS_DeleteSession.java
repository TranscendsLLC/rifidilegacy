/*
 * RS_DeleteSession.java
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

import org.rifidi.edge.api.SensorManagerService;
import org.rifidi.rmi.proxycache.cache.AbstractRMICommandObject;

/**
 * This command deletes a Reader Session.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 */
public class RS_DeleteSession extends
		AbstractRMICommandObject<Object, RuntimeException> {

	/** The ID of the reader */
	private String readerID;
	/** The ID of the session to delete */
	private String sessionID;

	/**
	 * 
	 * @param serverDescription
	 *            The server description to use
	 * @param readerID
	 *            The ID of the reader which has the session
	 * @param sessionID
	 *            The ID of the session to delete
	 */
	public RS_DeleteSession(RS_ServerDescription serverDescription,
			String readerID, String sessionID) {
		super(serverDescription);
		this.readerID = readerID;
		this.sessionID = sessionID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.rmi.proxycache.cache.AbstractRMICommandObject#performRemoteCall
	 * (java.lang.Object)
	 */
	@Override
	protected Object performRemoteCall(Object remoteObject)
			throws RuntimeException {
		SensorManagerService stub = (SensorManagerService) remoteObject;
		stub.deleteSession(readerID, sessionID);
		return null;
	}

}