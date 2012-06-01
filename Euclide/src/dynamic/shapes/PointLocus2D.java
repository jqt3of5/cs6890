/* file : PointLocus2D.java
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
 * Created on 17 avr. 2006
 *
 */
package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.point.PointArray2D;
import dynamic.*;

/**
 * A set of points marking successive position of a moving point.
 * @author dlegland
 */
public class PointLocus2D extends DynamicShape2D {
	
	DynamicShape2D parent1;
	
	PointArray2D points = new PointArray2D();
	
	public PointLocus2D(DynamicShape2D point){
		this.parent1 = point;

		parents.add(point);
		parents.trimToSize();
		this.defined = true;

		//update();
	}
		
	@Override
	public Shape2D getShape(){
		return points;
	}
	
	public void clearLocus() {
		points.clearPoints();
	}
	
	@Override
	public void update() {
		// check parent is defined
		if(!parent1.isDefined())
			return;
		
		// extract parent shape
		Shape2D shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point = (Point2D) shape;
		
		// update the locus
		points.addPoint(new Point2D(point.getX(), point.getY()));
	}
}
