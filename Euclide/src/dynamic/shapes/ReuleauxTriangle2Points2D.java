/*
 * File : ReuleauxTriangle2Points2D.java
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
import math.geom2d.domain.*;
import dynamic.*;

/**
 * @author Legland
 */
public class ReuleauxTriangle2Points2D extends DynamicShape2D{
	
	DynamicShape2D parent1;
	DynamicShape2D parent2;
	
	BoundaryPolyCurve2D<CircleArc2D> polyCurve = null;
	CircleArc2D arc1 = new CircleArc2D();
	CircleArc2D arc2 = new CircleArc2D();
	CircleArc2D arc3 = new CircleArc2D();
	
	public ReuleauxTriangle2Points2D(DynamicShape2D point1, DynamicShape2D point2){
		super();
		
		// save references to points
		parent1 = point1;
		parent2 = point2;

		parents.add(point1);
		parents.add(point2);
		parents.trimToSize();

		
		this.polyCurve = new BoundaryPolyCurve2D<CircleArc2D>();
		this.polyCurve.addCurve(arc1);
		this.polyCurve.addCurve(arc2);
		this.polyCurve.addCurve(arc3);
		
	//	update();
	}
	
	@Override
	public Shape2D getShape(){
		return polyCurve;
	}
		
	@Override
	public void update(){
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
		
		// compute position of third point
		double rho = point1.getDistance(point2);
		double angle = Angle2D.getHorizontalAngle(point1, point2);
		Point2D point3 = Point2D.createPolar(point1, rho, angle+Math.PI/3);
		
		// update parameters of arcs
		arc1 = new CircleArc2D(point1, rho, angle, Math.PI/3);
		arc2 = new CircleArc2D(point2, rho, angle+2*Math.PI/3, Math.PI/3);
		arc3 = new CircleArc2D(point3, rho, angle+4*Math.PI/3, Math.PI/3);		
		
		// recreate a new Polycurve....
		this.polyCurve.clearCurves();
		this.polyCurve.addCurve(arc1);
		this.polyCurve.addCurve(arc2);
		this.polyCurve.addCurve(arc3);

		this.defined = true;
	}
}
