/* file : Intersection2StraightObjects2D.java
 * 
 * Project : geometry
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
 * Created on 22 déc. 2005
 *
 */
package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.line.*;

import dynamic.*;

/**
 * @author dlegland
 */
public class Intersection2StraightObjects2D extends DynamicShape2D {

	DynamicShape2D parent1;
	DynamicShape2D parent2;
	
	private Point2D point = new Point2D();

	/**
	 * @param point1
	 * @param point2
	 */
	public Intersection2StraightObjects2D(DynamicShape2D line1, DynamicShape2D line2) {
		this.parent1 = line1;
		this.parent2 = line2;

		parents.add(line1);
		parents.add(line2);
		parents.trimToSize();

		update();
	}

	@Override
	public Shape2D getShape(){
		return point;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.dynamic.DynamicShape2D#update()
	 */
	@Override
	public void update() {
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;

		Shape2D shape;
		
		// extract first line
		shape = parent1.getShape();
		if(!(shape instanceof AbstractLine2D)) return;
		AbstractLine2D line1 = (AbstractLine2D) shape;
		
		// extract second line
		shape = parent2.getShape();
		if(!(shape instanceof LinearShape2D)) return;
		LinearShape2D line2 = (LinearShape2D) shape;
		
		// check parallelness
		if(line1.isParallel(line2)) return;
		
		// compute intersection point, if exists
		Point2D location = line1.getIntersection(line2);
		if(location==null)
			return;
		
		// update geometry
		this.point = location;
		this.defined=true;
	}

}
