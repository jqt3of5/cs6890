/* file : TangentRayCurvePoint2D.java
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

import util.CurveUtils;
import math.geom2d.*;
import math.geom2d.curve.*;
import math.geom2d.line.*;

import dynamic.*;

/**
 * @author dlegland
 */
public class TangentRayCurvePoint2D extends DynamicShape2D {

	DynamicShape2D parent1;
	DynamicShape2D parent2;
	
	private Ray2D ray = new Ray2D();
	
	/**
	 * Tangent to a curve at a given point. 
	 * @param curve
	 * @param point
	 */
	public TangentRayCurvePoint2D(DynamicShape2D curve, DynamicShape2D point) {
		this.parent1 = curve;
		this.parent2 = point;

		parents.add(curve);
		parents.add(point);
		parents.trimToSize();

		update();
	}	

	@Override
	public Shape2D getShape(){
		return ray;
	}
	
	@Override
	public void update(){
		this.defined = false;
		
		// check parents are defined
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
		
		// extract the parent curve
		Shape2D shape;
		shape = parent1.getShape();
		if(!(shape instanceof Curve2D)) return;
		Curve2D curve = (Curve2D) shape;
		
		// extract the parent point
		shape = parent2.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point = (Point2D) shape;
		
		// get position of the point on the curve
		double t = curve.getPosition(point);
		Point2D p0 = curve.getPoint(t);
		
		// compute tangent vector
		Vector2D vect = CurveUtils.getTangentVector(curve, t);
		if (vect==null)
			return;
		
		// create the new ray
		this.ray = new Ray2D(p0, vect);
		this.defined = true;
	}
}
