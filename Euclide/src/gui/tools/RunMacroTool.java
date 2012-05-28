/**
 * File: 	RunMacroTool.java
 * Project: Euclide-macros
 * 
 * Distributed under the LGPL License.
 *
 * Created: 22 août 09
 */
package gui.tools;

import gui.EuclideGui;
import gui.EuclideSheetView;
import gui.EuclideTool;
import gui.dialogs.ChooseMacroDialog;
import gui.macros.SimpleMacro;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import app.EuclideApp;

import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import model.EuclideFigure;
import dynamic.DynamicObject2D;
import dynamic.DynamicShape2D;



/**
 * @author dlegland
 *
 */
public class RunMacroTool extends EuclideTool {

	ArrayList<DynamicObject2D> inputs;
	SimpleMacro macro = null;

	/**
	 * @param gui
	 * @param name
	 */
	public RunMacroTool(EuclideGui gui, String name) {
		super(gui, name);
	}
	
	@Override
	public void select() {
		ChooseMacroDialog dlg = new ChooseMacroDialog(gui);
		gui.showDialog(dlg);
		
		// select a macro by its name
		this.macro = dlg.getMacro();
		
		if(macro==null)
			return;
		
		// initialize input array
		inputs = new ArrayList<DynamicObject2D>(macro.getInputSize());
		
		// set up user instructions array
		String[] instrs = new String[inputs.size()];
		for(int i=0; i<inputs.size(); i++)
			instrs[i] = macro.getUserInstruction(i);
		this.setInstructions(instrs);
		
		// set up current step
		this.step = 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void mousePressed(MouseEvent evt) {
		if (macro==null) {
			System.out.println("no macro selected, abort");
			return;
		}
		
		System.out.println("step " + step);
		
		EuclideApp appli = gui.getAppli();
		EuclideSheetView view 	= (EuclideSheetView) evt.getSource();

		Point2D point = view.getPosition(evt.getX(), evt.getY());
		
		// For the moment, support only shape parents
		// Unchecked class cast
		Class<? extends Shape2D> shapeClass = 
			(Class<? extends Shape2D>) macro.getInputClass(step-1);
			
		// find the shape with desired geometry the closest to the
		// clicked position
		EuclideFigure shape = view.getSnappedShape(point, shapeClass);
		
		// If no shape is found, creates a new free point
		if(shape==null){
			// Conditions for creating new free point : parent class
			// extends Point2D or Shape2D, 
			// and creation of new point is allowed by application
			if(!((Point2D.class.isAssignableFrom(shapeClass) || 
					Shape2D.class==shapeClass)&& 
					appli.isAllowingNewFreePoints()))
				return;
			
			// create a new Free point (possibly on a curve)
			shape = view.createNewPoint(point, appli);
			appli.getCurrentDoc().addFigure(shape, 
			        view.getSheet().getCurrentLayer());
		}
		
		// add dynamic construction to the list of inputs
		inputs.add(shape.getGeometry());
		
		// check if another shape is expected
		// in this case, wait for next one
		if(step<macro.getInputSize()) {
			step++;
			return;
		}
		
		// run the macro with selected arguments
		System.out.println("Call the run method of the macro " +
				macro.getName());
		macro.run(gui, inputs);
		
		// set up current step
		this.step = 1;
		inputs.clear();
		
		view.repaint();
	}

	@SuppressWarnings("unchecked")
	@Override 
	public void mouseMoved(MouseEvent evt){		
		if (macro==null) {
			return;
		}
		
		EuclideSheetView view 	= (EuclideSheetView) evt.getSource();
		EuclideApp appli 		= this.gui.getAppli();		
		
		view.setMouseLabel("");
		view.clearPreview();

		EuclideFigure elem;
		DynamicShape2D dynamic=null;
		Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);
		
		// determines the next expected class
		Class<?> class1 = macro.getInputClass(step-1);
		if(class1.isArray())
			class1 = class1.getComponentType();
		
		// small control
		if(!Shape2D.class.isAssignableFrom(class1)) return;

		// unchecked class cast
		Class<? extends Shape2D> shapeClass = 
		    (Class<? extends Shape2D>) class1;
		
		// try to find the shape with desired geometry the closest to the
		// clicked position
		elem = view.getSnappedShape(point, shapeClass);
		if(elem!=null){
			dynamic = elem.getGeometry();	

			// change label of mouse cursor
			String shapeName = gui.getAppli().getShapeString(dynamic);
			view.setMouseLabel("this " + shapeName);
			
		}else 
			// If no shape is found, try to create a new free point
			if((Point2D.class.isAssignableFrom(shapeClass) ||
				Shape2D.class==shapeClass) && 
				appli.isAllowingNewFreePoints()){
				
			EuclideFigure previewPoint;
			previewPoint = view.createNewPoint(point, appli);
			dynamic = previewPoint.getGeometry();
			view.setMouseLabel(gui.getMouseLabel(previewPoint.getGeometry()));
		
			view.addToPreview(previewPoint);
		}
		
	}
}
