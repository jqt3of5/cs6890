/* file : AngleMeasureLabel2D.java
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

/**
 * A specialized measure label for angles, taking into account the unit to
 * display angle.
 * 
 * @author dlegland
 */
public class AngleMeasureLabel2D extends MeasureLabel2D {

    AngleUnit unit = AngleUnit.DEGREE;
		
	public AngleMeasureLabel2D(DynamicShape2D position, DynamicMeasure2D measure, AngleUnit unit){
		super(position, measure);

		this.unit = unit;
		update();
	}

	@Override
	public void update() {
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
		
		Shape2D shape;
		AngleMeasure2D measure;
		
		// extract first point
		shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point = (Point2D) shape;

		if(unit==null)
			return;
		
		// extract second point
		measure = (AngleMeasure2D) parent2.getMeasure();
		double value = measure.getAngle(unit);
		
		String unitName = unit.getSymbol();
		
		this.label.setText(String.format(format, 
		        new Object[]{new Double(value), unitName}));
		this.label.setPosition(point);

		this.defined = true;
	}

}
