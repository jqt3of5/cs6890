/**
 * File: 	HtmlColorsTest.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 14 févr. 10
 */
package gui.util;

import java.awt.Color;

import junit.framework.TestCase;

import org.junit.Test;


/**
 * @author dlegland
 *
 */
public class HtmlColorsTest extends TestCase {

	/**
	 * Test method for {@link gui.util.HtmlColors#toRGBString(java.awt.Color)}.
	 */
	@Test
	public void testToRGBString() {
		Color color = new Color(128, 200, 50);
		String target = "128 200 50";
		String string = HtmlColors.toRGBString(color);
		assertEquals(target, string);
	}

	/**
	 * Test method for {@link gui.util.HtmlColors#toString(java.awt.Color)}.
	 */
	@Test
	public void testToStringColor() {
		Color color;
		String string;
		
		color = Color.WHITE;
		string = HtmlColors.toString(color);
		assertEquals("White", string);
		
		color = Color.BLACK;
		string = HtmlColors.toString(color);
		assertEquals("Black", string);
		
		color = Color.RED;
		string = HtmlColors.toString(color);
		assertEquals("Red", string);
		
		color = Color.BLUE;
		string = HtmlColors.toString(color);
		assertEquals("Blue", string);
	}

	/**
	 * Test method for {@link gui.util.HtmlColors#parseColor(java.lang.String)}.
	 */
	@Test
	public void testParseColor() {
		Color color;
		
		color = HtmlColors.parseColor("White");
		assertEquals(Color.WHITE, color);
		
		color = HtmlColors.parseColor("Black");
		assertEquals(Color.BLACK, color);
		
		color = HtmlColors.parseColor("Red");
		assertEquals(Color.RED, color);
		
		color = HtmlColors.parseColor("Blue");
		assertEquals(Color.BLUE, color);
	}

}
