/**
 * 
 */
package org.rifidi.edge.core.api.jms.notifications;
//TODO: Comments
import java.io.Serializable;

/**
 * A notification that is sent whenever a CommandConfiguration becomes available
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class CommandConfigurationAddedNotification implements Serializable {

	/** SerialVersionID */
	private static final long serialVersionUID = 1L;
	/** The ID of the command Configuration */
	private String commandConfigurationID;

	/**
	 * Constructor
	 * 
	 * @param commandConfigurationID
	 *            The ID of the commandConfig that was added
	 */
	public CommandConfigurationAddedNotification(String commandConfigurationID) {
		this.commandConfigurationID = commandConfigurationID;
	}

	/**
	 * @return the commandConfigurationID
	 */
	public String getCommandConfigurationID() {
		return commandConfigurationID;
	}
}
