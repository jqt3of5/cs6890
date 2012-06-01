/* file : VectorMultiplyVector2D.java
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
 * Created on 1 Apr. 2008
 *
 */
package dynamic.vectors;

import math.geom2d.*;
import dynamic.*;

public class VectorMultiplyVector2D extends DynamicVector2D{

	private DynamicVector2D parent;
	private DynamicMeasure2D factor;
	
	Vector2D vector = new Vector2D(0, 0);
	
	public VectorMultiplyVector2D(DynamicVector2D vector, DynamicMeasure2D measure){
		super();
		parent = vector;
		factor = measure;

		parents.add(vector);
		parents.add(measure);

		//update(); 
	}

	@Override
	public Vector2D getVector(){
		return vector;
	}
	
	@Override
	public void update() {
		this.defined = false;
		if(!parent.isDefined()) return;
		if(!factor.isDefined()) return;

		// extract parent vectors
		Vector2D v1 = parent.getVector();
		Measure2D meas = factor.getMeasure();
		double k = meas.getValue();
		
		// updates coordinate of the inner vector
		this.vector = new Vector2D(k*v1.getX(), k*v1.getY());
		this.defined = true;
	}
}
