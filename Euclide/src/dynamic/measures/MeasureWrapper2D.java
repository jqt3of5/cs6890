/* file : MeasureWrapper2D.java
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
 * A wrapper class to encapsulate a constant measure. At the difference with
 * Constant2D, this class is not supposed to be edited later by the user,
 * and is likely to be used only in the document.
 * @author dlegland
 */
public class MeasureWrapper2D extends DynamicMeasure2D {

	Measure2D measure = new Measure2D();
	
	public MeasureWrapper2D() {
		this(0.0);
	}

	public MeasureWrapper2D(double value) {
		super();
		this.measure.setValue(value);
		this.defined = true;
	}

	public MeasureWrapper2D(double value, String unit) {
		super();
		this.measure.setValue(value);
		this.measure.setUnitName(unit);
		this.defined = true;
	}

	public MeasureWrapper2D(Measure2D measure) {
		super();
		this.measure = measure;
		this.defined = true;
	}

	@Override
	public Measure2D getMeasure() {
		return this.measure;
	}
	
	/**
	 * Nothing to do for a constant.
	 */
	@Override
	public void update() {
	}
}
