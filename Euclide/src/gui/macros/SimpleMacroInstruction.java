/**
 * File: 	SimpleMacroInstruction.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 12 août 09
 */
package gui.macros;

import gui.EuclideGui;

import java.util.ArrayList;
import java.util.Collection;

import app.EuclideApp;
import dynamic.DynamicArray2D;
import dynamic.DynamicMeasure2D;
import dynamic.DynamicObject2D;
import dynamic.DynamicPredicate2D;
import dynamic.DynamicShape2D;
import dynamic.DynamicTransform2D;
import dynamic.DynamicVector2D;


/**
 * An instruction of a simple macro. Contains a set of parent id, a set of
 * parameters, and the name of the construction, that should inherits the
 * DynamicObject2D class.
 * @author dlegland
 *
 */
public class SimpleMacroInstruction {

	Class<? extends DynamicObject2D> constructionClass;
	
	int[] parentIDs;
	Object[] params;
	
	boolean debug = true;
	
	public SimpleMacroInstruction(
			Class<? extends DynamicObject2D> constructionClass,
			int[] parentIDs, Object[] params) {		
		this.constructionClass = constructionClass;
		this.parentIDs 	= parentIDs;
		this.params 	= params;
	}
	
	public SimpleMacroInstruction(
			Class<? extends DynamicObject2D> constructionClass,
			int[] parentIDs, Collection<? extends Object> params) {		
		this.constructionClass = constructionClass;
		this.parentIDs 	= parentIDs;
		this.params 	= params.toArray();
	}
	
	/**
	 * Executes the instruction. The following actions are made:
	 * <ul>
	 * <li>create an array of arguments</li>
	 * <li>add the registered parents</li>
	 * <li>eventually add some optional parameters</li>
	 * <li>Create the new dynamic object</li>
	 * <li>add it to the list of parents</li>
	 * </ul>
	 */
	public void execute(EuclideGui gui, ArrayList<DynamicObject2D> dynamics) {
		// get the parent application
		EuclideApp appli = gui.getAppli();

		// Special case of DynamicArray2D
		if (DynamicArray2D.class.isAssignableFrom(this.constructionClass)) {
			DynamicObject2D[] elements = new DynamicObject2D[parentIDs.length];
			for(int i=0; i<parentIDs.length; i++) {
				elements[i] = dynamics.get(parentIDs[i]);
			}
			
			// create array of classes, with only one element which refers to
			// the class of an array
			Class<?>[] classes = new Class<?>[]{
					(new DynamicObject2D[parentIDs.length]).getClass()};
			
			// call the method in appli for creating the new Dynamic Object
			DynamicObject2D construction = 
				appli.createObject(constructionClass, classes, new Object[]{elements});
			
			// add the created construction to the list
			dynamics.add(construction);
			
			return;
		}
		
		// array of arguments
		Object[] arguments = new Object[parentIDs.length + params.length];
				
		// array of classes
		Class<?>[] classes = new Class<?>[arguments.length];
		
		// initialize arrays for parents (DynamicObject2D)
		for(int i=0; i<parentIDs.length; i++) {
			DynamicObject2D parent = dynamics.get(parentIDs[i]);
			arguments[i] = parent;
			if (parent instanceof DynamicShape2D)
				classes[i] = DynamicShape2D.class;
			else if (parent instanceof DynamicTransform2D)
				classes[i] = DynamicTransform2D.class;
			else if (parent instanceof DynamicMeasure2D)
				classes[i] = DynamicMeasure2D.class;
			else if (parent instanceof DynamicVector2D)
				classes[i] = DynamicVector2D.class;
			else if (parent instanceof DynamicPredicate2D)
				classes[i] = DynamicPredicate2D.class;
			else if (parent instanceof DynamicArray2D) {
				// Process special case of an array
				Collection<? extends DynamicObject2D> arrayParents = 
					((DynamicArray2D) parent).getParents();
				DynamicObject2D sample = arrayParents.iterator().next();
				// Try to determine class of parameter
				if (sample instanceof DynamicShape2D){
					classes[i] = DynamicShape2D[].class;
					DynamicShape2D[] array = new DynamicShape2D[arrayParents.size()];
					int j=0;
					for(DynamicObject2D obj : arrayParents)
						array[j++] = (DynamicShape2D) obj;
					arguments[i] = array;
				} else {
					classes[i] = DynamicObject2D[].class;
					DynamicObject2D[] array = new DynamicObject2D[arrayParents.size()];
					int j=0;
					for(DynamicObject2D obj : arrayParents)
						array[j++] = obj;
					arguments[i] = array;
				}
			} else {
				System.out.println("unknown dynamic object class");
				classes[i] = DynamicObject2D.class;
			}
		}
		
		// initialize arrays for parameters (int, double, boolean, or string).
		for(int i=0; i<params.length; i++) {
			arguments[parentIDs.length+i] = params[i];
			
			classes[parentIDs.length+i] = 
				EuclideApp.getPrimitiveClass(params[i].getClass());
		}

		if (debug) {
			System.out.println("create object of class " + constructionClass +
			" with parent classes:");
			for(int i=0; i<parentIDs.length; i++)
				System.out.println(" - " + 
						dynamics.get(parentIDs[i]).getClass().getName());
			System.out.println("and parameters:");
			for(int i=0; i<params.length; i++)
				System.out.println(" - " + params[i]);
		}
		
		// call the method in appli for creating the new Dynamic Object
		DynamicObject2D construction = 
			appli.createObject(constructionClass, classes, arguments);
		
		// add the created construction to the list
		dynamics.add(construction);
		
		//TODO: create tag for the construction
	}
}
