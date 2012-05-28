/* File BooleanWrapper2D.java 
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
 * Created 2 févr. 08
 */

package dynamic.predicates;

import dynamic.DynamicPredicate2D;

/**
 * A wrapper class to encapsulate a boolean into a DynamicPredicate2D.
 * @author dlegland
 *
 */
public class BooleanWrapper2D extends DynamicPredicate2D {

	boolean b;
	
	/**
	 * 
	 */
	public BooleanWrapper2D(boolean bool) {
		this.b = bool;
		this.defined = true;
	}

	/* (non-Javadoc)
	 * @see dynamic.DynamicPredicate2D#getResult()
	 */
	@Override
	public boolean getResult() {
		return b;
	}

	/**
	 * Nothing to do for boolean wrapper.
	 */
	@Override
	public void update() {
	}

}
