/*
 * File : RadicalLine2Circles2D.java
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
import math.geom2d.line.*;
import dynamic.*;

/**
 * @author Legland
 */
public class RadicalLine2Circles2D extends  DynamicShape2D{
	
	DynamicShape2D parent1;
	DynamicShape2D parent2;
	
	private StraightLine2D line = new StraightLine2D();
	
	public RadicalLine2Circles2D(DynamicShape2D circle1, DynamicShape2D circle2){
		super();
		this.parent1 = circle1;
		this.parent2 = circle2;

		parents.add(circle1);
		parents.add(circle2);
		//parents.trimToSize();

		//update(); 
	}
	
	@Override
	public Shape2D getShape(){
		return line;
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
		shape = parent1.getShape();
		if(!(shape instanceof Circle2D)) return;
		Circle2D circle1 = (Circle2D) shape;
		
		shape = parent2.getShape();
		if(!(shape instanceof Circle2D)) return;
		Circle2D circle2 = (Circle2D) shape;
		
		// update position of point
		double r1 	= circle1.getRadius();
		double r2 	= circle2.getRadius();
		Point2D p1 	= circle1.getCenter();
		Point2D p2 	= circle2.getCenter();
		double dist = p1.getDistance(p2);

		StraightLine2D line = new StraightLine2D(p1, p2);
		
		//double angle = StraightObject2D.getHorizontalAngle(p1, p2);
		double angle = line.getHorizontalAngle();
		double d = (dist*dist+r1*r1-r2*r2)*.5/dist;
		double cot = Math.cos(angle);
		double sit = Math.sin(angle);
		
		// compute parameters of the line
		double x0 = p1.getX() + d*cot;
		double y0 = p1.getY() + d*sit;
		double dx = -sit;
		double dy = cot;
		
		// update state of current line
		line = new StraightLine2D(x0, y0, dx, dy);
		this.defined = true;
	}
}
