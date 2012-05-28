/* file : Colors.java
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
 * Created on 12 mai 2007
 *
 */
package gui.util;

import java.awt.Color;
import java.util.*;

/**
 * An utility class for converting colors to string and vice-versa.
 * <p>
 * The complete list of HTML colors is contained in enumeration HtmlColor.
 * To get the HTML Brown for example:<br>
 * <code>Color brown = HtmlColors.HtmlColor.BROWN.getColor();</code></p>
 * <p>
 * A simpler method is to process as follow:<br>
 * <code>Color brown = HtmlColors.parseColor("Brown");</code></p>
 *
 * @author dlegland
 */
public class HtmlColors {

	public enum HtmlColor {
		ALICE_BLUE(new Color(240, 248, 255), "AliceBlue"),
        ANTIQUE_WHITE(new Color(250, 235, 215), "AntiqueWhite"),
        AQUA(new Color(0, 255, 255), "Aqua"),
        AQUAMARINE(new Color(127, 255, 212), "Aquamarine"),
        AZURE(new Color(240, 255, 255), "Azure"),
        BEIGE(new Color(245, 245, 220), "Beige"),
        BISQUE(new Color(255, 228, 196), "Bisque"),
        BLACK(new Color(0, 0, 0), "Black"),
        BLANCHED_ALMOND(new Color(255, 235, 205), "BlanchedAlmond"),
        BLUE(new Color(0, 0, 255), "Blue"),
        BLUE_VIOLET(new Color(138, 43, 226), "BlueViolet"),
        BROWN(new Color(165, 42, 42), "Brown"),
        BURLY_WOOD(new Color(222, 184, 135), "BurlyWood"),
        CADET_BLUE(new Color(95, 158, 160), "CadetBlue"),
        CHARTREUSE(new Color(127, 255, 0), "Chartreuse"),
        CHOCOLATE(new Color(210, 105, 30), "Chocolate"),
        CORAL(new Color(255, 127, 80), "Coral"),
        CORNFLOWER_BLUE(new Color(100, 149, 237), "CornflowerBlue"),
        CORNSILK(new Color(255, 248, 220), "Cornsilk"),
        CRIMSON(new Color(220, 20, 60), "Crimson"),
        CYAN(new Color(0, 255, 255), "Cyan"),
        DARK_BLUE(new Color(0, 0, 139), "DarkBlue"),
        DARK_CYAN(new Color(0, 139, 139), "DarkCyan"),
        DARK_GOLDEN_ROD(new Color(184, 134, 11), "DarkGoldenRod"),
        DARK_GRAY(new Color(169, 169, 169), "DarkGray"),
        DARK_GREY(new Color(169, 169, 169), "DarkGrey"),
        DARK_GREEN(new Color(0, 100, 0), "DarkGreen"),
        DARK_KHAKI(new Color(189, 183, 107), "DarkKhaki"),
        DARK_MAGENTA(new Color(139, 0, 139), "DarkMagenta"),
        DARK_OLIVE_GREEN(new Color(85, 107, 47), "DarkOliveGreen"),
        DARK_ORANGE(new Color(255, 140, 0), "Darkorange"),
        DARK_ORCHID(new Color(153, 50, 204), "DarkOrchid"),
        DARK_RED(new Color(139, 0, 0), "DarkRed"),
        DARK_SALMON(new Color(233, 150, 122), "DarkSalmon"),
        DARK_SEA_GREEN(new Color(143, 188, 143), "DarkSeaGreen"),
        DARK_SLATE_BLUE(new Color(72, 61, 139), "DarkSlateBlue"),
        DARK_SLATE_GRAY(new Color(47, 79, 79), "DarkSlateGray"),
        DARK_SLATE_GREY(new Color(47, 79, 79), "DarkSlateGrey"),
        DARK_TURQUOISE(new Color(0, 206, 209), "DarkTurquoise"),
        DARK_VIOLET(new Color(148, 0, 211), "DarkViolet"),
        DEEP_PINK(new Color(255, 20, 147), "DeepPink"),
        DEEP_SKYB_LUE(new Color(0, 191, 255), "DeepSkyBlue"),
        DIM_GRAY(new Color(105, 105, 105), "DimGray"),
        DIM_GREY(new Color(105, 105, 105), "DimGrey"),
        DODGER_BLUE(new Color(30, 144, 255), "DodgerBlue"),
        FIRE_BRICK(new Color(178, 34, 34), "FireBrick"),
        FLORAL_WHITE(new Color(255, 250, 240), "FloralWhite"),
        FOREST_GREEN(new Color(34, 139, 34), "ForestGreen"),
        FUCHSIA(new Color(255, 0, 255), "Fuchsia"),
        GAINSBORO(new Color(220, 220, 220), "Gainsboro"),
        GHOST_WHITE(new Color(248, 248, 255), "GhostWhite"),
        GOLD(new Color(255, 215, 0), "Gold"),
        GOLDEN_ROD(new Color(218, 165, 32), "GoldenRod"),
        GRAY(new Color(128, 128, 128), "Gray"),
        GREY(new Color(128, 128, 128), "Grey"),
        GREEN(new Color(0, 128, 0), "Green"),
        GREEN_YELLOW(new Color(173, 255, 47), "GreenYellow"),
        HONEY_DEW(new Color(240, 255, 240), "HoneyDew"),
        HOT_PINK(new Color(255, 105, 180), "HotPink"),
        INDIAN_RED(new Color(205, 92, 92), "IndianRed"),
        INDIGO(new Color(75, 0, 130), "Indigo"),
        IVORY(new Color(255, 255, 240), "Ivory"),
        KHAKI(new Color(240, 230, 140), "Khaki"),
        LAVENDER(new Color(230, 230, 250), "Lavender"),
        LAVENDER_BLUSH(new Color(255, 240, 245), "LavenderBlush"),
        LAWN_GREEN(new Color(124, 252, 0), "LawnGreen"),
        LEMON_CHIFFON(new Color(255, 250, 205), "LemonChiffon"),
        LIGHT_BLUE(new Color(173, 216, 230), "LightBlue"),
        LIGHT_CORAL(new Color(240, 128, 128), "LightCoral"),
        LIGHT_CYAN(new Color(224, 255, 255), "LightCyan"),
        LIGHT_GOLDEN_ROD_YELLOW(new Color(250, 250, 210), "LightGoldenRodYellow"),
        LIGHT_GRAY(new Color(211, 211, 211), "LightGray"),
        LIGHT_GREY(new Color(211, 211, 211), "LightGrey"),
        LIGHT_GREEN(new Color(144, 238, 144), "LightGreen"),
        LIGHT_PINK(new Color(255, 182, 193), "LightPink"),
        LIGHT_SALMON(new Color(255, 160, 122), "LightSalmon"),
        LIGHT_SEA_GREEN(new Color(32, 178, 170), "LightSeaGreen"),
        LIGHT_SKY_BLUE(new Color(135, 206, 250), "LightSkyBlue"),
        LIGHT_SLATE_GRAY(new Color(119, 136, 153), "LightSlateGray"),
        LIGHT_SLATE_GREY(new Color(119, 136, 153), "LightSlateGrey"),
        LIGHT_STEEL_BLUE(new Color(176, 196, 222), "LightSteelBlue"),
        LIGHT_YELLOW(new Color(255, 255, 224), "LightYellow"),
        LIME(new Color(0, 255, 0), "Lime"),
        LIME_GREEN(new Color(50, 205, 50), "LimeGreen"),
        LINEN(new Color(250, 240, 230), "Linen"),
        MAGENTA(new Color(255, 0, 255), "Magenta"),
        MAROON(new Color(128, 0, 0), "Maroon"),
        MEDIUM_AQUA_MARINE(new Color(102, 205, 170), "MediumAquaMarine"),
        MEDIUM_BLUE(new Color(0, 0, 205), "MediumBlue"),
        MEDIUM_ORCHID(new Color(186, 85, 211), "MediumOrchid"),
        MEDIUM_PURPLE(new Color(147, 112, 216), "MediumPurple"),
        MEDIUM_SEA_GREEN(new Color(60, 179, 113), "MediumSeaGreen"),
        MEDIUM_SLATE_BLUE(new Color(123, 104, 238), "MediumSlateBlue"),
        MEDIUM_SPRING_GREEN(new Color(0, 250, 154), "MediumSpringGreen"),
        MEDIUM_TURQUOISE(new Color(72, 209, 204), "MediumTurquoise"),
        MEDIUM_VIOLET_RED(new Color(199, 21, 133), "MediumVioletRed"),
        MIDNIGHT_BLUE(new Color(25, 25, 112), "MidnightBlue"),
        MINT_CREAM(new Color(245, 255, 250), "MintCream"),
        MISTY_ROSE(new Color(255, 228, 225), "MistyRose"),
        MOCCASIN(new Color(255, 228, 181), "Moccasin"),
        NAVAJO_WHITE(new Color(255, 222, 173), "NavajoWhite"),
        NAVY(new Color(0, 0, 128), "Navy"),
        OLD_LACE(new Color(253, 245, 230), "OldLace"),
        OLIVE(new Color(128, 128, 0), "Olive"),
        OLIVE_DRAB(new Color(107, 142, 35), "OliveDrab"),
        ORANGE(new Color(255, 165, 0), "Orange"),
        ORANGE_RED(new Color(255, 69, 0), "OrangeRed"),
        ORCHID(new Color(218, 112, 214), "Orchid"),
        PALE_GOLDEN_ROD(new Color(238, 232, 170), "PaleGoldenRod"),
        PALE_GREEN(new Color(152, 251, 152), "PaleGreen"),
        PALE_TURQUOISE(new Color(175, 238, 238), "PaleTurquoise"),
        PALE_VIOLETRED(new Color(216, 112, 147), "PaleVioletRed"),
        PAPAYA_WHIP(new Color(255, 239, 213), "PapayaWhip"),
        PEACH_PUFF(new Color(255, 218, 185), "PeachPuff"),
        PERU(new Color(205, 133, 63), "Peru"),
        PINK(new Color(255, 192, 203), "Pink"),
        PLUM(new Color(221, 160, 221), "Plum"),
        POWDER_BLUE(new Color(176, 224, 230), "PowderBlue"),
        PURPLE(new Color(128, 0, 128), "Purple"),
        RED(new Color(255, 0, 0), "Red"),
        ROSYB_ROWN(new Color(188, 143, 143), "RosyBrown"),
        ROYAL_BLUE(new Color(65, 105, 225), "RoyalBlue"),
        SADDLE_BROWN(new Color(139, 69, 19), "SaddleBrown"),
        SALMON(new Color(250, 128, 114), "Salmon"),
        SANDY_BROWN(new Color(244, 164, 96), "SandyBrown"),
        SEA_GREEN(new Color(46, 139, 87), "SeaGreen"),
        SEA_SHELL(new Color(255, 245, 238), "SeaShell"),
        SIENNA(new Color(160, 82, 45), "Sienna"),
        SILVER(new Color(192, 192, 192), "Silver"),
        SKY_BLUE(new Color(135, 206, 235), "SkyBlue"),
        SLATE_BLUE(new Color(106, 90, 205), "SlateBlue"),
        SLATE_GRAY(new Color(112, 128, 144), "SlateGray"),
        SLATE_GREY(new Color(112, 128, 144), "SlateGrey"),
        SNOW(new Color(255, 250, 250), "Snow"),
        SPRING_GREEN(new Color(0, 255, 127), "SpringGreen"),
        STEEL_BLUE(new Color(70, 130, 180), "SteelBlue"),
        TAN(new Color(210, 180, 140), "Tan"),
        TEAL(new Color(0, 128, 128), "Teal"),
        THISTLE(new Color(216, 191, 216), "Thistle"),
        TOMATO(new Color(255, 99, 71), "Tomato"),
        TURQUOISE(new Color(64, 224, 208), "Turquoise"),
        VIOLET(new Color(238, 130, 238), "Violet"),
        WHEAT(new Color(245, 222, 179), "Wheat"),
        WHITE(new Color(255, 255, 255), "White"),
        WHITE_SMOKE(new Color(245, 245, 245), "WhiteSmoke"),
        YELLOW(new Color(255, 255, 0), "Yellow"),
        YELLOW_GREEN(new Color(154, 205, 50), "YellowGreen");
		
		private Color color;
		private String name;
		
		HtmlColor(Color color, String name){
			this.color = color;
			this.name = name;
		}
		
		public Color getColor(){return color;};
		public String getName(){return name;};
	};
	
	/**
	 * Converts a color to a string representing is red, green and blue
	 * components, coded between 0 and 255.
	 */
	public final static String toRGBString(Color color){
		return new String(color.getRed() + " " + color.getGreen() + " " + color.getBlue());
	}

	/**
	 * Try to find the string representing the HTML name of the give color.
	 * If no string is found, the color is converted to a string showing the
	 * values for red, green and blue between 0 and 255. 
	 */
	public final static String toString(Color color){
		for(HtmlColor htmlColor : HtmlColor.values()) {
			if(htmlColor.color.equals(color))
				return htmlColor.name;
		}
	    
		return toRGBString(color);
	}
	
	/**
	 * Converts a string representing a HTML color name to a Java Color.
	 * Example: <br>
	 * <code>Color brown = HtmlColors.parseColor("Brown");</code>
	 * <br>Method is case insensitive: <br>
	 * <code>Color brown = HtmlColors.parseColor("brown");</code>
	 */
	public final static Color parseColor(String string){
		string = string.trim().toLowerCase(Locale.ENGLISH);

		for(HtmlColor htmlColor : HtmlColor.values()) {
			if(htmlColor.name.toLowerCase(Locale.ENGLISH).equals(string))
				return htmlColor.color;
		}
		
		// try to parse R G B components
		String red=null, green=null, blue=null;
		StringTokenizer st = new StringTokenizer(string);
		if(st.hasMoreElements()) red = st.nextToken();
		if(st.hasMoreElements()) green = st.nextToken();
		if(st.hasMoreElements()) blue = st.nextToken();
		
		if(blue==null) return Color.BLACK;
		int r = Integer.parseInt(red);
		int g = Integer.parseInt(green);
		int b = Integer.parseInt(blue);
		return new Color(r, g, b);
	}
}
