/* file : AddFreePointTool.java
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



import java.awt.event.MouseEvent;
import math.geom2d.Point2D;

import dynamic.*;
import model.*;
import app.*;
import gui.*;

/**
 * @author dlegland
 */
public class AddFreePointTool extends EuclideTool {

	public AddFreePointTool(EuclideGui gui, String name){
		super(gui, name);
		setInstructions(new String[]{"Clic to add a Free Point"});
	}

	@Override
	public void mousePressed(MouseEvent evt) {		
		// get Position of clicked point
		EuclideSheetView view = (EuclideSheetView) evt.getSource();
		Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);
	
		// Create a shape with given geometry
		EuclideFigure shape = view.createNewPoint(point, gui.getAppli());
		DynamicShape2D dynamic = shape.getGeometry();
		
		// add to the doc
		gui.addNewObject(gui.getCurrentView().getDoc(), dynamic);									

		// update display
		view.repaint();		
	}
	@Override 
	public void mouseMoved(MouseEvent evt){		
		EuclideSheetView view 	= (EuclideSheetView) evt.getSource();
		EuclideApp appli 		= this.gui.getAppli();		
		
		view.setMouseLabel("");
		view.clearPreview();

		Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);
		
		// try to create a new free point
		EuclideFigure previewPoint = view.createNewPoint(point, appli);
		view.setMouseLabel(gui.getMouseLabel(previewPoint.getGeometry()));

		view.addToPreview(previewPoint);
	}

}
