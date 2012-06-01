/* file : StraightLinePointAngle2D.java
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

import dynamic.*;
import math.geom2d.line.*;

import math.geom2d.*;

/**
 * @author dlegland
 */
public class StraightLinePointAngle2D extends DynamicShape2D {

	DynamicShape2D 		parent1;
	DynamicMeasure2D 	parent2;

	private StraightLine2D line = new StraightLine2D();

	/**
	 * By default, LineArc2Points2D with only 2 points is a line segment.
	 * This is equivalent to LineArc2Points2D(point1, point2, 0, 1).
	 * @param point1
	 * @param point2
	 */
	public StraightLinePointAngle2D(DynamicShape2D point, DynamicMeasure2D angle) {
		super();
		this.parent1 = point;
		this.parent2 = angle;

		parents.add(point);
		parents.add(angle);
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

		// extract first point
		Shape2D shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point = (Point2D) shape;
		
		// extract second point
		Measure2D measure = parent2.getMeasure();
		if(!(measure instanceof AngleMeasure2D)) return;
		
		double angle = ((AngleMeasure2D)measure).getAngle(AngleUnit.RADIAN);
		
		// compute parameters of new line
		line = new StraightLine2D(point, angle);
		
		this.defined=true;
	}

}
