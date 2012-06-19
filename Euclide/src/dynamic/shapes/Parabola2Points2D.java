/* file : Parabola2Points2D.java
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
 * Created on 31 janv. 2007
 *
 */
package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.conic.*;

import dynamic.*;

public class Parabola2Points2D extends DynamicShape2D {

	DynamicShape2D parent1;
	DynamicShape2D parent2;
	
	private Parabola2D parabola = new Parabola2D();
	
	public Parabola2Points2D(DynamicShape2D vertex, DynamicShape2D focus) {
		super();
		this.parent1 = vertex;
		this.parent2 = focus;

		parents.add(vertex);
		parents.add(focus);
		//parents.trimToSize();

		//update();
	}
	
	@Override
	public Shape2D getShape(){
		return parabola;
	}
		
	@Override
	public void update(){
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
				
		Shape2D shape;
		
		shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D vertex = (Point2D) shape;

		shape = parent2.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D focus = (Point2D) shape;
		
		double theta = Angle2D.formatAngle(
				Angle2D.getHorizontalAngle(vertex, focus)-Math.PI/2);
		double p = vertex.getDistance(focus);
		
		this.parabola = new Parabola2D(vertex, 1./4./p, theta);
		this.defined = true;
	}
}
