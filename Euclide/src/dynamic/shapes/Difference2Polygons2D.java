/*
 * File : Difference2Polygons2D.java
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

import math.geom2d.Shape2D;
import math.geom2d.polygon.Polygon2D;
import math.geom2d.polygon.Polygon2DUtils;
import dynamic.DynamicShape2D;

/**
 * Compute intersection point between 2 circular shapes, either circles
 * or circle arcs.
 * @author Legland
 */
public class Difference2Polygons2D extends DynamicShape2D{
	
	DynamicShape2D parent1;
	DynamicShape2D parent2;
	
	private Polygon2D result;
	
	/**
	 * Computes the difference of Two polygons.
	 */
	public Difference2Polygons2D(DynamicShape2D poly1, DynamicShape2D poly2){
		super();
		this.parent1 = poly1;
		this.parent2 = poly2;
		
		parents.add(poly1);
		parents.add(poly2);
		parents.trimToSize();
		
		//update(); 
	}
	
	@Override
	public Shape2D getShape(){
		return result;
	}
	
	@Override
	public void update(){
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
		
		Shape2D shape;
		shape = parent1.getShape();
		if(!(shape instanceof Polygon2D)) return;
		Polygon2D polygon1 = (Polygon2D) shape;
		
		shape = parent2.getShape();
		if(!(shape instanceof Polygon2D)) return;
		Polygon2D polygon2 = (Polygon2D) shape;
		
		// can not make a difference with itself
		if (polygon1 == polygon2) {
			return;
		}

		// compute intersections
		result = Polygon2DUtils.difference(polygon1, polygon2);
		
		// Everything is fine
		this.defined = true;
	}
}
