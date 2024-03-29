/* file : Bisector3Points2D.java
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
import math.geom2d.line.*;

import dynamic.*;

/**
 * @author dlegland
 */
public class Bisector3Points2D extends DynamicShape2D {

	DynamicShape2D parent1;
	DynamicShape2D parent2;
	DynamicShape2D parent3;

	private Ray2D ray = new Ray2D();

	/**
	 * By default, Bisector3Points2D with only 2 points is a line segment.
	 * This is equivalent to Bisector3Points2D(point1, point2, 0, 1).
	 * @param point1
	 * @param point2
	 */
	public Bisector3Points2D(DynamicShape2D point1, DynamicShape2D point2, DynamicShape2D point3) {
		super();
		this.parent1 = point1;
		this.parent2 = point2;
		this.parent3 = point3;
		
		parents.add(point1);
		parents.add(point2);
		parents.add(point3);
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
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;

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

		double x0 = point2.getX(); 
		double y0 = point2.getY();
		double angle = Angle2D.getAngle(point1, point2, point3);
		angle = Angle2D.getHorizontalAngle(point2, point1) + angle*.5;
		double dx = Math.cos(angle);
		double dy = Math.sin(angle);
		
		this.ray = new Ray2D(x0, y0, dx, dy);
		this.defined = true;
	}
}
