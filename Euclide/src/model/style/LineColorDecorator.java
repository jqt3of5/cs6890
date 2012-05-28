/**
 * File: 	LineColorDecorator.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 3 feb 10
 */
package model.style;

import java.awt.Color;


/**
 * @author dlegland
 *
 */
public class LineColorDecorator extends DrawStyleDecorator {
	
	Color color;
	
	public LineColorDecorator(DrawStyle baseStyle, Color color) {
		super(baseStyle);
		this.color = color;
	}
	
	/* (non-Javadoc)
	 * @see model.style.DrawStyle#getLineWidth()
	 */
	@Override
	public Color getLineColor() {
		return color;
	}
}
