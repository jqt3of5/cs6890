/* file : ShapeWrapper2D.java
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
 * Created on 22 avr. 2007
 *
 */
package dynamic.shapes;

import dynamic.*;
import math.geom2d.Shape2D;

/**
 * A default shape builder, to wrap a Shape2D into a DynamicShape2D.
 * @author dlegland
 */
public class ShapeWrapper2D extends DynamicShape2D {

	Shape2D shape;
	
	public ShapeWrapper2D(Shape2D shape) {
		super();
		this.shape = shape;
		this.defined = true;
	}

	@Override
	public Shape2D getShape() {
		return shape;
	}

	public void setShape(Shape2D aShape) {
		this.shape = aShape;
	}

	/**
	 * Nothing to do for a ShapeWrapper2D
	 */
	@Override
	public void update(){
	}
}
