/* file : MultiRotatedShape2D.java
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
 * Created on 9 avr. 2006
 *
 */
package dynamic.shapes;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.*;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.Curve2DUtils;
import math.geom2d.curve.CurveArray2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.domain.*;
import dynamic.DynamicMeasure2D;
import dynamic.DynamicShape2D;


public class MultiRotatedShape2D extends DynamicShape2D {

	DynamicShape2D parent1;
	DynamicShape2D parent2;
	DynamicMeasure2D parent3;
	
	Shape2D transformed = new Point2D();
	
	public MultiRotatedShape2D(DynamicShape2D shape, 
			DynamicShape2D center, DynamicMeasure2D count){
		this.parent1 = shape;
		this.parent2 = center;
		this.parent3 = count;

		parents.add(shape);
		parents.add(center);
		parents.add(count);
		//parents.trimToSize();

		//update(); 
	}
	
	@Override
	public Shape2D getShape(){
		return this.transformed;
	}
		
	@Override
	public void update(){		
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
		if(!parent3.isDefined()) return;

		// extract shape to transform
		Shape2D shape = parent1.getShape();

		// extract rotation center
		Shape2D shape2 = parent2.getShape();
		if (!(shape2 instanceof Point2D))
			return;
		Point2D center = (Point2D) shape2;
		
		// extract number of times the rotation will be applied
		Measure2D measure = parent3.getMeasure();
		if(!(measure instanceof CountMeasure2D))
			return;
		int count = ((CountMeasure2D) measure).getCount();
		if (count<2)
			return;
		
		// Compute the transformation
		double angle = 2*Math.PI/count;
		AffineTransform2D transform =
			AffineTransform2D.createRotation(center, angle);
		
		// update position of shape
		if (shape instanceof Curve2D) {
			Curve2D curve = (Curve2D) shape;
			Curve2D[] curves = new Curve2D[count];
			for(int i=0; i<count; i++) {
				curves[i] = curve;
				curve = curve.transform(transform);
			}
			this.transformed = new CurveArray2D<Curve2D>(curves);
		} else if (shape instanceof Domain2D) {
				Boundary2D boundary = ((Domain2D) shape).getBoundary();
				Boundary2D[] curves = new Boundary2D[count];
				for(int i=0; i<count; i++) {
					curves[i] = boundary;
					boundary = boundary.transform(transform);
				}
				boundary = new LocalBoundarySet2D(curves);
				this.transformed = new GenericDomain2D(boundary);
		} else {
			Shape2D[] shapes = new Shape2D[count];
			for(int i=0; i<count; i++) {
				shapes[i] = shape;
				shape = shape.transform(transform);
			}
			this.transformed = new ShapeArray2D<Shape2D>(shapes);
		}
		
		if(this.transformed==null) return;
		this.defined = true;
	}

	/**
	 *  An internal helper class that concatenate several boundaries, and is
	 *  itself a boundary
	 */
	private class LocalBoundarySet2D extends CurveArray2D<Boundary2D> 
	implements Boundary2D {

	    public LocalBoundarySet2D(Boundary2D[] curves) {
	        super(curves);
	    }

	    public LocalBoundarySet2D(Collection<? extends Boundary2D> curves) {
	        super(curves);
	    }

	    // ===================================================================
	    // Methods implementing Boundary2D interface

	    public Collection<Contour2D> getContinuousCurves() {
	        ArrayList<Contour2D> list = 
	        	new ArrayList<Contour2D>(curves.size());
	        for (Boundary2D curve : this.curves)
	            list.addAll(curve.getContinuousCurves());
	        return list;
	    }
	    
	    public Collection<Contour2D> getBoundaryCurves() {
	    	return getContinuousCurves();
	    }

	    public Domain2D getDomain() {
	        return new GenericDomain2D(this);
	    }

	    public void fill(Graphics2D g2) {
	        g2.fill(this.getGeneralPath());
	    }

	    // ===================================================================
	    // Methods implementing OrientedCurve2D interface

	    public double getWindingAngle(Point2D point) {
	        double angle = 0;
	        for (OrientedCurve2D curve : this.getCurves())
	            angle += curve.getWindingAngle(point);
	        return angle;
	    }

	    public double getSignedDistance(Point2D p) {
	        return getSignedDistance(p.getX(), p.getY());
	    }

	    /*
	     * (non-Javadoc)
	     * 
	     * @see math.geom2d.Shape2D#getSignedDistance(math.geom2d.Point2D)
	     */
	    public double getSignedDistance(double x, double y) {
	        double minDist = Double.POSITIVE_INFINITY;
	        double dist = Double.POSITIVE_INFINITY;

	        for (OrientedCurve2D curve : this.getCurves()) {
	            dist = Math.min(dist, curve.getSignedDistance(x, y));
	            if (Math.abs(dist)<Math.abs(minDist))
	                minDist = dist;
	        }
	        return minDist;
	    }

	    public boolean isInside(Point2D point) {
	        return this.getSignedDistance(point.getX(), point.getY())<0;
	    }

	    // ===================================================================
	    // Methods implementing Curve2D interface

	    @Override
	    public LocalBoundarySet2D getReverseCurve() {
	        Boundary2D[] curves2 = new Boundary2D[curves.size()];
	        int n = curves.size();
	        for (int i = 0; i<n; i++)
	            curves2[i] = curves.get(n-1-i).getReverseCurve();
	        return new LocalBoundarySet2D(curves2);
	    }

	    @Override
	    public CurveSet2D<? extends ContinuousOrientedCurve2D> getSubCurve(
	            double t0, double t1) {
	        // get the subcurve
	        CurveSet2D<? extends Curve2D> curveSet = super.getSubCurve(t0, t1);

	        // create subcurve array
	        ArrayList<ContinuousOrientedCurve2D> curves = 
	        	new ArrayList<ContinuousOrientedCurve2D>();
	        for (Curve2D curve : curveSet.getCurves())
	            curves.add((ContinuousOrientedCurve2D) curve);

	        // Create CurveSet for the result
	        return new CurveArray2D<ContinuousOrientedCurve2D>(curves);
	    }

	    // ===================================================================
	    // Methods implementing the Shape2D interface

	    /**
	     * Clip the curve by a box. The result is an instance of
	     * CurveSet2D<ContinuousOrientedCurve2D>, which contains
	     * only instances of ContinuousOrientedCurve2D. 
	     * If the curve is not clipped, the result is an instance of 
	     * CurveSet2D<ContinuousOrientedCurve2D> which contains 0 curves.
	     */
	    @Override
	    public CurveSet2D<? extends ContinuousOrientedCurve2D> clip(Box2D box) {
	        // Clip the curve
	        CurveSet2D<? extends Curve2D> set = Curve2DUtils.clipCurve(this, box);

	        // Stores the result in appropriate structure
	        CurveArray2D<ContinuousOrientedCurve2D> result = 
	        	new CurveArray2D<ContinuousOrientedCurve2D>(set.getCurveNumber());

	        // convert the result
	        for (Curve2D curve : set.getCurves()) {
	            if (curve instanceof ContinuousOrientedCurve2D)
	                result.addCurve((ContinuousOrientedCurve2D) curve);
	        }
	        return result;
	    }

	    @Override
	    public LocalBoundarySet2D transform(
	            AffineTransform2D trans) {
	    	ArrayList<Boundary2D> transformedBoundaries = 
	    		new ArrayList<Boundary2D>(curves.size());
	        for (Boundary2D curve : curves)
	        	transformedBoundaries.add(curve.transform(trans));
	        return new LocalBoundarySet2D(transformedBoundaries);
	    }		
	}
}
