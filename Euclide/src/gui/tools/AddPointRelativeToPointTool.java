/* file : AddPointRelativeToPointTool.java
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
package gui.tools;



import gui.EuclideGui;
import gui.EuclideSheetView;
import gui.EuclideTool;

import java.awt.event.MouseEvent;

import math.geom2d.Point2D;
import model.EuclideDoc;
import model.EuclideLayer;
import model.EuclideFigure;
import app.EuclideApp;
import dynamic.DynamicShape2D;
import dynamic.shapes.FreePoint2D;
import dynamic.shapes.PointRelativePoint2D;
import dynamic.shapes.ShapeWrapper2D;

/**
 * @author dlegland
 */
public class AddPointRelativeToPointTool extends EuclideTool {

	/** The point used as reference */
	DynamicShape2D refPoint = null;
	
	public AddPointRelativeToPointTool(EuclideGui gui, String name){
		super(gui, name);
		setInstructions(new String[]{
				"Clic on the reference point", 
				"Clic on the initial position"});
	}

	@Override
	public void mousePressed(MouseEvent evt) {		
		EuclideFigure elem;
		
		// get Position of clicked point
		EuclideSheetView view = (EuclideSheetView) evt.getSource();
		Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);
		
		EuclideApp appli = this.gui.getAppli();
		if(step==1){			
			// find closest point
			elem = view.getSnappedShape(point, Point2D.class);
						
			// If no point is found, create a new free point
			if(elem==null){
				// Creates new free point, and adds to list of dynamic shapes
				DynamicShape2D refPoint = new FreePoint2D(point.getX(), point.getY());
				
				// Also creates an EuclideShape, and adds to current layer
				elem = appli.createEuclideShape(refPoint);
				
				// get current doc and layer
				EuclideDoc doc = appli.getCurrentDoc();
				EuclideLayer layer = view.getSheet().getCurrentLayer();
				
				// add to the current layer
				doc.addFigure(elem, layer);
				
			}
 			
 			// stores the found or created point, and update step
			refPoint = elem.getGeometry(); 			
			step = 2;
		}else{
			DynamicShape2D target = new ShapeWrapper2D(point);

			// Creates the relative point, and add to doc
			PointRelativePoint2D prp = new PointRelativePoint2D(refPoint, target);
			
			// Also creates an EuclideShape, and adds to current layer
			elem = appli.createEuclideShape(prp);
			
			// get current doc and layer
			EuclideDoc doc = appli.getCurrentDoc();
			EuclideLayer layer = view.getSheet().getCurrentLayer();
			
			// add to the current layer
			doc.addFigure(elem, layer);
			
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
		EuclideFigure item = view.getSnappedShape(point, Point2D.class);
		
		// Extract shape 
		if(item==null) return;		
		DynamicShape2D dynamic = item.getGeometry();
		
		// change label of mouse cursor
		String shapeName = gui.getAppli().getShapeString(dynamic);
		view.setMouseLabel("this " + shapeName);

	}
}
