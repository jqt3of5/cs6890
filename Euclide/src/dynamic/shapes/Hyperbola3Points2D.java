/*
 * File : Circle3Points2D.java
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
import math.geom2d.conic.*;

import dynamic.*;

/**
 * @author Legland
 */
public class Hyperbola3Points2D extends DynamicShape2D{
	
	DynamicShape2D parent1;
	DynamicShape2D parent2;
	DynamicShape2D parent3;
	
	Hyperbola2D hyperbola = new Hyperbola2D();
	
	public Hyperbola3Points2D(DynamicShape2D point1, DynamicShape2D point2, DynamicShape2D point3){
		super();
		this.parent1 = point1;
		this.parent2 = point2;
		this.parent3 = point3;

		parents.add(point1);
		parents.add(point2);
		parents.add(point3);
		//parents.trimToSize();

	//	update(); 
	}
	
	@Override
	public Shape2D getShape(){
		return hyperbola;
	}
			
	@Override
	public void update(){
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
		if(!parent3.isDefined()) return;
				
		Shape2D shape;
		
		shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point1 = (Point2D) shape;

		shape = parent2.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point2 = (Point2D) shape;
		
		shape = parent3.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point3 = (Point2D) shape;
		
		if(Point2D.isColinear(point1, point2, point3)) return;
		
		double a = point1.getDistance(point2);
		double b = point1.getDistance(point3);
		double theta = Angle2D.getHorizontalAngle(point1, point2);
		
		hyperbola = new Hyperbola2D(point1, a, b, theta, true);
		
		this.defined = true;		
	}
}
