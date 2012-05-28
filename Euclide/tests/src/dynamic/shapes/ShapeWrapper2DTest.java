/* file : ShapeWrapper2DTest.java
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


public class ShapeWrapper2DTest extends TestCase {

	/*
	 * Test method for 'dynamic.shapes.ShapeWrapper2D.getShape()'
	 */
	public void testGetShape() {
		Point2D point = new Point2D(10, 10);
		DynamicShape2D pointBuilder = new ShapeWrapper2D(point);
		assertTrue(point.equals(pointBuilder.getShape()));

		StraightLine2D line = new StraightLine2D(10, 10, 1, 2);
		DynamicShape2D lineBuilder = new ShapeWrapper2D(line);
		assertTrue(line.equals(lineBuilder.getShape()));
	}

}
