/*
 * File : AddEllipseArcAction.java
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
 * Created on 23 Apr 2006
 */
 
package gui.actions;

import java.awt.event.ActionEvent;

import math.geom2d.conic.*;

import dynamic.shapes.*;
import model.*;
import app.*;
import gui.*;

/**
 * Add a layer to the document
 * @author Legland
 */
public class AddEllipseArcAction extends EuclideAction {	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AddEllipseArcAction(EuclideGui gui, String name){
		super(gui, name);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		EuclideSheetView view = gui.getCurrentView().getCurrentSheetView();
		
		// Create ellipse arc
		EllipseArc2D arc = new EllipseArc2D(100, 100, 100, 50, 0, 0, 2*Math.PI);
		
		
		// create element with default styles 
		EuclideApp appli = gui.getAppli();
		EuclideFigure element = appli.createEuclideShape(new ShapeWrapper2D(arc));
		
		// and add it to the current layer
		EuclideLayer layer = view.getSheet().getCurrentLayer();
		layer.addFigure(element);
	}

}
