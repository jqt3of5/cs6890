/* file : Rotation3Points2D.java
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

public class Rotation3Points2D extends DynamicTransform2D{

	private DynamicShape2D parent1;
	private DynamicShape2D parent2;
	private DynamicShape2D parent3;
	
	AffineTransform2D trans = new AffineTransform2D();
	
	public Rotation3Points2D(DynamicShape2D point1, DynamicShape2D point2, DynamicShape2D point3){
		super();
		parent1 = point1;
		parent2 = point2;
		parent3 = point3;

		parents.add(point1);
		parents.add(point2);
		parents.add(point3);

	//	update(); 
	}

	@Override
	public Transform2D getTransform(){
		return trans;
	}
	
	@Override
	public void update() {
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
		if(!parent3.isDefined()) return;

		Shape2D shape;
		
		// extract first point
		shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point1 = (Point2D) shape;
		
		// extract second point
		shape = parent2.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point2 = (Point2D) shape;
		
		// extract second point
		shape = parent3.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point3 = (Point2D) shape;
		
		double theta = Angle2D.getAngle(point2, point1, point3);
		this.trans = AffineTransform2D.createRotation(point1, theta);
		this.defined = true;
	}
}
