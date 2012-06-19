/*
 * File : SubCurvePositions2D.java
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
 * Compute a curve portion from a parent curve and two positions.
 * @author Legland
 */
public class SubCurvePositions2D extends DynamicShape2D{
	
	DynamicShape2D parent1;
	DynamicMeasure2D parent2;
	DynamicMeasure2D parent3;
	
	Curve2D curve = null;
	
	public SubCurvePositions2D(DynamicShape2D baseCurve, 
			DynamicMeasure2D point1,DynamicMeasure2D point2){
		super();
		this.parent1 = baseCurve;
		this.parent2 = point1;
		this.parent3 = point2;

		parents.add(baseCurve);
		parents.add(point1);
		parents.add(point2);
		//parents.trimToSize();

		//update();
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

		// extract parent curve
		Shape2D shape = parent1.getShape();
		if(!(shape instanceof Curve2D)) return;
		Curve2D baseCurve = (Curve2D) shape;
		
		// extract first position
		Measure2D measure = parent2.getMeasure();
		double pos0 = measure.getValue();
		
		// extract second position
		measure = parent3.getMeasure();
		double pos1 = measure.getValue();
		
		// Check values correspond to the curve
		pos0 = Math.max(pos0, baseCurve.getT0());
		pos1 = Math.min(pos1, baseCurve.getT1());
		
		// extract sub curve
		curve = baseCurve.getSubCurve(pos0, pos1);
		
		// check validity
		if(curve==null) return;
		this.defined = true;
	}
}
