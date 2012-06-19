/* file : AddPolygonVertexTool.java
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


import gui.EuclideGui;
import gui.EuclideSheetView;
import gui.EuclideTool;

import java.awt.event.MouseEvent;

import math.geom2d.Point2D;
import math.geom2d.polygon.Polygon2D;
import model.EuclideFigure;
import dynamic.DynamicShape2D;
import dynamic.shapes.PolygonVertex2D;

/**
 * @author dlegland
 */
public class AddPolygonVertexTool extends EuclideTool {

	public AddPolygonVertexTool(EuclideGui gui, String name){
		super(gui, name);		
	}
	
	@Override
	public void mousePressed(MouseEvent evt) {
		EuclideSheetView view = (EuclideSheetView) evt.getSource();
		Point2D point = view.getPosition(evt.getX(), evt.getY());

		// find polygon closest to clicked position
		EuclideFigure item = view.getSnappedShape(point, Polygon2D.class);
		if(item==null) return;
		
		// get the polygon
		DynamicShape2D geometry = item.getGeometry();
		Polygon2D polygon = (Polygon2D) geometry.getShape(); 
	
		// find the index of cloest vertex
		int i = 0;
		double dist;
		double minDist = Double.MAX_VALUE;
		int vertexNumber = -1;
		
		for(Point2D vertex : polygon.getVertices()){
			dist = vertex.getDistance(point);
			if(dist<minDist){
				minDist = dist;
				vertexNumber = i;
			}
			i++;
		}
		
		// Create the construction
		PolygonVertex2D vertex = new PolygonVertex2D(geometry, vertexNumber);
		
		// add construction to current doc
		gui.addNewObject(gui.getCurrentView().getDoc(), vertex);
				
		//aspected
		//view.repaint();
	}

	@Override 
	public void mouseMoved(MouseEvent evt){		
		EuclideSheetView view = (EuclideSheetView) evt.getSource();
		view.setMouseLabel("");

		Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);
		
		// find the closest shape
		EuclideFigure item = view.getSnappedShape(point, Polygon2D.class);
		
		// Extract shape 
		if(item==null) return;		
		DynamicShape2D dynamic = item.getGeometry();
		
		// change label of mouse cursor
		String shapeName = gui.getAppli().getShapeString(dynamic);
		view.setMouseLabel("new vertex of this " + shapeName);
	}
	
}
