/**
 * 
 */
package model.style;

import java.awt.Color;

/**
 * @author dlegland
 *
 */
public interface DrawStyle {

	public enum MarkerSizeUnit {
		USER, PIXEL;
	}
	
	public enum LineWidthUnit {
		USER, PIXEL;
	}
	
	public enum EndCap {
		BUTT(java.awt.BasicStroke.CAP_BUTT), 
		ROUND(java.awt.BasicStroke.CAP_ROUND), 
		SQUARE(java.awt.BasicStroke.CAP_SQUARE);
		
		int javaEndCap;
		
		EndCap(int javaEndCap) {
			this.javaEndCap = javaEndCap;
		}
		
		public int getJavaEndCap() {
			return javaEndCap;
		}
	};
	
	public enum LineJoin {
		BEVEL(java.awt.BasicStroke.JOIN_BEVEL), 
		MITER(java.awt.BasicStroke.JOIN_MITER), 
		ROUND(java.awt.BasicStroke.JOIN_ROUND);
		
		int javaLineJoin;
		
		LineJoin(int javaLineJoin) {
			this.javaLineJoin = javaLineJoin;
		}
		
		public int getJavaLineJoin() {
			return javaLineJoin;
		}
	};

	/**
	 * Planned:
	 * public enum FillType {NONE, COLOR, GRADIENT, PATTERN, TEXTURE};
	 */
	public enum FillType {NONE, COLOR};
	
	

	// ===================================================================
	// Some properties for markers
	
	public abstract Color getMarkerColor();
	
	public abstract double getMarkerSize();
	
	public abstract MarkerSizeUnit getMarkerSizeUnit();
	
	public abstract Marker getMarker();
	
	public abstract Color getMarkerFillColor();
	
	public abstract double getMarkerLineWidth();
	
	
	// ===================================================================
	// Some properties for lines
	
	public abstract boolean isLineVisible();
	
	public abstract Color getLineColor();
	
	public abstract double getLineWidth();
	
	public abstract LineWidthUnit getLineWidthUnit();
	
	public abstract EndCap getLineEndCap();
	
	public abstract LineJoin getLineJoin();
	
	public abstract float[] getLineDash();
	
	public abstract float getLineDashPhase();
	
	
	// ===================================================================
	// Some properties for lines
	
	public abstract FillType getFillType();
	
	public abstract Color getFillColor();
	
	/**
	 * Returns the transparency, between 0 (totally transparent) and 1
	 * (totally opaque).
	 * @return the transparency between 0 and 1.
	 */
	public abstract double getFillTransparency();
}
