/* file : DynamicMeasure2D.java
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
 * Created on 22 avr. 2007
 *
 */

package dynamic;

import math.geom2d.*;


/**
 * A dynamic object which encapsulate a Measure2D. It also provides a type to
 * specify the type of stored measure.
 * @author dlegland
 */
public abstract class DynamicMeasure2D extends DynamicObject2D {

	/**
	 * Several types of measure, to facilitate management of units.
	 */
	public enum Type {
		NONE("None"),
		COUNTING("Counting"),
		ANGLE("Angle"),
		LENGTH("Distance");
		
		private String name;
		
		Type(String name){
			this.name = name;
		}
		
		public String getName(){
			return name;
		}
		
		@Override
		public String toString(){
			return name;
		}
	}
	
	/** Returns the constructed measure */
	public abstract Measure2D getMeasure();

	/**
	 * Determines the type of a dynamic measure.
	 */
	public static Type getMeasureType(DynamicMeasure2D dynMeasure) {
		return getMeasureType(dynMeasure.getMeasure());
	}
	
	/**
	 * Determines the type of a measure.
	 */
	public static Type getMeasureType(Measure2D measure) {
		if (measure instanceof LengthMeasure2D) 
			return Type.LENGTH;
		if (measure instanceof AngleMeasure2D) 
			return Type.ANGLE;
		if (measure instanceof CountMeasure2D) 
			return Type.COUNTING;
		
		// if not a nown class, return NONE
		return Type.NONE;
	}
}
