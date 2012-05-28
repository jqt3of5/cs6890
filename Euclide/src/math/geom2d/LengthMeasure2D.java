/* file : LengthMeasure2D.java
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
package math.geom2d;


public class LengthMeasure2D extends Measure2D {

	private LengthUnit unit = LengthUnit.MILLIMETER;

	
	public LengthMeasure2D(){
		this(0);
	}
	
	public LengthMeasure2D(double value){		
		this(value, LengthUnit.MILLIMETER);
	}
	
	public LengthMeasure2D(double value, LengthUnit unit){
		this.value = value;
		this.unit = unit;
	}

     public double getDistance(LengthUnit unit){
        return LengthUnit.convert(this.value, this.unit, unit);        
    }

	@Override
	public String getUnitName(){
		return unit.getName();
	}
	
	@Override
	public String getUnitSymbol(){
		return unit.getSymbol();
	}
}
