/*
 * AbstractStringWidget.java
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.Attribute;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.rifidi.edge.client.mbean.ui.widgets.data.StringWidgetData;

/**
 * This is a convenience abstract class to use for widgets who want to display a
 * string widget as a Text control. Implementors will still need to build the
 * control
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 */
public abstract class AbstractStringWidget<T extends StringWidgetData> extends
		AbstractWidget<T> {

	/** The Text control */
	protected Text text;
	/** The regular expression */
	private Pattern regexPattern;
	/** The regular expression matcher object used for validation */
	private Matcher matcher;
	/** True if the text control has been modified */
	private boolean dirty = false;

	/**
	 * Constructor.
	 * 
	 * @param data
	 */
	public AbstractStringWidget(T data) {
		super(data);
		String regexString = data.getRegex();
		if (regexString == null || regexString.equals("")) {
			regexPattern = Pattern.compile("(.)*");
		} else {
			regexPattern = Pattern.compile(regexString);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.client.mbean.ui.widgets.abstractwidgets.AbstractWidget
	 * #createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		createText(parent);
		initializeText();
		text.setEnabled(data.isEditable());
		addTextListeners();
	}

	/**
	 * Concrete subclasses should use this method to create the text control.
	 * Sublcasses should create a new Text control and assign it to the text
	 * control in this class
	 * 
	 * @param parent
	 */
	protected abstract void createText(Composite parent);

	/**
	 * Concrete subclasses should use this method to initialize the text control
	 */
	protected abstract void initializeText();

	/**
	 * Adds a key listener and a focus listener to listen for changes to the
	 * text control
	 */
	protected void addTextListeners() {
		// notify listeners of a user typing a key
		text.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.character != SWT.CR) {
					dirty = true;
					notifyListenersKeyReleased();
				} else {
					if (dirty == true) {
						dirty = false;
						notifyListenersDataChanged(text.getText());
					}
				}
			}
		});

		text.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (dirty == true) {
					dirty = false;
					notifyListenersDataChanged(text.getText());
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.dynamicswtforms.ui.widgets.DynamicSWTFormWidget#disable()
	 */
	@Override
	public void disable() {
		this.text.setEnabled(false);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.dynamicswtforms.ui.widgets.DynamicSWTFormWidget#enable()
	 */
	@Override
	public void enable() {
		if (data.isEditable()) {
			this.text.setEnabled(true);
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
		return new Attribute(getElementName(), text.getText());
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
		if (text != null) {
			text.setText((String) value.getValue());
		}
		dirty = false;
		notifyListenersClean();
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
		if (regexPattern != null) {
			if (matcher == null) {
				matcher = regexPattern.matcher(text.getText());
			} else {
				matcher.reset(text.getText());
			}
			if (!matcher.matches()) {
				return data.getDisplayName() + " is invalid";
			}
		}
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
		if (text != null) {
			text.dispose();
		}
	}

}
