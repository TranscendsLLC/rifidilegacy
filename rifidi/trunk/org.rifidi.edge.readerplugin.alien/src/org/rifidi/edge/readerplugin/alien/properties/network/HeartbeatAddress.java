/*
 *  Gateway.java
 *
 *  Created:	September 16th, 2008
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.edge.readerplugin.alien.properties.network;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.dynamicswtforms.xml.annotaions.Form;
import org.rifidi.dynamicswtforms.xml.annotaions.FormElement;
import org.rifidi.dynamicswtforms.xml.constants.FormElementType;
import org.rifidi.edge.core.api.communication.Connection;
import org.rifidi.edge.core.api.messageQueue.MessageQueue;
import org.rifidi.edge.core.api.readerplugin.commands.CommandConfiguration;
import org.rifidi.edge.core.api.readerplugin.property.api.Property;
import org.rifidi.edge.readerplugin.alien.properties.AlienResponse;

/**
 * 
 * 
 * @author Matthew Dean
 */
@Form(name = HeartbeatAddress.NAME, formElements = { @FormElement(type = FormElementType.STRING, elementName = HeartbeatAddress.DATA, editable = true, defaultValue = "0", displayName = HeartbeatAddress.DISPLAY) })
public class HeartbeatAddress implements Property {

	private static final String NAME = "HeartbeatAddress";

	private static final String DATA = "HeartbeatAddress_Data";

	private static final String DISPLAY = "Heartbeat Address";

	private static final Log logger = LogFactory.getLog(Gateway.class);

	private static final String command = "HeartbeatAddress";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.readerplugin.property.Property#getProperty(org.rifidi
	 * .edge.core.communication.Connection,
	 * org.rifidi.edge.core.messageQueue.MessageQueue, org.w3c.dom.Element)
	 */
	@Override
	public CommandConfiguration getProperty(Connection connection,
			MessageQueue errorQueue, CommandConfiguration propertyConfig) {
		AlienResponse response = new AlienResponse();
		String responseString = null;
		try {
			connection.sendMessage("\1get " + command + "\n");

			responseString = (String) connection.receiveMessage();

		} catch (IOException e) {
			logger.debug("IOException");
		}
		response.setResponseMessage(responseString);
		return response.formulateResponse(NAME, DATA);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.readerplugin.property.Property#setProperty(org.rifidi
	 * .edge.core.communication.Connection,
	 * org.rifidi.edge.core.messageQueue.MessageQueue, org.w3c.dom.Element)
	 */
	@Override
	public CommandConfiguration setProperty(Connection connection,
			MessageQueue errorQueue, CommandConfiguration propertyConfig) {

		String comm = "\1set " + command + " = "
				+ propertyConfig.getArgValue(DATA) + "\n";
		AlienResponse response = new AlienResponse();
		String responseString = null;
		try {
			connection.sendMessage(comm);
			responseString = (String) connection.receiveMessage();

		} catch (IOException e) {
			logger.debug("IOException");
		}
		response.setResponseMessage(responseString);
		return response.formulateResponse(NAME, DATA);
	}
}
