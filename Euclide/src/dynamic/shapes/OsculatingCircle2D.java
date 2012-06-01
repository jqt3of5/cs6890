/* file : OsculatingCircle2D.java
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
 * Created on 13/05/2007
 *
 */
package dynamic.shapes;

import util.CurveUtils;
import math.geom2d.*;
import math.geom2d.conic.*;
import math.geom2d.curve.*;
import dynamic.*;

/**
 * @author dlegland
 */
public class OsculatingCircle2D extends DynamicShape2D {

	DynamicShape2D parent1;
	DynamicShape2D parent2;
	
	private Circle2D circle = new Circle2D();
	
	/**
	 * Tangent to a curve at a given point. 
	 * @param curve
	 * @param point
	 */
	public OsculatingCircle2D(DynamicShape2D curve, DynamicShape2D point) {
		this.parent1 = curve;
		this.parent2 = point;

		parents.add(curve);
		parents.add(point);
		parents.trimToSize();

	//	update();
	}
	

	@Override
	public Shape2D getShape(){
		return circle;
	}
			
	@Override
	public void update(){
		this.defined = false;
		
		// check parents are defined
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
		
		// extract parent curve
		Shape2D shape;
		shape = parent1.getShape();
		if(!(shape instanceof Curve2D)) return;
		Curve2D curve = (Curve2D) shape;
		
		// extract parent point
		shape = parent2.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point = (Point2D) shape;
		
		// compute position of point on the curve
		double t = curve.project(point);
		
		// extract tangent direction
		Vector2D tan = CurveUtils.getTangentVector(curve, t);
		if(tan==null) return;
		Vector2D nor = new Vector2D(-tan.getY(), tan.getX());
		nor = nor.getNormalizedVector();
		
		// extract curvature
		double r = getCurvature(curve, t);
		if(Math.abs(r)<Shape2D.ACCURACY) return;
		
		// compute parameters of osculating circle
		Point2D p0 = curve.getPoint(t);
		Point2D center = new Point2D(
				p0.getX()+nor.getX()/r, 
				p0.getY()+nor.getY()/r);
		
		// create the circle
		circle = new Circle2D(center, 1/Math.abs(r));
		this.defined = true;
	}

	/**
	 * Compute the curvature of the curve, either on the curve if it is
	 * smooth, or by recursively inspecting inner curve of a curve set.
	 */
	private double getCurvature(Curve2D curve, double t) {
		if (curve instanceof SmoothCurve2D) {
			return ((SmoothCurve2D) curve).getCurvature(t);
		} else if (curve instanceof CurveSet2D){
			CurveSet2D<?> set = (CurveSet2D<?>) curve;
			return getCurvature(set.getChildCurve(t), 
					set.getLocalPosition(t));
		} else {
			System.err.println("unknown type of curve");
			return 0;
		}
	}
}
