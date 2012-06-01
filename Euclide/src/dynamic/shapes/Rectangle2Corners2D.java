/**
 * File: 	RectangleTwoCorners.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 13 d�c. 2010
 */
package dynamic.shapes;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.polygon.SimplePolygon2D;
import dynamic.DynamicShape2D;


/**
 * @author dlegland
 *
 */
public class Rectangle2Corners2D extends DynamicShape2D {

	DynamicShape2D parent1;
	DynamicShape2D parent2;
	
	protected SimplePolygon2D polygon = new SimplePolygon2D();

	/**
	 * Initialize from two corners
	 * @param point
	 */
	public Rectangle2Corners2D(DynamicShape2D point1, DynamicShape2D point2) {
		super();
		this.parent1 = point1;
		this.parent2 = point2;

		parents.add(point1);
		parents.add(point2);
		parents.trimToSize();

		//update();
	}
	
	/* (non-Javadoc)
	 * @see dynamic.DynamicShape2D#getShape()
	 */
	@Override
	public Shape2D getShape() {
		return polygon;
	}

	/* (non-Javadoc)
	 * @see dynamic.DynamicObject2D#update()
	 */
	@Override
	public void update() {
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;

		Shape2D shape;
		
		// extract first point
		shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point1 = (Point2D) shape;
		
		// extract second point
		shape = parent2.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point2 = (Point2D) shape;
		
		Box2D box = Box2D.create(point1, point2);
		double xmin = box.getMinX();
		double ymin = box.getMinY();
		double xmax = box.getMaxX();
		double ymax = box.getMaxY();
		this.polygon = new SimplePolygon2D(new Point2D[]{
			new Point2D(xmin, ymin), 
			new Point2D(xmax, ymin), 
			new Point2D(xmax, ymax),
			new Point2D(xmin, ymax)
		});
		
		this.defined = true;
	}

}
