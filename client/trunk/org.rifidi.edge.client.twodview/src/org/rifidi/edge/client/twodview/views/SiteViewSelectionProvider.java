/*
 * SiteViewSelectionProvider.java
 * 
 * Created:     July 8th, 2009
 * Project:       Rifidi Edge Server - A middleware platform for RFID applications
 *                    http://www.rifidi.org
 *                    http://rifidi.sourceforge.net
 * Copyright:   Pramari LLC and the Rifidi Project
 * License:      The software in this package is published under the terms of the EPL License
 *                    A copy of the license is included in this distribution under Rifidi-License.txt 
 */

package org.rifidi.edge.client.twodview.views;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.rifidi.edge.client.twodview.layers.ListeningScalableLayeredPane;
import org.rifidi.edge.client.twodview.listeners.SiteViewFigureSelectionListener;
import org.rifidi.edge.client.twodview.sfx.ReaderAlphaImageFigure;

/**
 * The selection provider for the SiteView
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 */
public class SiteViewSelectionProvider implements ISelectionProvider,
		SiteViewFigureSelectionListener {

	/** The selection listeners */
	private Set<ISelectionChangedListener> listeners;
	/** The current selection for this class */
	private ISelection selection;
	private ListeningScalableLayeredPane pane;

	/**
	 * Constructor
	 */
	public SiteViewSelectionProvider(ListeningScalableLayeredPane pane) {
		listeners = new HashSet<ISelectionChangedListener>();
		pane.addImageSelectionListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
	@Override
	public ISelection getSelection() {
		return selection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse
	 * .jface.viewers.ISelection)
	 */
	@Override
	public void setSelection(ISelection selection) {
		if (this.selection != selection) {
			this.selection = selection;
			SelectionChangedEvent sce = new SelectionChangedEvent(this,
					getSelection());
			for (ISelectionChangedListener l : listeners) {
				l.selectionChanged(sce);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ISelectionProvider#addSelectionChangedListener
	 * (org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		this.listeners.add(listener);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ISelectionProvider#removeSelectionChangedListener
	 * (org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	@Override
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		this.listeners.remove(listener);
	}

	/**
	 * Cleanup: remove this as a selectionlistener from a pane.
	 */
	public void dispose() {
		if (pane != null) {
			pane.removeImageSelectionListener(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.rifidi.edge.client.twodview.listeners.SiteViewFigureSelectionListener#figureSelected(org.eclipse.draw2d.IFigure)
	 */
	@Override
	public void figureSelected(IFigure figure) {
		if ((figure instanceof ReaderAlphaImageFigure) && (figure != null)) {
			
			setSelection(new StructuredSelection(figure));
			return;
		}
		setSelection(new StructuredSelection());

	}

}
