/* file : Constant2D.java
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
 * Created on 27 nov. 2006
 *
 */
package dynamic.measures;

import math.geom2d.AngleMeasure2D;
import math.geom2d.AngleUnit;
import math.geom2d.LengthMeasure2D;
import math.geom2d.LengthUnit;
import math.geom2d.Measure2D;
import dynamic.DynamicMeasure2D;

/**
 * A constant measure. The measure contained in this class can be changed
 * directly by the user.
 * See also MeasureWrapper2D and AngleWrapper2D.
 * @author dlegland
 */
public class Constant2D extends DynamicMeasure2D {

	Measure2D measure = new Measure2D();
	
	public Constant2D() {
		this(0.0);
	}

	public Constant2D(double value) {
		super();
		this.measure.setValue(value);
		this.defined = true;
	}

	public Constant2D(double value, String unit) {
		super();
		this.setupMeasure(value, unit);
		this.defined = true;
	}

	public Constant2D(Measure2D measure) {
		super();
		this.measure = measure;
		this.defined = true;
	}

	public Constant2D(Measure2D measure, String unit) {
		super();
		this.setupMeasure(measure.getValue(), unit);
		this.defined = true;
	}

	
	@Override
	public Measure2D getMeasure(){
		return this.measure;
	}
	
	public void setMeasure(Measure2D measure){
		this.measure = measure;
		this.defined = true;
	}
	
	public void setMeasure(Measure2D measure, String unit){
		this.setupMeasure(measure.getValue(), unit);
		this.defined = true;
	}
	
	private void setupMeasure(double value, String unit){
		unit = unit.toLowerCase();
		
		if(unit.startsWith("mm"))
			this.measure = new LengthMeasure2D(value, 
			        LengthUnit.MILLIMETER);
		else if(unit.equals("m"))
			this.measure = new LengthMeasure2D(value, 
					LengthUnit.METER);
		else if(unit.equals("cm"))
			this.measure = new LengthMeasure2D(value, 
					LengthUnit.CENTIMETER);
		else if(unit.equals("µm"))
			this.measure = new LengthMeasure2D(value, 
					LengthUnit.MICROMETER);

		else if(unit.startsWith("rad"))
			this.measure = new AngleMeasure2D(value, 
			        AngleUnit.RADIAN);
		else if(unit.startsWith("deg"))
			this.measure = new AngleMeasure2D(value, 
			        AngleUnit.DEGREE);
		else if(unit.equals("°"))
			this.measure = new AngleMeasure2D(value, 
			        AngleUnit.DEGREE);
		else
			this.measure = new Measure2D(value, unit);
	}
	
	/**
	 * Nothing to do for a constant.
	 */
	@Override
	public void update() {
	}
}
