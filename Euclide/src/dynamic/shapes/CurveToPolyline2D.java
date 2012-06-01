/*
 * File : CurveToPolyline2D.java
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
 * Created on 13 May 2006
 */
package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.curve.*;
import math.geom2d.polygon.Polyline2D;

import dynamic.*;

/**
 * A point located on a curve, at a fixed parametric position.
 * @author Legland
 */
public class CurveToPolyline2D extends DynamicShape2D{
	
	DynamicShape2D 		parent1;
	DynamicMeasure2D 	parent2;

	private Polyline2D polyline = new Polyline2D();

	public CurveToPolyline2D(DynamicShape2D curve, DynamicMeasure2D nbSeg){
		super();
		this.parent1 = curve;
		this.parent2 = nbSeg;

		parents.add(curve);
		parents.add(nbSeg);
		parents.trimToSize();

		//update(); 
	}	
	
	@Override
	public Shape2D getShape(){
		return polyline;
	}
	
	@Override
	public void update(){
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;

		Shape2D shape;
		Measure2D measure;
		
		shape = parent1.getShape();
		if(!(shape instanceof ContinuousCurve2D)) return;
		ContinuousCurve2D curve = (ContinuousCurve2D) shape;
		
		measure = parent2.getMeasure();
		double value = measure.getValue();
		int nbSeg = (int) Math.round(value);
		
		polyline = curve.getAsPolyline(nbSeg);
		this.defined = true;
	}
}
