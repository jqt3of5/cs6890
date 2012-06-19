/* file : FreePoint2D.java
 * 
 * Project : geometry
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
 * Created on 22 d�c. 2005
 *
 */
package dynamic.shapes;

import math.geom2d.Point2D;

/**
 * A point defined by two Cartesian coordinates, given as measures.
 * @author dlegland
 */
public class FreePoint2D extends DynamicMoveablePoint2D {

	private Point2D point = new Point2D();
	
	/**
	 * Initialize new free point with given values.
	 * @param x x-coordinate of the point
	 * @param y y-coordinate of the point
	 */
	public FreePoint2D(double x, double y) {
		// set up parameters
		parameters.add(x);
		parameters.add(y);
		//parameters.trimToSize();
		
		//update();
	}

	@Override
	public Point2D getShape(){
		return point;
	}

	@Override
	public void translate(double dx, double dy) {
		double x = ((Double) this.getParameter(0)) + dx;
		double y = ((Double) this.getParameter(1)) + dy;
		this.setParameter(0, x);
		this.setParameter(1, y);
		this.point = new Point2D(x, y);
	}
	
	/* (non-Javadoc)
	 * @see math.geom2d.dynamic.DynamicShape2D#update()
	 */
	@Override
	public void update() {
		this.defined = false;
		
		double x = (Double) getParameter(0);
		double y = (Double) getParameter(1);
		this.point = new Point2D(x, y);
		
		this.defined = true;
	}

}
