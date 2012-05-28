/*
 * File : PointOnCurvePosition2D.java
 *
 * Project : geometry
 *
 * ===========================================
 * 
 * This library is free software; you can reposribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is posributed in the hope that it will be useful, but 
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
 * A point located on a curve, at a fixed parametric position.
 * @author Legland
 */
public class PointOnCurvePosition2D extends DynamicShape2D{
	
	/** The parent curve */
	DynamicShape2D 		parent1;
	
	/** The position of the point on the curve */
	DynamicMeasure2D 	parent2;

	/** The resulting point */
	private Point2D point = new Point2D();
		
	public PointOnCurvePosition2D(DynamicShape2D curve, DynamicMeasure2D position){
		super();
		this.parent1 = curve;
		this.parent2 = position;

		parents.add(curve);
		parents.add(position);

		update(); 
	}
	
	
	@Override
	public Shape2D getShape(){
		return point;
	}
		
	@Override
	public void update(){
		this.defined = false;
		
		// check parents are defined
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;

		// Extract the parent shape
		Shape2D shape = parent1.getShape();
		if(!(shape instanceof Curve2D)) return;
		Curve2D curve = (Curve2D) shape;
		
		// extract the parent measure
		Measure2D measure = parent2.getMeasure();
		double pos = measure.getValue();
		
		// update position of the point
		point = curve.getPoint(pos);
		this.defined = true;
	}
}
