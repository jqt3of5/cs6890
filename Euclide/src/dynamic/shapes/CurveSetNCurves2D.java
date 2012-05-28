/* file : CurveSetNCurves2D.java
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
 * Created on 11 Nov. 2008
 *
 */
package dynamic.shapes;

import java.util.ArrayList;

import math.geom2d.*;
import math.geom2d.curve.*;
import dynamic.*;

/**
 * Concatenate several curves into a CurveArray2D.
 * @author dlegland
 */
public class CurveSetNCurves2D extends DynamicShape2D {

	CurveArray2D<Curve2D> curveSet;
	
	ArrayList<DynamicShape2D> shapes;
	
	/**
	 * Initialize with an array of DynamicShape2D
	 */
	public CurveSetNCurves2D(DynamicShape2D[] curves) {
		super();
		
		DynamicArray2D array = new DynamicArray2D(curves);
		
		initializeWithArray(array);
		update();
	}
	
	/**
	 * Initialize with a DynamicArray containing only instances of 
	 * DynamicShape2D.
	 */
	public CurveSetNCurves2D(DynamicArray2D array) {
		super();
		
		initializeWithArray(array);
		update();
	}
	
	private void initializeWithArray(DynamicArray2D array) {
		// add array to the list of parents
		this.parents.add(array);
		
		this.shapes = new ArrayList<DynamicShape2D>(array.getSize());
		
		for(DynamicObject2D shape : array.getParents())
			if(shape instanceof DynamicShape2D)
				this.shapes.add((DynamicShape2D) shape);		
	}
	
	@Override
	public Shape2D getShape(){
		return curveSet;
	}
	
	@Override
	public void update(){
		this.defined = false;
		Shape2D shape= null;
		
		curveSet = new CurveArray2D<Curve2D>();
	
		// add curves which are defined
		for(int i=0; i<shapes.size(); i++){
			DynamicObject2D dyn = shapes.get(i);
			if(!dyn.isDefined()) continue;			
			if(!(dyn instanceof DynamicShape2D)) continue;
			
			shape = ((DynamicShape2D) dyn).getShape();
			if(!(shape instanceof Curve2D)) continue;
			
			curveSet.addCurve((Curve2D) shape);
		}
		
		this.defined = true;
	}
}
