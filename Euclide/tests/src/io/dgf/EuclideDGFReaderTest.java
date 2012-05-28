/* file : EuclideDGFReaderTest.java
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
 * Created on 8 mai 2007
 *
 */
package io.dgf;

import io.dgf.EuclideDGFReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

import junit.framework.TestCase;
import model.EuclideDoc;

public class EuclideDGFReaderTest extends TestCase {

	/*
	 * Test method for 'app.in.EuclideDGFReader.parseFile(File)'
	 */
	public void testParseFile() {
		File testFile = new File("tests/files/testFile.dgf");
		Hashtable<String, String> hash = null;
		
		try{
			 hash = EuclideDGFReader.parseFile(testFile);
		}catch(FileNotFoundException ex){
			System.out.println("File not found");
		}catch(IOException ex){
			System.out.println("Other exception");
		}
	
		assertEquals(hash.get("docName"), "Test File");
//		// display contents of the file as set of keys
//		for(String key : hash.keySet())
//			System.out.printf("%s -> [%s]\n", key, hash.get(key));
	}


	/*
	 * Test method for 'app.in.EuclideDGFReader.readFile(File)'
	 */
	public void testReadFile() {
		//File testFile = new File("tests/files/intersectLines.dgf");
		File testFile = new File("tests/files/testFile.dgf");
		EuclideDoc doc = null;
		
		EuclideDGFReader reader = new EuclideDGFReader();
		
		try{
			 doc = reader.readFile(testFile);
		}catch(FileNotFoundException ex){
			System.out.println("File not found");
		}catch(IOException ex){
			System.out.println("Other exception");
		}
		
		assertEquals("Test File", doc.getName());
		assertEquals("myself", doc.getAuthorName());
	}

}
