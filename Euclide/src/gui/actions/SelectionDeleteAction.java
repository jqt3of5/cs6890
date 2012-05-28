/*
 * File : SelectionDeleteAction.java
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

import gui.EuclideAction;
import gui.EuclideDocView;
import gui.EuclideGui;
import gui.EuclideSheetView;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import model.EuclideDoc;
import model.EuclideFigure;
import dynamic.DynamicShape2D;

/**
 * Removes all selected items from the current document.
 * @author Legland
 */
public class SelectionDeleteAction extends EuclideAction {	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Apache log4j Logger */
	private static Logger logger = Logger.getLogger("Euclide");

	public SelectionDeleteAction(EuclideGui gui, String name){
		super(gui, name);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		logger.info("Delete selection");
		
		EuclideDocView docView = gui.getCurrentView();
		if (docView == null)
			return;
		
		EuclideSheetView view = docView.getCurrentSheetView();
		if (view == null)
			return;
		
		ArrayList<DynamicShape2D> shapes = new ArrayList<DynamicShape2D>();
		for(EuclideFigure item : view.getSelection())
			shapes.add(item.getGeometry());
		
		EuclideDoc doc = gui.getCurrentDoc();
		for(DynamicShape2D dynamic : shapes)
			doc.removeDynamicShape(dynamic);
		
		view.clearSelection();
		view.repaint();
	}

}
