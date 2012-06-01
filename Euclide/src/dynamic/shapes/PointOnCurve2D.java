/*
 * File : PointOnCurve2D.java
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

import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.*;
import math.geom2d.curve.*;

import dynamic.*;

/**
 * @author Legland
 */
public class PointOnCurve2D extends DynamicMoveablePoint2D {
	
	DynamicShape2D 	parent1;
	double pos = 0;
	
	private Point2D point = new Point2D();
	
	public PointOnCurve2D(DynamicShape2D parent1, DynamicShape2D parent2){
		super();
		this.parent1 = parent1;
	
		parents.add(parent1);
		
		// compute position of point on the curve
		Curve2D curve = (Curve2D) parent1.getShape();
		Point2D point = (Point2D) parent2.getShape();
		this.pos = curve.project(point);
		//update(); 
	}

	public PointOnCurve2D(DynamicShape2D parent1, DynamicMeasure2D parent2){
		super();
		this.parent1 = parent1;
		
		parents.add(parent1);
		
		// compute position of point on the curve
		this.pos = parent2.getMeasure().getValue();
		//update(); 
	}

	public PointOnCurve2D(DynamicShape2D parent1, double pos){
		super();
		this.parent1 = parent1;
		
		parents.add(parent1);
		
		// compute position of point on the curve
		this.pos = pos;
		//update(); 
	}

	@Override
	public Point2D getShape(){
		return point;
	}
	
	@Override
	public Collection<? extends Object> getParameters() {
		ArrayList<Object> array = new ArrayList<Object>();
		array.add(pos);
		return array;
	}
	
	@Override
	public void translate(double dx, double dy) {
		double x = point.getX() + dx;
		double y = point.getY() + dy;
		
		Shape2D shape = parent1.getShape();
		if(!(shape instanceof Curve2D)) return;
		Curve2D curve = (Curve2D) shape;
		
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
		if(!(shape instanceof Curve2D)) return;
		Curve2D curve = (Curve2D) shape;
				
		// update point coordinate
		this.point = curve.getPoint(pos);
		this.defined = true;
	}
}
