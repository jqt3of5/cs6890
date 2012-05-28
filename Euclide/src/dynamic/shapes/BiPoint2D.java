/*
 * File : BiPoint2D.java
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
 * Created on 27 Nov. 2007
 */
package dynamic.shapes;

import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.*;
import dynamic.*;

/**
 * @author Legland
 */
public class BiPoint2D extends DynamicMoveablePoint2D {
	
	/** The parent point */
	DynamicShape2D parent1;

	BiPoint2D twin = null;
	double dx = 0;
	double dy = 0;
	boolean b;
	
	/**
	 * Change behavior of class, such that translation of the point
	 * will update translation vector of dynamic shape.
	 */
	private Point2D point = new Point2D();

	/**
	 * Constructor needed for initialization from file
	 * @param basePoint center point of the two BiPoint
	 * @param x0 initial x coordinate of the point
	 * @param y0 initial y coordinate of the point
	 */
	public BiPoint2D(DynamicShape2D basePoint, double x0, double y0){
		super();
		this.parent1 = basePoint;
		
		parents.add(basePoint);
		parents.trimToSize();
		
		if(!basePoint.isDefined()) return;
				
		Shape2D shape = basePoint.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D base = (Point2D) shape;
		
		b = true;
		this.dx = x0-base.getX();
		this.dy = y0-base.getY();
		
		update();
	}

	public BiPoint2D(DynamicShape2D basePoint, DynamicShape2D location){
		super();
		this.parent1 = basePoint;
		
		parents.add(basePoint);
		if(location instanceof BiPoint2D)
			parents.add(location);
		parents.trimToSize();

		if(!basePoint.isDefined()) return;
		if(!location.isDefined()) return;
				
		Shape2D shape = basePoint.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D base = (Point2D) shape;
		
		shape = location.getShape();
		if(!(shape instanceof Point2D)) return;
		
		if(location instanceof BiPoint2D){
			b = false;
			this.twin = (BiPoint2D) location;
			this.twin.twin = this;
		}else{
			b = true;
			Point2D loc = (Point2D) shape;
			this.dx = loc.getX()-base.getX();
			this.dy = loc.getY()-base.getY();
		}
		
		update();
	}
	
	@Override
	public Point2D getShape(){
		return point;
	}
	
	@Override
	public Collection<Object> getParameters(){
		ArrayList<Object> array = new ArrayList<Object>();
		if(b){
			array.add(point.getX());
			array.add(point.getY());
		}
		return array;
	}
	
	@Override
	public void translate(double dx, double dy) {
		if(b) {
		 	this.dx += dx;
		 	this.dy += dy;
		} else {
			twin.dx -= dx;
			twin.dy -= dy;
		}
		update();
		twin.update();
	}
	
	@Override
	public void update(){
		this.defined = false;
		if(!parent1.isDefined()) return;

		Shape2D shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D base = (Point2D) shape;
		
		if(b){
			this.point = new Point2D(base.getX()+dx, base.getY()+dy);
		}else{
			this.point = new Point2D(base.getX()-twin.dx, base.getY()-twin.dy);
		}

		this.defined = true;
	}
	
}
