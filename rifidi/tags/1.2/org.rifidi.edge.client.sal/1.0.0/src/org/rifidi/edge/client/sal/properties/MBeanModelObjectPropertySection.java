/**
 * 
 */
package org.rifidi.edge.client.sal.properties;
//TODO: Comments
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

import javax.management.Attribute;
import javax.management.MBeanInfo;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.rifidi.edge.client.mbean.ui.MBeanInfoWidgetListener;
import org.rifidi.edge.client.model.sal.AbstractAttributeContributorModelObject;
import org.rifidi.edge.client.model.sal.properties.AttributeChangedEvent;
import org.rifidi.edge.client.sal.properties.mbeanwidgets.FlatFormSectionComposite;

/**
 * This displays a Tabbed Property Section for a
 * AbstractAttributeContributorModelObject
 * 
 * @author Kyle Neumeier
 * 
 */
public class MBeanModelObjectPropertySection extends AbstractPropertySection
		implements MBeanInfoWidgetListener, PropertyChangeListener {

	/** The MBeanInfo to display */
	private MBeanInfo info;
	/** The model object (either a reader or a commandConfig) */
	private AbstractAttributeContributorModelObject modelObject;
	/** The composite to display this on */
	private FlatFormSectionComposite composite;

	/***
	 * Constructor
	 * 
	 * @param mbeanInfo
	 *            The MBeanInfo to display
	 * @param modelObject
	 *            The ModelOBject (either a reader or a command config)
	 * @param category
	 *            The Category to display
	 */
	public MBeanModelObjectPropertySection(MBeanInfo mbeanInfo,
			AbstractAttributeContributorModelObject modelObject, String category) {
		this.info = mbeanInfo;
		this.modelObject = modelObject;
		Set<String> categories = new HashSet<String>();
		categories.add(category);
		composite = new FlatFormSectionComposite(mbeanInfo, categories, true);
		composite.addListner(this);
		modelObject.addPropertyChangeListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#createControls
	 * (org.eclipse.swt.widgets.Composite,
	 * org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		composite.createControls(parent, super.getWidgetFactory());
		for (Attribute attr : modelObject.getAttributes().asList()) {
			composite.setValue(attr);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		modelObject.removePropertyChangeListener(this);
		composite.removeListner(this);
		composite.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#setInput
	 * (org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.client.mbean.ui.MBeanInfoWidgetListener#dataChanged(java
	 * .lang.String)
	 */
	@Override
	public void dataChanged(String widgetName, String newData) {
		// TODO: verify the data?
		this.modelObject
				.updateProperty(this.composite.getAttribute(widgetName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.client.mbean.ui.MBeanInfoWidgetListener#keyReleased()
	 */
	@Override
	public void keyReleased(String widget) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.client.mbean.ui.MBeanInfoWidgetListener#clean(java.lang
	 * .String)
	 */
	@Override
	public void clean(String widgetName) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejava.beans.PropertyChangeListener#propertyChange(java.beans.
	 * PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if (arg0.getPropertyName().equals(
				AttributeChangedEvent.ATTRIBUTE_CHANGED_EVENT)) {
			AttributeChangedEvent bean = (AttributeChangedEvent) arg0
					.getNewValue();
			composite.setValue(bean.getAttribute());
		}
	}

}
