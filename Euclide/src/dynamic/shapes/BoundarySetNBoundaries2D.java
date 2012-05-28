/* file : BoundarySetNBoundaries2D.java
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
 * Created on 22 Nov. 2008
 *
 */
package dynamic.shapes;

import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.*;
import math.geom2d.domain.*;
import dynamic.*;

/**
 * @author dlegland
 */
public class BoundarySetNBoundaries2D extends DynamicShape2D {

	ContourArray2D<Contour2D> curve;
	
	ArrayList<DynamicShape2D> shapes;
	
	/**
	 * Initialize with an array of DynamicShape2D
	 * @param point
	 */
	public BoundarySetNBoundaries2D(DynamicShape2D[] curves) {
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
	
	@Override
	public Shape2D getShape(){
		return curve;
	}
	
	@Override
	public void update(){
		this.defined = false;
		Shape2D shape= null;
		
		Contour2D[] curves = new
		Contour2D[shapes.size()];
	
		// convert array of parents to array of ContinuousCurve2D
		for(int i=0; i<shapes.size(); i++){
			DynamicObject2D dyn = shapes.get(i);
			if(!dyn.isDefined()) return;	
			if(!(dyn instanceof DynamicShape2D)) continue;
			
			shape = ((DynamicShape2D) dyn).getShape();
			if(!(shape instanceof Contour2D)) continue;
			
			curves[i] = (Contour2D) shape;
		}
		
		curve = new ContourArray2D<Contour2D>(curves);
		
		this.defined = true;
	}
}
