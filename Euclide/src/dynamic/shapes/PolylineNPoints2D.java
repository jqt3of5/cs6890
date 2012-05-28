/* file : PolylineNPoints2D.java
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
 * Created on 2 Mar. 2006
 *
 */
package dynamic.shapes;

import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.polygon.Polyline2D;
import dynamic.DynamicArray2D;
import dynamic.DynamicObject2D;
import dynamic.DynamicShape2D;

/**
 * @author dlegland
 */
public class PolylineNPoints2D extends DynamicShape2D {

	ArrayList<DynamicShape2D> points = new ArrayList<DynamicShape2D>();
	
	Polyline2D polyline = new Polyline2D();
	
	/**
	 * Initialize only the first point
	 * @param point
	 */
	public PolylineNPoints2D(DynamicShape2D[] points) {
		super();
		DynamicArray2D array = new DynamicArray2D(points);
		this.parents.add(array);

		for(int i=0; i<points.length; i++)
			this.points.add(points[i]);
		
		update();
	}

	public PolylineNPoints2D(DynamicArray2D array) {
		super();
		
		this.parents.add(array);
		
		Collection<? extends DynamicObject2D> shapes = array.getParents();	
		this.points = new ArrayList<DynamicShape2D>(shapes.size());
		for(DynamicObject2D point : shapes)
			if(point instanceof DynamicShape2D)
				this.points.add((DynamicShape2D)point);
		
		update();
	}

	/**
	 * Initialize only the first point
	 * @param point
	 */
	public PolylineNPoints2D(DynamicShape2D point) {
		super();
		this.points.add(point);
		update();
	}

	
	@Override
	public Shape2D getShape(){
		return polyline;
	}
	
	@Override
	public void update(){
		this.defined = false;
		
		Shape2D shape = null;
		this.polyline.clearVertices();
		
		boolean ok = true;
		ArrayList<Point2D> vertices = new ArrayList<Point2D>(points.size());
		
		// iterate on points to update coordinate
		for(DynamicShape2D dyn : points){
			// check parent is defined
			if(!dyn.isDefined()) {
				ok = false;
				break;
			}
			
			// get parent point
			shape = dyn.getShape();
			if(!(shape instanceof Point2D)) {
				ok = false;
				break;
			}
			
			// add the current point
			vertices.add((Point2D) shape);
		}
		
		// check all parents are well defined
		if(!ok) {
			return;
		}
		
		this.polyline = new Polyline2D(vertices);
		this.defined = true;
	}
}
