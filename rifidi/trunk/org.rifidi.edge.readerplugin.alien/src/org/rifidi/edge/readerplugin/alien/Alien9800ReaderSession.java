/*
 * 
 * Alien9800ReaderSession.java
 *  
 * Created:     July 8th, 2009
 * Project:       Rifidi Edge Server - A middleware platform for RFID applications
 *                   http://www.rifidi.org
 *                   http://rifidi.sourceforge.net
 * Copyright:   Pramari LLC and the Rifidi Project
 * License:      The software in this package is published under the terms of the GPL License
 *                   A copy of the license is included in this distribution under RifidiEdge-License.txt 
 */
/**
 * 
 */
package org.rifidi.edge.readerplugin.alien;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.edge.api.SessionStatus;
import org.rifidi.edge.core.sensors.base.AbstractIPSensorSession;
import org.rifidi.edge.core.sensors.base.AbstractSensor;
import org.rifidi.edge.core.sensors.base.threads.MessageParsingStrategyFactory;
import org.rifidi.edge.core.sensors.commands.AbstractCommandConfiguration;
import org.rifidi.edge.core.sensors.commands.Command;
import org.rifidi.edge.core.sensors.messages.ByteMessage;
import org.rifidi.edge.core.services.notification.NotifierService;
import org.springframework.jms.core.JmsTemplate;

/**
 * A session that connects to an Alien9800Reader
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class Alien9800ReaderSession extends AbstractIPSensorSession {
	/** Logger for this class. */
	private static final Log logger = LogFactory
			.getLog(Alien9800ReaderSession.class);
	/** Set to true if the session is destroied. */
	private AtomicBoolean destroied=new AtomicBoolean(false);
	/** Username for connecting to the reader. */
	private final String username;
	/** Password for connnecting to the reader. */
	private final String password;
	/** Each command needs to be terminated with a newline. */
	public static final String NEWLINE = "\n";
	/** Welcome string. */
	public static final String WELCOME = "Alien";
	/** Service used to send out notifications */
	private volatile NotifierService notifierService;
	/** The FACTORY_ID of the reader this session belongs to */
	private final String readerID;
	/** The message parser that parses messages from the socket */
	private volatile AlienMessageParsingStrategy messageParser;
	/** Supplied by spring. */
	private final Set<AbstractCommandConfiguration<?>> commands;
	/**
	 * You can put this in front of a Alien command for terse output to come
	 * back to you, making things faster and easier to parse.
	 */
	public static final String PROMPT_SUPPRESS = "\1";

	/** Set antenna sequence */
	public static final String ANTENNA_SEQUENCE_COMMAND = "AntennaSequence";

	/**
	 * COMMANDS
	 */
	public static final String COMMAND_HEARTBEAT_ADDRESS = "heartbeataddress";
	public static final String COMMAND_ANTENNA_SEQUENCE = "antennasequence";
	public static final String COMMAND_MAX_ANTENNA = "maxantenna";
	public static final String COMMAND_PASSWORD = "password";
	public static final String COMMAND_READERNAME = "ReaderName";
	public static final String COMMAND_READERNUMBER = "ReaderNumber";
	public static final String COMMAND_READER_TYPE = "ReaderType";
	public static final String COMMAND_READER_VERSION = "ReaderVersion";
	public static final String COMMAND_RF_ATTENUATION = "RFAttenuation";
	public static final String COMMAND_EXTERNAL_INPUT = "ExternalInput";
	public static final String COMMAND_USERNAME = "username";
	public static final String COMMAND_UPTIME = "Uptime";
	public static final String COMMAND_TAG_LIST = "taglist";
	public static final String COMMAND_TAG_TYPE = "tagtype";
	public static final String COMMAND_TAG_LIST_FORMAT = "TagListFormat";
	public static final String COMMAND_TAG_LIST_CUSTOM_FORMAT = "TagListCustomFormat";
	public static final String COMMAND_EXTERNAL_OUTPUT = "ExternalOutput";
	public static final String COMMAND_INVERT_EXTERNAL_INPUT = "InvertExternalInput";
	public static final String COMMAND_INVERT_EXTERNAL_OUTPUT = "InvertExternalOutput";
	public static final String COOMMAND_COMMAND_PORT = "CommandPort";
	public static final String COMMAND_DHCP = "DHCP";
	public static final String COMMAND_PERSIST_TIME = "PersistTime";
	public static final String COMMAND_DNS = "DNS";
	public static final String COMMAND_GATEWAY = "Gateway";
	public static final String COMMAND_HEARTBEAT_COUNT = "HeartbeatCount";
	public static final String COMMAND_HEARTBEAT_PORT = "HeartbeatPort";
	public static final String COMMAND_HEARTBEAT_TIME = "HeartbeatTime";
	public static final String COMMAND_IPADDRESS = "IPAddress";
	public static final String COMMAND_MAC_ADDRESS = "MACAddress";
	public static final String COMMAND_NETMASK = "Netmask";
	public static final String COMMAND_NETWORK_TIMEOUT = "NetworkTimeout";
	public static final String COMMAND_TIME = "Time";
	public static final String COMMAND_TIME_SERVER = "TimeServer";
	public static final String COMMAND_TIME_ZONE = "TimeZone";

	/**
	 * 
	 * Constructor
	 * 
	 * @param sensor
	 * @param id
	 *            The FACTORY_ID of the session
	 * @param host
	 *            The IP to connect to
	 * @param port
	 *            The port to connect to
	 * @param reconnectionInterval
	 *            The wait time between reconnect attempts
	 * @param maxConAttempts
	 *            The maximum number of times to try to connect
	 * @param username
	 *            The Alien username
	 * @param password
	 *            The Alien password
	 * @param destination
	 *            The JMS destination for tags
	 * @param template
	 *            The JSM template for tags
	 * @param notifierService
	 *            The service for sending client notifications
	 * @param readerID
	 *            The FACTORY_ID of the reader that created this session
	 * @param commands
	 *            A thread safe set containing all available commands
	 */
	public Alien9800ReaderSession(AbstractSensor<?> sensor, String id,
			String host, int port, int reconnectionInterval,
			int maxConAttempts, String username, String password,
			JmsTemplate template, NotifierService notifierService,
			String readerID, Set<AbstractCommandConfiguration<?>> commands) {
		super(sensor, id, host, port, reconnectionInterval, maxConAttempts,
				template.getDefaultDestination(), template, commands);
		this.commands = commands;
		this.username = username;
		this.password = password;
		this.notifierService = notifierService;
		this.readerID = readerID;
		this.messageParser = new AlienMessageParsingStrategy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.edge.core.sensors.base.AbstractIPSensorSession#
	 * getMessageParsingStrategyFactory()
	 */
	@Override
	public MessageParsingStrategyFactory getMessageParsingStrategyFactory() {
		return new AlienMessageParsingStrategyFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.readers.impl.AbstractIPReaderSession#onConnect()
	 */
	@Override
	public boolean onConnect() throws IOException {
		logger.debug("getting the welcome response");
		String welcome = new String(receiveMessage().message);
		logger.debug("welcome message: " + welcome);

		if (welcome == null
				|| !welcome.contains(Alien9800ReaderSession.WELCOME)) {
			logger.fatal("SensorSession is not an alien sensorSession: "
					+ welcome);
			return false;
		} else if (welcome.toLowerCase().contains("busy")) {
			logger.error("SensorSession is busy: " + welcome);
			return false;
		} else {
			logger.debug("SensorSession is an alien.  Hoo-ray!");
		}

		logger.debug("sending username");
		sendMessage(new ByteMessage((Alien9800ReaderSession.PROMPT_SUPPRESS
				+ username + Alien9800ReaderSession.NEWLINE).getBytes()));
		logger.debug("getting the username response");
		receiveMessage();
		logger.debug("sending the password. ");
		sendMessage(new ByteMessage((Alien9800ReaderSession.PROMPT_SUPPRESS
				+ password + Alien9800ReaderSession.NEWLINE).getBytes()));
		logger.debug("recieving the password response");
		String authMessage = new String(receiveMessage().message);
		if (authMessage.contains("Invalid")) {
			logger.warn("Incorrect Password");
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.readers.impl.AbstractReaderSession#setStatus(org
	 * .rifidi.edge.core.api.SessionStatus)
	 */
	@Override
	protected void setStatus(SessionStatus status) {
		super.setStatus(status);

		// TODO: Remove this once we have aspectJ
		notifierService.sessionStatusChanged(this.readerID, this.getID(),
				status);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.readers.impl.AbstractReaderSession#killComand(java
	 * .lang.Integer)
	 */
	@Override
	public void killComand(Integer id) {
		super.killComand(id);

		// TODO: Remove this once we have aspectJ
		notifierService.jobDeleted(this.readerID, this.getID(), id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.sensors.SensorSession#submit(java.lang.String,
	 * long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public Integer submit(String commandID, long interval, TimeUnit unit) {
		Integer retVal = super.submit(commandID, interval, unit);
		// TODO: Remove this once we have aspectJ
		try {
			notifierService.jobSubmitted(this.readerID, this.getID(), retVal,
					commandID);
		} catch (Exception e) {
			// make sure the notification doesn't cause this method to exit
			// under any circumstances
			logger.error(e);
		}
		return retVal;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.sensors.SensorSession#submit(java.lang.String)
	 */
	@Override
	public void submit(String commandID) {
		super.submit(commandID);
		try {
			notifierService.jobSubmitted(this.readerID, this.getID(), -1,
					commandID);
		} catch (Exception e) {
			// make sure the notification doesn't cause this method to exit
			// under any circumstances
			logger.error(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.sensors.SensorSession#submit(org.rifidi.edge.core
	 * .sensors.commands.Command)
	 */
	@Override
	public void submit(Command command) {
		super.submit(command);
	}
}
