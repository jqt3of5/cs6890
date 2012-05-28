/**
 * File: 	Marker.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 19 févr. 10
 */
package model.style;

import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveArray2D;
import math.geom2d.domain.Boundary2D;
import math.geom2d.line.Line2D;
import math.geom2d.polygon.SimplePolygon2D;


/**
 * @author dlegland
 *
 */
public interface Marker {

	public final static Marker CIRCLE = new DefaultMarker(
			new Circle2D(0, 0, 1), new Circle2D(0, 0, 1));
	
	public final static Marker PLUS = new DefaultMarker(
			new CurveArray2D<Line2D>(new Line2D[]{
					new Line2D(new Point2D(-1, 0), new Point2D(1,  0)),
					new Line2D(new Point2D( 0, 1), new Point2D(0, -1))}));
	
	public final static Marker CROSS = new DefaultMarker(
			new CurveArray2D<Line2D>(new Line2D[]{
					new Line2D(new Point2D(-1, -1), new Point2D(1,  1)),
					new Line2D(new Point2D(-1,  1), new Point2D(1, -1))}));
	
	public final static Marker SQUARE = new DefaultMarker(
			new SimplePolygon2D(new Point2D[]{
					new Point2D(-1, -1), new Point2D(1, -1),
					new Point2D( 1,  1), new Point2D(-1, 1)}));
	
	public final static Marker DIAMOND = new DefaultMarker(
			new SimplePolygon2D(new Point2D[]{
					new Point2D(0, -1), new Point2D(1, 0),
					new Point2D( 0,  1), new Point2D(-1, 0)}));

	public boolean isFillable();
	public Curve2D getShape();
	public Boundary2D getFillBoundary();
}
