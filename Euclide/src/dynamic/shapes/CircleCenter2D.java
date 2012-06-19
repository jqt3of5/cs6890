/* file : CircleCenter2D.java
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
 * Created on 22 janv. 2006
 *
 */
package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.conic.*;
import dynamic.*;

/**
 * @author dlegland
 */
public class CircleCenter2D extends DynamicShape2D {
	
	DynamicShape2D parent1;
	
	Point2D point = new Point2D();
	
	public CircleCenter2D(DynamicShape2D circle){
		this.parent1 = circle;

		parents.add(circle);
		//parents.trimToSize();

		//update(); 
	}

	@Override
	public Shape2D getShape(){
		return point;
	}

	@Override
	public void update(){		
		this.defined = false;
		if(!parent1.isDefined()) return;

		Shape2D shape = parent1.getShape();
		if(!(shape instanceof Circle2D)) return;
		Circle2D circle = (Circle2D) shape;
		
		// update position of point
		this.point = circle.getCenter();
		this.defined = true;
	}
}
