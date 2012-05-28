/* file : IsColinearPoints2D.java
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
 * Created on 28 Oct. 2007
 *
 */

package dynamic.predicates;

import math.geom2d.*;
import dynamic.*;

/**
 * Check if 3 points are colinear or not.
 * @author dlegland
  */
public class IsColinearPoints2D extends DynamicPredicate2D {

	DynamicShape2D parent1;
	DynamicShape2D parent2;
	DynamicShape2D parent3;
	
	private boolean res = true;

	public IsColinearPoints2D(DynamicShape2D point1, DynamicShape2D point2, DynamicShape2D point3){
		this.parent1 = point1;
		this.parent2 = point2;
		this.parent3 = point3;
		
		parents.add(point1);
		parents.add(point2);
		parents.add(point3);
		parents.trimToSize();
		
		update();
	}
	
	@Override
	public boolean getResult() {
		return res;
	}

	@Override
	public void update() {
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
		if(!parent3.isDefined()) return;

		Shape2D shape;
		
		// extract the point 1
		shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point1 = (Point2D) shape;
		
		// extract the point 2
		shape = parent2.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point2 = (Point2D) shape;
		
		// extract the point 3
		shape = parent3.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point3 = (Point2D) shape;
		
		// update the predicate
		this.res = Point2D.isColinear(point1, point2, point3);
		this.defined = true;
	}

}
