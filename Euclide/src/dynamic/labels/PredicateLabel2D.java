/* file : PredicateLabel2D.java
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
 * Created on 29 avr. 2007
 *
 */
package dynamic.labels;

import math.geom2d.*;
import dynamic.*;

public class PredicateLabel2D extends DynamicLabel2D {
	
	DynamicShape2D 	parent1;
	DynamicPredicate2D parent2;
	
	Label2D label = new Label2D(new Point2D(), "");
	
	public PredicateLabel2D(DynamicShape2D position, DynamicPredicate2D predicate){
		super();
		this.parent1 = position;
		this.parent2 = predicate;
		
		parents.add(position);
		parents.add(predicate);
		parents.trimToSize();
		
		//update();
	}

	@Override
	public Label2D getLabel(){
		return this.label;
	}
	
	@Override
	public void update() {
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
		
		// extract position
		Shape2D shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point = (Point2D) shape;

		
		// extract predicate result
		boolean result = parent2.getResult();
		
		this.label.setText(Boolean.toString(result));
		this.label.setPosition(point);
		this.defined = true;
	}

}
