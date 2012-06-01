/* file : TransformedShape2D.java
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
 * Created on 9 avr. 2006
 *
 */
package dynamic.shapes;

import org.apache.log4j.Logger;

import math.geom2d.*;
import math.geom2d.circulinear.CirculinearShape2D;
import math.geom2d.transform.CircleInversion2D;
import math.geom2d.transform.Transform2D;

import dynamic.*;


public class TransformedShape2D extends DynamicShape2D {

	/** Apache log4j Logger */
	private static Logger logger = Logger.getLogger("Euclide");

	DynamicShape2D parent1;
	DynamicTransform2D parent2;
	
	Shape2D transformed = new Point2D();
	
	public TransformedShape2D(DynamicShape2D point, DynamicTransform2D transform){
		this.parent1 = point;
		this.parent2 = transform;

		parents.add(point);
		parents.add(transform);
		parents.trimToSize();

		//update(); 
	}
	
	@Override
	public Shape2D getShape(){
		return this.transformed;
	}
		
	@Override
	public void update(){		
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;

		Shape2D shape;
		Transform2D transform;
		
		// extract first point
		shape = parent1.getShape();
		
		// extract second point
		transform = parent2.getTransform();
		
		// update position of shape
		if (transform instanceof AffineTransform2D)
			this.transformed = shape.transform((AffineTransform2D) transform);
		else if (transform instanceof CircleInversion2D) {
			if (shape instanceof CirculinearShape2D) {
				CirculinearShape2D circu = (CirculinearShape2D) shape;
				this.transformed = circu.transform((CircleInversion2D) transform);
			} else {
				logger.error("Circle inversion transform requires circulinear shape, type is " + 
						shape.getClass().getName());
				this.transformed = null;
			}
		} else {
			logger.error("Unknown transform type in TransformedShape2D");
			this.transformed = null;
		}
		
		if(this.transformed==null) return;
		this.defined = true;
	}
}
