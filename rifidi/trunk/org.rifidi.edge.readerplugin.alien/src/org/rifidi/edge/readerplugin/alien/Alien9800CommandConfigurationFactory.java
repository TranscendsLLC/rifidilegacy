/**
 * 
 */
package org.rifidi.edge.readerplugin.alien;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.MBeanInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.edge.core.configuration.ConfigurationType;
import org.rifidi.edge.core.configuration.impl.AbstractCommandConfigurationFactory;
import org.rifidi.edge.core.sensors.commands.AbstractCommandConfiguration;
import org.rifidi.edge.core.sensors.commands.Command;
import org.rifidi.edge.readerplugin.alien.commands.AlienAutonomousModeCommandConfiguration;
import org.rifidi.edge.readerplugin.alien.commands.AlienAutonomousModeStopCommandConfiguration;
import org.rifidi.edge.readerplugin.alien.commands.AlienGetTagListCommandConfiguration;
import org.springframework.jms.core.JmsTemplate;

/**
 * The CommandConfigurationFactory for an Alien9800 reader
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class Alien9800CommandConfigurationFactory extends
		AbstractCommandConfigurationFactory<AbstractAlien9800Command> {
	/** Logger for this class. */
	private static final Log logger = LogFactory
			.getLog(Alien9800CommandConfigurationFactory.class);
	/**
	 * A map between IDs of command configutions factories and the classes of
	 * the commandconfigurations factories
	 */
	private final Map<String, Class<?>> factoryIdToClass;
	private final Map<String, AbstractCommandConfiguration<AbstractAlien9800Command>> commandIdToConfiguration;
	/** The gloabaly unique name for this factory */
	public static final String uniqueID = "Alien9800CommandConfigurationFactory";

	private final Map<String, MBeanInfo> commandIdToBlueprint;

	private volatile JmsTemplate template;

	/**
	 * Constructor
	 */
	public Alien9800CommandConfigurationFactory() {
		super();
		factoryIdToClass = new HashMap<String, Class<?>>();
		commandIdToConfiguration = new ConcurrentHashMap<String, AbstractCommandConfiguration<AbstractAlien9800Command>>();
		factoryIdToClass.put(AlienGetTagListCommandConfiguration.name,
				AlienGetTagListCommandConfiguration.class);
		factoryIdToClass.put(AlienAutonomousModeCommandConfiguration.name,
				AlienAutonomousModeCommandConfiguration.class);
		factoryIdToClass.put(AlienAutonomousModeStopCommandConfiguration.name,
				AlienAutonomousModeStopCommandConfiguration.class);
		commandIdToBlueprint = new HashMap<String, MBeanInfo>();
		commandIdToBlueprint.put(AlienGetTagListCommandConfiguration.name,
				(new AlienGetTagListCommandConfiguration()).getMBeanInfo());
		commandIdToBlueprint.put(AlienAutonomousModeCommandConfiguration.name,
				(new AlienAutonomousModeCommandConfiguration()).getMBeanInfo());
		commandIdToBlueprint.put(
				AlienAutonomousModeStopCommandConfiguration.name,
				(new AlienAutonomousModeStopCommandConfiguration())
						.getMBeanInfo());
	}

	/**
	 * @param template
	 *            the template to set
	 */
	public void setTemplate(JmsTemplate template) {
		this.template = template;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.configuration.ServiceFactory#createInstance(java
	 * .lang.String, java.lang.String)
	 */
	@Override
	public void createInstance(String factoryID, String serviceID) {
		// TODO: polish
		try {
			assert (factoryIdToClass.containsKey(factoryID));
			AbstractCommandConfiguration<AbstractAlien9800Command> instance = (AbstractCommandConfiguration<AbstractAlien9800Command>) factoryIdToClass
					.get(factoryID).newInstance();
			instance.setID(serviceID);
			Set<String> interfaces = new HashSet<String>();
			interfaces.add(AbstractCommandConfiguration.class
					.getCanonicalName());
			commandIdToConfiguration.put(serviceID, instance);
			Map<String, String> parms = new HashMap<String, String>();
			parms.put("type", ConfigurationType.COMMAND.toString());
			instance.register(getContext(), interfaces, parms);
		} catch (InstantiationException e) {
			logger.error(e);
		} catch (IllegalAccessException e) {
			logger.error(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.configuration.impl.AbstractCommandConfigurationFactory
	 * #getCommandInstance(java.lang.String, java.lang.String)
	 */
	@Override
	public Command getCommandInstance(String commandID, String readerID) {
		if(commandIdToConfiguration.get(commandID)!=null){
			return commandIdToConfiguration.get(commandID).getCommand(readerID);	
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.configuration.ServiceFactory#getFactoryIDs()
	 */
	@Override
	public List<String> getFactoryIDs() {
		return new ArrayList<String>(factoryIdToClass.keySet());
	}

	/**
	 * @return the FACTORY_ID of the factory these commands work with
	 */
	@Override
	public String getReaderFactoryID() {
		return Alien9800ReaderFactory.FACTORY_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.configuration.ServiceFactory#getServiceDescription
	 * (java.lang.String)
	 */
	@Override
	public MBeanInfo getServiceDescription(String factoryID) {
		return (MBeanInfo) commandIdToBlueprint.get(factoryID).clone();
	}

}
