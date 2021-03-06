/**
 * 
 */
package org.rifidi.edge.core.commands;
//TODO: Comments
import java.util.HashSet;

import org.rifidi.configuration.impl.AbstractMultiServiceFactory;
import org.rifidi.edge.core.api.rmi.dto.CommandConfigFactoryDTO;

/**
 * A base class that all CommandConfigurationFactories should extend. Concrete
 * implementations should register themselves to OSGi under both the
 * AbstractCommandConfigurationFactory and
 * org.rifidi.configuration.ServiceFactory interfaces.
 * 
 * @author Jochen Mader - jochen@pramari.com
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public abstract class AbstractCommandConfigurationFactory extends
		AbstractMultiServiceFactory {
	/**
	 * Get the ID of the reader factory that this command factory is associated
	 * with
	 * 
	 * @return The ID of the reader factory that this CommandFactory produces
	 *         commands for
	 */
	public abstract String getReaderFactoryID();
	
	public CommandConfigFactoryDTO getDTO(){
		return new CommandConfigFactoryDTO(getReaderFactoryID(), new HashSet<String>(this.getFactoryIDs()));
	}
}
