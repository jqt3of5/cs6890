/**
 * File: 	ShapeArray2D.java
 * Project: Euclide-multiTransform
 * 
 * Distributed under the LGPL License.
 *
 * Created: 7 mars 10
 */
package math.geom2d;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


/**
 * @author dlegland
 *
 */
public class ShapeArray2D <T extends Shape2D> implements Shape2D, Iterable<T> {

	ArrayList<T> shapes;
	
	public ShapeArray2D(Collection<T> shapes) {
		this.shapes = new ArrayList<T>(shapes.size());
		this.shapes.addAll(shapes);
	}
	
	public ShapeArray2D(T[] shapes) {
		this.shapes = new ArrayList<T>(shapes.length);
		for(int i=0; i<shapes.length; i++)
			this.shapes.add(shapes[i]);
	}
	
	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#clip(math.geom2d.Box2D)
	 */
	public Shape2D clip(Box2D box) {
		ArrayList<Shape2D> clippedShapes = 
			new ArrayList<Shape2D>(this.shapes.size());
		for (Shape2D shape : shapes)
			clippedShapes.add(shape.clip(box));
		return new ShapeArray2D<Shape2D>(clippedShapes);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#contains(java.awt.geom.Point2D)
	 */
	public boolean contains(Point2D p) {
		for (Shape2D shape : shapes)
			if (shape.contains(p))
				return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#contains(double, double)
	 */
	public boolean contains(double x, double y) {
		for (Shape2D shape : shapes)
			if (shape.contains(x, y))
				return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#draw(java.awt.Graphics2D)
	 */
	public void draw(Graphics2D g2) {
		for (Shape2D shape : shapes)
			shape.draw(g2);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#getBoundingBox()
	 */
	public Box2D getBoundingBox() {
		Box2D box = new Box2D(
				Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 
				Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
		for (Shape2D shape : shapes)
			box = box.union(shape.getBoundingBox());
		return box;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#getDistance(java.awt.geom.Point2D)
	 */
	public double getDistance(Point2D p) {
		return this.getDistance(p.getX(), p.getY());
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#getDistance(double, double)
	 */
	public double getDistance(double x, double y) {
		double minDist = Double.POSITIVE_INFINITY;
		for (Shape2D shape : shapes) {
			minDist = Math.min(minDist, shape.getDistance(x, y));
		}
		return minDist;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#isBounded()
	 */
	public boolean isBounded() {
		for (Shape2D shape : shapes) {
			if (!shape.isBounded())
				return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#isEmpty()
	 */
	public boolean isEmpty() {
		if (shapes.size()==0)
			return true;
		for (Shape2D shape : shapes) {
			if (!shape.isEmpty())
				return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#transform(math.geom2d.AffineTransform2D)
	 */
	public ShapeArray2D<? extends Shape2D> transform(AffineTransform2D trans) {
		ArrayList<Shape2D> transformedShapes = 
			new ArrayList<Shape2D>(this.shapes.size());
		for (Shape2D shape : shapes)
			transformedShapes.add(shape.transform(trans));
		return new ShapeArray2D<Shape2D>(transformedShapes);
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<T> iterator() {
		return shapes.iterator();
	}

	/* (non-Javadoc)
	 * @see math.geom2d.GeometricObject2D#almostEquals(math.geom2d.GeometricObject2D, double)
	 */
	public boolean almostEquals(GeometricObject2D arg0, double arg1) {
		// TODO Auto-generated method stub
		return false;
	}
}
