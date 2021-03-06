/**
 * 
 */
package org.rifidi.edge.readerplugin.alien;
//TODO: Comments
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import javax.jms.Destination;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.configuration.annotations.JMXMBean;
import org.rifidi.configuration.annotations.Operation;
import org.rifidi.configuration.annotations.Property;
import org.rifidi.configuration.annotations.PropertyType;
import org.rifidi.edge.core.notifications.NotifierService;
import org.rifidi.edge.core.readers.AbstractReader;
import org.rifidi.edge.core.readers.ReaderSession;
import org.rifidi.edge.readerplugin.alien.commandobject.AlienCommandObjectWrapper;
import org.rifidi.edge.readerplugin.alien.commandobject.AlienGetCommandObject;
import org.rifidi.edge.readerplugin.alien.commandobject.AlienSetCommandObject;
import org.rifidi.edge.readerplugin.alien.commands.internal.AlienPropertyCommand;
import org.springframework.jms.core.JmsTemplate;

/**
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
@JMXMBean
public class Alien9800Reader extends AbstractReader<Alien9800ReaderSession> {
	/** Logger for this class. */
	private Log logger = LogFactory.getLog(Alien9800Reader.class);
	/** The only session an alien reader allows. */
	private Alien9800ReaderSession session;
	/** A queue for putting commands to be executed next */
	private LinkedBlockingQueue<AlienCommandObjectWrapper> propCommandsToBeExecuted;
	/** A hashmap containing all the properties for this reader */
	private ConcurrentHashMap<String, String> readerProperties;
	/** IP address of the readerSession. */
	private String ipAddress = AlienReaderDefaultValues.ipAddress;
	/** Port to connect to. */
	private Integer port = Integer.parseInt(AlienReaderDefaultValues.port);
	/** Username for the telnet interface. */
	private String username = AlienReaderDefaultValues.username;
	/** Password for the telnet interface. */
	private String password = AlienReaderDefaultValues.password;
	/** Time between two connection attempts. */
	private Long reconnectionInterval = Long
			.parseLong(AlienReaderDefaultValues.reconnectionInterval);
	/** Number of connection attempts before a connection goes into fail state. */
	private Integer maxNumConnectionAttempts = Integer
			.parseInt(AlienReaderDefaultValues.maxNumConnectionAttempts);
	/** JMS destination. */
	private Destination destination;
	/** Spring JMS template */
	private JmsTemplate template;
	/** The ID of the session */
	private int sessionID = 0;
	/** A wrapper containing the service to send jms notifications */
	private NotifierServiceWrapper notifyServiceWrapper;

	/**
	 * READER PROPERTIES - SETTABE, SET ON CONNECTION
	 */
	public static final String PROP_RF_ATTENUATION = "RFAttenuation";
	public static final String PROP_EXTERNAL_OUTPUT = "externalOutput";
	public static final String PROP_INVERT_EXTERNAL_INPUT = "invertExternalInput";
	public static final String PROP_INVERT_EXTERNAL_OUTPUT = "inverExternalOutput";

	/**
	 * READER PROPERTIES - SETTABLE, INITIALIZED BY AQUIRE READER PROPERTIES
	 */
	public static final String PROP_COMMAND_PORT = "commandPort";
	public static final String PROP_DHCP = "dhcp";
	public static final String PROP_DNS = "dns";
	public static final String PROP_GATEWAY = "gateway";
	private String heartbeat_address;
	private String heartbeat_count;
	private String heartbeat_port;
	private String heartbeat_time;
	private String netmask;
	private String network_timeout;
	private String readername;
	private String time;
	private String time_server;
	private String time_zone;
	/** The unique number of the readerSession */
	public static final String PROP_READER_NUMBER = "readerNumber";

	/**
	 * READER PROPERTIES - READ ONLY, INITIALZIED BY AQUIRE READER PROPERTIES
	 */
	/** MAC Address of the readerSession */
	public static final String PROP_MAC_ADDRESS = "macAddress";
	/** Maximum number of antennas supported */
	public static final String PROP_MAX_ANTENNA = "maxAntenna";
	/** The type of the alien readerSession */
	public static final String PROP_READER_TYPE = "readerType";
	/** The version of the readerSession */
	public static final String PROP_READER_VERSION = "readerVersion";
	/** GPO value */
	public static final String PROP_EXTERNAL_INPUT = "externalinput";
	/** Uptime of the readerSession */
	public static final String PROP_UPTIME = "uptime";

	/**
	 * Constructor.
	 */
	public Alien9800Reader() {
		readerProperties = new ConcurrentHashMap<String, String>();
		readerProperties.put(PROP_READER_NUMBER, "0");
		readerProperties.put(PROP_READER_VERSION, "Unavailable");
		readerProperties.put(PROP_READER_TYPE, "Unavailable");
		readerProperties.put(PROP_MAC_ADDRESS, "Unavailable");
		readerProperties.put(PROP_MAX_ANTENNA, "0");
		readerProperties.put(PROP_EXTERNAL_INPUT, "0");
		readerProperties.put(PROP_UPTIME, "0");
		readerProperties.put(PROP_EXTERNAL_OUTPUT, "0");
		readerProperties.put(PROP_RF_ATTENUATION, "0");
		readerProperties.put(PROP_INVERT_EXTERNAL_OUTPUT, "0");
		readerProperties.put(PROP_INVERT_EXTERNAL_INPUT, "0");
		

		propCommandsToBeExecuted = new LinkedBlockingQueue<AlienCommandObjectWrapper>();
		propCommandsToBeExecuted.add(new AlienCommandObjectWrapper(
				PROP_READER_NUMBER, new AlienGetCommandObject(
						Alien9800ReaderSession.COMMAND_READERNUMBER)));
		propCommandsToBeExecuted.add(new AlienCommandObjectWrapper(
				PROP_READER_VERSION, new AlienGetCommandObject(
						Alien9800ReaderSession.COMMAND_READER_VERSION)));
		propCommandsToBeExecuted.add(new AlienCommandObjectWrapper(
				PROP_READER_TYPE, new AlienGetCommandObject(
						Alien9800ReaderSession.COMMAND_READER_TYPE)));
		propCommandsToBeExecuted.add(new AlienCommandObjectWrapper(
				PROP_MAC_ADDRESS, new AlienGetCommandObject(
						Alien9800ReaderSession.COMMAND_MAC_ADDRESS)));
		propCommandsToBeExecuted.add(new AlienCommandObjectWrapper(
				PROP_MAX_ANTENNA, new AlienGetCommandObject(
						Alien9800ReaderSession.COMMAND_MAX_ANTENNA)));
		propCommandsToBeExecuted.add(new AlienCommandObjectWrapper(
				PROP_EXTERNAL_INPUT, new AlienGetCommandObject(
						Alien9800ReaderSession.COMMAND_EXTERNAL_INPUT)));
		propCommandsToBeExecuted
				.add(new AlienCommandObjectWrapper(PROP_UPTIME,
						new AlienGetCommandObject(
								Alien9800ReaderSession.COMMAND_UPTIME)));

		propCommandsToBeExecuted.add(new AlienCommandObjectWrapper(
				PROP_EXTERNAL_OUTPUT, new AlienSetCommandObject(
						Alien9800ReaderSession.COMMAND_EXTERNAL_OUTPUT,
						this.readerProperties.get(PROP_EXTERNAL_OUTPUT))));

		propCommandsToBeExecuted.add(new AlienCommandObjectWrapper(
				PROP_RF_ATTENUATION, new AlienSetCommandObject(
						Alien9800ReaderSession.COMMAND_RF_ATTENUATION,
						this.readerProperties.get(PROP_RF_ATTENUATION))));

		logger.debug("New instance of Alien9800Reader created.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.readers.AbstractReader#createReaderSession()
	 */
	@Override
	public synchronized ReaderSession createReaderSession() {
		if (session == null) {
			sessionID++;
			session = new Alien9800ReaderSession(Integer.toString(sessionID),
					ipAddress, port, (int) (long) reconnectionInterval,
					maxNumConnectionAttempts, username, password, destination,
					template, notifyServiceWrapper, this.getID());

			// TODO: remove this once we get AspectJ in here!
			NotifierService service = notifyServiceWrapper.getNotifierService();
			if (service != null) {
				service.addSessionEvent(this.getID(), Integer
						.toString(sessionID));
			}
			return session;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.configuration.RifidiService#destroy()
	 */
	@Override
	public void destroy() {
		super.unregister();
		destroyReaderSession(this.session);
	}

	/**
	 * @param destination
	 *            the destination to set
	 */
	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	/**
	 * @param template
	 *            the template to set
	 */
	public void setTemplate(JmsTemplate template) {
		this.template = template;
	}

	/***
	 * 
	 * @param wrapper
	 *            The JMS Notifier to set
	 */
	public void setNotifiyServiceWrapper(NotifierServiceWrapper wrapper) {
		this.notifyServiceWrapper = wrapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.readers.AbstractReader#destroyReaderSession(org.
	 * rifidi.edge.core.readers.ReaderSession)
	 */
	@Override
	public void destroyReaderSession(ReaderSession session) {
		if (session != null) {
			for (Integer id : session.currentCommands().keySet()) {
				session.killComand(id);
			}
			this.session.disconnect();
			this.session = null;

			// TODO: remove this once we get AspectJ in here!
			NotifierService service = notifyServiceWrapper.getNotifierService();
			if (service != null) {
				service.removeSessionEvent(this.getID(), Integer
						.toString(sessionID));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.readers.AbstractReader#getReaderSessions()
	 */
	@Override
	public Map<String, ReaderSession> getReaderSessions() {
		Map<String, ReaderSession> ret = new HashMap<String, ReaderSession>();
		if (session != null) {
			ret.put(session.getID(), session);
		}
		return ret;
	}

	/*
	 * JMX PROPERTY GETTER/SETTERS
	 */

	/**
	 * @return the ipAddress
	 */
	@Property(displayName = "IP Address", description = "Address of the readerSession.", writable = true, category = "Connection", defaultValue = AlienReaderDefaultValues.ipAddress, orderValue = 0)
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress
	 *            the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the port
	 */
	@Property(displayName = "Port", description = "Port of the readerSession.", writable = true, type = PropertyType.PT_INTEGER, category = "Connection", defaultValue = AlienReaderDefaultValues.port, orderValue = 1, minValue = "0", maxValue = "65535")
	public Integer getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(Integer port) {
		this.port = port;
	}

	/**
	 * @return the username
	 */
	@Property(displayName = "Username", description = "Username for logging into the readerSession.", writable = true, category = "Connection", defaultValue = AlienReaderDefaultValues.username, orderValue = 2)
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	@Property(displayName = "Password", description = "Password for logging into the readerSession.", writable = true, category = "Connection", defaultValue = AlienReaderDefaultValues.password, orderValue = 3)
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the reconnectionInterval
	 */
	@Property(displayName = "Reconnection Interval", description = "Time between two connection attempts (ms).", writable = true, type = PropertyType.PT_LONG, category = "Connection", defaultValue = AlienReaderDefaultValues.reconnectionInterval, orderValue = 4, minValue = "0")
	public Long getReconnectionInterval() {
		return reconnectionInterval;
	}

	/**
	 * @param reconnectionInterval
	 *            the reconnectionInterval to set
	 */
	public void setReconnectionInterval(Long reconnectionInterval) {
		this.reconnectionInterval = reconnectionInterval;
	}

	/**
	 * @return the maxNumConnectionAttempts
	 */
	@Property(displayName = "Maximum Connection Attempts", description = "Number of times to try to connect to the readerSession before the connection is marked as failed.", writable = true, type = PropertyType.PT_INTEGER, category = "Connection", defaultValue = AlienReaderDefaultValues.maxNumConnectionAttempts, orderValue = 5, minValue = "0")
	public Integer getMaxNumConnectionAttempts() {
		return maxNumConnectionAttempts;
	}

	/**
	 * @param maxNumConnectionAttempts
	 *            the maxNumConnectionAttempts to set
	 */
	public void setMaxNumConnectionAttempts(Integer maxNumConnectionAttempts) {
		this.maxNumConnectionAttempts = maxNumConnectionAttempts;
	}

	@Property(displayName = "GPO Output", description = "Ouput of GPO", writable = true, type = PropertyType.PT_INTEGER, minValue = "0", maxValue = "255", category = "GPIO")
	public Integer getExternalOutput() {
		return Integer.parseInt(readerProperties.get(PROP_EXTERNAL_OUTPUT));
	}

	public void setExternalOutput(Integer externalOutput) {
		if (externalOutput >= 0 && externalOutput <= 255) {
			readerProperties.put(PROP_EXTERNAL_OUTPUT, Integer
					.toString(externalOutput));
			propCommandsToBeExecuted.add(new AlienCommandObjectWrapper(
					PROP_EXTERNAL_OUTPUT, new AlienSetCommandObject(
							Alien9800ReaderSession.COMMAND_EXTERNAL_OUTPUT,
							Integer.toString(externalOutput))));
			return;
		}
		logger.warn("ExternalOutput must be an"
				+ " integer between 0 and 255, but was " + externalOutput);
	}

	@Property(displayName = "Invert External Output", description = "Inverts the output of GPO", writable = true, type = PropertyType.PT_INTEGER, minValue = "0", maxValue = "1", category = "GPIO", defaultValue = "0")
	public Integer getInvertExternalOuput() {
		return Integer.parseInt(readerProperties
				.get(PROP_INVERT_EXTERNAL_OUTPUT));
	}

	public void setInvertExternalOutput(Integer invertExternalOutput) {
		if (invertExternalOutput >= 0 && invertExternalOutput <= 1) {
			readerProperties.put(PROP_INVERT_EXTERNAL_OUTPUT, Integer
					.toString(invertExternalOutput));
			propCommandsToBeExecuted
					.add(new AlienCommandObjectWrapper(
							PROP_INVERT_EXTERNAL_OUTPUT,
							new AlienSetCommandObject(
									Alien9800ReaderSession.COMMAND_INVERT_EXTERNAL_OUTPUT,
									Integer.toString(invertExternalOutput))));
			return;
		}
		logger.warn("InvertExternalOutput must be an"
				+ " integer between 0 and 1, but was " + invertExternalOutput);
	}

	@Property(displayName = "Invert External Input", description = "Inverts the input of GPI", writable = true, type = PropertyType.PT_INTEGER, minValue = "0", maxValue = "1", category = "GPIO", defaultValue = "0")
	public Integer getInvertExternalInput() {
		return Integer.parseInt(readerProperties
				.get(PROP_INVERT_EXTERNAL_OUTPUT));
	}

	public void setInvertExternalInput(Integer invertExternalInput) {
		if (invertExternalInput >= 0 && invertExternalInput <= 1) {
			readerProperties.put(PROP_INVERT_EXTERNAL_INPUT, Integer
					.toString(invertExternalInput));
			propCommandsToBeExecuted
					.add(new AlienCommandObjectWrapper(
							PROP_INVERT_EXTERNAL_INPUT,
							new AlienSetCommandObject(
									Alien9800ReaderSession.COMMAND_INVERT_EXTERNAL_INPUT,
									Integer.toString(invertExternalInput))));
			return;
		}
		logger.warn("InvertExternalInput must be an"
				+ " integer between 0 and 1, but was " + invertExternalInput);
	}

	@Property(displayName = "RF Attenuation", description = "RF Attenuation", writable = true, type = PropertyType.PT_INTEGER)
	public Integer getRFAttenuation() {
		return Integer.parseInt(readerProperties.get(PROP_RF_ATTENUATION));
	}

	public void setRFAttenuation(Integer rfAttenuation) {
		if (rfAttenuation >= 0 && rfAttenuation <= 100) {
			readerProperties.put(PROP_RF_ATTENUATION, Integer
					.toString(rfAttenuation));
			propCommandsToBeExecuted.add(new AlienCommandObjectWrapper(
					PROP_RF_ATTENUATION, new AlienSetCommandObject(
							Alien9800ReaderSession.COMMAND_RF_ATTENUATION,
							Integer.toString(rfAttenuation))));
		} else {
			logger.warn("RFAttenuation bust be an integer "
					+ "between 0 and 100,  but was " + rfAttenuation);
		}
	}

	@Property(displayName = "Reader Number", description = "Reader Number", writable = true, type = PropertyType.PT_STRING, category="General")
	public String getReaderNumber() {
		return readerProperties.get(PROP_READER_NUMBER);
	}

	public void setReaderNumber(String readerNumber) {
		readerProperties.put(PROP_READER_NUMBER, readerNumber);
		propCommandsToBeExecuted.add(new AlienCommandObjectWrapper(
				PROP_READER_NUMBER, new AlienSetCommandObject(
						Alien9800ReaderSession.COMMAND_RF_ATTENUATION,
						readerNumber)));
	}

	@Property(displayName = "ReaderSession Version", description = "Version Number of the readerSession", writable = false, category = "General")
	public String getReaderVersion() {
		return (String) readerProperties.get(PROP_READER_VERSION);
	}

	@Property(displayName = "ReaderSession Type", description = "Type of ReaderSession", writable = false)
	public String getReaderType() {
		return (String) readerProperties.get(PROP_READER_TYPE);
	}

	@Property(displayName = "Max Antennas", description = "Maximum number of antennas", writable = false, type = PropertyType.PT_INTEGER, category = "General")
	public Integer getMaxAntennas() {
		return Integer.parseInt(readerProperties.get(PROP_MAX_ANTENNA));
	}

	@Property(displayName = "MAC Address", description = "MAC Address of readerSession", writable = false, category="General")
	public String getMACAddress() {
		return (String) readerProperties.get(PROP_MAC_ADDRESS);
	}

	@Property(displayName = "GPI Input", description = "Input of GPI", writable = false, type = PropertyType.PT_INTEGER, category="GPIO")
	public Integer getExternalInput() {
		return Integer.parseInt(readerProperties.get(PROP_EXTERNAL_INPUT));
	}

	@Property(displayName = "Uptime", description = "Uptime of readerSession", writable = false, type = PropertyType.PT_INTEGER, category="General")
	public Integer getUptime() {
		return Integer.parseInt(readerProperties.get(PROP_UPTIME));
	}

	@Override
	@Operation(description = "Apply all property changes to reader")
	public synchronized void applyPropertyChanges() {
		// TODO: may need to synchnonize the hashmap before I clear it?
		if (session != null) {
			ArrayList<AlienCommandObjectWrapper> commands = new ArrayList<AlienCommandObjectWrapper>();
			this.propCommandsToBeExecuted.drainTo(commands);
			AlienPropertyCommand command = new AlienPropertyCommand("",
					readerProperties, commands);
			session.submit(command);
		}
	}
}
