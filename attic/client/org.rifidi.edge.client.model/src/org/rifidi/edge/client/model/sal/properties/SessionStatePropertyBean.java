/*
 * SessionStatePropertyBean.java
 * 
 * Created:     July 8th, 2009
 * Project:       Rifidi Edge Server - A middleware platform for RFID applications
 *                    http://www.rifidi.org
 *                    http://rifidi.sourceforge.net
 * Copyright:   Pramari LLC and the Rifidi Project
 * License:      The software in this package is published under the terms of the EPL License
 *                    A copy of the license is included in this distribution under Rifidi-License.txt 
 */

package org.rifidi.edge.client.model.sal.properties;

import org.rifidi.edge.api.SessionStatus;

/**
 * A class that wraps a bean property for property change events for when a
 * session changes state
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 */
public class SessionStatePropertyBean {

	/** The name of the state property */
	public static final String SESSION_STATUS_PROPERTY = "org.rifidi.edge.client.model.sal.RemoteSession.status";
	/** The id of the reader */
	private String readerID;
	/** The ID of the session */
	private String sessionID;
	/** The new status */
	private SessionStatus status;
	/** HashString for this class */
	private String hashString;

	/**
	 * Constructor.  
	 * 
	 * @param readerID
	 * @param sessionID
	 * @param status
	 */
	public SessionStatePropertyBean(String readerID, String sessionID,
			SessionStatus status) {
		this.readerID = readerID;
		this.sessionID = sessionID;
		this.status = status;
		hashString = readerID + sessionID + status.toString();
	}

	/**
	 * @return the readerID
	 */
	public String getReaderID() {
		return readerID;
	}

	/**
	 * @return the sessionID
	 */
	public String getSessionID() {
		return sessionID;
	}

	/**
	 * @return the status
	 */
	public SessionStatus getStatus() {
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SessionStatePropertyBean) {
			return hashString
					.equals(((SessionStatePropertyBean) obj).hashString);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return hashString.hashCode();
	}

}
