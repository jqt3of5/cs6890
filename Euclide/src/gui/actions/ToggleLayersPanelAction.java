/* file : ToggleLayersPanelAction.java
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
 * Created on 2 janv. 2006
 *
 */
package gui.actions;

import java.awt.event.ActionEvent;

import model.EuclideSheet;

import gui.*;
import gui.dialogs.EuclidePanelDialog;
import gui.panels.LayersPanel;

/**
 * @author dlegland
 */
public class ToggleLayersPanelAction extends EuclideAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ToggleLayersPanelAction(EuclideGui gui, String name){
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
		
		// extract current sheet
		EuclideSheet sheet = view.getSheet();
		
		// avoid null case
		if (sheet == null) {
			gui.showMessage("No sheet error",
					"No sheet is currently displayed");
			return;
		}
		
		// create appropriate layers panel
		LayersPanel panel = new LayersPanel(gui, sheet);
		
		this.gui.getAppli().addApplicationListener(panel);
		
		// show EuclideDialog containing the layers panel
		EuclidePanelDialog dlg = new EuclidePanelDialog(this.gui, 
				panel, "Layers Edition");
		dlg.pack();
		gui.showDialog(dlg);
		dlg.doLayout();
		dlg.pack();
	}
}
