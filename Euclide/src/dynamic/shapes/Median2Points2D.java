/* file : Median2Points2D.java
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
 * Created on 2 feb. 2006
 *
 */
package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.line.*;

import dynamic.*;

/**
 * The Median of two points, which is the straight line containing all points
 * at equal distance of the 2 input points.
 * @author dlegland
 */
public class Median2Points2D extends DynamicShape2D {

	DynamicShape2D parent1;
	DynamicShape2D parent2;
	
	private StraightLine2D line = new StraightLine2D();

	/**
	 * By default, LineArc2Points2D with only 2 points is a line segment.
	 * This is equivalent to LineArc2Points2D(point1, point2, 0, 1).
	 * @param point1
	 * @param point2
	 */
	public Median2Points2D(DynamicShape2D point1, DynamicShape2D point2) {
		super();
		this.parent1 = point1;
		this.parent2 = point2;

		parents.add(point1);
		parents.add(point2);
		//parents.trimToSize();

		//update();
	}
	
	@Override
	public Shape2D getShape(){
		return line;
	}
	
	/* (non-Javadoc)
	 * @see math.geom2d.dynamic.DynamicShape2D#update()
	 */
	@Override
	public void update() {
		this.defined = false;
		
		// Check parents are defined
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;

		Shape2D shape;
		
		// extract first point
		shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point1 = (Point2D) shape;
		
		// extract second point
		shape = parent2.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point2 = (Point2D) shape;
		
		// compute parameters of new line
		double x0 = (point1.getX()+point2.getX())*.5;
		double y0 = (point1.getY()+point2.getY())*.5;
		double dx = point1.getY()-point2.getY();
		double dy = point2.getX()-point1.getX();

		// Check the median can be defined
		if(Math.hypot(dx, dy)<Shape2D.ACCURACY)
			return;
		
		// update new line
		line = new StraightLine2D(x0, y0, dx, dy);
		this.defined = true;		
	}
}
