/*
 * File : Box2Points2D.java
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
 * Created on 25 janv. 2004
 */
package dynamic.shapes;

import math.geom2d.*;
import dynamic.*;

/**
 * @author Legland
 */
public class Box2Points2D extends DynamicShape2D{
	
	DynamicShape2D parent1;
	DynamicShape2D parent2;
		
	protected Box2D box = new Box2D();
	
	public Box2Points2D(DynamicShape2D point1, DynamicShape2D point2){
		super(); //
		this.parent1 = point1;
		this.parent2 = point2;

		parents.add(point1);
		parents.add(point2);
		parents.trimToSize();

	//	update(); 
	}
	
	@Override
	public Shape2D getShape(){
		return box.getAsRectangle();
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
		
		// compute max position of points
		double xmin = Math.min(point1.getX(), point2.getX());
		double ymin = Math.min(point1.getY(), point2.getY());
		double xmax = Math.max(point1.getX(), point2.getX());
		double ymax = Math.max(point1.getY(), point2.getY());

		this.box = new Box2D(xmin, ymin, xmax-xmin, ymax-ymin);
		this.defined = true;
	}
}
