/*
 * File : ChangeFillStyleAction.java
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
 * Created on 14 Apr 2008
 */
 
package gui.actions;

import java.awt.event.ActionEvent;

import gui.*;
import gui.dialogs.EuclidePanelDialog;
import gui.panels.FillStyleEditPanel;

/**
 * adds a sheet to the current document, and creates the corresponding view.
 * @author Legland
 */
public class ChangeFillStyleAction extends EuclideAction {	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ChangeFillStyleAction(EuclideGui gui, String name){
		super(gui, name);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		EuclideDocView docView = gui.getCurrentView();
		if (docView == null)
			return;
		
		EuclideSheetView view = docView.getCurrentSheetView();
		if (view == null)
			return;
				
		EuclidePanelDialog dlg = new EuclidePanelDialog(
				this.gui,
				new FillStyleEditPanel(gui),
				"Edit Fill Style");
		gui.showDialog(dlg);
		//aspected
	//	view.repaint();
	}
}
