/* file : ComposedTransform2D.java
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
 * Created on 16 Dec 2007
 *
 */
package dynamic.transforms;

import math.geom2d.AffineTransform2D;
import math.geom2d.transform.Transform2D;
import dynamic.*;

/**
 * The composition of 2 affine transforms. The second chosen transform is,
 * applied first, then the first chosen transform. 
 * @author dlegland
 *
 */
public class ComposedTransform2D extends DynamicTransform2D{

	private DynamicTransform2D parent1;
	private DynamicTransform2D parent2;
	
	AffineTransform2D trans = new AffineTransform2D();
	
	public ComposedTransform2D(DynamicTransform2D trans1, DynamicTransform2D trans2){
		super();
		parent1 = trans1;
		parent2 = trans2;

		parents.add(trans1);
		parents.add(trans2);

		//update(); 
	}

	@Override
	public Transform2D getTransform(){
		return trans;
	}
	
	@Override
	public void update() {
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;

		// extract first transform
		Transform2D trans1 = parent1.getTransform();
		if(!(trans1 instanceof AffineTransform2D)) return;
		AffineTransform2D aff1 = (AffineTransform2D) trans1;
		
		// extract second transform
		Transform2D trans2 = parent2.getTransform();
		if(!(trans2 instanceof AffineTransform2D)) return;
		AffineTransform2D aff2 = (AffineTransform2D) trans2;

		// update distance measure
		this.trans = aff2.concatenate(aff1);
		this.defined = true;
	}
}
