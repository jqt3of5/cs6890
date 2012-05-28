/*
 * File : CloseCurrentDocumentAction.java
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
 * Created on 22 Nov. 2008
 */
 
package gui.actions;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

import model.EuclideDoc;

import app.EuclideApp;
import gui.*;

/**
 * Action called when leaving program.
 * @author Legland
 */
public class CloseCurrentDocumentAction extends EuclideAction {	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Apache log4j Logger */
	private static Logger logger = Logger.getLogger("Euclide");

	public CloseCurrentDocumentAction(EuclideGui gui, String name){
		super(gui, name);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		EuclideApp appli = gui.getAppli();
		
		EuclideDoc doc = gui.getCurrentDoc();
		if(doc==null)
			return;
		logger.info("Closing document: " + doc.getName());
		
		gui.closeDocument(doc);
		
		if(appli.getDocumentNumber()==0){
			//TODO: not very clean
			gui.getCurrentFrame().setCurrentView(null);
		}
		
		gui.getCurrentFrame().repaint();
	}

}
