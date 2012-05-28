/*
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
 * Created on 27 Oct. 2007
 *
 */

package dynamic.predicates;

import math.geom2d.Shape2D;
import dynamic.DynamicPredicate2D;
import dynamic.DynamicShape2D;

/**
 * Check if a shape is empty or not.
 * @author dlegland
 *
 */
public class IsEmpty2D extends DynamicPredicate2D {

	DynamicShape2D parent1;
	
	private boolean res = true;

	public IsEmpty2D(DynamicShape2D shape){
		this.parent1 = shape;
		
		// Add tothe list of parents
		parents.add(shape);
		parents.trimToSize();
		
		update();
	}
	
	@Override
	public boolean getResult() {
		return res;
	}

	@Override
	public void update() {
		this.defined = false;
		
		// Check parent is defined
		if(!parent1.isDefined()) return;
		
		// extract the point
		Shape2D shape = parent1.getShape();
		
		// Update predicate result
		this.res = shape.isEmpty();
		this.defined = true;
	}
}
