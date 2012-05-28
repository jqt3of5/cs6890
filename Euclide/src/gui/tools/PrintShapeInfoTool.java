/* file : PrintShapeInfoTool.java
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


import gui.EuclideGui;
import gui.EuclideSheetView;
import gui.EuclideTool;

import java.awt.event.MouseEvent;
import java.io.PrintStream;
import java.util.Locale;

import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import model.EuclideFigure;
import dynamic.DynamicObject2D;
import dynamic.DynamicShape2D;

/**
 * Select a shape, and display its information (parents, parameters) 
 * on the console
 * @author dlegland
 */
public class PrintShapeInfoTool extends EuclideTool {
	
	PrintStream console = System.out;
	
	public PrintShapeInfoTool(EuclideGui gui, String name){
		super(gui, name);
		setInstructions(new String[]{"Clic on the shapes to select"});
	}
	
	@Override 
	public void deselect(){
		super.deselect();
		EuclideSheetView view = gui.getCurrentView().getCurrentSheetView();
		view.clearSelection();
		view.setMouseLabel("");
	}
	
	@Override 
	public void mousePressed(MouseEvent evt) {
		EuclideSheetView view = (EuclideSheetView) evt.getSource();

		Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);
		EuclideFigure item = view.getSnappedShape(point, Shape2D.class);
		
		if(item==null)
			return;
		
		// Info of Euclide shape
		String name = item.getName();
		if(name!=null)
			console.println("Shape: " + name);
		else
			console.println("Shape: ");
		
		// extract the construction
		DynamicShape2D dynamic = item.getGeometry();
		
		// Info of the corresponding geometry
		console.println("Construction: " + dynamic.getName());
		console.println("  Tag = " + dynamic.getTag());
		console.println("  Name = " + dynamic.getName());
		console.println("  Type = " + dynamic.getClass().getSimpleName());
		
		// Display info for parents
		console.println("  Parents:");
		for(DynamicObject2D parent : dynamic.getParents()) {
			console.format(Locale.US, "    %s (%s)\n", parent.getName(), 
					parent.getClass().getSimpleName());
		}
		
		// Display info for parameters
		console.println("  Parameters:");
		for(Object param : dynamic.getParameters()) {
			console.format(Locale.US, "    %s\n", param.toString());
		}
	}
	
	/**
	 * Display the type of the closest shape.
	 */
	@Override 
	public void mouseMoved(MouseEvent evt){		
		EuclideSheetView view = (EuclideSheetView) evt.getSource();
		view.setMouseLabel("");

		Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);
		EuclideFigure item = view.getSnappedShape(point, Shape2D.class);
				
		// Extract shape 
		if(item==null) return;		
		DynamicShape2D dynamic = item.getGeometry();
		
		// change label of mouse cursor
		String shapeName = gui.getAppli().getShapeString(dynamic);
		view.setMouseLabel("this " + shapeName);
	}
}
