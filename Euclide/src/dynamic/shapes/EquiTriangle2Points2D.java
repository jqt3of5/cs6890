/* file : EquiTriangle2Points2D.java
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
 * Created on 18 Apr. 2007
 *
 */
package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.polygon.*;

import dynamic.*;

/**
 * @author dlegland
 */
public class EquiTriangle2Points2D extends DynamicShape2D {

	DynamicShape2D parent1;
	DynamicShape2D parent2;
	
	protected SimplePolygon2D polygon = new SimplePolygon2D();

	/**
	 * Initilize only the first point
	 * @param point
	 */
	public EquiTriangle2Points2D(DynamicShape2D point1, DynamicShape2D point2) {
		super();
		this.parent1 = point1;
		this.parent2 = point2;

		parents.add(point1);
		parents.add(point2);
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

		Shape2D shape;
		
		// extract first point
		shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point1 = (Point2D) shape;
		
		// extract second point
		shape = parent2.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point2 = (Point2D) shape;
		
		this.polygon.clearVertices();
		this.polygon.addVertex(point1);
		this.polygon.addVertex(point2);
		double l = point1.getDistance(point2);
		double a = Angle2D.getHorizontalAngle(point1, point2);
		this.polygon.addVertex(Point2D.createPolar(point1, l, a+Math.PI/3));
		
		this.defined = true;
	}
}
