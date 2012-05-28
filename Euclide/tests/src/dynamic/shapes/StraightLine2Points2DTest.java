/* file : StraightLine2Points2DTest.java
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
 * Created on 22 avr. 2007
 *
 */
package dynamic.shapes;

import junit.framework.TestCase;
import dynamic.*;
import math.geom2d.*;
import math.geom2d.line.*;


public class StraightLine2Points2DTest extends TestCase {

	/*
	 * Test method for 'dynamic.shapes.DefaultShapeBuilder.getShape()'
	 */
	public void testGetShape() {
		Point2D p1 = new Point2D(0, 0);
		Point2D p2 = new Point2D(10, 0);
		Point2D p3 = new Point2D(0, 10);
		Point2D p4 = new Point2D(10, 10);
		DynamicShape2D builder1;
		DynamicShape2D builder2;
		
		builder1 = new StraightLine2Points2D(
				new ShapeWrapper2D(p1), new ShapeWrapper2D(p2));
		builder2 = new StraightLine2Points2D(
				new ShapeWrapper2D(p3), new ShapeWrapper2D(p4));
		
		StraightLine2D line1 = new StraightLine2D(p1, p2);
		StraightLine2D line2 = new StraightLine2D(p3, p4);
		
		assertTrue(line1.equals(builder1.getShape()));
		assertTrue(line2.equals(builder2.getShape()));
		assertTrue(!line2.equals(builder1.getShape()));
		assertTrue(!line1.equals(builder2.getShape()));
	}
		
}
