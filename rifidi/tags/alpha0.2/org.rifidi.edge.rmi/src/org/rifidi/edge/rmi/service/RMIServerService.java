/*
 *  RMIServerService.java
 *
 *  Created:	Jun 19, 2008
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.edge.rmi.service;

public interface RMIServerService {

	/**
	 * Start the RMIServer
	 */
	public void start();

	/**
	 * Stop the RMIServer
	 */
	public void stop();

}
