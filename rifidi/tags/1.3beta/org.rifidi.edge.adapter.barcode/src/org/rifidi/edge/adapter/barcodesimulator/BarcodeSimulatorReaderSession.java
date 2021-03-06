/*
 *  BarcodeSimulatorReaderSession.java
 *
 *  Created:	Apr 22, 2010
 *  Project:	Rifidi Edge Server - A middleware platform for RFID applications
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	GNU Public License (GPL)
 *  				http://www.opensource.org/licenses/gpl-3.0.html
 */
package org.rifidi.edge.adapter.barcodesimulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.rifidi.edge.adapter.barcodesimulator.tag.BarcodeSimulatorTagHandler;
import org.rifidi.edge.api.SessionStatus;
import org.rifidi.edge.notification.NotifierService;
import org.rifidi.edge.notification.ReadCycle;
import org.rifidi.edge.sensors.AbstractCommandConfiguration;
import org.rifidi.edge.sensors.AbstractSensor;
import org.rifidi.edge.sensors.SensorSession;
import org.rifidi.edge.sensors.sessions.AbstractServerSocketSensorSession;
import org.rifidi.edge.sensors.sessions.MessageParsingStrategy;
import org.rifidi.edge.sensors.sessions.MessageParsingStrategyFactory;
import org.rifidi.edge.sensors.sessions.MessageProcessingStrategy;
import org.rifidi.edge.sensors.sessions.MessageProcessingStrategyFactory;

/**
 * The session for the Barcode reader.
 * 
 * @author Matthew Dean - matt@pramari.com
 */
public class BarcodeSimulatorReaderSession extends
		AbstractServerSocketSensorSession {

	/** Service used to send out notifications */
	private volatile NotifierService notifierService;

	/** The ID for the reader. */
	private String readerID = null;

	/** A class for handling incoming tags. */
	private BarcodeSimulatorTagHandler tagHandler = null;

	/**
	 * 
	 * 
	 * @param sensor
	 * @param ID
	 * @param destination
	 * @param template
	 * @param commandConfigurations
	 */
	public BarcodeSimulatorReaderSession(AbstractSensor<?> sensor, String id,
			int port, NotifierService notifierService, String readerID,
			Set<AbstractCommandConfiguration<?>> commandConfigurations) {
		super(sensor, id, port, 10, commandConfigurations);
		this.readerID = readerID;
		this.notifierService = notifierService;
		this.tagHandler = new BarcodeSimulatorTagHandler(readerID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.readers.impl.AbstractReaderSession#setStatus(org
	 * .rifidi.edge.core.api.SessionStatus)
	 */
	@Override
	protected void setStatus(SessionStatus status) {
		super.setStatus(status);
		// TODO: Remove this once we have aspectJ
		notifierService.sessionStatusChanged(this.readerID, this.getID(),
				status);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.edge.core.sensors.sessions.AbstractIPSensorSession#
	 * getMessageParsingStrategyFactory()
	 */
	@Override
	protected MessageParsingStrategyFactory getMessageParsingStrategyFactory() {
		return new BarcodeMessageParsingStrategyFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.sensors.sessions.AbstractIPSensorSession#
	 * getMessageProcessingStrategyFactory()
	 */
	@Override
	protected MessageProcessingStrategyFactory getMessageProcessingStrategyFactory() {
		return new BarcodeMessageProcessingStrategyFactory(this);
	}

	/**
	 * Process and send the tag.
	 */
	public void sendTag(byte[] tag) {
		ReadCycle cycle = this.tagHandler.processTag(tag);

		this.getSensor().send(cycle);

		//TODO: SEND TAGS
		//this.template.send(this.template.getDefaultDestination(),
		//		new ReadCycleMessageCreator(cycle));
	}

	/**
	 * Factory class for creating AmbientBarcodeMessageParsingStrategies.
	 * 
	 * @author Matthew Dean - matt@pramari.com
	 */
	private class BarcodeMessageParsingStrategyFactory implements
			MessageParsingStrategyFactory {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.rifidi.edge.sensors.sessions.MessageParsingStrategyFactory
		 * #createMessageParser()
		 */
		@Override
		public MessageParsingStrategy createMessageParser() {
			return new BarcodeMessageParsingStrategy();
		}
	}

	/**
	 * Message processing object for the Ambient barcode reader. This class
	 * takes byte arrays representing a physical barcode and converts them into
	 * a tag object that can be used by Rifidi Edge.
	 * 
	 * @author Matthew Dean - matt@pramari.com
	 */
	private class BarcodeMessageProcessingStrategy implements
			MessageProcessingStrategy {

		/**
		 * The session object.
		 */
		private BarcodeSimulatorReaderSession session = null;

		/**
		 * Processing strategy for the Ambient Barcode.
		 * 
		 * @param session
		 * @param template
		 */
		public BarcodeMessageProcessingStrategy(SensorSession session) {
			this.session = (BarcodeSimulatorReaderSession) session;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.rifidi.edge.core.sensors.sessions.MessageProcessingStrategy#
		 * processMessage(byte[])
		 */
		@Override
		public void processMessage(byte[] message) {
			this.session.sendTag(message);
		}
	}

	/**
	 * Factory to generate MessageProcessingStrategy classes.
	 * 
	 * @author Matthew Dean - matt@pramari.com
	 */
	private class BarcodeMessageProcessingStrategyFactory implements
			MessageProcessingStrategyFactory {

		/** The session */
		private BarcodeSimulatorReaderSession session;

		/**
		 * Factory class for the ProcessingStrategy.
		 */
		public BarcodeMessageProcessingStrategyFactory(
				BarcodeSimulatorReaderSession session) {
			this.session = session;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.rifidi.edge.sensors.sessions.MessageProcessingStrategyFactory
		 * #createMessageProcessor()
		 */
		@Override
		public MessageProcessingStrategy createMessageProcessor() {
			return new BarcodeMessageProcessingStrategy(session);
		}

	}

	/**
	 * All messages coming back from the barcode reader are 10 characters long.
	 * 
	 * @author Matthew Dean - matt@pramari.com
	 */
	private class BarcodeMessageParsingStrategy implements
			MessageParsingStrategy {

		/** The message currently being processed. */
		private List<Byte> messagebuilder = new ArrayList<Byte>();
		/** Once we hit this size, we have a completed message */
		private static final int MSGSIZE = 9;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.rifidi.edge.sensors.sessions.MessageParsingStrategy#isMessage
		 * (byte)
		 */
		@Override
		public byte[] isMessage(byte message) {
			messagebuilder.add(message);
			if (messagebuilder.size() == MSGSIZE) {
				byte retval[] = new byte[MSGSIZE];
				for (int i = 0; i < MSGSIZE; i++) {
					retval[i] = messagebuilder.get(i);
				}
				messagebuilder.clear();
				return retval;
			}
			return null;
		}
	}

}
