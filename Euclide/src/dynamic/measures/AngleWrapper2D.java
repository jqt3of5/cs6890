/* file : AngleWrapper2D.java
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

import math.geom2d.*;
import dynamic.*;

/**
 * A constant angle measure.
 * @author dlegland
 */
public class AngleWrapper2D extends DynamicMeasure2D {

	AngleMeasure2D measure = new AngleMeasure2D();
	
	public AngleWrapper2D() {
		this(0.0);
	}

	/**
	 * Construct from a numerical value, in degrees.
	 * @param value
	 */
	public AngleWrapper2D(double value) {
		this(new AngleMeasure2D(value)); 
	}

	public AngleWrapper2D(AngleMeasure2D measure) {
		super();
		this.measure = measure;
		this.parameters.add(measure.getAngle(AngleUnit.DEGREE));
		//this.parameters.trimToSize();
		this.defined = true;
	}

	@Override
	public AngleMeasure2D getMeasure(){
		return this.measure;
	}
	
	/**
	 * Nothing to do for a constant.
	 */
	@Override
	public void update() {
	}
}
