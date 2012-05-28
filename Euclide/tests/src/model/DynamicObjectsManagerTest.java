/**
 * File: 	DynamicObjectsManagerTest.java
 * Project: Euclide-DepOrder
 * 
 * Distributed under the LGPL License.
 *
 * Created: 5 févr. 2011
 */
package model;

import java.util.Collection;

import junit.framework.TestCase;

import org.junit.Test;

import dynamic.DynamicArray2D;
import dynamic.DynamicObject2D;
import dynamic.DynamicShape2D;
import dynamic.shapes.CurveSetNCurves2D;
import dynamic.shapes.FreePoint2D;
import dynamic.shapes.LineSegment2Points2D;


/**
 * @author dlegland
 *
 */
public class DynamicObjectsManagerTest extends TestCase {

	/**
	 * Test method for {@link model.DynamicObjectsManager#updateDependencies()}.
	 */
	@Test
	public void testHierarchyWithAnArray() {
		DynamicObjectsManager manager =
			new DynamicObjectsManager("manager", "manager");
		
		FreePoint2D p1 = new FreePoint2D(2, 3);
		p1.setTag("p1");
		FreePoint2D p2 = new FreePoint2D(3, 5);
		p2.setTag("p2");
		FreePoint2D p3 = new FreePoint2D(4, 4);
		p3.setTag("p3");
		
		LineSegment2Points2D line1 = new LineSegment2Points2D(p1, p2);
		line1.setTag("line1");
		LineSegment2Points2D line2 = new LineSegment2Points2D(p2, p3);
		line2.setTag("line2");
		LineSegment2Points2D line3 = new LineSegment2Points2D(p3, p1);
		line3.setTag("line3");
		
		DynamicArray2D array = new DynamicArray2D(new DynamicShape2D[]{
				line1, line2, line3
		});
		array.setTag("array1");
		CurveSetNCurves2D curve = new CurveSetNCurves2D(array);
		curve.setTag("pCurve");
		
		manager.addDynamicObject(p1);
		manager.addDynamicObject(p2);
		manager.addDynamicObject(p3);
		manager.addDynamicObject(line1);
		manager.addDynamicObject(line2);
		manager.addDynamicObject(line3);
		manager.addDynamicObject(array);
		manager.addDynamicObject(curve);
		
		Collection<DynamicObject2D> children;
		
		children = manager.getChildren(p1);
		assertTrue(children.contains(line1));
		assertTrue(children.contains(line3));
		assertFalse(children.contains(line2));
		assertFalse(children.contains(curve));
		
		children = manager.getDescendants(p1);
		assertTrue(children.contains(line1));
		assertTrue(children.contains(line3));
		assertFalse(children.contains(line2));
		assertTrue(children.contains(curve));
	}
	
	@Test
	public void testRemoveTaggedObject() {
		DynamicObjectsManager manager =
			new DynamicObjectsManager("manager", "manager");
		
		FreePoint2D p1 = new FreePoint2D(2, 3);
		p1.setTag("p1");
		FreePoint2D p2 = new FreePoint2D(3, 5);
		p2.setTag("p2");

		manager.addDynamicObject(p1);
		manager.addDynamicObject(p2);
		
		manager.removeTaggedObject(p1);
		
		assertFalse(manager.containsTaggedObject(p1.getTag()));
		assertFalse(manager.getDynamicObjects().contains(p1));
	}
}
