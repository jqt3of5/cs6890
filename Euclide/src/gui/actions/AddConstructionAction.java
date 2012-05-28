/**
 * 
 */
package gui.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import dynamic.DynamicObject2D;

import app.EuclideApp;

import gui.EuclideAction;
import gui.EuclideGui;
import gui.EuclideSheetView;

/**
 * @author dlegland
 *
 */
public class AddConstructionAction extends EuclideAction {
	
	/**	serial field */
	private static final long serialVersionUID = 1L;

	/** Class of object to create */
	Class<?>	elementClass;
	
	/** Classes of object ancestors */
	Class<?>[] 	parentClasses=null;
	
	/** 
	 * Collection of objects which will be used for creating an argument
	 * when this argument is an array.*/
	ArrayList<Object> 	arrayObjects;
	
	/** Classes of optional parameters */ 
	Class<?>[]  paramClasses;
	
	/** instances of optional parameters */
	Object[] param;

	/**
	 * Transform a class such as Integer.class into the class of the 
	 * corresponding primitive type: int.class.
	 * @param aClass
	 * @return
	 */
	private final static Class<?> typeClass(Class<?> aClass){
		Class<?> theClass = aClass;
		
		// Transform Classes of types into primitives types
		if(theClass==Integer.class)	theClass = int.class;
		if(theClass==Double.class)	theClass = double.class;
		if(theClass==Boolean.class)	theClass = boolean.class;			
		return theClass;
	}
	
	
	public AddConstructionAction(EuclideGui app, String name, 
			Class<?> elementClass, Class<?>[] classes){
		this(app, name, elementClass, classes, new Object[]{});
	}

	public AddConstructionAction(EuclideGui app, String name, 
			Class<?> elementClass, Class<?>[] parentClasses,
			Object[] options){		
		super(app, name);
		
		this.elementClass 	= elementClass;
		this.parentClasses 	= parentClasses;
		
		if(options==null) options = new Object[0];
		
		// create arrays
		param = new Object[parentClasses.length + options.length];
		paramClasses = new Class[parentClasses.length + options.length];
		
		// set up class parameters arrays
		for(int i=0; i<parentClasses.length; i++)
			paramClasses[i] = typeClass(parentClasses[i]);
	
		// set up parameters for creating new object
		for(int i=0; i<options.length; i++){
			param[parentClasses.length+i] = options[i];
			paramClasses[parentClasses.length+i] = typeClass(options[i].getClass());
		}
		
		// if first class of first parent is an array, initialize variable
		// 'objects'
		if(parentClasses[0].isArray())
			arrayObjects = new ArrayList<Object>();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		
		EuclideApp appli 		= this.gui.getAppli();		
		EuclideSheetView view 	= this.gui.getCurrentView().getCurrentSheetView();
		
		int step = 1;

		Class<?> class1 = paramClasses[step-1];
		
		// Check if next argument should be asked with a dialog box
		while(step<parentClasses.length+1){
			class1 = paramClasses[step-1];
		
			if(class1.isArray()){
				//TODO: be able to manage arrays of non shape elements
				
			}else{
				DynamicObject2D obj = gui.chooseDynamicObject(class1, "");

				if(obj==null){
					step = 1;
					return;
				}

				param[step-1] = obj;
				step++;

			}
		}

		
		// =========
		
		// if added the last object, create new element

		DynamicObject2D newObject = null;
		newObject = appli.createObject(elementClass, paramClasses, param);
		if(newObject==null){
			step = 1;
			return;
		}

		gui.addNewObject(gui.getCurrentView().getDoc(), newObject);

		view.repaint();		
	}
}
