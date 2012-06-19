/*
 * File : TangentCircleLine2D.java
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
import math.geom2d.conic.*;
import math.geom2d.line.*;

import dynamic.*;

/**
 * @author Legland
 */
public class TangentCircleLine2D extends DynamicShape2D{
	
	DynamicShape2D parent1;
	DynamicShape2D parent2;
	
	private StraightLine2D line = new StraightLine2D();
		
	public TangentCircleLine2D(DynamicShape2D circle, DynamicShape2D line){
		super();
		this.parent1 = circle;
		this.parent2 = line;

		parents.add(circle);
		parents.add(line);
		//parents.trimToSize();

		//update(); 
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
		shape = parent1.getShape();
		if(!(shape instanceof Circle2D)) return;
		Circle2D circle = (Circle2D) shape;
		
		shape = parent2.getShape();
		if(!(shape instanceof LinearShape2D)) return;
		LinearShape2D baseLine = (LinearShape2D) shape;
		
		double radius = circle.getRadius();
		Point2D center = circle.getCenter();
		double theta = baseLine.getHorizontalAngle()-Math.PI/2;
		//if(!dir2) theta+=Math.PI;
		
		double x0 = center.getX()+radius*Math.cos(theta);
		double y0 = center.getY()+radius*Math.sin(theta);
		double dx = Math.cos(theta+Math.PI/2);
		double dy = Math.sin(theta+Math.PI/2);
		
		line = new StraightLine2D(x0, y0, dx, dy);
		this.defined=true;
	}
}
