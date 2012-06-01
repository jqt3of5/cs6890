/* file : DotProduct2Vectors2D.java
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
package dynamic.measures;

import math.geom2d.*;
import dynamic.*;

public class DotProduct2Vectors2D extends DynamicMeasure2D{

	private DynamicVector2D parent1;
	private DynamicVector2D parent2;
	
	Measure2D measure = new Measure2D(0);
	
	public DotProduct2Vectors2D(DynamicVector2D vector1, DynamicVector2D vector2){
		super();
		parent1 = vector1;
		parent2 = vector2;

		parents.add(vector1);
		parents.add(vector2);

		//update(); 
	}

	@Override
	public Measure2D getMeasure(){
		return measure;
	}
	
	@Override
	public void update() {
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;

		// extract parent vectors
		Vector2D v1 = parent1.getVector();
		Vector2D v2 = parent2.getVector();
		
		// updates coordinate of the inner vector
		this.measure.setValue(Vector2D.dot(v1, v2));
		this.defined = true;
	}
}
