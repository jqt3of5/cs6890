/* file : TestCreateInt.java
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
 * Created on 19 mars 2006
 *
 */

import java.util.*;


/**
 * @author dlegland
 */
public class TestCreateInt {

	public static void main(String[] args) {
		
		ArrayList<Object> arguments = new ArrayList<Object>();
		
		// try to read as Integer
		String string="32";
		try{
			Integer x = Integer.valueOf(string);
			arguments.add(x);
		}catch(NumberFormatException ex){
			System.out.println("number exception");
		}
		
		try{
			int x = Integer.valueOf(string).intValue();
			System.out.println(x);
		}catch(NumberFormatException ex){
			System.out.println("number exception");
		}		
	}
	
	class FooClass{
		
		public FooClass(String string, int value){
			System.out.println("constructor of FooClass");
			System.out.println("	String : " + string);
			System.out.println("	int    : " + value);
		}
	}
}
