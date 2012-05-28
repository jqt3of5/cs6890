/* file : TangentVectorCurvePosition2D.java
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
 * Created on 22 déc. 2005
 *
 */
package dynamic.vectors;

import math.geom2d.Measure2D;
import math.geom2d.Shape2D;
import math.geom2d.Vector2D;
import math.geom2d.curve.Curve2D;
import util.CurveUtils;
import dynamic.DynamicMeasure2D;
import dynamic.DynamicShape2D;
import dynamic.DynamicVector2D;

/**
 * @author dlegland
 */
public class TangentVectorCurvePosition2D extends DynamicVector2D {

	DynamicShape2D parent1;
	DynamicMeasure2D parent2;
	
	private Vector2D vector = new Vector2D();
	
	/**
	 * Tangent to a curve at a given point. 
	 * @param curve
	 * @param point
	 */
	public TangentVectorCurvePosition2D(DynamicShape2D curve, 
			DynamicMeasure2D position) {
		this.parent1 = curve;
		this.parent2 = position;

		parents.add(curve);
		parents.add(position);
		parents.trimToSize();

		update();
	}	

	/* (non-Javadoc)
	 * @see dynamic.DynamicVector2D#getVector()
	 */
	@Override
	public Vector2D getVector() {
		return vector;
	}
	
	@Override
	public void update(){
		this.defined = false;
		// check parents are defined
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
		
		// Extract the shape
		Shape2D shape = parent1.getShape();
		if(!(shape instanceof Curve2D)) return;
		Curve2D curve = (Curve2D) shape;
		
		// extract a point on the curve
		Measure2D measure = parent2.getMeasure();
		double pos = measure.getValue();
		
		// compute position of the point on the curve
		
		// extract local tangent
		vector = CurveUtils.getTangentVector(curve, pos);		
		this.defined = true;
	}

}
