/* file : BezierCurve4Points2D.java
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
 * Created on 17 janv. 2006
 *
 */
package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.spline.*;
import dynamic.*;

/**
 * A cubic bezier curve defined by 4 control points, in the usual order.
 * @author dlegland
 */
public class BezierCurve4Points2D extends DynamicShape2D {
	
	DynamicShape2D parent1;
	DynamicShape2D parent2;
	DynamicShape2D parent3;
	DynamicShape2D parent4;
	
	private CubicBezierCurve2D curve = new CubicBezierCurve2D();
	
	public BezierCurve4Points2D(DynamicShape2D point1, DynamicShape2D point2, 
			DynamicShape2D point3, DynamicShape2D point4){
		super();
		parent1 = point1;
		parent2 = point2;
		parent3 = point3;
		parent4 = point4;
		
		parents.add(point1);
		parents.add(point2);
		parents.add(point3);
		parents.add(point4);
		parents.trimToSize();
		
	//	update(); 
	}

	@Override
	public Shape2D getShape(){
		return curve;
	}
	
	@Override
	public void update(){
		this.defined = false;
		
		// Check parents are defined
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
		if(!parent3.isDefined()) return;
		if(!parent4.isDefined()) return;

		Shape2D shape;
		
		// extract first point
		shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point1 = (Point2D) shape;
		
		// extract second point
		shape = parent2.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point2 = (Point2D) shape;
		
		// extract third point
		shape = parent3.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point3 = (Point2D) shape;
		
		// extract fourth point
		shape = parent4.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point4 = (Point2D) shape;

		// Update the shape 
		this.curve = new CubicBezierCurve2D(point1, point2, point3, point4);
		this.defined = true;
	}
}
