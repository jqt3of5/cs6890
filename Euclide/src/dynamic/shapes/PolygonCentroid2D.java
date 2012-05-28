/*
 * File : PolygonCentroid2D.java
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
 * Created on 06 Sept. 09
 */
package dynamic.shapes;

import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.polygon.Polygon2D;
import math.geom2d.polygon.SimplePolygon2D;
import dynamic.DynamicShape2D;

/**
 * Computes the centroid of a simple polygon.
 * @author Legland
 */
public class PolygonCentroid2D extends DynamicShape2D{
	
	DynamicShape2D 		parent1;
	
	Point2D point = new Point2D();
	
	public PolygonCentroid2D(DynamicShape2D parent1){
		super();
		this.parent1 = parent1;	
		parents.add(parent1);
		
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

		// Extract parent polygon
		Shape2D shape;
		shape = parent1.getShape();
		if(!(shape instanceof Polygon2D)) return;
		Polygon2D polygon = (Polygon2D) shape;
			
		if(polygon instanceof SimplePolygon2D) {
			point = ((SimplePolygon2D) polygon).getCentroid();
		} else {
			return;
		}
		
		// check polygon has enough vertices
		
		this.defined = true;
	}
}
