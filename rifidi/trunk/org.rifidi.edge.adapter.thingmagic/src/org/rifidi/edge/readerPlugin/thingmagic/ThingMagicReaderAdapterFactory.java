package org.rifidi.edge.readerPlugin.thingmagic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.edge.core.exception.readerPlugin.RifidiReaderAdapterCreationException;
import org.rifidi.edge.core.readerPlugin.AbstractReaderInfo;
import org.rifidi.edge.core.readerPlugin.IReaderPlugin;
import org.rifidi.edge.core.readerPlugin.ISpecificReaderPluginFactory;

public class ThingMagicReaderAdapterFactory implements ISpecificReaderPluginFactory {
	private static final Log logger = LogFactory.getLog(ThingMagicReaderAdapterFactory.class);	
	@Override
	public IReaderPlugin createSpecificReaderAdapter(
			AbstractReaderInfo abstractConnectionInfo) throws RifidiReaderAdapterCreationException{
		logger.debug("createSpecificReaderAdapter called.");
		return new ThingMagicReaderAdapter((ThingMagicConnectionInfo) abstractConnectionInfo);
	}

}
