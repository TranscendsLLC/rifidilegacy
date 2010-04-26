/*
 * 
 * AlienTagReadEventFactory.java
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
package org.rifidi.edge.readerplugin.alien.messages;

import java.math.BigInteger;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.rifidi.edge.core.services.notification.data.DatacontainerEvent;
import org.rifidi.edge.core.services.notification.data.EPCGeneration1Event;
import org.rifidi.edge.core.services.notification.data.EPCGeneration2Event;
import org.rifidi.edge.core.services.notification.data.TagReadEvent;

/**
 * This is a factory to create new TagReadEvent objects from AlienTag objects
 * for a single reader.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class AlienTagReadEventFactory {

	/** The reader this factory is used for */
	private String readerID;

	/**
	 * Constructor
	 * 
	 * @param readrID
	 *            The FACTORY_ID of the reader this factory is used for
	 */
	public AlienTagReadEventFactory(String readerID) {
		this.readerID = readerID;
	}

	/**
	 * Convert an AlienTag into a TagReadEvent. This method is threadsafe.
	 * 
	 * @param alienTag
	 *            The tag to convert
	 * @return
	 */
	public TagReadEvent getTagReadEvent(AlienTag alienTag) {
		// the new event
		DatacontainerEvent tagData = null;
		// a big integer representation of the epc
		BigInteger epc = null;
		try {
			epc = new BigInteger(Hex.decodeHex(alienTag.getId_hex().toCharArray()));
		} catch (DecoderException e) {
			throw new RuntimeException("Cannot decode tag: " + alienTag.getId_hex());
		}
		int numbits = alienTag.getId_hex().length() * 4;

		// choose whether to make a gen1 or a gen2 tag
		if (alienTag.getProtocol() == 1) {
			EPCGeneration1Event gen1event = new EPCGeneration1Event();
			// make some wild guesses on the length of the epc field
			gen1event.setEPCMemory(epc, numbits);
			tagData = gen1event;
		} else {
			EPCGeneration2Event gen2event = new EPCGeneration2Event();
			gen2event.setEPCMemory(epc, numbits);
			tagData = gen2event;
		}
		TagReadEvent retVal = new TagReadEvent(readerID, tagData, alienTag
				.getAntenna(), alienTag.getLastSeenDate().getTime());
		if (alienTag.getSpeed() != null) {
			retVal.addExtraInformation(AlienTag.SPEED_ID, alienTag.getSpeed());
		}
		if (alienTag.getRssi() != null) {
			retVal.addExtraInformation(AlienTag.RSSI_ID, alienTag.getRssi());
		}
		if (alienTag.getDirection() != null) {
			retVal.addExtraInformation(AlienTag.DIRECTION, alienTag
					.getDirection());
		}
		return retVal;
	}

}
