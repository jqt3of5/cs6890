/* File TranslationVector2D.java 
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
 * Created 2 f�vr. 08
 */

package dynamic.transforms;

import math.geom2d.*;
import math.geom2d.AffineTransform2D;
import math.geom2d.transform.*;

import dynamic.DynamicVector2D;
import dynamic.DynamicTransform2D;

/**
 * @author dlegland
 *
 */
public class TranslationVector2D extends DynamicTransform2D {

	private DynamicVector2D parent;

	AffineTransform2D trans = new AffineTransform2D();
	
	public TranslationVector2D(DynamicVector2D vector){
		super();
		this.parent = vector;

		parents.add(vector);
		
		//update(); 
	}

	/* (non-Javadoc)
	 * @see dynamic.DynamicTransform2D#getTransform()
	 */
	@Override
	public Transform2D getTransform() {
		return trans;
	}

	/* (non-Javadoc)
	 * @see dynamic.DynamicObject2D#update()
	 */
	@Override
	public void update() {
		this.defined = false;
		if(!parent.isDefined()) return;
		
		// extract the vector of translation
		Vector2D vector = parent.getVector();		

		// update distance measure
		this.trans = AffineTransform2D.createTranslation(vector);
		this.defined = true;
	}

}
