/* file : ParabolaLinePoint2D.java
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
import math.geom2d.line.*;

import dynamic.*;

public class ParabolaLinePoint2D extends DynamicShape2D {

	DynamicShape2D parent1;
	DynamicShape2D parent2;
	
	private ParabolaArc2D parabola = new ParabolaArc2D(new Parabola2D(), -1, 1);
	
	public ParabolaLinePoint2D(DynamicShape2D line, DynamicShape2D focus) {
		super();
		this.parent1 = line;
		this.parent2 = focus;

		parents.add(line);
		parents.add(focus);
		parents.trimToSize();

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
		if(!(shape instanceof LinearShape2D)) return;
		LinearShape2D line = (LinearShape2D) shape;

		shape = parent2.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D focus = (Point2D) shape;
		
		StraightLine2D support = line.getSupportingLine();
		Point2D p0 = support.getProjectedPoint(focus);
		double p = p0.getDistance(focus)/2;
		if(!support.isInside(focus))
			p = -p;
		
		double xv = (focus.getX() + p0.getX())/2;
		double yv = (focus.getY() + p0.getY())/2;
		
		double theta = line.getHorizontalAngle();
		
		this.parabola = new ParabolaArc2D(new Parabola2D(xv, yv, 1./4./p, theta), -100, 100);
		this.defined = true;
	}
}
