/*
 * EdgeServerDropTargetListener.java
 * 
 * Created:     July 8th, 2009
 * Project:       Rifidi Edge Server - A middleware platform for RFID applications
 *                    http://www.rifidi.org
 *                    http://rifidi.sourceforge.net
 * Copyright:   Pramari LLC and the Rifidi Project
 * License:      The software in this package is published under the terms of the EPL License
 *                    A copy of the license is included in this distribution under Rifidi-License.txt 
 */

package org.rifidi.edge.client.sal.controller.edgeserver;

import org.eclipse.core.databinding.observable.map.ObservableMap;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.rifidi.edge.client.model.sal.RemoteCommandConfiguration;
import org.rifidi.edge.client.model.sal.RemoteSession;
import org.rifidi.edge.client.sal.wizards.submitjob.SimpleSubmitJobWizard;

/**
 * A DroptTargetListener for the EdgeServerView
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 */
public class EdgeServerDropTargetListener extends ViewerDropAdapter {

	/** The remote command configurations defined on this server */
	private ObservableMap remoteCommandConfigurations;
	private EdgeServerController controller;

	/**
	 * @param remoteCommandConfigurations
	 */
	public EdgeServerDropTargetListener(
			ObservableMap remoteCommandConfigurations,
			EdgeServerController controller, Viewer viewer) {
		super(viewer);
		this.remoteCommandConfigurations = remoteCommandConfigurations;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerDropAdapter#performDrop(java.lang.Object)
	 */
	@Override
	public boolean performDrop(Object data) {
		Object target = getCurrentTarget();
		if (target instanceof RemoteSession && data instanceof String) {
			RemoteSession session = (RemoteSession) target;
			RemoteCommandConfiguration config = (RemoteCommandConfiguration) remoteCommandConfigurations
					.get((String) data);
			if (config != null) {
				if (config.getReaderFactoryID().equals(
						session.getReaderFactoryID())) {
					SimpleSubmitJobWizard wizard = new SimpleSubmitJobWizard(
							config, session);
					IWorkbenchWindow window = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow();
					WizardDialog dialog = new WizardDialog(window.getShell(),
							wizard);
					dialog.open();

				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerDropAdapter#validateDrop(java.lang.Object, int, org.eclipse.swt.dnd.TransferData)
	 */
	@Override
	public boolean validateDrop(Object target, int operation,
			TransferData transferType) {
		return TextTransfer.getInstance().isSupportedType(transferType);
	}
}
