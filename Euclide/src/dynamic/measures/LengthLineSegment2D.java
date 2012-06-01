/* file : LengthLineSegment2D.java
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
 * Created on 16 avr. 2006
 *
 */
package dynamic.measures;

import math.geom2d.*;
import math.geom2d.line.*;

import dynamic.*;

public class LengthLineSegment2D extends DynamicMeasure2D {

	private DynamicShape2D parent1;
	
	Measure2D measure = new LengthMeasure2D(0, LengthUnit.MILLIMETER);
	
	public LengthLineSegment2D(DynamicShape2D segment){
		this.parent1 = segment;

		parents.add(segment);
		parents.trimToSize();
		
		//update();
	}
	
	@Override
	public Measure2D getMeasure() {
		return measure;
	}

	@Override
	public void update() {
		this.defined = false;
		// check parent is defined
		if(!parent1.isDefined()) return;
	
		// extract line segment
		Shape2D shape = parent1.getShape();
		if(!(shape instanceof LineSegment2D)) return;
		LineSegment2D segment = (LineSegment2D) shape;
		
		// update distance measure
		this.measure.setValue(segment.getLength());
		
		// check bound is correct
		if(Double.isInfinite(this.measure.getValue())) 
			return;
		this.defined = true;		
	}
}
