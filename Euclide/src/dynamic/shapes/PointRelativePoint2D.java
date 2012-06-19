/*
 * File : PointRelativePoint2D.java
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
 * Created on 8 Apr. 2006
 */
package dynamic.shapes;

import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.*;
import dynamic.*;

/**
 * @author Legland
 */
public class PointRelativePoint2D extends DynamicMoveablePoint2D {
	
	DynamicShape2D parent1;

	private Point2D point = new Point2D();

	public PointRelativePoint2D(DynamicShape2D basePoint, DynamicShape2D location){
		super();
		this.parent1 = basePoint;		

		parents.add(basePoint);
		//parents.trimToSize();

		parameters.add(0);
		parameters.add(1);
		//parameters.trimToSize();
		
		if(!basePoint.isDefined()) return;
		if(!location.isDefined()) return;
				
		Shape2D shape;		
		shape = basePoint.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point1 = (Point2D) shape;

		shape = location.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point2 = (Point2D) shape;
		
		double dx = point2.getX()-point1.getX();
		double dy = point2.getY()-point1.getY();
		this.setParameter(0, dx);
		this.setParameter(1, dy);
		//update(); 
	}
	
	/**
	 * This constructor is needed to be able to construct to point when loading a file.
	 * @param basePoint the reference point
	 * @param x x-coordinate of translation vector
	 * @param y y-coordinate of translation vector
	 */
	public PointRelativePoint2D(DynamicShape2D basePoint, double dx, double dy){
		super();
		this.parent1 = basePoint;		

		parents.add(basePoint);
		//parents.trimToSize();

		this.parameters.add(dx);
		this.parameters.add(dy);
		//parameters.trimToSize();
		
		//update(); 
	}
	
	@Override
	public Point2D getShape() {
		return point;
	}

	@Override
	public Collection<? extends Object> getParameters() {
		ArrayList<Object> array = new ArrayList<Object>();
		array.add(point.getX());
		array.add(point.getY());
		return array;
	}
	
	@Override
	public void translate(double dx, double dy) {
		double vx = ((Double) this.getParameter(0)) + dx;
		double vy = ((Double) this.getParameter(1)) + dy;
		this.setParameter(0, vx);
		this.setParameter(1, vy);
		update();
	}
	
	@Override
	public void update(){
		this.defined = false;
		if(!parent1.isDefined()) return;

		Shape2D shape;
		
		shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point1 = (Point2D) shape;

		double dx = (Double) this.getParameter(0);
		double dy = (Double) this.getParameter(1);
		
		// update the position of the point
		this.point = new Point2D(point1.getX()+dx, point1.getY()+dy);
		this.defined = true;
	}
}
