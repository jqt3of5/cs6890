/* file: DefaultDrawStyle.java
 * 
 * Project: Euclide
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
 * Created on 03 Feb. 2010
 *
 */
package model.style;

import java.awt.Color;


/**
 * Concrete implementation of a style for a graphical item.
 * @author dlegland
 *
 */
public class DefaultDrawStyle implements DrawStyle {

	// ===================================================================
	// class fields
	
	// ------------------------------------------
	// marker properties
	
	private Marker marker = Marker.CIRCLE;
	
	private double markerSize = 1;
	
	private MarkerSizeUnit markerSizeUnit = MarkerSizeUnit.PIXEL;
	
	private Color markerColor = Color.BLACK;
	
	private Color markerFillColor = Color.red;
	
	private double markerLineWidth = 1;
	
	
	// ------------------------------------------
	// line properties
	
	private boolean lineVisible = true;
	
	private double lineWidth = 1;

	private LineWidthUnit lineWidthUnit = LineWidthUnit.PIXEL;
	
	private Color lineColor = Color.black;

	private EndCap lineEndCap = EndCap.BUTT;

	private LineJoin lineJoin = LineJoin.ROUND;

	private float[] lineDash = new float[]{};

	private float lineDashPhase = 0;


	// ------------------------------------------
	// fill properties
	
	private FillType fillType = FillType.COLOR;
	
	private Color fillColor = Color.CYAN;
	
	private double fillTransparency = 1.0;
	
	
	// ===================================================================
	// constructors
	
	public DefaultDrawStyle(){
	}
	
	public DefaultDrawStyle(DrawStyle style){
		this.markerSize = style.getMarkerSize();
		this.markerColor = style.getMarkerColor();
		this.markerFillColor = style.getMarkerFillColor();
		this.markerLineWidth = style.getMarkerLineWidth();
		
		this.lineVisible = style.isLineVisible();
		this.lineWidth = style.getLineWidth();
		this.lineColor = style.getLineColor();
		this.lineEndCap = style.getLineEndCap();
		this.lineJoin = style.getLineJoin();
		this.lineDash = style.getLineDash();
		this.lineDashPhase = style.getLineDashPhase();
		
		this.fillType = style.getFillType();
		this.fillColor = style.getFillColor();
		this.fillTransparency = style.getFillTransparency();
	}

	
	// ===================================================================
	// Marker management
		
	/**
	 * @return the markerType
	 */
	public Marker getMarker() {
		return marker;
	}

	/**
	 * @param markerType the markerType to set
	 */
	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	/**
	 * @return the markerSize
	 */
	public double getMarkerSize() {
		return markerSize;
	}

	/**
	 * @param markerSize the markerSize to set
	 */
	public void setMarkerSize(double markerSize) {
		this.markerSize = markerSize;
	}

	/**
	 * @return the markerSizeUnit
	 */
	public MarkerSizeUnit getMarkerSizeUnit() {
		return markerSizeUnit;
	}

	
	/**
	 * @param markerSizeUnit the markerSizeUnit to set
	 */
	public void setMarkerSizeUnit(MarkerSizeUnit markerSizeUnit) {
		this.markerSizeUnit = markerSizeUnit;
	}

	/**
	 * @return the markerColor
	 */
	public Color getMarkerColor() {
		return markerColor;
	}

	/**
	 * @param markerColor the markerColor to set
	 */
	public void setMarkerColor(Color markerColor) {
		this.markerColor = markerColor;
	}

	/**
	 * @return the markerFillColor
	 */
	public Color getMarkerFillColor() {
		return markerFillColor;
	}

	/**
	 * @param markerFillColor the markerFillColor to set
	 */
	public void setMarkerFillColor(Color markerFillColor) {
		this.markerFillColor = markerFillColor;
	}

	
	// ===================================================================
	// line management
		
	/**
	 * @return the markerLineWidth
	 */
	public double getMarkerLineWidth() {
		return markerLineWidth;
	}

	/**
	 * @param markerLineWidth the markerLineWidth to set
	 */
	public void setMarkerLineWidth(double markerLineWidth) {
		this.markerLineWidth = markerLineWidth;
	}

	/**
	 * @return the lineVisibility
	 */
	public boolean isLineVisible() {
		return lineVisible;
	}

	/**
	 * @param lineVisibility the lineVisibility to set
	 */
	public void setLineVisible(boolean lineVisible) {
		this.lineVisible = lineVisible;
	}

	/**
	 * @return the lineWidth
	 */
	public double getLineWidth() {
		return lineWidth;
	}

	/**
	 * @param lineWidth the lineWidth to set
	 */
	public void setLineWidth(double lineWidth) {
		this.lineWidth = lineWidth;
	}

	/**
	 * @return the lineWidthUnit
	 */
	public LineWidthUnit getLineWidthUnit() {
		return lineWidthUnit;
	}

	
	/**
	 * @param lineWidthUnit the lineWidthUnit to set
	 */
	public void setLineWidthUnit(LineWidthUnit lineWidthUnit) {
		this.lineWidthUnit = lineWidthUnit;
	}

	/**
	 * @return the lineColor
	 */
	public Color getLineColor() {
		return lineColor;
	}

	/**
	 * @param lineColor the lineColor to set
	 */
	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}

	/**
	 * @return the lineEndCap
	 */
	public EndCap getLineEndCap() {
		return lineEndCap;
	}

	/**
	 * @param lineEndCap the lineEndCap to set
	 */
	public void setLineEndCap(EndCap lineEndCap) {
		this.lineEndCap = lineEndCap;
	}

	/**
	 * @return the lineJoin
	 */
	public LineJoin getLineJoin() {
		return lineJoin;
	}

	/**
	 * @param lineJoin the lineJoin to set
	 */
	public void setLineJoin(LineJoin lineJoin) {
		this.lineJoin = lineJoin;
	}

	/**
	 * @return the lineDash
	 */
	public float[] getLineDash() {
		return lineDash;
	}

	/**
	 * @param lineDash the lineDash to set
	 */
	public void setLineDash(float[] lineDash) {
		this.lineDash = lineDash;
	}

	/**
	 * @return the lineDashPhase
	 */
	public float getLineDashPhase() {
		return lineDashPhase;
	}

	/**
	 * @param lineDashPhase the lineDashPhase to set
	 */
	public void setLineDashPhase(float lineDashPhase) {
		this.lineDashPhase = lineDashPhase;
	}

	
	// ===================================================================
	// fill management
		
	/**
	 * @return the fillType
	 */
	public FillType getFillType() {
		return fillType;
	}

	/**
	 * @param fillType the fillType to set
	 */
	public void setFillType(FillType fillType) {
		this.fillType = fillType;
	}
	
	/**
	 * @return the fillColor
	 */
	public Color getFillColor() {
		return fillColor;
	}
	
	/**
	 * @param fillColor the fillColor to set
	 */
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	/**
	 * @return the fillTransparency
	 */
	public double getFillTransparency() {
		return fillTransparency;
	}

	/**
	 * @param fillTransparency the fillTransparency to set
	 */
	public void setFillTransparency(double fillTransparency) {
		this.fillTransparency = fillTransparency;
	}	
}
