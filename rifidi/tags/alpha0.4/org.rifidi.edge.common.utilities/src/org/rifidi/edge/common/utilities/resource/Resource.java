/*
 *  Resource.java
 *
 *  Created:	Jun 27, 2008
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.edge.common.utilities.resource;

import java.io.InputStream;



/**
 * @author Jerry Maine - jerry@pramari.com
 *
 */
public class Resource {
	
	
	/**
	 * Helper method to get a resource located at the same Java
	 * class path directory as any supplied Class object. 
	 * @param klass To help find the location of the resource.
	 * @param resource Name of resource (file) to get.
	 * @return A input stream representing that resource. Null if it could not be found.
	 */
	public static InputStream getResource(Class<?> klass, String resource) {
		String path = klass.getName().replace(klass.getSimpleName(), "");
		path = path.replace(".", "/");

		path = path + resource;
		
		System.out.println(path);
		InputStream xml = klass.getClassLoader().getResourceAsStream(path);
	
		return xml;
	}
}
