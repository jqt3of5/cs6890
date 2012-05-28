/*
 * File : SubCurve2D.java
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
import math.geom2d.curve.*;
import dynamic.*;

/**
 * Compute a curve portion between two points.
 * @author Legland
 */
public class SubCurve2D extends DynamicShape2D{
	
	DynamicShape2D parent1;
	DynamicShape2D parent2;
	DynamicShape2D parent3;
	
	Curve2D curve = null;
	
	public SubCurve2D(DynamicShape2D baseCurve, DynamicShape2D point1, DynamicShape2D point2){
		super();
		this.parent1 = baseCurve;
		this.parent2 = point1;
		this.parent3 = point2;

		parents.add(baseCurve);
		parents.add(point1);
		parents.add(point2);
		parents.trimToSize();

		update();
	}
	
	@Override
	public Shape2D getShape(){
		return curve;
	}
		
	@Override
	public void update(){
		this.defined = false;
		
		// check parents are defined
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
		if(!parent3.isDefined()) return;

		Shape2D shape;
		
		// extract parent curve
		shape = parent1.getShape();
		if(!(shape instanceof Curve2D)) return;
		Curve2D baseCurve = (Curve2D) shape;
		
		// extract second point
		shape = parent2.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point1 = (Point2D) shape;
		
		// extract second point
		shape = parent3.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point2 = (Point2D) shape;
		
		// position of points on the curve
		double pos0 = baseCurve.getPosition(point1);
		double pos1 = baseCurve.getPosition(point2);
		
		// extract sub curve
		curve = baseCurve.getSubCurve(pos0, pos1);
		
		// check validity
		if(curve==null) return;
		if(curve.isEmpty()) return;
		
		this.defined = true;
	}
}
