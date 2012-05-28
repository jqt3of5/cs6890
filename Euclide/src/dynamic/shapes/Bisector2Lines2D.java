/* file : Bisector2Lines2D.java
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
 * Created on 22 déc. 2005
 *
 */
package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.line.*;

import dynamic.*;
import dynamic.predicates.BooleanWrapper2D;

/**
 * @author dlegland
 */
public class Bisector2Lines2D extends DynamicShape2D{

	DynamicShape2D parent1;
	DynamicShape2D parent2;
	DynamicPredicate2D parent3;

	private Ray2D ray = new Ray2D();

	/**
	 * @param point1
	 * @param point2
	 */
	public Bisector2Lines2D(DynamicShape2D line1, DynamicShape2D line2) {
		this.parent1 = line1;
		this.parent2 = line2;
		this.parent3 = new BooleanWrapper2D(true);
			
		parents.add(line1);
		parents.add(line2);
		parents.trimToSize();
		
		update();
	}
	
	public Bisector2Lines2D(DynamicShape2D line1, DynamicShape2D line2, 
			DynamicPredicate2D pred) {
		this.parent1 = line1;
		this.parent2 = line2;
		this.parent3 = pred;
		
		parents.add(line1);
		parents.add(line2);
		parents.add(pred);
		parents.trimToSize();
		
		update();
	}


	@Override
	public Shape2D getShape(){
		return ray;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.dynamic.DynamicShape2D#update()
	 */
	@Override
	public void update() {
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
		if(!parent3.isDefined()) return;

		Shape2D shape;
		
		// extract first line
		shape = parent1.getShape();
		if(!(shape instanceof AbstractLine2D)) return;
		AbstractLine2D line1 = (AbstractLine2D) shape;
		
		// extract second line
		shape = parent2.getShape();
		if(!(shape instanceof LinearShape2D)) return;
		LinearShape2D line2 = (LinearShape2D) shape;
		
		// extract direction
		boolean direct = parent3.getResult();
		
		// Find intersection point of the 2 rays
		Point2D point1 = line1.getIntersection(line2);
		if(point1==null) return;
		
		// Compute origin of result ray, and angle between lines
		double x0 = point1.getX(); 
		double y0 = point1.getY();
		double angle = line1.getHorizontalAngle() +
			Angle2D.getAngle(line1, line2)*.5;
		
		// set of ray origin and direction depending on orientation
		if(direct)
			this.ray = new Ray2D(x0, y0, Math.cos(angle), Math.sin(angle));
		else
			this.ray = new Ray2D(x0, y0, -Math.sin(angle), Math.cos(angle));
			
		this.defined = true;
	}

}
