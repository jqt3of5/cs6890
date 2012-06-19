/* file : MeasureLabel2D.java
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

public class MeasureLabel2D extends DynamicLabel2D {

	DynamicShape2D 	parent1;
	DynamicMeasure2D parent2;
	
	Label2D label = new Label2D(new Point2D(), "");
	String format = "%.2f %s";
		
	public MeasureLabel2D(DynamicShape2D position, DynamicMeasure2D measure){
		super();
		this.parent1 = position;
		this.parent2 = measure;
		
		parents.add(position);
		parents.add(measure);
		//parents.trimToSize();
		
	//	update();
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
		
		Shape2D shape;
		Measure2D measure;
		
		// extract first point
		shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point = (Point2D) shape;

		// extract the measure
		measure       = parent2.getMeasure();
		double value  = measure.getValue();		
		String symbol = measure.getUnitSymbol();
		//TODO: more freedom in unit
		if(measure instanceof AngleMeasure2D){
		    symbol    = AngleUnit.DEGREE.getSymbol();
			value 	  = ((AngleMeasure2D)measure).getAngle(AngleUnit.DEGREE);
		}
		
		this.label.setText(String.format(format, 
		        new Object[]{new Double(value), symbol}));

		if(measure instanceof CountMeasure2D){
			int count = ((CountMeasure2D)measure).getCount();
			this.label.setText("" + count);
		}
		
		this.label.setPosition(point);

		this.defined = true;
	}

}
