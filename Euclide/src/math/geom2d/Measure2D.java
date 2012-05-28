/* file : Measure2D.java
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

/**
 * Measure of a parameter on a geometric planar shape. Result can be a
 * length, an area, a number, an angle...<p>
 * Further version of this class may manage different types of units, maybe
 * linked with JScience API.
 * @author dlegland
 */
public class Measure2D {

	protected double value = 0;
	protected String unitName = "";
	protected String unitSymbol = "";
	
	public Measure2D(){
		this(0);
	}
	
	public Measure2D(double value){		
		this.value = value;
	}
	
	public Measure2D(double value, String unit){
		this.value = value;
		this.unitName = unit;
	}
	
	public Measure2D(double value, String unit, String symbol){
		this.value = value;
		this.unitName = unit;
		this.unitSymbol = symbol;
	}
	
	public String getUnitName(){
		return unitName;
	}
	
	public String getUnitSymbol(){
		return unitSymbol;
	}
	
	public void setUnitName(String unit){
		this.unitName = unit;
	}
	
	public double getValue(){
		return value;
	}
	
	public void setValue(double value){
		this.value = value;
	}	
	
	@Override
	public String toString(){
		return String.format("%5.2f %s", getValue(), getUnitName());
	}
}
