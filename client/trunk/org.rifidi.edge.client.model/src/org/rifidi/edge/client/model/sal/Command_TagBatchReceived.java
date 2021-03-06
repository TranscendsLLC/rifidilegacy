/*
 * Command_TagBatchReceived.java
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

import java.util.HashSet;
import java.util.Set;

import org.rifidi.edge.api.TagBatch;
import org.rifidi.edge.api.TagDTO;
import org.rifidi.edge.client.model.sal.commands.RemoteEdgeServerCommand;

/**
 * A handler method for when a TagBatch notification is sent
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 */
public class Command_TagBatchReceived implements RemoteEdgeServerCommand {

	/** The edge server model object */
	private RemoteEdgeServer edgeServer;
	/** The Tags that were seeen */
	private TagBatch batch;
	/** The set of tag model objects */
	private Set<RemoteTag> tags = null;

	/**
	 * Constructor
	 * 
	 * @param server
	 *            The edge server model object
	 * @param batch
	 *            The batch of tags that were read
	 */
	public Command_TagBatchReceived(RemoteEdgeServer server, TagBatch batch) {
		this.edgeServer = server;
		this.batch = batch;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.client.model.sal.commands.RemoteEdgeServerCommand#execute
	 * ()
	 */
	@Override
	public void execute() {
		tags = new HashSet<RemoteTag>();
		for (TagDTO dto : batch.getTags()) {
			tags.add(new RemoteTag(dto, edgeServer.tdtEngine));
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.edge.client.model.sal.commands.RemoteEdgeServerCommand#
	 * executeEclipse()
	 */
	@Override
	public void executeEclipse() {
		RemoteReader reader = (RemoteReader) edgeServer.getRemoteReaders().get(
				batch.getReaderID());
		if (reader != null) {
			// remove all tags that are not in the new set
			reader.getTags().retainAll(tags);
			// add all the new tags
			reader.getTags().addAll(tags);
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
		return "TAG_BATCH_RECEIVED";
	}

}
