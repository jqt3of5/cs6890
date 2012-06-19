/* file : AddPointOnCurveTool.java
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
 * Created on 13 jan. 2007
 *
 */

package gui.tools;


import java.awt.event.*;

import math.geom2d.*;
import math.geom2d.curve.*;

import dynamic.*;
import dynamic.shapes.*;
import model.*;
import app.*;
import gui.*;

/**
 * @author dlegland
 */
public class AddPointOnCurveTool extends EuclideTool {

	public AddPointOnCurveTool(EuclideGui gui, String name){
		super(gui, name);		
	}
	
	@Override
	public void mousePressed(MouseEvent evt) {
		EuclideSheetView view = (EuclideSheetView) evt.getSource();
		Point2D point = view.getPosition(evt.getX(), evt.getY());

		// find point closest to clicked position
		EuclideFigure elem = view.getSnappedShape(point, Curve2D.class);
		if(elem==null) return;
		
		// check if the point if close enough
		DynamicShape2D curve = elem.getGeometry();
		
		PointOnCurve2D poc = new PointOnCurve2D(curve, new ShapeWrapper2D(point));
		
		// create element with default styles 
		EuclideApp appli = gui.getAppli();
		EuclideFigure element = appli.createEuclideShape(poc);
		
		// get current doc and layer
		EuclideDoc doc = appli.getCurrentDoc();
		EuclideLayer layer = view.getSheet().getCurrentLayer();
		
		// add to the current layer
		doc.addFigure(element, layer);
		
		//aspected
		//view.repaint();
	}

	@Override 
	public void mouseMoved(MouseEvent evt){		
		EuclideSheetView view = (EuclideSheetView) evt.getSource();
		view.setMouseLabel("");

		Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);
		
		// find the closest shape
		EuclideFigure item = view.getSnappedShape(point, Curve2D.class);
		
		// Extract shape 
		if(item==null) return;		
		DynamicShape2D dynamic = item.getGeometry();
		
		// change label of mouse cursor
		String shapeName = gui.getAppli().getShapeString(dynamic);
		view.setMouseLabel("new point on this " + shapeName);
	}
	
}
