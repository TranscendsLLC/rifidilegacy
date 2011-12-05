/*
 * RS_SubmitCommand.java
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

import java.util.concurrent.TimeUnit;

import org.rifidi.edge.api.CommandSubmissionException;
import org.rifidi.edge.api.SensorManagerService;
import org.rifidi.rmi.proxycache.cache.AbstractRMICommandObject;

/**
 * This command allows you to submit commands to a reader session. If the
 * repeatInterval >0, then the command will be scheduled for repeated
 * executions. It returns the process ID of the submitted job, which can be used
 * to kill the command. It throws a CommandSubmissionException if there was a
 * problem
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 */
public class RS_SubmitCommand extends
		AbstractRMICommandObject<Integer, CommandSubmissionException> {

	/** The ID of the reader */
	private String readerID;
	/** The id of the session to submit the command to */
	private String sessionID;
	/** The ID of the command to submit */
	private String commandID;
	/** The repeat interval */
	private Long repeatInterval;
	/** The timeunit for the repeat interval */
	private TimeUnit timeUnit;

	/**
	 * Constructor
	 * 
	 * @param serverDescription
	 *            The ServerDescription of the stub
	 * @param readerID
	 *            The ID of the reader to submit the command to
	 * @param sessionID
	 *            The Index Number of the session to use
	 * @param commandID
	 *            The ID of the command to submit
	 * @param repeatInterval
	 *            How often the command should be repeated. If the command
	 *            should only execute once, set this value to 0
	 * @param timeUnit
	 *            The TimeUnit used for the repeat interval. Ignored if the
	 *            repeatInterval is 0
	 */
	public RS_SubmitCommand(RS_ServerDescription serverDescription,
			String readerID, String sessionID, String commandID,
			Long repeatInterval, TimeUnit timeUnit) {
		super(serverDescription);
		this.readerID = readerID;
		this.sessionID = sessionID;
		this.commandID = commandID;
		this.repeatInterval = repeatInterval;
		this.timeUnit = timeUnit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.rmi.proxycache.cache.AbstractRMICommandObject#performRemoteCall
	 * (java.lang.Object)
	 */
	@Override
	protected Integer performRemoteCall(Object remoteObject)
			throws CommandSubmissionException {
		SensorManagerService stub = (SensorManagerService) remoteObject;
		stub.submitCommand(readerID, sessionID, commandID, repeatInterval,
				timeUnit);
		// TODO: this might not be clean
		return 0;
	}
}
