/*
 * AbstractChoiceWidget.java
 * 
 * Created:     July 8th, 2009
 * Project:       Rifidi Edge Server - A middleware platform for RFID applications
 *                    http://www.rifidi.org
 *                    http://rifidi.sourceforge.net
 * Copyright:   Pramari LLC and the Rifidi Project
 * License:      The software in this package is published under the terms of the EPL License
 *                    A copy of the license is included in this distribution under Rifidi-License.txt 
 */

package org.rifidi.edge.client.mbean.ui.widgets.abstractwidgets;

import java.util.List;

import javax.management.Attribute;

import org.eclipse.swt.widgets.Combo;
import org.rifidi.edge.client.mbean.ui.widgets.data.ChoiceWidgetData;

/**
 * This is a convenience abstract class to use for widgets who want to display a
 * choice widget as a combo control. Implementors will still need to build the
 * control
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 */
public abstract class AbstractChoiceWidget<T extends ChoiceWidgetData> extends
		AbstractWidget<T> {

	/**
	 * The combo control
	 */
	protected Combo combo;

	/**
	 * Constructor.  
	 * 
	 * @param data
	 */
	public AbstractChoiceWidget(T data) {
		super(data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.dynamicswtforms.ui.widgets.DynamicSWTFormWidget#disable()
	 */
	@Override
	public void disable() {
		this.combo.setEnabled(false);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.dynamicswtforms.ui.widgets.DynamicSWTFormWidget#enable()
	 */
	@Override
	public void enable() {
		if (data.isEditable()) {
			this.combo.setEnabled(true);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.dynamicswtforms.ui.widgets.DynamicSWTFormWidget#getValue()
	 */
	@Override
	public Attribute getAttribute() {
		try {
			return new Attribute(getElementName(), combo.getItem(combo
					.getSelectionIndex()));
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.dynamicswtforms.ui.widgets.DynamicSWTFormWidget#setValue(java
	 * .lang.String)
	 */
	@Override
	public String setValue(Attribute value) {
		if (combo != null) {
			List<String> choices = data.possibleChoices();

			int index = choices.indexOf(value.getValue());
			if (index == -1) {
				return value + " is not a valid choice";
			}
			combo.select(choices.indexOf(value));
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.dynamicswtforms.ui.widgets.DynamicSWTFormWidget#validate()
	 */
	@Override
	public String validate() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.client.mbean.ui.widgets.abstractwidgets.AbstractWidget
	 * #dispose()
	 */
	@Override
	public void dispose() {
		if (combo != null) {
			combo.dispose();
		}
	}

}
