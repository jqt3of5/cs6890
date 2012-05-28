/**
 * File: 	LineWidthDecorator.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 3 feb 10
 */
package model.style;


/**
 * @author dlegland
 *
 */
public class LineWidthDecorator extends DrawStyleDecorator {
	
	double width;
	
	public LineWidthDecorator(DrawStyle baseStyle, double width) {
		super(baseStyle);
		this.width = width;
	}
	
	/* (non-Javadoc)
	 * @see model.style.DrawStyle#getLineWidth()
	 */
	@Override
	public double getLineWidth() {
		return width;
	}
}
