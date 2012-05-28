/* file : DynamicObject2D.java
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
 * Created on 3 avr. 2006
 *
 */
package dynamic;

import java.util.ArrayList;
import java.util.Collection;

import model.EuclideObject;

/**
 * Base abstract class for Dynamic objects. Creates empty arrays for parents
 * and parameters, and manages the 'defined' flag.
 * 
 */
public abstract class DynamicObject2D extends EuclideObject {	
	
	protected ArrayList<DynamicObject2D> parents = null;
	protected ArrayList<Object> parameters = null;

	protected boolean defined = false;
	
	protected DynamicObject2D(){
		parents = new ArrayList<DynamicObject2D>(0);
		parameters = new ArrayList<Object>(0);
	}
	
	protected DynamicObject2D(String name){
		super(name);
		parents = new ArrayList<DynamicObject2D>(0);
		parameters = new ArrayList<Object>(0);
	}
	
	/**
	 * Returns true if the object is well defined. For example, the 
	 * intersection point between two lines is undefined if lines 
	 * are parallel.
	 */
	public boolean isDefined() {
		return defined;
	}
		
	/**
	 * Returns the objects whose change directly affect this shape.
	 * Parents are instances of DynamicObject2D.
	 */
	public Collection<? extends DynamicObject2D> getParents() {
		return parents;
	}
	
	/**
	 * Returns the objects, not dynamic, which are needed to build the
	 * DynamicObject2D.
	 * @return
	 */
	public Collection<? extends Object> getParameters() {
		return parameters;
	}
	
	public Object getParameter(int index) {
		return parameters.get(index);
	}
	
	public void setParameter(int index, Object value) {
		parameters.set(index, value);
	}
	
	public int getParameterNumber() {
		return parameters.size();
	}
	
	/**
	 * Update inner data of the geometric objects depending on position of
	 * ancestors.
	 */
	public abstract void update();
}
