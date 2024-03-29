/* file : ConicFocii2D.java
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
import math.geom2d.point.PointArray2D;
import dynamic.*;

/**
 * @author dlegland
 */
public class ConicFocii2D extends DynamicShape2D {
	
	DynamicShape2D parent1;
	
	PointArray2D set = new PointArray2D();
	
	public ConicFocii2D(DynamicShape2D conic){
		this.parent1 = conic;

		parents.add(conic);
		//parents.trimToSize();

	//	update(); 
	}

	@Override
	public Shape2D getShape(){
		return set;
	}

	@Override
	public void update(){		
		this.defined = false;
		
		// check parent is defined
		if(!parent1.isDefined()) return;

		// extract the parent conic
		Shape2D shape = parent1.getShape();
		if(!(shape instanceof Conic2D)) return;
		Conic2D conic = (Conic2D) shape;
		
		// update the point set
		set = new PointArray2D(2);
		switch(conic.getConicType()) {
		case CIRCLE:
			set.addPoint(((Circle2D)conic).getCenter());
			break;
		case ELLIPSE:
			set.addPoint(((Ellipse2D)conic).getFocus1());
			set.addPoint(((Ellipse2D)conic).getFocus2());
			break;
		case PARABOLA:
			set.addPoint(((Parabola2D)conic).getFocus());
			break;
		case HYPERBOLA:
			set.addPoint(((Hyperbola2D)conic).getFocus1());
			set.addPoint(((Hyperbola2D)conic).getFocus2());
			break;
		default:
			System.err.println("ConicFocii2D: unknown type of conic !");
		}
		this.defined = true;
	}
}
