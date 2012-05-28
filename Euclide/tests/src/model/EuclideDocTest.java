package model;

import java.util.Collection;

import junit.framework.TestCase;

import math.geom2d.Point2D;

import org.junit.Test;

import dynamic.DynamicObject2D;
import dynamic.DynamicShape2D;
import dynamic.shapes.Intersection2StraightObjects2D;
import dynamic.shapes.ShapeWrapper2D;
import dynamic.shapes.StraightLine2Points2D;

public class EuclideDocTest extends TestCase {
	
	public EuclideDocTest(){	
	}
	
	@Test
	public void testCreateDefaultDoc() {
		EuclideDoc doc = EuclideDoc.createDefaultDoc();
		
		assertEquals(0, doc.getPredicates().size());
		assertEquals(0, doc.getVectors().size());
		assertEquals(0, doc.getTransforms().size());
		assertEquals(0, doc.getMeasures().size());
	}
	
	@Test
	public void testGetDescendants() {
		
		EuclideDoc doc = EuclideDoc.createDefaultDoc();
		
		EuclideLayer layer = doc.getFirstSheet().getLayer(0);
		
		// create some geometries
		DynamicShape2D p1 = new ShapeWrapper2D(new Point2D(10, 0));
		DynamicShape2D p2 = new ShapeWrapper2D(new Point2D(0, 10));
		DynamicShape2D p3 = new ShapeWrapper2D(new Point2D(0, 0));
		DynamicShape2D p4 = new ShapeWrapper2D(new Point2D(10, 10));
		
		DynamicShape2D l1 = new StraightLine2Points2D(p1, p2);
		DynamicShape2D l2 = new StraightLine2Points2D(p3, p4);
		
		DynamicShape2D pi = new Intersection2StraightObjects2D(l1, l2);
		
		// add dynamic objects to the document
		doc.addDynamicObject(p1, "p1");
		doc.addDynamicObject(p2, "p2");
		doc.addDynamicObject(p3, "p3");
		doc.addDynamicObject(p4, "p4");
		doc.addDynamicObject(l1, "l1");
		doc.addDynamicObject(l2, "l2");
		doc.addDynamicObject(pi, "pi");

		// adds corresponding shapes to the doc
		doc.addFigure(new EuclideFigure(p1, "p1", "p1"), layer);
		doc.addFigure(new EuclideFigure(p2, "p2", "p2"), layer);
		doc.addFigure(new EuclideFigure(p3, "p3", "p3"), layer);
		doc.addFigure(new EuclideFigure(p4, "p4", "p4"), layer);
		doc.addFigure(new EuclideFigure(l1, "l1", "l1"), layer);
		doc.addFigure(new EuclideFigure(l2, "l1", "l1"), layer);
		doc.addFigure(new EuclideFigure(pi, "pi", "pi"), layer);
		
	
		Collection<DynamicObject2D> set;

		set = doc.getDynamicObjects();
		assertEquals(7, set.size());
		
		set = doc.getChildren(p1);
		assertEquals(1, set.size());
		
		set = doc.getChildren(l1);
		assertEquals(1, set.size());
		
		set = doc.getDescendants(p1);
		assertEquals(2, set.size());
	
		doc.removeDynamicObject(pi);
		set = doc.getDynamicObjects();
		assertEquals(6, set.size());
		
		doc.removeDynamicObject(p1);
		set = doc.getDynamicObjects();
		assertEquals(4, set.size());
	}

}
