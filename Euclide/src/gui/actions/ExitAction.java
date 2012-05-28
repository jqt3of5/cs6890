/*
 * File : ExitAction.java
 *
 * Project : Euclide2
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
 * Created on 25 déc. 2003
 */
 
package gui.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;

import model.EuclideDoc;

import app.EuclideApp;
import gui.*;

/**
 * Action called when leaving program.
 * @author Legland
 */
public class ExitAction extends EuclideAction {	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExitAction(EuclideGui gui, String name){
		super(gui, name);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		EuclideApp appli = gui.getAppli();
		
		// use a temporary array to avoid removing an iterated collection
		Collection<EuclideDoc> docs = new ArrayList<EuclideDoc>();
		docs.addAll(appli.getDocuments());
		
		// try to close each open doc.
		boolean b = true;
		for(EuclideDoc doc : docs){
			b = gui.closeDocument(doc);
			if(!b)
				break;
		}
		
		// Does not close if there are still document(s)
		if(!b){
			gui.getCurrentFrame().updateTitle();
			return;
		}
		
		gui.getCurrentFrame().dispose();
		System.exit(0);
	}

}
