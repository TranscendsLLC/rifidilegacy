/*
 * 
 * LLRPGetTagListCommandConfigurationFactory.java
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
package org.rifidi.edge.adapter.llrp.commands;

import javax.management.MBeanInfo;

import org.rifidi.edge.adapter.llrp.LLRPReaderFactory;
import org.rifidi.edge.configuration.AbstractCommandConfigurationFactory;
import org.rifidi.edge.configuration.AnnotationMBeanInfoStrategy;
import org.rifidi.edge.sensors.AbstractCommandConfiguration;

/**
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class LLRPGetTagListCommandConfigurationFactory
		extends
		AbstractCommandConfigurationFactory<AbstractCommandConfiguration<?>> {
	/** Name of the command. */
	public static final String name = "LLRP-Poll";
	/** Mbeaninfo for this class. */
	public static final MBeanInfo mbeaninfo;
	static {
		AnnotationMBeanInfoStrategy strategy = new AnnotationMBeanInfoStrategy();
		mbeaninfo = strategy
				.getMBeanInfo(LLRPGetTagListCommandConfiguration.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.configuration.impl.AbstractCommandConfigurationFactory
	 * #getReaderFactoryID()
	 */
	@Override
	public String getReaderFactoryID() {
		return LLRPReaderFactory.FACTORY_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.configuration.ServiceFactory#createInstance(java
	 * .lang.String)
	 */
	@Override
	public void createInstance(String serviceID) {
		LLRPGetTagListCommandConfiguration commandConfig = new LLRPGetTagListCommandConfiguration();
		commandConfig.setID(serviceID);
		commandConfig.register(getContext(), LLRPReaderFactory.FACTORY_ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.configuration.ServiceFactory#getFactoryID()
	 */
	@Override
	public String getFactoryID() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.configuration.ServiceFactory#getServiceDescription
	 * (java.lang.String)
	 */
	@Override
	public MBeanInfo getServiceDescription(String factoryID) {
		return (MBeanInfo) mbeaninfo.clone();
	}

	/* (non-Javadoc)
	 * @see org.rifidi.edge.configuration.impl.AbstractCommandConfigurationFactory#getCommandDescription()
	 */
	@Override
	public String getCommandDescription() {
		return "Poll the LLRP reader for its tags. "
		+ "For monitoring of the read zone, first use the LLRP Configure command to configure the " +
				"LLRP reader for Poll Mode. Then submit " +
				"this command for recurring execution.";
	}

	/* (non-Javadoc)
	 * @see org.rifidi.edge.configuration.impl.AbstractCommandConfigurationFactory#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return "LLRP Poll";
	}
	
	

}
