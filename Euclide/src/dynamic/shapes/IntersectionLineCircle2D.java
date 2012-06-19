/*
 * File : IntersectionLineCircle2D.java
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
 * Created on 2 Apr. 2006
 */
package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.conic.*;
import math.geom2d.line.*;
import dynamic.*;

import java.util.*;


/**
 * @author Legland
 */
public class IntersectionLineCircle2D extends DynamicShape2D{
	
	DynamicShape2D parent1;
	DynamicShape2D parent2;

	int num;
	Point2D point = new Point2D();
	
	public IntersectionLineCircle2D(DynamicShape2D line, DynamicShape2D circle, int num){
		super();
		this.parent1 = line;
		this.parent2 = circle;
		this.num = num;

		parents.add(line);
		parents.add(circle);
		//parents.trimToSize();
		
		parameters.add(num);
		//parameters.trimToSize();

		this.point = new Point2D();
	//	update(); 
	}
	
	@Override
	public Shape2D getShape(){
		return point;
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
		if(!(shape instanceof CircularShape2D)) return;
		CircularShape2D circle = (CircularShape2D) shape;
		
		if(num<0) return;

		// update position of point
		Collection<Point2D> points = circle.getIntersections(line);
		
		// check there are enough intersections
		if(points.size()-1<num)
			return;
		
		// iterate over intersections until we find the right one
		Iterator<Point2D> iter = points.iterator();
		for(int i=0; i<=num; i++)
			this.point = iter.next();
		
		// keep the result, and return
		this.defined = true;
	}
}
