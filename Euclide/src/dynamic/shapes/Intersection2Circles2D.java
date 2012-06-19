/*
 * File : Intersection2Circles2D.java
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

import java.util.Collection;
import java.util.Iterator;

import math.geom2d.*;
import math.geom2d.circulinear.CirculinearCurve2DUtils;
import math.geom2d.conic.*;
import dynamic.*;

/**
 * Compute intersection point between 2 circular shapes, either circles
 * or circle arcs.
 * @author Legland
 */
public class Intersection2Circles2D extends DynamicShape2D{
	
	DynamicShape2D parent1;
	DynamicShape2D parent2;
	
	int num;
	private Point2D point = new Point2D();
	
	/**
	 * This constructor provides two dynamic shapes which build circles, and a
	 * number which can take values 0 and 1.
	 * If the value is 0, the intersection is the first intersection of first
	 * circle, starting on the angle between the centers of the two circles.
	 * If the number is 1, this is the other intersection.
	 */
	public Intersection2Circles2D(
			DynamicShape2D circle1, DynamicShape2D circle2,
			int num){
		super();
		this.parent1 = circle1;
		this.parent2 = circle2;
		this.num = num;

		parents.add(circle1);
		parents.add(circle2);
		//parents.trimToSize();
		
		parameters.add(num);
		//parameters.trimToSize();

		//update(); 
	}
	
	@Override
	public Shape2D getShape(){
		return point;
	}
	
	@Override
	public void update(){
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
		
		Shape2D shape;
		shape = parent1.getShape();
		if(!(shape instanceof CircularShape2D)) return;
		CircularShape2D shape1 = (CircularShape2D) shape;
		
		shape = parent2.getShape();
		if(!(shape instanceof CircularShape2D)) return;
		CircularShape2D shape2 = (CircularShape2D) shape;

		num = (Integer) parameters.get(0);
		
		// compute intersections
		Collection<Point2D> points = 
			CirculinearCurve2DUtils.findIntersections(shape1, shape2);
	
		// Check there are enough intersections
		if(num>points.size()-1)
			return;
		
		// iterate until we get the wanted intersection
		Iterator<Point2D> iter = points.iterator();
		int i=0;
		do {
			point = iter.next();
		} while(i++<num);
		
		// Everything is fine
		this.defined = true;
	}
}
