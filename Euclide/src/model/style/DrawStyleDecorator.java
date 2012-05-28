/**
 * File: 	DrawStyleDecorator.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 3 feb. 10
 */
package model.style;

import java.awt.Color;


/**
 * Base class for all other decorators.
 * @author dlegland
 */
public class DrawStyleDecorator implements DrawStyle {
	
	protected DrawStyle baseStyle = null;

	protected DrawStyleDecorator(DrawStyle baseStyle) {
		this.baseStyle = baseStyle;
	}
	
	/**
	 * @return the baseStyle
	 */
	public DrawStyle getBaseStyle() {
		return baseStyle;
	}

	/**
	 * @param baseStyle the baseStyle to set
	 */
	public void setBaseStyle(DrawStyle baseStyle) {
		this.baseStyle = baseStyle;
	}


	/* (non-Javadoc)
	 * @see model.style.DrawStyle#getMarkerType()
	 */
	public Marker getMarker() {
		return baseStyle.getMarker();
	}

	/* (non-Javadoc)
	 * @see model.style.DrawStyle#getMarkerSize()
	 */
	public double getMarkerSize() {
		return baseStyle.getMarkerSize();
	}

	/* (non-Javadoc)
	 * @see model.style.DrawStyle#getMarkerSizeUnit()
	 */
	public MarkerSizeUnit getMarkerSizeUnit() {
		return baseStyle.getMarkerSizeUnit();
	}

	/* (non-Javadoc)
	 * @see model.style.DrawStyle#getMarkerColor()
	 */
	public Color getMarkerColor() {
		return baseStyle.getMarkerColor();
	}

	/* (non-Javadoc)
	 * @see model.style.DrawStyle#getMarkerLineWidth()
	 */
	public double getMarkerLineWidth() {
		return baseStyle.getMarkerLineWidth();
	}

	/* (non-Javadoc)
	 * @see model.style.DrawStyle#getMarkerFillColor()
	 */
	public Color getMarkerFillColor() {
		return baseStyle.getMarkerFillColor();
	}

	/* (non-Javadoc)
	 * @see model.style.DrawStyle#isLineVisible()
	 */
	public boolean isLineVisible() {
		return baseStyle.isLineVisible();
	}

	/* (non-Javadoc)
	 * @see model.style.DrawStyle#getLineWidth()
	 */
	public double getLineWidth() {
		return baseStyle.getLineWidth();
	}

	/* (non-Javadoc)
	 * @see model.style.DrawStyle#getLineWidthUnit()
	 */
	public LineWidthUnit getLineWidthUnit() {
		return baseStyle.getLineWidthUnit();
	}

	/* (non-Javadoc)
	 * @see model.style.DrawStyle#getLineColor()
	 */
	public Color getLineColor() {
		return baseStyle.getLineColor();
	}

	/* (non-Javadoc)
	 * @see model.style.DrawStyle#getLineDash()
	 */
	public float[] getLineDash() {
		return baseStyle.getLineDash();
	}

	/* (non-Javadoc)
	 * @see model.style.DrawStyle#getLineDashPhase()
	 */
	public float getLineDashPhase() {
		return baseStyle.getLineDashPhase();
	}

	/* (non-Javadoc)
	 * @see model.style.DrawStyle#getLineEndCap()
	 */
	public EndCap getLineEndCap() {
		return baseStyle.getLineEndCap();
	}

	/* (non-Javadoc)
	 * @see model.style.DrawStyle#getLineJoin()
	 */
	public LineJoin getLineJoin() {
		return baseStyle.getLineJoin();
	}

	/* (non-Javadoc)
	 * @see model.style.DrawStyle#getFillType()
	 */
	public FillType getFillType() {
		return baseStyle.getFillType();
	}

	/* (non-Javadoc)
	 * @see model.style.DrawStyle#getFillColor()
	 */
	public Color getFillColor() {
		return baseStyle.getFillColor();
	}

	/* (non-Javadoc)
	 * @see model.style.DrawStyle#getFillTransparency()
	 */
	public double getFillTransparency() {
		return baseStyle.getFillTransparency();
	}
}
