/*
 * CCGetCommandConfigFactory.java
 * 
 * Created:     July 8th, 2009
 * Project:       Rifidi Edge Server - A middleware platform for RFID applications
 *                    http://www.rifidi.org
 *                    http://rifidi.sourceforge.net
 * Copyright:   Pramari LLC and the Rifidi Project
 * License:      The software in this package is published under the terms of the EPL License
 *                    A copy of the license is included in this distribution under Rifidi-License.txt 
 */
package org.rifidi.edge.core.rmi.client.commandconfigurationstub;

import org.rifidi.edge.api.rmi.dto.CommandConfigFactoryDTO;
import org.rifidi.edge.api.rmi.services.CommandManagerService;
import org.rifidi.rmi.proxycache.cache.AbstractRMICommandObject;

/**
 * This remote call gets a CommandConfigFactoryDTO for a given commandFactoryID.
 * It returns null if a commandFactory with the given ID is not avaialable
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 */
public class CCGetCommandConfigFactory extends
		AbstractRMICommandObject<CommandConfigFactoryDTO, RuntimeException> {

	private String commandFactoryID;

	/**
	 * Constructor.
	 * 
	 * @param serverDescription
	 */
	public CCGetCommandConfigFactory(CCServerDescription serverDescription,
			String commandFactoryID) {
		super(serverDescription);
		this.commandFactoryID = commandFactoryID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.rmi.proxycache.cache.AbstractRMICommandObject#performRemoteCall
	 * (java.lang.Object)
	 */
	@Override
	protected CommandConfigFactoryDTO performRemoteCall(Object remoteObject)
			throws RuntimeException {
		CommandManagerService stub = (CommandManagerService) remoteObject;
		return stub.getCommandConfigFactory(commandFactoryID);
	}

}
