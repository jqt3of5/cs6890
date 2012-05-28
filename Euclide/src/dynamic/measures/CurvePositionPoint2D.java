/* file : CurvePositionPoint2D.java
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
 * Created on 30 avr. 2006
 *
 */
package dynamic.measures;

import math.geom2d.*;
import math.geom2d.curve.*;

import dynamic.*;

public class CurvePositionPoint2D extends DynamicMeasure2D {

	private DynamicShape2D parent1;
	private DynamicShape2D parent2;
	
	Measure2D measure = new Measure2D(0, "");
	
	public CurvePositionPoint2D(DynamicShape2D curve, DynamicShape2D point){
		this.parent1 = curve;
		this.parent2 = point;
		
		parents.add(curve);
		parents.add(point);
		
		update();
	}
	
	@Override
	public Measure2D getMeasure(){
		return measure;
	}
	
	@Override
	public void update() {
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;

		Shape2D shape;
		
		// extract first point
		shape = parent1.getShape();
		if(!(shape instanceof Curve2D)) return;
		Curve2D curve = (Curve2D) shape;
		
		// extract second point
		shape = parent2.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point = (Point2D) shape;

		// update distance measure
		this.measure.setValue(curve.getPosition(point));
		this.defined = true;
	}
}
