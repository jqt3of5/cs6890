/* file : CurveLocus2D.java
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
 * Created on 17 avr. 2006
 *
 */
package dynamic.shapes;

import math.geom2d.Shape2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveArray2D;
import dynamic.DynamicShape2D;

/**
 * A set of points marking successive position of a moving point.
 * @author dlegland
 */
public class CurveLocus2D extends DynamicShape2D {
	
	DynamicShape2D parent1;
	
	CurveArray2D<Curve2D> curves = new CurveArray2D<Curve2D>();
	
	public CurveLocus2D(DynamicShape2D point){
		this.parent1 = point;

		parents.add(point);
		parents.trimToSize();
		this.defined = true;

		update();
	}
		
	@Override
	public Shape2D getShape(){
		return curves;
	}
	
	public void clearLocus() {
		curves.clearCurves();
	}
	
	@Override
	public void update() {
		// check parent is defined
		if(!parent1.isDefined())
			return;
		
		// extract parent shape
		Shape2D shape = parent1.getShape();
		if(!(shape instanceof Curve2D)) return;
		Curve2D curve = (Curve2D) shape;
		
		// update the locus
		curves.addCurve(curve.clone());
	}
}
