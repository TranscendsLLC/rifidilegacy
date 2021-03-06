/*
 * Command_SessionRemoved.java
 * 
 * Created:     July 8th, 2009
 * Project:       Rifidi Edge Server - A middleware platform for RFID applications
 *                    http://www.rifidi.org
 *                    http://rifidi.sourceforge.net
 * Copyright:   Pramari LLC and the Rifidi Project
 * License:      The software in this package is published under the terms of the EPL License
 *                    A copy of the license is included in this distribution under Rifidi-License.txt 
 */

package org.rifidi.edge.client.model.sal;

import org.rifidi.edge.api.SessionRemovedNotification;
import org.rifidi.edge.client.model.sal.commands.RemoteEdgeServerCommand;

/**
 * A command that is executed when a session is remvoed
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 */
public class Command_SessionRemoved implements RemoteEdgeServerCommand {

	/** The ID of the session that was removed */
	private String sessionID;
	/** The remote edge server */
	private RemoteEdgeServer server;
	/** The ID of the reader this session belongs to */
	private String readerID;

	/***
	 * Constructor
	 * 
	 * @param server
	 *            The instance of the remote edge server
	 * @param notification
	 *            The JMS notificaiton that was received
	 */
	public Command_SessionRemoved(RemoteEdgeServer server,
			SessionRemovedNotification notification) {
		this.sessionID = notification.getSessionID();
		this.readerID = notification.getReaderID();
		this.server = server;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.client.model.sal.commands.RemoteEdgeServerCommand
	 * #execute()
	 */
	@Override
	public void execute() {
		// do nothing

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.edge.client.model.sal.commands.RemoteEdgeServerCommand#
	 * executeEclipse()
	 */
	@Override
	public void executeEclipse() {

		RemoteReader reader = (RemoteReader) server.getRemoteReaders().get(
				readerID);
		if (reader != null) {
			RemoteSession session = ((RemoteSession) reader.getRemoteSessions()
					.get(sessionID));
			if (session != null) {
				session.getRemoteJobs().clear();
			}
			reader.getRemoteSessions().remove(sessionID);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.client.model.sal.commands.RemoteEdgeServerCommand#getType
	 * ()
	 */
	@Override
	public String getType() {
		return "SESSION_REMOVED";
	}

}
