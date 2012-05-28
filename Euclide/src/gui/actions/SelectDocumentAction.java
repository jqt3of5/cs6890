/**
 * File: 	SelectDocumentAction.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 28 mars 10
 */

package gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import model.EuclideDoc;

import gui.EuclideAction;
import gui.EuclideGui;

/**
 * @author dlegland
 */
public class SelectDocumentAction extends EuclideAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	EuclideDoc doc;

	/**
	 * @param gui
	 * @param name
	 */
	public SelectDocumentAction(EuclideGui gui, String name, EuclideDoc doc) {
		super(gui, name);
		this.doc = doc;
	}

	public EuclideDoc getDocument() {
		return doc;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		gui.getAppli().setCurrentDoc(doc);
		JFrame frame = gui.getCurrentFrame();
		frame.validate();
		frame.repaint();
	}
}
