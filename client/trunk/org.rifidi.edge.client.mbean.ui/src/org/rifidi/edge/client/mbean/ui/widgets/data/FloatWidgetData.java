/*
 * FloatWidgetData.java
 * 
 * Created:     July 8th, 2009
 * Project:       Rifidi Edge Server - A middleware platform for RFID applications
 *                    http://www.rifidi.org
 *                    http://rifidi.sourceforge.net
 * Copyright:   Pramari LLC and the Rifidi Project
 * License:      The software in this package is published under the terms of the EPL License
 *                    A copy of the license is included in this distribution under Rifidi-License.txt 
 */

package org.rifidi.edge.client.mbean.ui.widgets.data;

import javax.management.JMX;
import javax.management.MBeanAttributeInfo;

/**
 * This is the concrete class for data used to construct a Float Widget
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 */
public class FloatWidgetData extends AbstractWidgetData {

	/**
	 * Constructor.  
	 * 
	 * @param element
	 */	
	public FloatWidgetData(MBeanAttributeInfo element) {
		super(element);
	}

	/**
	 * @return The maximum value of this widget
	 */
	public Float maxValue() {
		Object max = element.getDescriptor().getFieldValue(JMX.MAX_VALUE_FIELD);
		if(max==null){
			return Float.MAX_VALUE;
		}else{
			return (Float)max;
		}
	}

	/**
	 * @return The minimum value for this widget
	 */
	public Float minValue() {
		Object min = element.getDescriptor().getFieldValue(JMX.MIN_VALUE_FIELD);
		if(min==null){
			return Float.MAX_VALUE;
		}else{
			return (Float)min;
		}
	}

	/**
	 * TODO: returns two decimal places for now.  Need to do this in a better way 
	 *
	 * @return The number of decimal places to display
	 */
	public int getNumDecimalPlaces() {
		return 2;
	}
	
	/* (non-Javadoc)
	 * @see org.rifidi.edge.client.mbean.ui.widgets.data.AbstractWidgetData#getDefaultValue()
	 */
	@Override
	public Float getDefaultValue() {
		Object defVal = element.getDescriptor().getFieldValue(JMX.DEFAULT_VALUE_FIELD);
		if(defVal==null){
			return 0f;
		}else{
			return (Float)defVal;
		}
	}

}
