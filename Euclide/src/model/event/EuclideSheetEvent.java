/* file : EuclideSheetEvent.java
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
public abstract class EuclideSheetEvent {
		
	private final static int ADDED_MASK 	= 1;
	private final static int REMOVED_MASK 	= 2;
	private final static int CHANGED_MASK 	= 4;
	
	private final static int LAYER_MASK 	= 8;
	private final static int CURRENT_MASK 	= 1024;
	
	
	public final static int NAME_CHANGED 	= 16;
	public final static int SIZE_CHANGED 	= 32;
	
	public final static int LAYER_ADDED 	= LAYER_MASK + ADDED_MASK;
	public final static int LAYER_REMOVED 	= LAYER_MASK + REMOVED_MASK;
	public final static int LAYER_MOVED 	= 
		LAYER_MASK + ADDED_MASK + REMOVED_MASK;
	public final static int CURRENT_LAYER_CHANGED = 
		CURRENT_MASK + LAYER_MASK + CHANGED_MASK;
	
	public abstract int getState();
	
	public abstract EuclideSheet getSheet();
}


