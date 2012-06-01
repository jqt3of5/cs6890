/* file : RegularPolygonCenter2Points2D.java
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
 * Created on 06 May. 2007
 *
 */
package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.polygon.*;

import dynamic.*;

/**
 * A regular polygon defined by the center of the enclosing circle, and 2 points.
 * Parents are: 
 * <ol>
 * <li> the center of the enclosing circle</li>
 * <li> the first point of the polygon</li>
 * <li> another point used for computing number of sides of polygon</li>
 * </ol>
 *  * @author dlegland
 */
public class RegularPolygonCenter2Points2D extends DynamicShape2D {

	DynamicShape2D parent1;
	DynamicShape2D parent2;
	DynamicShape2D parent3;
	
	protected SimplePolygon2D polygon = new SimplePolygon2D();
	
	/**
	 * @param point
	 */
	public RegularPolygonCenter2Points2D(DynamicShape2D point1, DynamicShape2D point2, DynamicShape2D point3) {
		super();
		this.parent1 = point1;
		this.parent2 = point2;
		this.parent3 = point3;

		parents.add(point1);
		parents.add(point2);
		parents.add(point3);
		parents.trimToSize();

		//update();
	}

	
	@Override
	public Shape2D getShape(){
		return polygon;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.dynamic.DynamicShape2D#update()
	 */
	@Override
	public void update() {
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
		if(!parent3.isDefined()) return;

		Shape2D shape;
		
		// extract first point
		shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point1 = (Point2D) shape;
		
		// extract second point
		shape = parent2.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point2 	= (Point2D) shape;
		
		// extract first point
		shape = parent3.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point3 	= (Point2D) shape;
		
		// Compute radius and start angle of enclosing circle
		double radius 	= point1.getDistance(point2);
		double theta 	= Angle2D.getHorizontalAngle(point1, point2);
		
		// compute angle between 2 vertices wrt center
		double alpha	= Angle2D.getAngle(point2, point1, point3);
		
		// check if polygon will be oriented
		boolean direct 	= true;
		if(alpha>Math.PI){
			alpha 	= 2*Math.PI-alpha;
			direct 	= false;
		}
		
		// compute  number of vertices
		int n = (int) Math.round(2*Math.PI/alpha);
		if(n>24) 	n=24;
		if(n<3) 	n=3;

		// angle increment for each point
		double dtheta = 2*Math.PI/n;
		
		// compute points of the polygon
		Point2D[] points = new Point2D[n];
		if(direct)
			for(int i=0; i<n; i++)
				points[i] = Point2D.createPolar(point1, radius, theta+i*dtheta);
		else
			for(int i=0; i<n; i++)
				points[i] = Point2D.createPolar(point1, radius, theta-i*dtheta);

		this.polygon = new SimplePolygon2D(points);
		
		this.defined = true;
	}
}
