/* file : CountWrapper2D.java
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
 * Created on 25 Nov. 2009
 *
 */
package dynamic.measures;

import math.geom2d.*;
import dynamic.*;

/**
 * A constant angle measure.
 * @author dlegland
 */
public class CountWrapper2D extends DynamicMeasure2D {

	CountMeasure2D measure = new CountMeasure2D();
	
	public CountWrapper2D() {
		this(0);
	}

	/**
	 * Construct from a numerical value, in degrees.
	 * @param value
	 */
	public CountWrapper2D(int count) {
		this(new CountMeasure2D(count)); 
	}

	public CountWrapper2D(CountMeasure2D measure) {
		super();
		this.measure = measure;
		this.parameters.add(measure.getCount());
		this.parameters.trimToSize();
		this.defined = true;
	}

	@Override
	public CountMeasure2D getMeasure(){
		return this.measure;
	}
	
	/**
	 * Nothing to do for a constant.
	 */
	@Override
	public void update() {
	}
}
