/* file : AddIntersection2CirclesTool.java
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
 * Created on 2 Apr. 2006
 *
 */
package gui.tools;


import gui.EuclideGui;
import gui.EuclideSheetView;
import gui.EuclideTool;

import java.awt.event.MouseEvent;

import math.geom2d.Point2D;
import math.geom2d.conic.CircularShape2D;
import model.EuclideDoc;
import model.EuclideLayer;
import model.EuclideFigure;
import app.EuclideApp;
import dynamic.DynamicShape2D;
import dynamic.shapes.Intersection2Circles2D;

/**
 * @author dlegland
 */
public class AddIntersection2CirclesTool extends EuclideTool {

	DynamicShape2D circle = null;
	
	public AddIntersection2CirclesTool(EuclideGui gui, String name){
		super(gui, name);		
		setInstructions(new String[]{
			"clic on the first circle", 
			"clic on the second circle"});
	}

	@Override
	public void select(){
		step = 1;
	}
	
	@Override
	public void mousePressed(MouseEvent evt) {
		EuclideFigure elem;
		
		// get Position of clicked point
		EuclideSheetView view = (EuclideSheetView) evt.getSource();
		Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);
		
		if(step==1){
			// pick a line
			elem = view.getSnappedShape(point, CircularShape2D.class);
			if(elem==null) return;
 			circle = elem.getGeometry();
			step = 2;
		}else{
			// pick a circle or a circle arc
			elem = view.getSnappedShape(point, CircularShape2D.class);
			if(elem==null) return;
 			DynamicShape2D circle2 = elem.getGeometry();

 			// Compute intersections
 			Intersection2Circles2D p1 = 
 				new Intersection2Circles2D(circle, circle2, 0);
 			Intersection2Circles2D p2 = 
 				new Intersection2Circles2D(circle, circle2, 1);
			
			// create element with default styles 
			EuclideApp appli = gui.getAppli();
			EuclideFigure elem1 = appli.createEuclideShape(p1);
			EuclideFigure elem2 = appli.createEuclideShape(p2);
			
			// get current doc and layer
			EuclideDoc doc = appli.getCurrentDoc();
			EuclideLayer layer = view.getSheet().getCurrentLayer();
			
			// add to the current layer
			doc.addFigure(elem1, layer);
			doc.addFigure(elem2, layer);
						
			// return to first step
			step = 1;
		}
		//aspected
		//view.repaint();		
		//view.setInstruction(this.getInstruction());
	}

	@Override 
	public void mouseMoved(MouseEvent evt){		
		EuclideSheetView view = (EuclideSheetView) evt.getSource();
		view.setMouseLabel("");

		Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);
		
		// find the closest shape
		EuclideFigure item = view.getSnappedShape(point, CircularShape2D.class);
		
		// Extract shape 
		if(item==null) return;
		DynamicShape2D dynamic = item.getGeometry();
		
		// change label of mouse cursor
		String shapeName = gui.getAppli().getShapeString(dynamic);
		view.setMouseLabel("this " + shapeName);
	}
}
