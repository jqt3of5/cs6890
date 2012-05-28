/*
 * File : PerpendicularLine2D.java
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
 * Created on 2 feb. 2006
 */
package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.line.*;

import dynamic.*;

/**
 * @author Legland
 */
public class PerpendicularLine2D extends DynamicShape2D{
	
	DynamicShape2D parent1;
	DynamicShape2D parent2;
	
	StraightLine2D line = new StraightLine2D();
	
	public PerpendicularLine2D(DynamicShape2D baseLine, DynamicShape2D point){
		super(); //
		this.parent1 = baseLine;
		this.parent2 = point;

		parents.add(baseLine);
		parents.add(point);
		parents.trimToSize();

		update(); 
	}
	
	@Override
	public Shape2D getShape(){
		return line;
	}
	
	@Override
	public void update(){
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;

		Shape2D shape;
		
		// extract first point
		shape = parent1.getShape();
		if(!(shape instanceof LinearShape2D)) return;
		LinearShape2D baseLine = (LinearShape2D) shape;
		
		// extract second point
		shape = parent2.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point = (Point2D) shape;
		
		Vector2D vect = baseLine.getVector();
		double x0 = point.getX();
		double y0 = point.getY();
		
		line = new StraightLine2D(x0, y0, -vect.getY(), vect.getX());
		this.defined = true;
	}
}
