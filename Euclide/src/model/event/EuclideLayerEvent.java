/* file : EuclideLayerEvent.java
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
 * Created on 29 janv. 2006
 *
 */
package model.event;

import model.*;

/**
 * @author dlegland
 */
public abstract class EuclideLayerEvent {
	
	public final static int NAME_CHANGED = 1;
	public final static int VISIBLE_CHANGED = 2;
	public final static int EDITABLE_CHANGED = 3;
	
	public final static int ELEMENT_ADDED = 4;
	public final static int ELEMENT_REMOVED = 5;
	public final static int ELEMENT_MOVED = 6;
		
	public abstract int getState();
	
	public abstract EuclideLayer getLayer();
}


