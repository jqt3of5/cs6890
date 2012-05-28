/* file : AngleMeasure2D.java
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
 * Created on 17 avr. 2006
 *
 */
package math.geom2d;

/**
 * This is simply a wrapper for all Measure class which measure angles.
 * @author dlegland
 */
public class AngleMeasure2D extends Measure2D {

	private AngleUnit unit = AngleUnit.RADIAN;
	
	public AngleMeasure2D(){
		this(0);
	}
	
	public AngleMeasure2D(double value){		
		this(value, AngleUnit.RADIAN);
	}
	
	public AngleMeasure2D(double value, AngleUnit unit){
		this.value = value;
		this.unit = unit;
	}
	
	public double getAngle(AngleUnit unit){
		return AngleUnit.convert(this.value, this.unit, unit);		
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
