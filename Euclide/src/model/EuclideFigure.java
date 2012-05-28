/* file : EuclideElement.java
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
 * Created on 25 déc. 2005
 *
 */
package model;

import model.style.DrawStyle;
import model.style.DefaultDrawStyle;
import dynamic.*;

/**
 * Contains information for a figure: geometry and drawing style. Geometry
 * can be fixed (one shape) or a dynamic geometry object. 
 * @author dlegland
 */
public class EuclideFigure extends EuclideObject{

	// ===================================================================
	// inner class variables

	/** Construction for the geometry of this figure */
	DynamicShape2D geometry = null;

	/**
	 * Drawing style of the shape
	 */
	DrawStyle drawStyle = new DefaultDrawStyle();	
	
	
	// ===================================================================
	// constructors

	public EuclideFigure() {
	}
	
	public EuclideFigure(DynamicShape2D geometry){
		this.geometry = geometry;
	}
	
	public EuclideFigure(DynamicShape2D geometry, String name){
		super(name);
		this.geometry = geometry;
	}
	
	public EuclideFigure(DynamicShape2D geometry, String name, String tag){
		super(name, tag);
		this.geometry = geometry;
	}
	
	
	// ===================================================================
	// general methods

	public DynamicShape2D getGeometry(){
		return geometry;
	}

	public void setGeometry(DynamicShape2D geometry){
		this.geometry = geometry;
	}
	
	/**
	 * Returns the drawing style.
	 * @return the fillStyle.
	 */
	public DrawStyle getDrawStyle() {
		return drawStyle;
	}
	
	/**
	 * @return Returns the lineStyle.
	 */
	public void setDrawStyle(DrawStyle drawStyle) {
		this.drawStyle = drawStyle;
	}
}
