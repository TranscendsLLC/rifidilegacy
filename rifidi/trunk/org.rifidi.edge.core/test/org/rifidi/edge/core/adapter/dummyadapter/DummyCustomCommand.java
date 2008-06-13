package org.rifidi.edge.core.adapter.dummyadapter;

import org.rifidi.edge.core.readerAdapter.commands.ICustomCommand;

public class DummyCustomCommand implements ICustomCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6543542716375722430L;
	
	private String command;

	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @param command the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}
	

}
