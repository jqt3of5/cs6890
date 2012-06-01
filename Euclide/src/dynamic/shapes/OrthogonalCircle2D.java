/*
 * File : OrthogonalCircle2D.java
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
 * author : Legland
 * Created on 2 feb. 2006
 */
package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.conic.*;
import dynamic.*;

/**
 * A circle orthogonal to another circle, and containing a given point.
 * @author Legland
 */
public class OrthogonalCircle2D extends DynamicShape2D{
	
	DynamicShape2D parent1;
	DynamicShape2D parent2;

	private Circle2D circle = new Circle2D(0 ,0, 0);
	
	public OrthogonalCircle2D(DynamicShape2D circle, DynamicShape2D point){
		super();
		this.parent1 = circle;
		this.parent2 = point;

		parents.add(circle);
		parents.add(point);
		parents.trimToSize();

	//	update(); 
	}
	
	@Override
	public Shape2D getShape(){
		return circle;
	}
	
	/* (non-Javadoc)
	 * @see math.geom2d.dynamic.DynamicShape2D#update()
	 */
	@Override
	public void update() {
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;

		Shape2D shape;
		
		// extract first point
		shape = parent1.getShape();
		if(!(shape instanceof Circle2D)) return;
		Circle2D circle = (Circle2D) shape;
		
		// extract second point
		shape = parent2.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point = (Point2D) shape;
	
		// extract needed values
		double d = circle.getCenter().getDistance(point);
		double rc = circle.getRadius();
		
		// check that circle exists
		if(d<rc)
			return;
		
		// Compute radius of new circle
		double r  = Math.sqrt(d*d - rc*rc);
		
		// create the new shape
		this.circle = new Circle2D(point, r);
		this.defined=true;
	}
}
