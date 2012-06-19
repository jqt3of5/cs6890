/**
 * File: 	CreateMacroTool.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 11 août 09
 */
package gui.tools;

import gui.EuclideGui;
import gui.EuclideSheetView;
import gui.EuclideTool;
import gui.dialogs.ChooseMacroNameDialog;
import gui.macros.SimpleMacro;
import gui.macros.SimpleMacroInstruction;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import model.EuclideFigure;
import dynamic.DynamicObject2D;
import dynamic.DynamicShape2D;


/**
 * @author dlegland
 *
 */
public class CreateMacroTool extends EuclideTool {
	
	ArrayList<DynamicObject2D> inputs;
	DynamicObject2D result = null;
	
	public CreateMacroTool(EuclideGui gui, String name){
		super(gui, name);
		inputs = new ArrayList<DynamicObject2D>();
		this.setInstructions(new String[]{
				"Click on the next input",
				"Click on the output"});
	}
		
	public ArrayList<SimpleMacroInstruction> 
	createInstructions(DynamicObject2D object) {
		
		// the set of instructions to build
		ArrayList<SimpleMacroInstruction> instructions =
			new ArrayList<SimpleMacroInstruction>();
		
		
		// First part of the algorithm:
		// Compute each "generation level".
	
		ArrayList<ArrayList<DynamicObject2D>> ancesters =
			new ArrayList<ArrayList<DynamicObject2D>>();
		
		ArrayList<DynamicObject2D> parents = 
			new ArrayList<DynamicObject2D>(0);
		parents.addAll(object.getParents());
		ancesters.add(parents);

		while(!parents.isEmpty()) {
			ArrayList<DynamicObject2D> newParents = 
				new ArrayList<DynamicObject2D>();
			for(DynamicObject2D parent : parents) {
				// Do not add input objects
				if(inputs.contains(parent))
					continue;
				
				if(parent.getParents().size()==0) {
					//TODO: manage object without parents (constants...)
				}
				
				newParents.addAll(parent.getParents());
			}
			
			parents = newParents;
			
			if(!parents.isEmpty())
				ancesters.add(parents);
		}
		
		// Second part:
		// For each generation level, from the oldest, extract all elements
		// and build their instructions.
		
		// the set of already constructed objects
		ArrayList<DynamicObject2D> constructions =
			new ArrayList<DynamicObject2D>();
		constructions.addAll(inputs);
		
		// Iterate on generation levels from the oldest ones to the youngest
		// ones
		for(int i=ancesters.size()-1; i>=0; i--) {
			parents = ancesters.get(i);
			
			for(DynamicObject2D parent : parents) {
				// Avoid to build object several times
				if(constructions.contains(parent))
					continue;
				
				// create the instruction for current parent
				instructions.add(createInstruction(parent, constructions));
				
				// the parent is now constructed
				constructions.add(parent);
			}
		}
		
		// also add the instruction for the target object !
		instructions.add(createInstruction(object, constructions));
		
		// return the resulting set of instructions
		return instructions;
	}
	
	public SimpleMacroInstruction createInstruction(DynamicObject2D object, 
			ArrayList<DynamicObject2D> constructions) {
		Class<? extends DynamicObject2D> instrClass = object.getClass();
		
		Collection<? extends DynamicObject2D> parents = object.getParents();
		int[] indices = new int[parents.size()];
		
		// compute indices of parents
		int i=0;
		for(DynamicObject2D parent : object.getParents())
			indices[i++] = constructions.indexOf(parent);
		
		// create array for parameters
		Collection<? extends Object> params = object.getParameters();
		
		return new SimpleMacroInstruction(instrClass, indices, params);
	}

	@Override
	public void select(){
		super.select();
		inputs = new ArrayList<DynamicObject2D>();
		step = 1;
	}
	
	@Override
	public void mousePressed(MouseEvent evt) {
		EuclideSheetView view = (EuclideSheetView) evt.getSource();
		Point2D point = view.getPosition(evt.getX(), evt.getY());

		// check if right (or middle) button was clicked
		boolean rightButton = 
			(evt.getModifiers() & InputEvent.BUTTON3_MASK)!=0 ||
			(evt.getModifiers() & InputEvent.BUTTON2_MASK)!=0;

		// clean up board
        view.setMouseLabel("");

        if (step==1) {
        	if(rightButton) {
        		System.out.println("Right button");
        		step++;
        		view.setInstruction(this.getInstruction());
        		return;
        	}
        	
        	// find point closest to clicked position
        	EuclideFigure elem = view.getSnappedShape(point, Shape2D.class);
        	if(elem==null) return;

        	// add to the list of parents
        	inputs.add(elem.getGeometry());

        	// clean up board
        	view.setInstruction("Click on the next input [" + 
        			inputs.size() + "]");

        	return;
        }
        
    	// Clicking the right button here causes the tool to abort
    	if(rightButton) {
    		System.out.println("abort");
    		inputs.clear();
    		step = 1;
    		return;
    	}

    	// find point closest to clicked position
    	EuclideFigure elem = view.getSnappedShape(point, Shape2D.class);
    	if(elem==null) return;

    	// Create the list of instruction that are necessary to build the 
    	// target object from the list of inputs
    	ArrayList<SimpleMacroInstruction> instructions = null;
    	try {
    		instructions = createInstructions(elem.getGeometry());
    	} catch(Exception ex) {
    		System.err.println(ex.getStackTrace());
    	}
    	
    	// define classes of input objects
    	ArrayList<Class<?>> classes = new ArrayList<Class<?>>(inputs.size());
    	for(int i=0; i<inputs.size(); i++) {
    		DynamicObject2D input = inputs.get(i);
    		Class<?> inputClass = Object.class;
    		if (input instanceof DynamicShape2D) {
    			//TODO: add better control
    			inputClass = Shape2D.class;
    		} else {
    			System.err.println("unhandled class for input");
    		}
    		classes.add(inputClass);
    	}
    	
    	// Choose the name of the macro
    	ChooseMacroNameDialog dlg = new ChooseMacroNameDialog(gui);
    	gui.showDialog(dlg);
		
    	String macroName = dlg.getMacroName();
    	
    	// Create the macro
    	SimpleMacro macro = new SimpleMacro(macroName,
    			classes,
    			instructions);
    		
    	gui.getCurrentDoc().addMacro(macro);
    	
		//aspected
		//view.repaint();
	}

	@Override 
	public void mouseMoved(MouseEvent evt){		
		EuclideSheetView view = (EuclideSheetView) evt.getSource();
		view.setMouseLabel("");
		
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
			view.setMouseLabel("add this " + shapeName);
		}
		
		view.repaint();
	}
}

class IncompleteHierarchyException extends Exception {
	/**
	 * default ID
	 */
	private static final long	serialVersionUID	= 1L;

	public IncompleteHierarchyException() {
		
	}
}
