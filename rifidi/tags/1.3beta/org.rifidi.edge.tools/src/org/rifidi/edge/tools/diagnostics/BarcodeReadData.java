package org.rifidi.edge.tools.diagnostics;

import org.rifidi.edge.notification.BarcodeTagEvent;

public class BarcodeReadData extends AbstractReadData<BarcodeTagEvent> {

	/**
	 * @param tagID
	 * @param readerID
	 * @param antennaID
	 */
	public BarcodeReadData(String tagID, String readerID, int antennaID) {
		super(tagID, readerID, antennaID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.diagnostics.tags.generator.AbstractReadData#createTag(java
	 * .lang.String)
	 */
	@Override
	protected BarcodeTagEvent createTag(String id) {
		char[] chars = id.toCharArray();
		byte[] memBank = new byte[chars.length];
		for (int i = 0; i < chars.length; i++) {
			memBank[i] = (byte) chars[i];
		}
		BarcodeTagEvent tag = new BarcodeTagEvent();
		tag.setBarcode(memBank);
		return tag;
	}

}
