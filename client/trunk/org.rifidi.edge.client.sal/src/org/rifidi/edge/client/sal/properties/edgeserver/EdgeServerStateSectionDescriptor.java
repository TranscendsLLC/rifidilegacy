/*
 * EdgeServerStateSectionDescriptor.java
 * 
 * Created:     July 8th, 2009
 * Project:       Rifidi Edge Server - A middleware platform for RFID applications
 *                    http://www.rifidi.org
 *                    http://rifidi.sourceforge.net
 * Copyright:   Pramari LLC and the Rifidi Project
 * License:      The software in this package is published under the terms of the EPL License
 *                    A copy of the license is included in this distribution under Rifidi-License.txt 
 */
package org.rifidi.edge.client.sal.properties.edgeserver;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.tabbed.AbstractSectionDescriptor;
import org.eclipse.ui.views.properties.tabbed.ISection;
import org.rifidi.edge.client.model.sal.RemoteEdgeServer;

/**
 * A TabbedProperties Section Descriptor for displaying the connection state of
 * an edge server
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 */
public class EdgeServerStateSectionDescriptor extends AbstractSectionDescriptor {

	/** The ID of the Edge Server State Section Descriptor */
	public static final String ID = "org.rifidi.edge.client.sal.props.edgeserver.state";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.tabbed.ISectionDescriptor#getId()
	 */
	@Override
	public String getId() {
		return ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.tabbed.ISectionDescriptor#getSectionClass
	 * ()
	 */
	@Override
	public ISection getSectionClass() {
		return new EdgeServerStateSection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.tabbed.ISectionDescriptor#getTargetTab()
	 */
	@Override
	public String getTargetTab() {
		return EdgeServerTabDescriptor.ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.views.properties.tabbed.AbstractSectionDescriptor#
	 * getInputTypes()
	 */
	@Override
	public List getInputTypes() {
		List<String> type = new ArrayList<String>();
		type.add(RemoteEdgeServer.class.getName());
		return type;
	}

}
