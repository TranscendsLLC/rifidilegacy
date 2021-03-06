/*
 * ChangeFloorPlan.java
 * 
 * Created:     July 8th, 2009
 * Project:       Rifidi Edge Server - A middleware platform for RFID applications
 *                    http://www.rifidi.org
 *                    http://rifidi.sourceforge.net
 * Copyright:   Pramari LLC and the Rifidi Project
 * License:      The software in this package is published under the terms of the EPL License
 *                    A copy of the license is included in this distribution under Rifidi-License.txt 
 */

package org.rifidi.edge.client.twodview.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;
import org.rifidi.edge.client.twodview.views.SiteView;

/**
 * Initiates a change of the background image - the floorplan in the twodview.  
 * 
 * @author Tobias Hoppenthaler - tobias@pramari.com
 */
public class ChangeFloorPlan implements IHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.IHandler#addHandlerListener(org.eclipse.core
	 * .commands.IHandlerListener)
	 */
	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.IHandler#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject project = root.getProject("MyProject");
		if (!project.exists()) {
			try {
				project.create(null);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			project.open(null);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IFolder folder = project.getFolder("Floorplans");
		FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(),
				SWT.OPEN);
		fd.setText("Open");
		fd.setFilterPath(folder.getLocation().toString());
		String[] filterExt = { "*.*", "*.png", "*.jpg", "*.gif", "*.bmp" };
		fd.setFilterExtensions(filterExt);
		String selected = fd.open();
		SiteView siteView=null;
		siteView = (SiteView) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().findView(
						"org.rifidi.edge.client.twodview.views.SiteView");
		if(siteView!=null)siteView.getFloorplanLayer().setFloorplan(selected);

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.IHandler#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.IHandler#isHandled()
	 */
	@Override
	public boolean isHandled() {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.IHandler#removeHandlerListener(org.eclipse.
	 * core.commands.IHandlerListener)
	 */
	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

}
