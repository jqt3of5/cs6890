/* file : VectorAdd2Vectors2D.java
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
 * Created on 1 feb. 2008
 *
 */
package dynamic.vectors;

import math.geom2d.*;
import dynamic.*;

public class VectorAdd2Vectors2D extends DynamicVector2D{

	private DynamicVector2D parent1;
	private DynamicVector2D parent2;
	
	Vector2D vector = new Vector2D(0, 0);
	
	public VectorAdd2Vectors2D(DynamicVector2D point1, DynamicVector2D point2){
		super();
		parent1 = point1;
		parent2 = point2;

		parents.add(point1);
		parents.add(point2);

	//	update(); 
	}

	@Override
	public Vector2D getVector(){
		return vector;
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
		this.vector = new Vector2D(v1.getX()+v2.getX(), v1.getY()+v2.getY());
		this.defined = true;
	}
}
