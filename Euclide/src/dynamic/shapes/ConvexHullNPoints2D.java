/* file : ConvexHullNPoints2D.java
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
 * Created on 23 Feb. 2007
 *
 */
package dynamic.shapes;

import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.*;
import math.geom2d.point.PointArray2D;
import math.geom2d.polygon.*;
import math.geom2d.polygon.convhull.*;

import dynamic.*;

/**
 * @author dlegland
 */
public class ConvexHullNPoints2D extends DynamicShape2D {

	ArrayList<DynamicShape2D> points;
	
	Polygon2D polygon;
	
	/**
	 * Initialize from an array of points.
	 * @param points the point array creating the polygon
	 */
	public ConvexHullNPoints2D(DynamicShape2D[] points) {
		super();
		
		DynamicArray2D array = new DynamicArray2D(points);
		this.parents.add(array);

		this.points = new ArrayList<DynamicShape2D>(points.length);
		for(DynamicShape2D point : points){
			this.points.add(point);
		}
			
		//update();
	}

	/**
	 * Initialize from an array of  dynamic shapes.
	 * @param array ShapeArray containing instances of DynamicShape2D which return points.
	 */
	public ConvexHullNPoints2D(DynamicArray2D array) {
		super();
		
		this.parents.add(array);
		
		Collection<? extends DynamicObject2D> shapes = array.getParents();	
		this.points = new ArrayList<DynamicShape2D>(shapes.size());
		for(DynamicObject2D point : shapes)
			if(point instanceof DynamicShape2D)
				this.points.add((DynamicShape2D)point);
		//this.update();
	}
	
	@Override
	public Shape2D getShape(){
		return polygon;
	}
	
	@Override
	public void update(){
		this.defined = false;
		
		Shape2D shape= null;
		
		// iterate on points to update coordinate
		PointArray2D pointSet = new PointArray2D(points.size());
		for(DynamicShape2D dyn : points){
			if(!dyn.isDefined()) continue;
			
			shape = dyn.getShape();
			if(!(shape instanceof Point2D)) continue;
			
			pointSet.addPoint((Point2D) shape);
		}
		
		polygon = new GrahamScan2D().convexHull(pointSet.getPoints());
		
		this.defined = true;
	}
}
