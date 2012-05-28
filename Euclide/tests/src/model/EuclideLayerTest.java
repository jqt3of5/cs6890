package model;

import junit.framework.TestCase;

import org.junit.Test;

public class EuclideLayerTest extends TestCase {

	@Test
	public void testSetShapeIndex() {
		EuclideLayer layer = new EuclideLayer();
		
		EuclideFigure element1 = new EuclideFigure();
		EuclideFigure element2 = new EuclideFigure();
		EuclideFigure element3 = new EuclideFigure();
		
		layer.addFigure(element1);
		layer.addFigure(element2);
		layer.addFigure(element3);
		
		// check number of elements
		assertTrue(layer.getShapeNumber()==3);
		
		// Change to the same position
		layer.setShapeIndex(element2, 1);	
		assertEquals(layer.getShapePosition(element1), 0);
		assertEquals(layer.getShapePosition(element2), 1);
		assertEquals(layer.getShapePosition(element3), 2);

		// change to another position
		layer.setShapeIndex(element2, 0);		
		assertEquals(layer.getShapePosition(element1), 1);
		assertEquals(layer.getShapePosition(element2), 0);
		assertEquals(layer.getShapePosition(element3), 2);
	}

}
