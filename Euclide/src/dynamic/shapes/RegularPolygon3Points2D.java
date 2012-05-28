/* file : RegularPolygon3Points2D.java
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
 * @author dlegland
 */
public class RegularPolygon3Points2D extends DynamicShape2D {

	DynamicShape2D parent1;
	DynamicShape2D parent2;
	DynamicShape2D parent3;

	protected SimplePolygon2D polygon = new SimplePolygon2D();
	
	/**
	 * Initialize only the first point
	 * @param point
	 */
	public RegularPolygon3Points2D(DynamicShape2D point1, DynamicShape2D point2, DynamicShape2D point3) {
		super();
		this.parent1 = point1;
		this.parent2 = point2;
		this.parent3 = point3;

		parents.add(point1);
		parents.add(point2);
		parents.add(point3);
		parents.trimToSize();

		update();
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
		Point2D point2 = (Point2D) shape;
		
		// extract first point
		shape = parent3.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point3 = (Point2D) shape;
		
		// compute angle and side length of polygon
		double theta = Angle2D.getHorizontalAngle(point1, point2);
		double alpha = Angle2D.getAngle(point3, point2, point1);
		double side = point1.getDistance(point2);
		
		// check if polygon will be oriented
		boolean direct = true;
		if(alpha>Math.PI){
			alpha 	= 2*Math.PI-alpha;
			direct 	= false;
		}
		
		// compute  number of vertices
		int n = (int) Math.round(2*Math.PI/(Math.PI-alpha));
		if(n>24) 	n=24;
		if(n<3) 	n=3;

		// center and radius of the polygon
		alpha = Math.PI-2*Math.PI/n;
		double radius = (side/2)/Math.cos(alpha/2);
		Point2D center;
		if(direct)
			center = Point2D.createPolar(point1, radius, theta+alpha/2);
		else
			center = Point2D.createPolar(point1, radius, theta-alpha/2);
		double beta0 = Angle2D.getHorizontalAngle(center, point1);
		double dbeta = 2*Math.PI/n;
		
		// compute points of the polygon
		Point2D[] points = new Point2D[n];
		if(direct)
			for(int i=0; i<n; i++)
				points[i] = Point2D.createPolar(center, radius, beta0+i*dbeta);
		else
			for(int i=0; i<n; i++)
				points[i] = Point2D.createPolar(center, radius, beta0-i*dbeta);

		this.polygon = new SimplePolygon2D(points);
		
		this.defined = true;
	}
}
