/* file : SelectShapeTool.java
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


import java.awt.event.*;

import dynamic.DynamicShape2D;

import math.geom2d.*;
import math.geom2d.curve.*;
import math.geom2d.domain.Domain2D;

import model.*;
import gui.*;

/**
 * @author dlegland
 */
public class SelectShapeTool extends EuclideTool {
	
	public SelectShapeTool(EuclideGui gui, String name){
		super(gui, name);
		setInstructions(new String[]{"Clic on the shapes to select"});
	}
	
	@Override 
	public void deselect(){
		super.deselect();
		EuclideDocView docView = gui.getCurrentView();
		if (docView == null)
			return;
		
		EuclideSheetView view = docView.getCurrentSheetView();
		if (view == null)
			return;
			
		view.clearSelection();
		view.setMouseLabel("");
	}
	
	private EuclideFigure findShape(EuclideSheetView view, Point2D point){
		EuclideFigure shape;
		
		// first try to find a point
		shape = view.getSnappedShape(point, Point2D.class);
		if (shape!=null) return shape;
		
		// try to find a curve
		shape = view.getSnappedShape(point, Curve2D.class);			
		if (shape!=null) return shape;
		
		// finally try to find a domain
		shape = view.getSnappedShape(point, Domain2D.class);			
		
		return shape;
	}
	
	@Override 
	public void mousePressed(MouseEvent evt) {
		EuclideSheetView view = (EuclideSheetView) evt.getSource();

		Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);
		EuclideFigure elem = this.findShape(view, point);
		
		// ensure a valid shape has been found
		if(elem!=null){
			if((evt.getModifiers() & InputEvent.CTRL_MASK)==0){
				view.clearSelection();
			}
			if(view.getSelection().contains(elem))
				view.removeFromSelection(elem);
			else
				view.addToSelection(elem);
		}else{		
			if((evt.getModifiers() & InputEvent.CTRL_MASK)==0)
				view.clearSelection();
		}
		
//		this.gui.getCurrentFrame().getPaintOptionsPanel().updateState();
//		this.gui.getCurrentFrame().getPaintOptionsPanel().repaint();
		view.repaint();
	}
	
	/**
	 * Display the type of the closest shape.
	 */
	@Override 
	public void mouseMoved(MouseEvent evt){		
		EuclideSheetView view = (EuclideSheetView) evt.getSource();
		view.setMouseLabel("");

		Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);
		EuclideFigure item = this.findShape(view, point);
				
		// Extract shape 
		if(item==null) return;		
		DynamicShape2D dynamic = item.getGeometry();
		
		// change label of mouse cursor
		String shapeName = gui.getAppli().getShapeString(dynamic);
		view.setMouseLabel("this " + shapeName);
	}
}
