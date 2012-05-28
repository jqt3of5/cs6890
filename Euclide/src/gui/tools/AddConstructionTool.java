/* file : AddConstructionTool.java
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
 * Created on 22 janv. 2006
 *
 */

package gui.tools;

import gui.EuclideGui;
import gui.EuclideSheetView;
import gui.EuclideTool;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;

import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.polygon.Polygon2D;
import model.EuclideFigure;
import app.EuclideApp;
import dynamic.DynamicObject2D;
import dynamic.DynamicShape2D;
import dynamic.shapes.PolygonEdge2D;

/**
 * @author dlegland
 */
// TODO: add possibility to create several objects with the same ancestors
// Ex: intersection 2 circles, intersection line circle...
public class AddConstructionTool extends EuclideTool {

	/** Class of object to create */
	Class<?> elementClass;

	/** Classes of object ancestors */
	Class<?>[] parentClasses = null;

	/**
	 * Collection of objects which will be used for creating an argument when
	 * this argument is an array.
	 */
	ArrayList<DynamicObject2D> arrayObjects;

	/** Classes of optional parameters */
	Class<?>[] paramClasses;

	/** instances of optional parameters */
	Object[] param;

	/**
	 * @param gui the parent application
	 * @param name the key to retrieve this tool
	 * @param elementClass class of the object to create
	 * @param classes classes of objects which define the created object
	 */
	public AddConstructionTool(EuclideGui gui, String name,
			Class<?> elementClass, Class<?>[] classes) {
		this(gui, name, elementClass, classes, new Object[] {});
	}

	/**
	 * @param gui the parent application
	 * @param name the key to retrieve this tool
	 * @param elementClass class of the object to create
	 * @param classes classes of objects which define the created object
	 * @param parameters optional parameters for created object
	 */
	public AddConstructionTool(EuclideGui gui, String name,
			Class<?> elementClass, Class<?>[] parentClasses, Object[] options) {
		super(gui, name);

		this.elementClass = elementClass;
		this.parentClasses = parentClasses;

		// ensure options is a valid array
		if (options==null)
			options = new Object[0];

		// number of parents and of options
		int parentNumber = parentClasses.length;
		int optionNumber = options.length;
		
		// create arrays
		param = new Object[parentNumber+optionNumber];
		paramClasses = new Class[parentNumber+optionNumber];

		// set up class parameters arrays
		for (int i = 0; i<parentNumber; i++)
			paramClasses[i] = typeClass(parentClasses[i]);

		// set up parameters for creating new object
		for (int i = 0; i<optionNumber; i++) {
			param[parentNumber+i] = options[i];
			paramClasses[parentNumber+i] = typeClass(options[i].getClass());
		}

		// if first class of first parent is an array, initialize variable
		// 'objects'
		if (parentClasses[0].isArray())
			arrayObjects = new ArrayList<DynamicObject2D>();
	}

	/**
	 * Transform a class such as Integer.class into the class of the
	 * corresponding primitive type: int.class.
	 * 
	 * @param aClass
	 * @return
	 */
	private final static Class<?> typeClass(Class<?> aClass) {
		Class<?> theClass = aClass;

		// Transform Classes of types into primitives types
		if (theClass==Integer.class)
			theClass = int.class;
		if (theClass==Double.class)
			theClass = double.class;
		if (theClass==Boolean.class)
			theClass = boolean.class;
		return theClass;
	}

	@Override
	public void mousePressed(MouseEvent evt) {

		// extract view associated with event
		EuclideSheetView view = (EuclideSheetView) evt.getSource();

		// clean up board
		view.setMouseLabel("");

		// class of current parent
		Class<?> parentClass = paramClasses[step-1];
		
		// check if ancestor is a single object or an array
		boolean isSingleParent = true;
		if (parentClass.isArray()) {
			isSingleParent = false;
			parentClass = parentClass.getComponentType();
		}

		// The class of current ancestor should inherits either Shape2D or an
		// Array of Shape2D
		assert Shape2D.class.isAssignableFrom(parentClass):
			"Variable class1 should be a subclass of Shape2D";
		
		// check if left button was clicked
		boolean leftButton = isLeftButtonClicked(evt);

		if (leftButton) {
			// get position of clicked point
			Point2D position = view.getPosition(evt.getX(), evt.getY());
			
			// find or create a shape given this position
			DynamicObject2D ancestor = findParentShape(parentClass, position);
			if (ancestor == null)
				return;
			
			if (isSingleParent) {
				// Single argument: add it to ancestors
				param[step-1] = ancestor;
				step++;
			} else {
				// add current object to list of objects for the array
				addParentShapeToArray(ancestor);
				return;
			}
		} else {
			// Manage right-click case
			if (isSingleParent) {
				// cancel current construction
				step = 1;
				return;
			} else {
				// if list of objects is empty, start again from beginning
				if (arrayObjects.size()==0) {
					step = 1;
					return;
				}
				createParentsArray(parentClass);
				step++;
			}
		}
		
		// update display because new shapes can have been added
		view.setInstruction(this.getInstruction());
		view.repaint();

		// Check if next argument should be asked with a dialog box
		while (step < parentClasses.length + 1) {
			parentClass = paramClasses[step - 1];

			if (parentClass.isArray()) {
				//TODO: be able to manage arrays of non shape elements

			} else if (String.class.isAssignableFrom(parentClass)) {
				// Next argument is a String: open a dialog
				String string = gui.chooseString("Enter the string:");

				// Check valid string
				if (string == null) {
					step = 1;
					return;
				}

				// Set up argument
				param[step-1] = string;
				step++;
			} else {
				// Next object is a shape: wait for next mouse click
				if (Shape2D.class.isAssignableFrom(parentClass))
					return;

				// Next object is not a shape: get from a dialog
				DynamicObject2D obj = gui.chooseDynamicObject(parentClass, 
						getInstruction());

				// if no object found, restart from beginning
				if (obj == null) {
					step = 1;
					return;
				}

				// keep object as parent, and prepare for next input
				param[step-1] = obj;
				step++;
			}
		}

		// =========

		// if added the last object, create new element
		if (step == parentClasses.length + 1) {

			DynamicObject2D newObject = createNewDynamicObject();
			if (newObject == null) {
				step = 1;
				return;
			}

			gui.addNewObject(gui.getCurrentDoc(), newObject);
			step = 1;
		}

		gui.getCurrentDoc().setModified(true);
		gui.getCurrentFrame().updateTitle();
		
		view.repaint();
		view.setInstruction(this.getInstruction());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void mouseMoved(MouseEvent evt) {
		EuclideSheetView view = (EuclideSheetView) evt.getSource();
		EuclideApp appli = this.gui.getAppli();

		view.setMouseLabel("");
		view.clearPreview();

		EuclideFigure elem;
		DynamicShape2D dynamic = null;
		Point2D point = view.getPosition(evt.getX(), evt.getY(), false, false);

		// determines the next expected class
		Class<?> class1 = paramClasses[step-1];
		if (class1.isArray())
			class1 = class1.getComponentType();

		// small control
		if (!Shape2D.class.isAssignableFrom(class1))
			return;

		// unchecked class cast
		Class<? extends Shape2D> shapeClass = (Class<? extends Shape2D>) class1;

		// try to find the shape with desired geometry the closest to the
		// clicked position
		elem = view.getSnappedShape(point, shapeClass);
		if (elem != null) {
			dynamic = elem.getGeometry();

			// change label of mouse cursor
			String shapeName = gui.getAppli().getShapeString(dynamic);
			view.setMouseLabel("this "+shapeName);
		} else {
			// If no shape is found, try to create a new shape automatically
			
			// try to snap a polygon edge
			if (shapeClass.isAssignableFrom(LineSegment2D.class)) {
				elem = view.getSnappedShape(point, Polygon2D.class);
				if (elem != null) {
					DynamicShape2D construction = elem.getGeometry();
					Polygon2D polygon = (Polygon2D) construction.getShape();
					double pos = polygon.getBoundary().project(point) * polygon.getVertexNumber();
					int num = (int) Math.floor(pos);
					
					dynamic = new PolygonEdge2D((DynamicShape2D) construction, num);

					// change label of mouse cursor
					view.setMouseLabel("this polygon edge");
				}
			}
			
			// create a preview point
			if (elem == null && canCreateNewFreePoint(shapeClass)) {
				EuclideFigure previewPoint = view.createNewPoint(point, appli);
				dynamic = previewPoint.getGeometry();
				view.setMouseLabel(gui.getMouseLabel(dynamic));
				view.addToPreview(previewPoint);
			}
		}
		
		// Try to create the new object from already found parents
		if (step==parentClasses.length
				&&DynamicShape2D.class.isAssignableFrom(elementClass)
				&&dynamic!=null) {

			// Different behavior if the last parameter is an array or a
			// single shape
			if (parentClasses[step-1].isArray()) {
				// Create a temporary array with stored shapes + picked shape
				int n = arrayObjects.size();
				DynamicShape2D[] newArray = new DynamicShape2D[n+1];
				for (int i = 0; i<n; i++)
					newArray[i] = (DynamicShape2D) arrayObjects.get(i);
				newArray[n] = dynamic;

				param[step-1] = newArray;
			} else {
				// the last parameter is the picked object
				param[step-1] = dynamic;
			}

			// Try to make a new construction
			DynamicObject2D newObject = createNewDynamicObject();

			// If a new object was constructed, make a preview of it
			if (newObject!=null) {
				EuclideFigure shape = appli
						.createEuclideShape((DynamicShape2D) newObject);
				view.addToPreview(shape);
			}
		}
	}
	
	private boolean isLeftButtonClicked(MouseEvent evt) {
		return (evt.getModifiers()&InputEvent.BUTTON1_MASK)!=0;
	}
	
	private void addParentShapeToArray(DynamicObject2D ancestor) {
		if (ancestor==null)
			return;

		arrayObjects.add(ancestor);
		
		EuclideSheetView view = gui.getCurrentSheetView();
		String instr = String.format("%s [%d]", getInstruction(), 
				arrayObjects.size());
		view.setInstruction(instr);
		// TODO: add selected object/item to selection, for display
		view.repaint();
	}
	
	private void createParentsArray(Class<?> class1) {
		// class of argument
		Class<? extends DynamicObject2D> class2 = 
			EuclideApp.getDynamicClass(class1);

		// add list of objects to param array
		int l = arrayObjects.size();
		Object array = Array.newInstance(class2, l);
		for (int i = 0; i<l; i++)
			Array.set(array, i, arrayObjects.get(i));
		param[step-1] = array;

		//TODO: Should find a way to add an array to the doc.
		
		// Clean object list
		arrayObjects.clear();
	}
	
	@SuppressWarnings("unchecked")
	private DynamicShape2D findParentShape(Class<?> class1, Point2D point) {
		EuclideApp appli = gui.getAppli();
		EuclideSheetView view = gui.getCurrentSheetView();
		
		// unchecked class cast
		Class<? extends Shape2D> shapeClass = (Class<? extends Shape2D>) class1;

		// find the shape with desired geometry the closest to the
		// clicked position
		EuclideFigure shape = view.getSnappedShape(point, shapeClass);
		
		if (shape != null)
			return shape.getGeometry();

		// try to snap a polygon edge
		if (shapeClass.isAssignableFrom(LineSegment2D.class)) {
			shape = view.getSnappedShape(point, Polygon2D.class);
			if (shape != null) {
				DynamicShape2D construction = shape.getGeometry();
				Polygon2D polygon = (Polygon2D) construction.getShape();
				double pos = polygon.getBoundary().project(point);
				int num = (int) Math.floor(pos * polygon.getVertexNumber());
				
				DynamicShape2D poly = new PolygonEdge2D(construction, num);
				gui.addNewObject(gui.getCurrentView().getDoc(), poly);
				
				// change label of mouse cursor
				view.setMouseLabel("this polygon edge");
				return poly;
			}
		}
		
		// If no shape is found, creates a new free point
		if (!canCreateNewFreePoint(shapeClass))
			return null;

		// create a new Free point (possibly on another shape)
		DynamicShape2D geom = view.createNewDynamicPoint(point, appli);
		
		// add the construction to the doc
		gui.addNewObject(gui.getCurrentView().getDoc(), geom);

		return geom;
	}
	
	/**
	 * Conditions for creating new free point: 
	 * (1) parent class extends Point2D or Shape2D, and
	 * (2) and creation of new point is allowed by application
	 */
	private boolean canCreateNewFreePoint(Class<?> shapeClass) {
		if (!gui.getAppli().isAllowingNewFreePoints())
			return false;
		if (Point2D.class.isAssignableFrom(shapeClass))
			return true;
		if (Shape2D.class == shapeClass)
			return true;
		return false;
	}
	
	/**
	 * Create a dynamic object with current selected parents/parameters
	 * @return
	 */
	private DynamicObject2D createNewDynamicObject() {
		EuclideApp appli = gui.getAppli();
		return appli.createObject(elementClass, paramClasses, param);
	}
}
