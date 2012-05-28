/* file : TestArrayClass.java
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
 * Created on 1 avr. 2006
 *
 */

import java.lang.reflect.*;
import math.geom2d.*;

public class TestArrayClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Point2D p1 = new Point2D(10, 20);
		Point2D p2 = new Point2D(20, 10);
		
		Object array = Array.newInstance(Point2D.class, 2);
		Array.set(array, 0, p1);
		Array.set(array, 0, p2);
	}

}
