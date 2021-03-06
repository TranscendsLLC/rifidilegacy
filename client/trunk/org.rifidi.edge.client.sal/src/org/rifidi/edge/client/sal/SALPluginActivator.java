/*
 * SALPluginActivator.java
 * 
 * Created:     July 8th, 2009
 * Project:       Rifidi Edge Server - A middleware platform for RFID applications
 *                    http://www.rifidi.org
 *                    http://rifidi.sourceforge.net
 * Copyright:   Pramari LLC and the Rifidi Project
 * License:      The software in this package is published under the terms of the EPL License
 *                    A copy of the license is included in this distribution under Rifidi-License.txt 
 */
package org.rifidi.edge.client.sal;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.rifidi.edge.client.sal.modelmanager.SALModelService;
import org.rifidi.edge.client.sal.modelmanager.SALModelServiceImpl;

/**
 * The activator class controls the plug-in life cycle
 */
public class SALPluginActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.rifidi.edge.client.sal";

	// The shared instance
	private static SALPluginActivator plugin;

	public static final String IMAGE_FOLDER = "folder";

	public static final String IMAGE_COG = "cog";

	public static final String IMAGE_TAG = "tag";

	private SALModelService salModelService;

	/**
	 * The constructor
	 */
	public SALPluginActivator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Get the model.
	 * 
	 * @return
	 */
	public SALModelService getSalModelService() {
		if(salModelService==null){
			salModelService=new SALModelServiceImpl();
		}
		return salModelService;
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static SALPluginActivator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path.
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#initializeImageRegistry(org.eclipse
	 * .jface.resource.ImageRegistry)
	 */
	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		super.initializeImageRegistry(reg);
		reg.put(IMAGE_FOLDER, getImageDescriptor("icons/folder.png"));
		reg.put(IMAGE_COG, getImageDescriptor("icons/cog.png"));
		reg.put(IMAGE_TAG, getImageDescriptor("icons/newspaper.png"));
	}
}
