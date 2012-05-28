/*
 * File : AddSheetAction.java
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
 * Created on 27 Jan 2006
 */
 
package gui.actions;

import java.awt.event.ActionEvent;
import model.*;
import gui.*;
import gui.dialogs.SheetEditDialog;

/**
 * adds a sheet to the current document, and creates the corresponding view.
 * @author Legland
 */
public class AddSheetAction extends EuclideAction {	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AddSheetAction(EuclideGui gui, String name){
		super(gui, name);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		EuclideDocView docView = gui.getCurrentView();
		if (docView == null)
			return;
		
		EuclideDoc doc = docView.getDoc();
		
		// Find a valid name for the new sheet
		String baseSheetName = "Sheet ";
		int i = 1;
		String name = baseSheetName + i;
		boolean ok;
		do {
			ok = true;
			for (EuclideSheet sheet : doc.getSheets())
				if (sheet.getName().equals(name)) {
					ok = false;
					name = baseSheetName + (++i);
					break;
				}
		} while (!ok);

		// create a new sheet (not yet attached to the doc)
		EuclideSheet sheet = new EuclideSheet(doc, name);
		EuclideLayer layer = new EuclideLayer("layer 0");
		sheet.addLayer(layer);
		
		// Open dialog to set up sheet properties
		SheetEditDialog dlg = new SheetEditDialog(gui, sheet,
			"Create a new sheet", true);
		gui.showDialog(dlg);
		
		// check the button that was used for closing the dialog
		if(dlg.isValid()){
			// add the sheet to the doc
			doc.addSheet(sheet);

			// add sheet view
			EuclideSheetView view = new EuclideSheetView(sheet);
			gui.getCurrentView().addEuclideSheetView(view);
			gui.getCurrentView().setCurrentSheetView(view);
		}
		
		dlg.dispose();
		gui.getCurrentFrame().validate();
	}

}
