/* file : PointSetNPoints2D.java
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
 * Created on 6 Apr. 2007
 *
 */
package dynamic.shapes;

import java.util.*;

import math.geom2d.*;
import math.geom2d.point.PointArray2D;
import dynamic.*;

/**
 * @author dlegland
 */
public class PointSetNPoints2D extends DynamicShape2D {

	ArrayList<DynamicShape2D> points = new ArrayList<DynamicShape2D>();
	
	PointArray2D pointSet = new PointArray2D();
	
	/**
	 * Initilize only the first point
	 * @param point
	 */
	public PointSetNPoints2D(DynamicShape2D[] points) {
		super();
		
		DynamicArray2D array = new DynamicArray2D(points);
		this.parents.add(array);

		this.points.ensureCapacity(points.length);
		for(int i=0; i<points.length; i++)
			this.points.add(points[i]);
		update();
	}

	/**
	 * Initilize only the first point
	 * @param point
	 */
	public PointSetNPoints2D(DynamicShape2D point) {
		super();
		this.points.add(point);
		update();
	}

	@Override
	public Shape2D getShape(){
		return pointSet;
	}
	
	@Override
	public void update(){
		this.defined = false;
		
		Shape2D shape= null;
		this.pointSet.clearPoints();
		
		// iterate on points to update coordinate
		for(DynamicShape2D dyn : points){
			if(!dyn.isDefined()) continue;
			
			shape = dyn.getShape();
			if(!(shape instanceof Point2D)) continue;
			
			pointSet.addPoint((Point2D) shape);
		}
		
		this.defined = true;
	}
}
