/*
 * File : SaveFileAction.java
 *
 * Project : Euclide
 *
 * ===========================================
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY, without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. if not, write to :
 * The Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 * 
 * author : Legland
 * Created on 08 May 2007
 */
 
package gui.actions;

import gui.EuclideAction;
import gui.EuclideGui;

import java.awt.event.ActionEvent;
import java.io.File;

import model.EuclideDoc;

/**
 * Action creates a new document.
 * @author Legland
 */
public class SaveFileAction extends EuclideAction {	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SaveFileAction(EuclideGui gui, String name){
		super(gui, name);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {		
		EuclideDoc doc = gui.getAppli().getCurrentDoc();
		if (doc == null)
			return;
		
		File file = gui.chooseDocumentFile(doc);
		if (file == null)
			return;
		
		gui.saveDocument(doc, file);
	}
}
