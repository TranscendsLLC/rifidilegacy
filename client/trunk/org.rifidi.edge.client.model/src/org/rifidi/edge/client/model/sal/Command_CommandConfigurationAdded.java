/*
 * Command_CommandConfigurationAdded.java
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

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.databinding.observable.map.ObservableMap;
import org.rifidi.edge.api.CommandConfigurationAddedNotification;
import org.rifidi.edge.api.CommandConfigurationDTO;
import org.rifidi.edge.client.model.sal.commands.RemoteEdgeServerCommand;
import org.rifidi.edge.client.model.sal.commands.RequestExecuterSingleton;
import org.rifidi.edge.core.rmi.client.commandconfigurationstub.CCGetCommandConfiguration;
import org.rifidi.edge.core.rmi.client.commandconfigurationstub.CCServerDescription;
import org.rifidi.rmi.proxycache.exceptions.AuthenticationException;
import org.rifidi.rmi.proxycache.exceptions.ServerUnavailable;

/**
 * This is a command that is executed when a new command configuration is added
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 */
public class Command_CommandConfigurationAdded implements
		RemoteEdgeServerCommand {

	/** The map of command configurations */
	private ObservableMap commandConfigurations;
	/** CommandConfiguraiotnFactories */
	private ObservableMap commandConfigFactories;
	/** The ID of the command configuration */
	private String commandConfigurationID;
	/** The DTO of the CommandConfiguration */
	private CommandConfigurationDTO dto;
	/** The server description of the RMI Command stub */
	private CCServerDescription serverDescription;
	/** A command to run if there is a problem */
	private Command_Disconnect disconnectCommand;
	/** The logger for this class */
	private Log logger = LogFactory
			.getLog(Command_CommandConfigurationAdded.class);

	/**
	 * Constructor.
	 * 
	 * @param server
	 * @param notification
	 */
	public Command_CommandConfigurationAdded(RemoteEdgeServer server,
			CommandConfigurationAddedNotification notification) {
		this.commandConfigurations = server.commandConfigurations;
		this.commandConfigurationID = notification.getCommandConfigurationID();
		this.serverDescription = server.getCCServerDescription();
		this.disconnectCommand = new Command_Disconnect(server);
		this.commandConfigFactories = server.commandConfigFactories;
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
		CCGetCommandConfiguration getConfiguration = new CCGetCommandConfiguration(
				serverDescription, commandConfigurationID);
		try {
			dto = getConfiguration.makeCall();
		} catch (ServerUnavailable e) {
			logger.error("Error while making getCommandConfiguration call", e);
			RequestExecuterSingleton.getInstance().scheduleRequest(
					disconnectCommand);
		} catch (AuthenticationException e) {
			logger.warn("Authentication Exception ", e);
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
		if (dto != null) {
			Collection<RemoteCommandConfigFactory> factories = (Collection<RemoteCommandConfigFactory>) this.commandConfigFactories
					.values();
			for (RemoteCommandConfigFactory factory : factories) {
				if (factory.getCommandConfigFactoryID().equals(
						dto.getCommandConfigFactoryID())) {
					this.commandConfigurations.put(this.commandConfigurationID,
							new RemoteCommandConfiguration(dto, factory));
				}
			}

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
		return "COMMAND_CONFIGURATION_ADDED";
	}

}
