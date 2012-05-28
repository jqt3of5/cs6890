/* file : CountMeasure2D.java
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


public class CountMeasure2D extends Measure2D {

	int count;
	
	public CountMeasure2D(){
		this(0);
	}
	
	public CountMeasure2D(int value){
		this.value = value;
		this.count = value;
	}

	
	public int getCount(){
		return count;
	}
	
	public void setCount(int count){
		this.count = count;
	}
	
	@Override
	public double getValue(){
		return count;
	}

	@Override
	public String getUnitName(){
		return "";
	}
	
	@Override
	public String getUnitSymbol(){
		return "";
	}
}
