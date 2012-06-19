/* file : MovePointTool.java
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
 * Created on 3 janv. 2006
 *
 */
package gui.tools;


import java.awt.event.MouseEvent;

import dynamic.DynamicShape2D;

import math.geom2d.Point2D;
import math.geom2d.Shape2D;

import model.*;
import app.*;
import gui.*;

/**
 * @author dlegland
 */
public class MovePointTool extends EuclideTool {

	private Point2D point1;
	
	public MovePointTool(EuclideGui gui, String name){
		super(gui, name);
		setInstructions(new String[]{"Clic on the point to move and drag it"});
	}
	
	@Override
	public void mousePressed(MouseEvent evt) {
		EuclideSheetView view = (EuclideSheetView) evt.getSource();
		Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);
		
		EuclideFigure elem = view.getSnappedShape(point, Point2D.class);
		if(elem == null) return;		

		point1 = (Point2D)(elem.getGeometry().getShape());
		
		view.setMouseLabel("");
	//	view.repaint();
	}

	@Override
	public void mouseDragged(MouseEvent evt){
		EuclideSheetView view = (EuclideSheetView) evt.getSource();
		Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);
		
		if(point1==null) return;		

		view.setMouseLabel("");
		
		this.point1 = point;
		
		EuclideApp appli = gui.getAppli();
		appli.getCurrentDoc().updateDependencies();
		//view.repaint();
	}
	
	@Override 
	public void mouseMoved(MouseEvent evt){		
		EuclideSheetView view = (EuclideSheetView) evt.getSource();
		view.setMouseLabel("");
		point1 = null;

		Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);
		
		// find the closest shape
		EuclideFigure item = view.getSnappedShape(point, Point2D.class);
		
		// Extract shape 
		if(item==null) return;		
		Shape2D shape = item.getGeometry().getShape();
		DynamicShape2D dynamic = item.getGeometry();
		
		// change label of mouse cursor
		if(view.isSnapped(point, shape)){
			String shapeName = gui.getAppli().getShapeString(dynamic);
			view.setMouseLabel("move this " + shapeName);
		}
		//view.repaint();
	}
	
	@Override 
	public void mouseReleased(MouseEvent evt){
		EuclideSheetView view = (EuclideSheetView) evt.getSource();
		view.setMouseLabel("");

		Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);
		
		// find the closest shape
		EuclideFigure item = view.getSnappedShape(point, Point2D.class);
		
		// Extract shape 
		if(item==null) return;		
		Shape2D shape = item.getGeometry().getShape();
		DynamicShape2D dynamic = item.getGeometry();
		
		// change label of mouse cursor
		if(view.isSnapped(point, shape)){
			String shapeName = gui.getAppli().getShapeString(dynamic);
			view.setMouseLabel("move this " + shapeName);
		}
	//	view.repaint();
	}
}
