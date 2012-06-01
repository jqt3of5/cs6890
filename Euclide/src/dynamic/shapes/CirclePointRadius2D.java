/* file : CirclePointRadius2D.java
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
 * Created on 5 mars 2006
 *
 */
package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.conic.*;
import dynamic.*;

/**
 * @author dlegland
 */
public class CirclePointRadius2D extends DynamicShape2D {

	DynamicShape2D parent1;
	DynamicMeasure2D parent2;
	
	private Circle2D circle = new Circle2D(0, 0, 0);

	public CirclePointRadius2D(DynamicShape2D center, DynamicMeasure2D radius){
		super();
		this.parent1 = center;
		this.parent2 = radius;

		parents.add(center);
		parents.add(radius);
		parents.trimToSize();

		//update(); 
	}
	
	@Override
	public Shape2D getShape(){
		return circle;
	}
	
	@Override
	public void update(){
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;

		Shape2D shape;
		Measure2D measure;
		
		// extract first point
		shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point1 = (Point2D) shape;
		
		// extract circle radius
		measure = parent2.getMeasure();
		double radius = measure.getValue();
		
		circle = new Circle2D(point1, radius);
		this.defined = true;
	}
}
