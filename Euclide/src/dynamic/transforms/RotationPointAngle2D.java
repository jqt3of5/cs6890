/* file : RotationPointAngle2D.java
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
 * Created on 9 avr. 2006
 *
 */
package dynamic.transforms;

import math.geom2d.*;
import math.geom2d.AffineTransform2D;
import math.geom2d.transform.*;

import dynamic.*;

public class RotationPointAngle2D extends DynamicTransform2D{

	private DynamicShape2D parent1;
	private DynamicMeasure2D parent2;
	
	AffineTransform2D trans = new AffineTransform2D();
	
	public RotationPointAngle2D(DynamicShape2D point1, DynamicMeasure2D point2){
		super();
		parent1 = point1;
		parent2 = point2;

		parents.add(point1);
		parents.add(point2);

	//	update(); 
	}

	@Override
	public Transform2D getTransform(){
		return trans;
	}
	
	@Override
	public void update() {
		this.defined = false;
		
		// check parents are defined
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;

		// extract rotation center
		Shape2D shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D center = (Point2D) shape;
		
		// extract rotation angle
		Measure2D measure = parent2.getMeasure();
		if(!(measure instanceof AngleMeasure2D)) return;
		double angle = ((AngleMeasure2D) measure).getAngle(AngleUnit.RADIAN);

		// update distance measure
		this.trans = AffineTransform2D.createRotation(center, angle);
		
		this.defined = true;
	}
}
