/*
 * 
 * TagEventListener.java
 *  
 * Created:     July 8th, 2009
 * Project:       Rifidi Edge Server - A middleware platform for RFID applications
 *                   http://www.rifidi.org
 *                   http://rifidi.sourceforge.net
 * Copyright:   Pramari LLC and the Rifidi Project
 * License:      The software in this package is published under the terms of the GPL License
 *                   A copy of the license is included in this distribution under RifidiEdge-License.txt 
 */
package org.rifidi.edge.core.services.notification.internal;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.rifidi.edge.core.services.notification.data.ReadCycle;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;

/**
 * An object that listens to the internal message bus for tag events and places
 * them on the external message topic
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 */
public class TagEventListener implements MessageListener {

	/** The template for sending out Notification messages */
	private JmsTemplate extNotificationTemplate;
	/** The queue to send out notifications on */
	private Destination extNotificationDest;

	/**
	 * Called by Spring
	 * 
	 * @param exextNotificationTemplate
	 *            the exextNotificationQueue to set
	 */
	public void setExtNotificationTemplate(JmsOperations extNotificationTemplate) {
		this.extNotificationTemplate = (JmsTemplate) extNotificationTemplate;
	}

	/**
	 * called by Spring
	 * 
	 * @param extNotificationDest
	 *            the extNotificationDest to set
	 */
	public void setExtNotificationDest(Destination extNotificationDest) {
		this.extNotificationDest = extNotificationDest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@Override
	public void onMessage(Message arg0) {
		try {
			Object obj = ((ObjectMessage) arg0).getObject();
			if (obj instanceof ReadCycle) {
				ReadCycle event = (ReadCycle) obj;
				extNotificationTemplate.send(extNotificationDest,
						new TagMessageMessageCreator(event.getBatch()));
			}
		} catch (JMSException e) {
		}

	}

}
