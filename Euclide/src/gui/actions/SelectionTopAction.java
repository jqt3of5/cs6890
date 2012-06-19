/*
 * File : SelectionTopAction.java
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
import java.util.*;

import org.apache.log4j.Logger;

import model.*;
import gui.*;

/**
 * adds a sheet to the current document, and creates the corresponding view.
 * @author Legland
 */
public class SelectionTopAction extends EuclideAction {	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Apache log4j Logger */
	private static Logger logger = Logger.getLogger("Euclide");

	public SelectionTopAction(EuclideGui gui, String name){
		super(gui, name);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		logger.info("Move selection to top");
		
		EuclideDocView docView = gui.getCurrentView();
		if (docView == null)
			return;
		
		EuclideSheetView view = docView.getCurrentSheetView();
		if (view == null)
			return;
		
		Collection<EuclideFigure> selection = view.getSelection();

		// Select only visible and editable layers
		ArrayList<EuclideLayer> layers = new ArrayList<EuclideLayer>();
		for(EuclideLayer layer : view.getSheet().getLayers())
			if(layer.isVisible() && layer.isEditable())
				layers.add(layer);

		// For each element, iterate on layers, and update position on layer
		for(EuclideFigure element : selection)
			for(EuclideLayer layer : layers){
				layer.setShapeIndex(element, layer.getShapeNumber()-1);
			}
		//aspected
		//view.repaint();
	}

}
