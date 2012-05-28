/* file : PolySmoothCurveNCurves2D.java
 * 
 * Project : Euclide
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
 * Created on 23 Feb. 2007
 *
 */
package dynamic.shapes;

import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.*;
import math.geom2d.circulinear.BoundaryPolyCirculinearCurve2D;
import math.geom2d.circulinear.CirculinearContinuousCurve2D;
import math.geom2d.circulinear.PolyCirculinearCurve2D;
import math.geom2d.curve.*;
import math.geom2d.domain.*;
import dynamic.*;
import dynamic.predicates.BooleanWrapper2D;

/**
 * @author dlegland
 */
public class PolySmoothCurveNCurves2D extends DynamicShape2D {

	PolyCurve2D<? extends ContinuousOrientedCurve2D> curve;
	
	ArrayList<DynamicShape2D> shapes;
	DynamicPredicate2D pred = new BooleanWrapper2D(true);

	/**
	 * Initialize with an array of DynamicShape2D
	 * @param point
	 */
	public PolySmoothCurveNCurves2D(DynamicShape2D[] curves) {
		super();
		
		DynamicArray2D array = new DynamicArray2D(curves);
		this.parents.add(array);
		
		// Keep only the dynamic objects which are instances of DynamicShape2D
		Collection<? extends DynamicObject2D> shapes = array.getParents();	
		this.shapes = new ArrayList<DynamicShape2D>(shapes.size());
		for(DynamicObject2D curve : shapes)
			if(curve instanceof DynamicShape2D)
				this.shapes.add((DynamicShape2D) curve);

		update();
	}
	
	/**
	 * Initialize with an array of DynamicShape2D
	 * @param point
	 */
	public PolySmoothCurveNCurves2D(DynamicShape2D[] curves, 
			DynamicPredicate2D pred) {
		super();
		
		DynamicArray2D array = new DynamicArray2D(curves);
		this.parents.add(array);
		if (pred instanceof BooleanWrapper2D)
			parameters.add(pred.getResult());
		else
			parents.add(pred);
		
		this.pred = pred;
		
		// Keep only the dynamic objects which are instances of DynamicShape2D
		Collection<? extends DynamicObject2D> shapes = array.getParents();	
		this.shapes = new ArrayList<DynamicShape2D>(shapes.size());
		for(DynamicObject2D curve : shapes)
			if(curve instanceof DynamicShape2D)
				this.shapes.add((DynamicShape2D) curve);

		update();
	}
	
	/**
	 * Initialize with an array of DynamicShape2D
	 * @param point
	 */
	public PolySmoothCurveNCurves2D(DynamicShape2D[] curves, boolean closed) {
		super();
		
		DynamicArray2D array = new DynamicArray2D(curves);
		this.parents.add(array);
		this.parameters.add(closed);
		
		this.pred = new BooleanWrapper2D(closed);
		
		// Keep only the dynamic objects which are instances of DynamicShape2D
		Collection<? extends DynamicObject2D> shapes = array.getParents();	
		this.shapes = new ArrayList<DynamicShape2D>(shapes.size());
		for(DynamicObject2D curve : shapes)
			if(curve instanceof DynamicShape2D)
				this.shapes.add((DynamicShape2D) curve);

		update();
	}
	
	
	@Override
	public Shape2D getShape(){
		return curve;
	}
	
	@Override
	public void update(){
		this.defined = false;
		Shape2D shape= null;
		
		
		if(!pred.isDefined()) return;
		boolean closed = pred.getResult();

		ContinuousOrientedCurve2D[] curves = new
			ContinuousOrientedCurve2D[shapes.size()];
	
		boolean circu = true;
		
		// convert array of parents to array of ContinuousCurve2D
		for (int i=0; i<shapes.size(); i++) {
			DynamicObject2D dyn = shapes.get(i);
			if(!dyn.isDefined()) return;			
			if(!(dyn instanceof DynamicShape2D)) continue;
			
			shape = ((DynamicShape2D) dyn).getShape();
			// All curves need to be both continuous and oriented 
			if(!(shape instanceof ContinuousOrientedCurve2D)) 
				return;
			
			curves[i] = (ContinuousOrientedCurve2D) shape;
			
			// check if curve is also circulinear
			if (!(shape instanceof CirculinearContinuousCurve2D))
				circu = false;
		}
		
		//TODO rewrite in a cleaner way
		if (circu) {
			// Create a circulinear polycurve or contour

			// first, convert to array of circulinear curves
			CirculinearContinuousCurve2D[] circuCurves = 
				new CirculinearContinuousCurve2D[shapes.size()];
			for (int i=0; i<shapes.size(); i++) {
				circuCurves[i] = (CirculinearContinuousCurve2D) curves[i];
			}

			// create the corresponding structure
			if(closed)
				curve = new BoundaryPolyCirculinearCurve2D<CirculinearContinuousCurve2D>(circuCurves);
				//TODO: use static constructor when javaGeom is fixed (0.9.0)
			else
				curve = PolyCirculinearCurve2D.create(circuCurves);
		} else {
			// create a normal polycurve or contour
			if(closed)
				curve = BoundaryPolyCurve2D.create(curves);
			else
				curve = PolyCurve2D.create(curves);
		}
			
		curve.setClosed(closed);
		this.defined = true;
	}
}
