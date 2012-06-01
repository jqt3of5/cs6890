/*
 * File : CurveCurveSetIndex2D.java
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
 * Created on 23 Aug. 2009
 */
package dynamic.shapes;

import math.geom2d.Measure2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveSet2D;
import dynamic.DynamicMeasure2D;
import dynamic.DynamicShape2D;
import dynamic.measures.Constant2D;

/**
 * @author Legland
 */
public class CurveCurveSetIndex2D extends DynamicShape2D{
	
	DynamicShape2D 		parent1;
	DynamicMeasure2D 	parent2;
	
	Curve2D curve = null;
	
	public CurveCurveSetIndex2D(DynamicShape2D parent1, int num){
		super();
		this.parent1 = parent1;
		this.parent2 = new Constant2D(num);
	
		parents.add(parent1);
		parameters.add(num);
		
		
		//update(); 
	}

	public CurveCurveSetIndex2D(DynamicShape2D curveSet, DynamicMeasure2D count){
		super();
		this.parent1 = curveSet;
		this.parent2 = count;
		
		parents.add(curveSet);
		parents.add(count);
		
		//update(); 
	}


	/**
	 * Computes the initial index of the vertex by choosing the closest vertex
	 * to a given point.
	 * @param parent1 a dynamic shape containing a polygon
	 * @param parent2 a dynamic shape containing a point
	 */
	public CurveCurveSetIndex2D(DynamicShape2D parent1, DynamicShape2D parent2){
		super();
		this.parent1 = parent1;
	
		parents.add(parent1);
		
		// Extract position of parent
		Shape2D shape = parent1.getShape();
		if(!(shape instanceof CurveSet2D)) return;
		CurveSet2D<?> set = (CurveSet2D<?>) shape;
		
		// Extract the parent point
		shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point = (Point2D) shape;
		
		// Compute initial vertex number of point on polygon
		int num = set.getCurveIndex(set.project(point));
		this.parent2 = new Constant2D(num);
		
		parameters.add(num);
		
		//update(); 
	}
	
	@Override
	public Shape2D getShape(){
		return curve;
	}
	
	@Override
	public void update(){
		this.defined = false;
		if(!parent1.isDefined()) return;

		// Extract parent polygon
		Shape2D shape = parent1.getShape();
		if(!(shape instanceof CurveSet2D)) return;
		CurveSet2D<?> set = (CurveSet2D<?>) shape;
				
		// get index of curve
		Measure2D meas = parent2.getMeasure();
		int num = (int) meas.getValue();
		if(set.getCurveNumber()<num) return;
		
		// update point coordinates
		curve = set.getCurve(num);
		this.defined = true;
	}
}
