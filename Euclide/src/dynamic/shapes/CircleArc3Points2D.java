/* file : CircleArc3Points2D.java
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
 * Created on 31 jan. 2006
 *
 */
package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.conic.*;
import dynamic.*;

/**
 * @author dlegland
 */
public class CircleArc3Points2D extends DynamicShape2D {

	DynamicShape2D parent1;
	DynamicShape2D parent2;
	DynamicShape2D parent3;

	CircleArc2D circleArc = new CircleArc2D(0, 0, 0, 0, 0);

	/**
	 * By default, CircleArc3Points2D with only 2 points is a line segment.
	 * This is equivalent to CircleArc3Points2D(point1, point2, 0, 1).
	 * @param point1
	 * @param point2
	 */
	public CircleArc3Points2D(DynamicShape2D point1, DynamicShape2D point2,
			DynamicShape2D point3) {
		super(); // need to call a default constructor
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
	public Shape2D getShape(){
		return circleArc;
	}
			
	@Override
	public void update(){
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
		if(!parent3.isDefined()) return;
				
		Shape2D shape;
		
		shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point1 = (Point2D) shape;

		shape = parent2.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point2 = (Point2D) shape;
		
		shape = parent3.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point3 = (Point2D) shape;
		
		if(Point2D.isColinear(point1, point2, point3)) return;

		// update support circle
		Circle2D circle = (Circle2D) new Circle3Points2D(parent1, parent2,
				parent3).getShape();
		Point2D center = circle.getCenter();
		double radius = circle.getRadius();

		// compute angles
		double a1 = Angle2D.getHorizontalAngle(center, point1);
		double a2 = Angle2D.getHorizontalAngle(center, point2);
		double a3 = Angle2D.getHorizontalAngle(center, point3);
		
		// update circle arc angles
		double startAngle = a1;
		double angleExtent = Angle2D.formatAngle(a3-startAngle);	
		if(!Angle2D.containsAngle(a1, a3, a2))
			angleExtent = angleExtent-2*Math.PI;
		
		this.circleArc = new CircleArc2D(center, radius,
				startAngle, angleExtent);
		this.defined = true;
	}
}
