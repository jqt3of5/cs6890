/*
 * File : PointOnBoundary2D.java
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
 * Created on 13 jan. 2007
 */
package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.curve.*;
import math.geom2d.domain.*;

import dynamic.*;
import dynamic.measures.MeasureWrapper2D;

/**
 * @author Legland
 */
public class PointOnBoundary2D extends DynamicMoveablePoint2D {
	
	DynamicShape2D 		parent1;
	MeasureWrapper2D	parent2;
	double pos = 0;
	
	private Point2D point = new Point2D();
	
	public PointOnBoundary2D(DynamicShape2D shape, DynamicShape2D pointer){
		super();
		this.parent1 = shape;
		this.parent2 = new MeasureWrapper2D(0);

		parents.add(parent1);
		parents.add(parent2);
		
		// compute position of point on the curve
		Curve2D curve = ((Domain2D) parent1.getShape()).getBoundary();
		Point2D point = (Point2D) pointer.getShape();
		this.pos = curve.project(point);
	//	update(); 
	}

	@Override
	public Point2D getShape(){
		return point;
	}
	
	@Override
	public void translate(double dx, double dy) {
		double x = point.getX() + dx;
		double y = point.getY() + dy;
		
		Shape2D shape = parent1.getShape();
		if(!(shape instanceof Domain2D)) return;
		Boundary2D curve = ((Domain2D) shape).getBoundary();
		
		pos = curve.project(new Point2D(x, y));
		
		this.point = new Point2D(x, y);
	}

	@Override
	public void update(){
		this.defined = false;
		if(!parent1.isDefined()) return;

		// Extract parent curve
		Shape2D shape;
		shape = parent1.getShape();
		if(!(shape instanceof Domain2D)) return;
		Boundary2D curve = ((Domain2D) shape).getBoundary();
				
		// update point coordinate
		this.point = curve.getPoint(pos);		
		this.defined = true;
	}
}
