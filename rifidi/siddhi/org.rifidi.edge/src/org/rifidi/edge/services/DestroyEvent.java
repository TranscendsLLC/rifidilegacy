/*******************************************************************************
 * Copyright (c) 2014 Transcends, LLC.
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU 
 * General Public License as published by the Free Software Foundation; either version 2 of the 
 * License, or (at your option) any later version. This program is distributed in the hope that it will 
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You 
 * should have received a copy of the GNU General Public License along with this program; if not, 
 * write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, 
 * USA. 
 * http://www.gnu.org/licenses/gpl-2.0.html
 *******************************************************************************/
/**
 * 
 */
package org.rifidi.edge.services;

import org.rifidi.edge.util.RifidiEventInterface;

/**
 * @author Jochen Mader - jochen@pramari.com
 *
 */
public class DestroyEvent
		implements RifidiEventInterface {
	/** Name of the associated ec spec. */
	private String name;

	/**
	 * @param name
	 *            name of the associated ec spec
	 */
	public DestroyEvent(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public Object[] getEventAttributes() {
		// TODO Auto-generated method stub
		return new Object[]{name};
	}

	@Override
	public String getEventName() {
		// TODO Auto-generated method stub
		return this.getClass().getSimpleName();
	}
}
