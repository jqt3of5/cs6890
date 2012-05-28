/*
 * File : ClearLociAction.java
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
 * Created on 19 Apr 2009
 */
 
package gui.actions;

import gui.EuclideAction;
import gui.EuclideDocView;
import gui.EuclideGui;
import gui.EuclideSheetView;

import java.awt.event.ActionEvent;

import model.EuclideDoc;
import dynamic.DynamicShape2D;
import dynamic.shapes.CurveLocus2D;
import dynamic.shapes.PointLocus2D;

/**
 * Reset all loci in current view.
 * @author Legland
 */
public class ClearLociAction extends EuclideAction {	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ClearLociAction(EuclideGui gui, String name){
		super(gui, name);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		System.out.println("clear loci");
		
		EuclideDocView docView = gui.getCurrentView();
		if (docView == null)
			return;
		
		// get current document
		EuclideDoc doc = docView.getDoc();
		
		// iterate on constructions
		for(DynamicShape2D shape : doc.getDynamicShapes()) {
			if(shape instanceof PointLocus2D) {
				// check Point Locus
				((PointLocus2D) shape).clearLocus();
			} else if(shape instanceof CurveLocus2D) {
				// check curve Locus
				((CurveLocus2D) shape).clearLocus();
			}
		}
		
		// Update display
		EuclideSheetView view = gui.getCurrentView().getCurrentSheetView();		
		view.clearSelection();
		view.repaint();
	}

}
