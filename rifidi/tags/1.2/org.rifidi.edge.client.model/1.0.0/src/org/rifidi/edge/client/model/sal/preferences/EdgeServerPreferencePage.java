/**
 * 
 */
package org.rifidi.edge.client.model.sal.preferences;
//TODO: Comments
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.rifidi.edge.client.model.SALModelPlugin;

/**
 * The contributed preference page for the RemoteEdgeServer
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class EdgeServerPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	@Override
	protected void createFieldEditors() {

		addField(new StringFieldEditor(EdgeServerPreferences.EDGE_SERVER_IP,
				"Edge Server IP", getFieldEditorParent()));
		addField(new IntegerFieldEditor(
				EdgeServerPreferences.EDGE_SERVER_PORT_RMI,
				"Edge Server RMI Port", getFieldEditorParent()));
		addField(new IntegerFieldEditor(
				EdgeServerPreferences.EDGE_SERVER_PORT_JMS,
				"Edge Server JMS Port", getFieldEditorParent()));
		addField(new StringFieldEditor(
				EdgeServerPreferences.EDGE_SERVER_JMS_QUEUE,
				"Edge Server JMS Notifications Queue Name", getFieldEditorParent()));
		addField(new StringFieldEditor(
				EdgeServerPreferences.EDGE_SERVER_JMS_QUEUE_TAGS,
				"Edge Server JMS Tags Queue Name", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#doGetPreferenceStore()
	 */
	@Override
	protected IPreferenceStore doGetPreferenceStore() {
		return SALModelPlugin.getDefault().getPreferenceStore();
	}

}
