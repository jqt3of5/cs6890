/* file : Angle2Lines2D.java
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

public class Angle2Lines2D extends DynamicMeasure2D {

	private DynamicShape2D parent1;
	private DynamicShape2D parent2;
	
	AngleMeasure2D measure = new AngleMeasure2D();
	
	public Angle2Lines2D(DynamicShape2D line1, DynamicShape2D line2){
		this.parent1 = line1;
		this.parent2 = line2;
		
		parents.add(line1);
		parents.add(line2);
		
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
		if(!(shape instanceof LinearShape2D)) return;
		LinearShape2D line1 = (LinearShape2D) shape;
		
		// extract second point
		shape = parent2.getShape();
		if(!(shape instanceof LinearShape2D)) return;
		LinearShape2D line2 = (LinearShape2D) shape;

		// update distance measure
		this.measure.setValue(Angle2D.getAngle(line1, line2));
		this.defined = true;
	}
}
