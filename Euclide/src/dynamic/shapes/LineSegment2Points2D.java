/* file : LineSegment2Points2D.java
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
 * Created on 22 d�c. 2005
 *
 */
package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.line.*;
import dynamic.*;

/**
 * @author dlegland
 */
public class LineSegment2Points2D extends DynamicShape2D {

	DynamicShape2D parent1;
	DynamicShape2D parent2;
	
	LineSegment2D line = new LineSegment2D(0, 0, 0, 0);
	
	/**
	 * By default, LineSegment2Points2D with only 2 points is a line segment.
	 * This is equivalent to LineSegment2Points2D(point1, point2, 0, 1).
	 * @param point1
	 * @param point2
	 */
	public LineSegment2Points2D(DynamicShape2D point1, DynamicShape2D point2) {
		super();
		this.parent1 = point1;
		this.parent2 = point2;

		parents.add(point1);
		parents.add(point2);
		parents.trimToSize();

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
		double x1 = point1.getX();
		double y1 = point1.getY();
		double x2 = point2.getX();
		double y2 = point2.getY();		
		double dx = x2-x1;
		double dy = y2-y1;
		
		// ensure a line is created, even if points are on same location
		if(dx*dx+dy*dy<Shape2D.ACCURACY) dx = 1;
		
		line = new LineSegment2D(x1, y1, x2, y2);
		this.defined = true;
	}

}
