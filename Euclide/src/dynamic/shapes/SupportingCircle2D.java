/* file : SupportingCircle2D.java
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
 * Created on 15 Oct. 2009
 *
 */
package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.conic.*;
import dynamic.*;

/**
 * Create the circle that correspond to a circle arc (or a circle, in this
 * case the result equals the parent circle).
 * @author dlegland
 */
public class SupportingCircle2D extends DynamicShape2D {
	
	DynamicShape2D parent1;
	
	Circle2D circle;
	
	public SupportingCircle2D(DynamicShape2D circle){
		this.parent1 = circle;

		parents.add(circle);
		parents.trimToSize();

	//	update(); 
	}

	@Override
	public Shape2D getShape(){
		return circle;
	}

	@Override
	public void update(){		
		this.defined = false;
		if(!parent1.isDefined()) return;

		Shape2D shape = parent1.getShape();
		if(!(shape instanceof CircularShape2D)) return;
		CircularShape2D arc = (CircularShape2D) shape;
		
		// update the circle
		this.circle = arc.getSupportingCircle();
		this.defined = true;
	}
}
