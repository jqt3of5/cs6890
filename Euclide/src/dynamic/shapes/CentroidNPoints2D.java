/* file : CentroidNPoints2D.java
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
 * Created on 2 feb. 2006
 *
 */
package dynamic.shapes;

import java.util.ArrayList;

import math.geom2d.*;
import dynamic.*;


/**
 * @author dlegland
 */
public class CentroidNPoints2D extends DynamicShape2D {

	ArrayList<DynamicShape2D> points = new ArrayList<DynamicShape2D>();
	
	Point2D centroid = new Point2D();
	
	/**
	 * Initialize with an array of points
	 * @param point
	 */
	public CentroidNPoints2D(DynamicShape2D[] points) {
		
		DynamicArray2D array = new DynamicArray2D(points);
		this.parents.add(array);

		for(int i=0; i<points.length; i++)
			this.points.add(points[i]);
		
		update();
	}	

	@Override
	public Shape2D getShape(){
		return centroid;
	}
	
	@Override
	public void update(){
		double cumX=0, cumY=0;
		Point2D point;

		this.defined = false;
		
		Shape2D shape= null;
		
		// iterate on points to update coordinate
		for(DynamicShape2D dyn : points){
			if(!dyn.isDefined())
				return;
			
			shape = dyn.getShape();
			if(!(shape instanceof Point2D)) return;
			point = (Point2D) shape;
			
			point = (Point2D) dyn.getShape();
			cumX += point.getX();
			cumY += point.getY();
		}
		cumX /= points.size();
		cumY /= points.size();
		
		this.centroid = new Point2D(cumX, cumY);
		this.defined = true;
	}
}
