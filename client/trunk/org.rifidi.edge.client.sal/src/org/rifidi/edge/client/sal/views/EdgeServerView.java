/*
 * EdgeServerView.java
 * 
 * Created:     July 8th, 2009
 * Project:       Rifidi Edge Server - A middleware platform for RFID applications
 *                    http://www.rifidi.org
 *                    http://rifidi.sourceforge.net
 * Copyright:   Pramari LLC and the Rifidi Project
 * License:      The software in this package is published under the terms of the EPL License
 *                    A copy of the license is included in this distribution under Rifidi-License.txt 
 */
package org.rifidi.edge.client.sal.views;

import java.util.ArrayList;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.rifidi.edge.client.model.sal.RemoteEdgeServer;
import org.rifidi.edge.client.model.sal.RemoteReader;
import org.rifidi.edge.client.sal.SALPluginActivator;
import org.rifidi.edge.client.sal.controller.edgeserver.EdgeServerTreeContentProvider;
import org.rifidi.edge.client.sal.controller.edgeserver.EdgeServerTreeLabelProvider;
import org.rifidi.edge.client.sal.modelmanager.SALModelService;
import org.rifidi.edge.client.sal.modelmanager.SALModelServiceListener;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * The View for displaying readers
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 */
@Configurable
public class EdgeServerView extends ViewPart implements
		ITabbedPropertySheetPageContributor, DragSourceListener,
		SALModelServiceListener {

	public static final String ID = "org.rifidi.edge.client.sal.views.EdgeServerView";
	/** The tree viewer to use */
	private AbstractTreeViewer treeViewer;
	/** The service that injects the model */
	private volatile SALModelService modelService;

	/**
	 * 
	 */
	public EdgeServerView() {
		super();
		this.modelService = SALPluginActivator.getDefault().getSalModelService();
	}

	/**
	 * @param modelService
	 *            the modelService to set
	 */
	public void setModelService(SALModelService modelService) {
		this.modelService = modelService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {

		GridLayout layout = new GridLayout(2, false);
		parent.setLayout(layout);

		treeViewer = new TreeViewer(parent);
		GridData treeViewerLayoutData = new GridData(GridData.FILL_BOTH);
		treeViewerLayoutData.horizontalSpan = 2;
		treeViewer.getControl().setLayoutData(treeViewerLayoutData);
		treeViewer.setContentProvider(new EdgeServerTreeContentProvider());
		treeViewer.setLabelProvider(new EdgeServerTreeLabelProvider());
		treeViewer.setComparator(new ViewerComparator());
		createContextMenu();

		modelService.registerListener(this);
		treeViewer.addDragSupport(DND.DROP_MOVE, new Transfer[] { TextTransfer
				.getInstance() }, this);
		this.getSite().setSelectionProvider(treeViewer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {

	}

	/**
	 * Create a context menu for this viewer
	 */
	private void createContextMenu() {
		// Create menu manager.
		MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager mgr) {
				mgr
						.add(new GroupMarker(
								IWorkbenchActionConstants.MB_ADDITIONS));
			}
		});

		// Create menu.
		Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
		treeViewer.getControl().setMenu(menu);

		// Register menu for extension.
		getSite().registerContextMenu(menuMgr, treeViewer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		this.modelService.unregisterListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor
	 * #getContributorId()
	 */
	@Override
	public String getContributorId() {
		return "org.rifidi.edge.client.sal.tabbedPropContributer";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.dnd.DragSourceListener#dragFinished(org.eclipse.swt.dnd
	 * .DragSourceEvent)
	 */
	@Override
	public void dragFinished(DragSourceEvent event) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.dnd.DragSourceListener#dragSetData(org.eclipse.swt.dnd
	 * .DragSourceEvent)
	 */
	@Override
	public void dragSetData(DragSourceEvent event) {
		if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
			ISelection sel = treeViewer.getSelection();
			Object o = ((TreeSelection) sel).getFirstElement();
			event.data = ((RemoteReader) o).getID();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.dnd.DragSourceListener#dragStart(org.eclipse.swt.dnd.
	 * DragSourceEvent)
	 */
	@Override
	public void dragStart(DragSourceEvent event) {
		ISelection sel = treeViewer.getSelection();
		Object o = ((TreeSelection) sel).getFirstElement();
		if (o instanceof RemoteReader) {
			event.doit = true;
			return;
		}
		event.doit = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.client.sal.modelmanager.SALModelServiceListener#setModel
	 * (java.lang.Object)
	 */
	@Override
	public void setModel(RemoteEdgeServer model) {
		ArrayList<RemoteEdgeServer> stubby = new ArrayList<RemoteEdgeServer>();
		stubby.add(model);
		this.treeViewer.setInput(stubby);
	}

}
