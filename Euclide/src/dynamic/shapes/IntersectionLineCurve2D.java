/*
 * File : IntersectionLineCurve2D.java
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
 * Created on 8 Apr. 2007
 */
package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.curve.*;
import math.geom2d.line.*;
import math.geom2d.point.PointArray2D;
import dynamic.*;

import java.util.*;

/**
 * A set of points located at the intersections of a line and an arbitrary curve.
 * @author Legland
 */
public class IntersectionLineCurve2D extends DynamicShape2D{
	
	DynamicShape2D parent1;
	DynamicShape2D parent2;

	PointArray2D points = new PointArray2D();
	
	public IntersectionLineCurve2D(DynamicShape2D line, DynamicShape2D curve){
		super(); //
		this.parent1 = line;
		this.parent2 = curve;

		parents.add(line);
		parents.add(curve);
		//parents.trimToSize();

		//update();
	}
	
	@Override
	public Shape2D getShape(){
		return points;
	}
	
	@Override
	public void update(){
		defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
		
		Shape2D shape;
		shape = parent1.getShape();
		if(!(shape instanceof LinearShape2D)) return;
		LinearShape2D line = (LinearShape2D) shape;
		
		shape = parent2.getShape();
		if(!(shape instanceof Curve2D)) return;
		Curve2D curve = (Curve2D) shape;
		
		// update position of point
		Collection<Point2D> inters = curve.getIntersections(line);
		if (inters.size()==0) return;
		
		this.points.clearPoints();
		this.points.addPoints(inters);
		this.defined = true;
	}
}
