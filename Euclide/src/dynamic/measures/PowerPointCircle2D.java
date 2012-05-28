/* file : PowerPointCircle2D.java
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
 * Created on 05 avr. 2008
 *
 */
package dynamic.measures;

import math.geom2d.*;
import math.geom2d.conic.Circle2D;

import dynamic.*;

public class PowerPointCircle2D extends DynamicMeasure2D {

	private DynamicShape2D parent1;
	private DynamicShape2D parent2;
	
	Measure2D measure = new Measure2D(0);
	
	public PowerPointCircle2D(DynamicShape2D point, DynamicShape2D circle){
		this.parent1 = point;
		this.parent2 = circle;

		parents.add(point);
		parents.add(circle);

		update();
	}
	
	@Override
	public Measure2D getMeasure() {
		return measure;
	}

	@Override
	public void update() {
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;

		Shape2D shape;
		
		// extract point
		shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point = (Point2D) shape;
		
		// extract circle
		shape = parent2.getShape();
		if(!(shape instanceof Circle2D)) return;
		Circle2D circle = (Circle2D) shape;

		// update distance measure
		double r = circle.getRadius();
		double d = point.getDistance(circle.getCenter());
		this.measure.setValue(d*d - r*r);
		
		this.defined = true;
	}
}
