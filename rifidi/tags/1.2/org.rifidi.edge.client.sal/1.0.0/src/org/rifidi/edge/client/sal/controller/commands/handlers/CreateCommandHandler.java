/**
 * 
 */
package org.rifidi.edge.client.sal.controller.commands.handlers;
//TODO: Comments
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler2;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.rifidi.edge.client.model.sal.RemoteCommandConfigType;
import org.rifidi.edge.client.sal.controller.commands.CommandController;
import org.rifidi.edge.client.sal.controller.commands.CommandTreeContentProvider;

/**
 * A handler that creates a new Command Configuration. The selection must be a
 * RemoteCommandConfigType object
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class CreateCommandHandler extends AbstractHandler implements IHandler2 {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection sel = HandlerUtil.getCurrentSelection(event);
		CommandController controller = CommandTreeContentProvider
				.getController();

		Object obj = ((TreeSelection) sel).getFirstElement();
		controller.createCommand(((RemoteCommandConfigType) obj)
				.getCommandConfigType());

		return null;
	}

}
