/* file : TestDateFormat.java
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
 * Created on 1 avr. 2006
 *
 */

import java.util.Date;
import java.util.Locale;
import java.text.DateFormat;

public class TestDateFormat {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Date date = new Date();
		DateFormat df = DateFormat.getDateInstance();
		DateFormat df2 = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
		String dateString = df.format(date); 
		System.out.println(dateString);
		String dateString2 = df2.format(date); 
		System.out.println(dateString2);
		
		Date creationDate = null;
		try{
			creationDate = df.parse(dateString);
		}catch(java.text.ParseException ex){
			System.out.println(ex);
		}
		System.out.println(creationDate);
		
	}

}
