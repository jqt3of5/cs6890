/*
 * File : Ellipse3Points2D.java
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
 * Created on 28 dec. 2006
 */
package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.conic.*;
import math.geom2d.line.*;

import dynamic.*;

/**
 * Construct an ellipse from 3 points: the center, the point marking the end
 * of the major semiaxis, and a third point defining the length of the minor
 * semiaxis. Last point also specifies orientation of ellipse.
 * @author Legland
 */
public class Ellipse3Points2D extends DynamicShape2D{
	DynamicShape2D parent1;
	DynamicShape2D parent2;
	DynamicShape2D parent3;

	private Ellipse2D ellipse = new Ellipse2D();

	public Ellipse3Points2D(DynamicShape2D point1, DynamicShape2D point2, DynamicShape2D point3){
		super();
		parent1 = point1;
		parent2 = point2;
		parent3 = point3;

		parents.add(point1);
		parents.add(point2);
		parents.add(point3);
		parents.trimToSize();

		//update(); 
	}
	
	@Override
	public Shape2D getShape(){
		return ellipse;
	}
		
	@Override
	public void update(){
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
		if(!parent3.isDefined()) return;
				
		Shape2D shape;
		
		shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point1 = (Point2D) shape;

		shape = parent2.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point2 = (Point2D) shape;
		
		shape = parent3.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point3 = (Point2D) shape;
		
		if(Point2D.isColinear(point1, point2, point3)) return;
		
		// Update coord of center
		double xc = point1.getX();
		double yc = point1.getY();
		
		StraightLine2D line1 = new StraightLine2D(point1, point2);
		StraightLine2D line2 = StraightLine2D.createPerpendicular(line1, point1);

		// update length of semi major axis
		double r1 = point2.getDistance(point1);		
		double r2 = line2.getProjectedPoint(point3).getDistance(point1);
		
		// update angle
		double theta = line1.getHorizontalAngle();
		
		// update directed flag
		boolean direct = line1.isInside(point3);
		
		ellipse = new Ellipse2D(xc, yc, r1, r2, theta, direct);
		this.defined = true;
	}
}
