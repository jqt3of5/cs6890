/* file : LengthCurve2D.java
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
 * Created on 16 avr. 2006
 *
 */
package dynamic.measures;

import java.util.Collection;
import java.util.Iterator;

import math.geom2d.LengthMeasure2D;
import math.geom2d.LengthUnit;
import math.geom2d.Measure2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.curve.ContinuousCurve2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.curve.SmoothCurve2D;
import math.geom2d.line.LineArc2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.LinearShape2D;
import math.geom2d.polygon.Polyline2D;
import dynamic.DynamicMeasure2D;
import dynamic.DynamicShape2D;

/**
 * Compute the length of a curve
 * @author dlegland
 *
 */
public class LengthCurve2D extends DynamicMeasure2D {

	private final static int Ns = 317;
	
	private DynamicShape2D parent1;
	
	Measure2D measure = new LengthMeasure2D(0, LengthUnit.MILLIMETER);
	
	public LengthCurve2D(DynamicShape2D segment){
		this.parent1 = segment;

		parents.add(segment);
		//parents.trimToSize();
		
		//update();
	}
	
	@Override
	public Measure2D getMeasure() {
		return measure;
	}

	@Override
	public void update() {
		this.defined = false;
		// check parent is defined
		if(!parent1.isDefined()) return;
	
		// extract parent Curve
		Shape2D shape = parent1.getShape();
		
		// cast to Curve2D
		if(!(shape instanceof Curve2D)) return;
		Curve2D curve = (Curve2D) shape;
		
		// update distance measure
		double length = computeLengthCurve(curve);
		this.measure.setValue(length);
		
		// check bound is correct
		if(Double.isInfinite(this.measure.getValue())) 
			return;
		this.defined = true;		
	}
	
	
	@SuppressWarnings("unchecked")
	private final static double computeLengthCurve(Curve2D curve) {
		
		if (curve instanceof ContinuousCurve2D)
			return computeLengthContinuousCurve((ContinuousCurve2D) curve);
		
		if (curve instanceof CurveSet2D) {
			// cast to CurveSet2D
			CurveSet2D<Curve2D> curveSet = (CurveSet2D<Curve2D>) curve;
			
			double length;
			double sum = 0;
			for(ContinuousCurve2D continuous : curveSet.getContinuousCurves()){
				length = computeLengthContinuousCurve(continuous);
				// Check length is definite
				if(Double.isNaN(length)) 
					return Double.NaN;
				// Check length is finite
				if(Double.isInfinite(length)) 
					return Double.POSITIVE_INFINITY;
				// compute cumulative length
				sum += length;
			}
			return sum;
		} 

		System.err.println("Unknown type of Continuous Curve: " +
				curve.getClass().getSimpleName());
		return Double.NaN;
	}
	
	private final static double computeLengthContinuousCurve(
			ContinuousCurve2D curve) {
		
		if (curve instanceof SmoothCurve2D)
			return computeLengthSmoothCurve((SmoothCurve2D)curve);
		
		double length;
		double sum = 0;

		// extract collection of smooth curves
		Collection<? extends SmoothCurve2D> smoothCurves = 
			curve.getSmoothPieces();
		Iterator<? extends SmoothCurve2D> iterator =
			smoothCurves.iterator();

		// previous curve
		SmoothCurve2D current = null;

		// check if curve is empty
		if(!iterator.hasNext())
			return 0;

		// iterate on smooth pieces
		while(iterator.hasNext()){
			// get current curve
			current = iterator.next();

			length = computeLengthSmoothCurve(current);

			// Check length is definite
			if(Double.isNaN(length)) 
				return Double.NaN;
			// Check length is finite
			if(Double.isInfinite(length)) 
				return Double.POSITIVE_INFINITY;
			// compute cumulative length
			sum += length;
		}
		return sum;	
	}
	
	/**
	 * Return a parallel curve for some basic curves, otherwise return null.
	 */
	private final static double computeLengthSmoothCurve(
			SmoothCurve2D curve) {
		
		if (curve instanceof LinearShape2D){
			if (curve instanceof LineSegment2D) {
				// class cast
				LineSegment2D segment = (LineSegment2D) curve;
				return segment.getLength();
			} else if (curve instanceof LineArc2D) {
				// class cast
				LineArc2D lineArc = (LineArc2D) curve;				
				return lineArc.getLength();
			} else {
				return Double.POSITIVE_INFINITY;
			}
		} else if (curve instanceof Circle2D){
			// For circles, return a new circle with same center and different
			// radius
			Circle2D circle = (Circle2D) curve;
			return circle.getLength();
		} else if (curve instanceof CircleArc2D){
			CircleArc2D arc = (CircleArc2D) curve;
			return arc.getLength();
		}
		
		assert curve.isBounded(): 
			"Can not compute length of an unbounded curve";
		
		Point2D p1 = curve.getFirstPoint();
		Point2D p2 = curve.getLastPoint();
		
		// Convert to polyline before computing length
		// Use a prime number to avoid some critical cases.
		Polyline2D polyline = curve.getAsPolyline(Ns);
		
		// Compute length by summing distances between points
		Iterator<Point2D> iterator = polyline.getVertices().iterator();
		double length = 0;
		p1 = iterator.next();
		while(iterator.hasNext()){
			p2 = iterator.next();
			length += Point2D.getDistance(p1, p2);
			p1 = p2;
		}
		
		// Special case of closed rings
		if(polyline.isClosed()){
			p2 = polyline.getFirstPoint();
			length += Point2D.getDistance(p1, p2);
		}
		
		return length;
	}
}
