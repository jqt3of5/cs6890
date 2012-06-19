/*
 * File : SelectionChangeLayerAction.java
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
 * Created on 23 Apr. 2008
 */
 
package gui.actions;

import java.awt.event.ActionEvent;
import java.util.*;

import model.*;
import gui.*;
import gui.dialogs.ChooseLayerDialog;

/**
 * puts the selected shapes on a different layer
 * @author Legland
 */
public class SelectionChangeLayerAction extends EuclideAction {	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SelectionChangeLayerAction(EuclideGui gui, String name){
		super(gui, name);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		EuclideSheetView view = gui.getCurrentSheetView();
		
		if(view==null) return;		
		
		Collection<EuclideFigure> selection = view.getSelection();
		if(selection.isEmpty()) return;

		ChooseLayerDialog dialog = 
			(ChooseLayerDialog) gui.getDialog("chooseLayer");
		if(dialog==null) return;
		gui.showDialog(dialog);
		
		EuclideLayer layer = dialog.getLayer();
		if(layer==null) return;
		
		// For each shape, change layer it belongs to
		EuclideDoc doc = gui.getAppli().getCurrentDoc();
		for(EuclideFigure shape : selection)
			doc.setFigureLayer(shape, layer);
		
		selection.clear();
		//aspected
		//view.repaint();
	}

}
