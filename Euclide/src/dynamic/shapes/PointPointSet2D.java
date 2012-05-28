/* file : PointPointSet2D.java
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
 * Created on 11 Nov. 2008
 *
 */
package dynamic.shapes;

import java.util.Iterator;

import math.geom2d.CountMeasure2D;
import math.geom2d.Measure2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.point.PointSet2D;
import dynamic.DynamicMeasure2D;
import dynamic.DynamicShape2D;
import dynamic.measures.CountWrapper2D;

/**
 * Extract a point from a point set, given its index.
 * @author dlegland
 */
public class PointPointSet2D extends DynamicShape2D {
	
	DynamicShape2D 		parent1;
	DynamicMeasure2D 	parent2;
	
	Point2D point = new Point2D();
	
	public PointPointSet2D(DynamicShape2D pointSet, DynamicMeasure2D index){
		this.parent1 = pointSet;
		this.parent2 = index;

		parents.add(pointSet);
		parents.add(index);
		parents.trimToSize();

		update(); 
	}

	public PointPointSet2D(DynamicShape2D pointSet, int index){
		this.parent1 = pointSet;
		this.parent2 = new CountWrapper2D(index);

		parents.add(pointSet);
		parents.trimToSize();
		
		parameters.add(new CountWrapper2D(index));
		parameters.trimToSize();
		
		update(); 
	}

	/**
	 * Computes the initial index of the vertex by choosing the closest vertex
	 * to a given point.
	 * @param parent1 a dynamic shape containing a polygon
	 * @param parent2 a dynamic shape containing a point
	 */
	public PointPointSet2D(DynamicShape2D parent1, DynamicShape2D parent2){
		super();
		this.parent1 = parent1;
	
		parents.add(parent1);
		
		// Extract position of parent2
		Shape2D shape = parent2.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D initPoint = (Point2D) shape;
		
		// Extract the parent polygon
		shape = parent1.getShape();
		if(!(shape instanceof PointSet2D)) return;
		PointSet2D pointSet = (PointSet2D) shape;
		
		// compute index of closest point
		double minDist = Double.MAX_VALUE;
		int num = 0;
		int i=0;
		for (Point2D point : pointSet) {
			double dist = point.getDistance(initPoint);
			if (dist<minDist) {
				dist = minDist;
				num = i;
			}
			i++;
		}
		
		// store measure of index
		this.parent2 = new CountWrapper2D(num);
		
		this.parameters.add(num);
		this.parameters.trimToSize();
		
		update(); 
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

		// extract parent point set
		Shape2D shape = parent1.getShape();
		if(!(shape instanceof PointSet2D)) return;
		PointSet2D pointSet = (PointSet2D) shape;
		
		// extract parent count measure
		Measure2D measure = parent2.getMeasure();
		if(!(measure instanceof CountMeasure2D)) return;
		int index = (int) Math.round(measure.getValue());
			
		// update position of point
		if(index>=pointSet.getPointNumber()) return;
		
		// get the point corresponding the the index
		Iterator<Point2D> pointIterator = pointSet.getPoints().iterator();
		for(int i=0; i<=index; i++)
			point = pointIterator.next();
		
		// set up defined flag
		this.defined = true;
	}
}
