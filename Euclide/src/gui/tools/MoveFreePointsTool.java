/* file : MoveFreePointsTool.java
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
 * Created on 3 Jan. 2006
 *
 */
package gui.tools;


import gui.EuclideGui;
import gui.EuclideSheetView;
import gui.EuclideTool;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import model.EuclideDoc;
import model.EuclideFigure;
import dynamic.DynamicObject2D;
import dynamic.DynamicShape2D;
import dynamic.shapes.DynamicMoveablePoint2D;
import dynamic.shapes.FreePoint2D;
import dynamic.shapes.ShapeWrapper2D;

/**
 * @author dlegland
 */
public class MoveFreePointsTool extends EuclideTool {

	private DynamicShape2D dynamic;
	
	double xp;
	double yp;
	
	public MoveFreePointsTool(EuclideGui gui, String name){
		super(gui, name);
		setInstructions(new String[]{"Clic on the point to move and drag it"});
	}
	
	@Override
	public void mousePressed(MouseEvent evt) {
		EuclideSheetView view = (EuclideSheetView) evt.getSource();
		Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);
		
		view.setMouseLabel("");
		dynamic = null;
		
		// update position of mouse cursor
		xp = point.getX();
		yp = point.getY();
		
		// find a shape
		EuclideFigure item = view.snapShape(point);
		if(item==null)
			return;
		
		// extract geometry of selected shape
		DynamicShape2D geometry = item.getGeometry();
		
		// check the shape has a valid class
		if (geometry instanceof DynamicMoveablePoint2D) {
			// stores the free point
			dynamic = geometry;
		} else {
			dynamic = geometry;
			return;
		}
		
		//view.repaint();
	}

	@Override
	public void mouseDragged(MouseEvent evt){
		EuclideSheetView view = (EuclideSheetView) evt.getSource();
		Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);
		
		view.setMouseLabel("");
		
		if (dynamic==null)
			return;
		
		double x = point.getX();
		double y = point.getY();
		double dx = x-xp;
		double dy = y-yp;
		
		if (dynamic instanceof DynamicMoveablePoint2D) {
			((DynamicMoveablePoint2D) dynamic).translate(dx, dy);
		} else if (dynamic instanceof ShapeWrapper2D) {
			ShapeWrapper2D wrapper = (ShapeWrapper2D)  dynamic;
			Shape2D shape = wrapper.getShape();
			if(shape instanceof Point2D) {
				Point2D pt = ((Point2D) shape).translate(dx, dy);
				((Point2D) shape).translate(dx, dy);
				wrapper.setShape(pt);
			}
		} else {
			// extract all parents that are free
			Collection<DynamicShape2D> points = 
				new ArrayList<DynamicShape2D>();
			for(DynamicObject2D parent : dynamic.getParents()) {
				if(parent instanceof FreePoint2D) {
					// add the current parent to the list
					points.add((FreePoint2D) parent);
				} else {
					// if at least one parent is not a free point, escape
					return;
				}
			}
			
			// translate each parent point
			for(DynamicShape2D dyn : points) {
				((FreePoint2D) dyn).translate(dx, dy);
			}
		}
		
		// update mouse coordinate for next drag
		xp = x;
		yp = y;
		
		// update appli and display
		EuclideDoc doc = gui.getCurrentDoc();
		doc.updateDependencies();
		doc.setModified(true);
		gui.getCurrentFrame().updateTitle();
		//view.repaint();
	}
	
	@Override 
	public void mouseMoved(MouseEvent evt){		
		EuclideSheetView view = (EuclideSheetView) evt.getSource();
		view.setMouseLabel("");
		dynamic = null;

		Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);
		
		// find the closest shape
		EuclideFigure item = view.snapShape(point);
		if(item==null)
			return;
		
		// Extract shape 
		DynamicShape2D geom = item.getGeometry();
		
		// change label of mouse cursor
		if(view.isSnapped(point, geom.getShape())){
			String shapeName = gui.getAppli().getShapeString(geom);
			view.setMouseLabel("move this " + shapeName);
		}
		//view.repaint();
	}
	
	@Override 
	public void mouseReleased(MouseEvent evt){
		EuclideSheetView view = (EuclideSheetView) evt.getSource();
		view.setMouseLabel("");

		dynamic = null;
		
		Point2D point = view.getPosition(evt.getX(), evt.getY(), 
				false, false);
		
		// find the closest shape
		EuclideFigure item = view.snapShape(point);
		if(item==null)
			return;
		
		// Extract shape 
		Shape2D shape = item.getGeometry().getShape();
		
		// change label of mouse cursor
		if(view.isSnapped(point, shape)){
			String shapeName = gui.getAppli().getShapeString(
					item.getGeometry());
			view.setMouseLabel("move this " + shapeName);
		}
		
	//	view.repaint();
	}
}
