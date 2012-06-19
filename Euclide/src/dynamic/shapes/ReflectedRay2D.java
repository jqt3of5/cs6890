/* file : ReflectedRay2D.java
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
 * Created on 1 f�vr. 2007
 *
 */
package dynamic.shapes;

import java.util.Collection;
import java.util.Iterator;

import util.CurveUtils;

import math.geom2d.*;
import math.geom2d.curve.*;
import math.geom2d.line.*;

import dynamic.*;

public class ReflectedRay2D extends DynamicShape2D {

	DynamicShape2D parent1;
	DynamicShape2D parent2;
	
	private Ray2D ray = new Ray2D();

	/**
	 * By default, LineArc2Points2D with only 2 points is a line segment.
	 * This is equivalent to LineArc2Points2D(ray, curve, 0, 1).
	 * @param ray
	 * @param curve
	 */
	public ReflectedRay2D(DynamicShape2D ray, DynamicShape2D curve) {
		this.parent1 = ray;
		this.parent2 = curve;

		parents.add(ray);
		parents.add(curve);
		//parents.trimToSize();

		//update();
	}
	
	@Override
	public Shape2D getShape(){
		return ray;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.dynamic.DynamicShape2D#update()
	 */
	@Override
	public void update() {
		this.defined = false;
		
		// check parents are defined
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;

		// extract parent line
		Shape2D shape;
		shape = parent1.getShape();
		if(!(shape instanceof LinearShape2D)) return;
		LinearShape2D line = (LinearShape2D) shape;
		
		// extract parent curve
		shape = parent2.getShape();
		if(!(shape instanceof Curve2D)) return;
		Curve2D curve = (Curve2D) shape;
		
		
		Collection<Point2D> pts = curve.getIntersections(line);
		if(pts.size()==0) return;
		
		Iterator<Point2D> iter = pts.iterator();
		
		Point2D point = iter.next();
		double pos = curve.getPosition(point);
		
		// special case were first intersection point is the ray origin
		if(point.getDistance(line.getFirstPoint())<Shape2D.ACCURACY){
			if (pts.size()==1)
				return;
			point = iter.next();
			pos = curve.getPosition(point);
		}
		
		// compute tangent of the curve, then the normal
		Vector2D tangent = CurveUtils.getTangentVector(curve, pos);
		if(tangent==null)
			return;
		Vector2D normal = new Vector2D(tangent.getY(), -tangent.getX());
		
		// director vector of the ray
		Vector2D vectRay = line.getVector();
		Vector2D invert = new Vector2D(-vectRay.getX(), -vectRay.getY());
		
		// compute new angle of the ray
		double theta = Angle2D.getAngle(invert, normal);
		double angle = line.getHorizontalAngle() + 2*theta + Math.PI;
			
		// update internal data of reflected ray
		this.ray = new Ray2D(point.getX(), point.getY(),
				Math.cos(angle), Math.sin(angle));
		this.defined = true;
	}
	
}
