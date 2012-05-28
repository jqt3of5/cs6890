/*
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
 * Created on 27 Oct. 2007
 *
 */

package dynamic.predicates;

import math.geom2d.*;
import math.geom2d.domain.OrientedCurve2D;

import dynamic.*;

public class IsPointInside2D extends DynamicPredicate2D {

	DynamicShape2D parent1;
	DynamicShape2D parent2;
	
	private boolean res = true;

	public IsPointInside2D(DynamicShape2D point, DynamicShape2D curve){
		this.parent1 = point;
		this.parent2 = curve;
		
		parents.add(point);
		parents.add(curve);
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

		Shape2D shape;
		
		// extract the point
		shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point = (Point2D) shape;
		
		// extract the curve
		shape = parent2.getShape();
		if(!(shape instanceof OrientedCurve2D)) return;
		OrientedCurve2D curve = (OrientedCurve2D) shape;
		
		this.res = curve.isInside(point);
		this.defined = true;
	}
}
