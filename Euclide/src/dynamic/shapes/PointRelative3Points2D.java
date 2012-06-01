/* file : PointRelative3Points2D.java
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
 * Created on 30 Sep. 2006
 *
 */
package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.line.*;

import dynamic.*;
import dynamic.measures.Constant2D;

/**
 * A point defined as the result of a linear combination of 2 vectors. 
 * Origin of each vector is given by first point, the direction and norm
 * of the two vectors is given by point1 and point2, and alpha and beta
 * give the linear coefficient associated to each vector.
 * @author dlegland
 */
public class PointRelative3Points2D extends DynamicShape2D {

	DynamicShape2D parent1;
	DynamicShape2D parent2;
	DynamicShape2D parent3;	
	
	DynamicMeasure2D parent4;
	DynamicMeasure2D parent5;
	
	private Point2D point = new Point2D();
	
	/**
	 * Origin of each vector is given by first point, the direction and norm
	 * of the two vectors is given by point1 and point2, and alpha and beta
	 * give the linear coefficient associated to each vector.
 	 * @param point0
	 * @param point1
	 */
	public PointRelative3Points2D(
			DynamicShape2D point0, DynamicShape2D point1, 
			DynamicShape2D point2, double alpha, double beta) {
		super();
		this.parent1 = point0;
		this.parent2 = point1;
		this.parent3 = point2;

		// stores parents
		parents.add(point0);
		parents.add(point1);
		parents.add(point2);
		parents.trimToSize();
		
		// stores parameters
		parameters.add(alpha);
		parameters.add(beta);
		parameters.trimToSize();

		this.parent4 = new Constant2D(alpha);
		this.parent5 = new Constant2D(beta);
		//update();
	}
	
	/**
	 * Origin of each vector is given by first point, the direction and norm
	 * of the two vectors is given by point1 and point2, and alpha and beta
	 * give the linear coefficient associated to each vector.
 	 * @param point0
	 * @param point1
	 */
	public PointRelative3Points2D(
			DynamicShape2D point0, DynamicShape2D point1, 
			DynamicShape2D point2, DynamicMeasure2D alpha, 
			DynamicMeasure2D beta) {
		super();
		this.parent1 = point0;
		this.parent2 = point1;
		this.parent3 = point2;
		this.parent4 = alpha;
		this.parent5 = beta;

		// stores parents
		parents.add(point0);
		parents.add(point1);
		parents.add(point2);
		parents.add(alpha);
		parents.add(beta);
		parents.trimToSize();
		
		//update();
	}
	
	/**
	 * Same as standard constructor, but alpha and beta parameters
	 * are computed from the position of another point.
	 * @param point0
	 * @param point1
	 * @param point2
	 * @param pos
	 */
	public PointRelative3Points2D(
			DynamicShape2D p0, DynamicShape2D p1, DynamicShape2D p2, 
			DynamicShape2D pi) {
		super();
		this.parent1 = p0;
		this.parent2 = p1;
		this.parent3 = p2;		

		parents.add(p0);
		parents.add(p1);
		parents.add(p2);
		parents.trimToSize();

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

		shape = pi.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point4 = (Point2D) shape;

		if(Point2D.isColinear(point1, point2, point3)) return;
		
		// project the fourth point on the two axes, 
		// with direction given by the two vectors
		StraightLine2D line1 = new StraightLine2D(point1, point2); 
		StraightLine2D line2 = new StraightLine2D(point1, point3);
		StraightLine2D par1 = StraightLine2D.createParallel(line1, point4);
		StraightLine2D par2 = StraightLine2D.createParallel(line2, point4);
		Point2D inter1 = AbstractLine2D.getIntersection(line1, par2); 
		Point2D inter2 = AbstractLine2D.getIntersection(line2, par1); 

		// initialize alpha and beta with position of projected points
		double alpha 	= line1.getPositionOnLine(inter1);
		double beta 	= line2.getPositionOnLine(inter2);

		parameters.add(alpha);
		parameters.add(beta);
		parameters.trimToSize();

		this.parent4 = new Constant2D(alpha);
		this.parent5 = new Constant2D(beta);
		//update();
	}	
	
	@Override
	public Shape2D getShape(){
		return point;
	}
		
	@Override
	public void update(){
		// check parents are defined
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
		if(!parent3.isDefined()) return;
		if(!parent4.isDefined()) return;
		if(!parent5.isDefined()) return;
				
		Shape2D shape;
		
		// extract the 3 parent points
		shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point1 = (Point2D) shape;

		shape = parent2.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point2 = (Point2D) shape;
		
		shape = parent3.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point3 = (Point2D) shape;
		
		// extract alpha and beta
		double alpha = parent4.getMeasure().getValue();
		double beta  = parent5.getMeasure().getValue();
		
		// coordinates of reference point
		double x0 = point1.getX();			
		double y0 = point1.getY();
		
		// coordinates of each vector
		double dx1 = point2.getX()-x0;		
		double dy1 = point2.getY()-y0;
		double dx2 = point3.getX()-x0;		
		double dy2 = point3.getY()-y0;
		
		// update point location
		this.point = new Point2D(
				x0 + alpha*dx1 + beta*dx2, 
				y0 + alpha*dy1 + beta*dy2);
		this.defined = true;
	}

}
