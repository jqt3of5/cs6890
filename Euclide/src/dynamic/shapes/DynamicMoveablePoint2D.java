/**
 * File: 	DynamicMoveablePoint2D.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 19 oct. 09
 */
package dynamic.shapes;

import math.geom2d.Point2D;
import dynamic.DynamicShape2D;


/**
 * An abstract class for all points that can be moved, eventually with some
 * restrictions (point on a curve, bi-point...)
 * @author dlegland
 *
 */
public abstract class DynamicMoveablePoint2D extends DynamicShape2D {

	/* (non-Javadoc)
	 * @see dynamic.DynamicShape2D#getShape()
	 */
	@Override
	public abstract Point2D getShape();

	public abstract void translate(double dx, double dy);
}
