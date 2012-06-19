/*
 * File : ParallelCurveDistance2D.java
 *
 * Project : geometry
 *
 * ===========================================
 * 
 * This library is free software; you can redist_ribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is dist_ributed in the hope that it will be useful, but 
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
 * author : Legland
 * Created on 2 feb. 2006
 */
package dynamic.shapes;

import java.util.Collection;
import java.util.Iterator;

import math.geom2d.Angle2D;
import math.geom2d.Measure2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.Vector2D;
import math.geom2d.circulinear.CirculinearContinuousCurve2D;
import math.geom2d.circulinear.CirculinearCurve2D;
import math.geom2d.circulinear.CirculinearElement2D;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.curve.ContinuousCurve2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveArray2D;
import math.geom2d.curve.PolyCurve2D;
import math.geom2d.curve.SmoothCurve2D;
import math.geom2d.polygon.Polyline2D;
import dynamic.DynamicMeasure2D;
import dynamic.DynamicShape2D;

/**
 * Build a curve parallel to another curve, at a given distance.
 * @author Legland
 */
public class ParallelCurveDistance2D extends DynamicShape2D{
	
	DynamicShape2D 		parent1;
	DynamicMeasure2D 	parent2;
	
	Curve2D parallel = null;
	
	public ParallelCurveDistance2D(DynamicShape2D line, 
			DynamicMeasure2D distanceMeasure){
		super(); //
		this.parent1 = line;
		this.parent2 = distanceMeasure;

		parents.add(line);
		parents.add(distanceMeasure);
		//parents.trimToSize();

		//update(); 
	}
		
	@Override
	public Shape2D getShape(){
		return parallel;
	}
		
	@Override
	public void update(){
		this.defined = false;
		
		// check parents are defined
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;

		// Extract parent curve
		Shape2D shape;
		shape = parent1.getShape();
		if(!(shape instanceof Curve2D)) return;
		Curve2D curve = (Curve2D) shape;
		
		// extract distance between curve and its parallel
		Measure2D measure;
		measure = parent2.getMeasure();
		double distance = measure.getValue();

		CirculinearCurve2D circlin = null;
		if(curve instanceof CirculinearCurve2D) {
			circlin = (CirculinearCurve2D) curve;
			parallel = circlin.getParallel(distance);
		} else {
			parallel = computeParallel(curve, distance);
		}
		
		if (parallel==null)
			return;
		
		this.defined = true;
	}
	
	private final static Curve2D computeParallel(Curve2D curve,
			double dist) {
		if (curve instanceof ContinuousCurve2D) {
			return computeParallelContinuous((ContinuousCurve2D)curve, 
					dist);
		} 
		
		// compute parallel of each continuous curve, and create a new
		// curve array with the resulting curves
		CurveArray2D<Curve2D> parallels = new CurveArray2D<Curve2D>();
		for(ContinuousCurve2D cont : curve.getContinuousCurves()){
			Curve2D parallel = computeParallelContinuous(cont, dist);
			if(parallel!=null)
				parallels.addCurve(parallel);
		}
		return parallels;
	} 

	private final static ContinuousCurve2D computeParallelContinuous(
			ContinuousCurve2D curve, double dist) {
		
		if (curve instanceof CirculinearContinuousCurve2D) {
			return ((CirculinearContinuousCurve2D) curve).getParallel(dist);
		}

		if (curve instanceof SmoothCurve2D) {
			return computeParallelSmooth((SmoothCurve2D)curve, dist);
		}

		// extract collection of smooth curves
		Collection<? extends SmoothCurve2D> smoothCurves = 
			curve.getSmoothPieces();
		Iterator<? extends SmoothCurve2D> iterator = smoothCurves.iterator();

		// previous curve
		SmoothCurve2D previous = null;
		SmoothCurve2D current = null;

		// init result
		PolyCurve2D<ContinuousCurve2D> parallel = 
			new PolyCurve2D<ContinuousCurve2D> ();

		// check if curve is empty
		if(!iterator.hasNext())
			return parallel;

		// add parallel to the first curve
		previous = iterator.next();
		ContinuousCurve2D smoothParallel = 
			computeParallelSmooth(previous, dist);
		parallel.addCurve(smoothParallel);

		// iterate on smooth pieces
		while(iterator.hasNext()){
			// get current curve
			current = iterator.next();

			// center of circle arc
			Point2D center = current.getFirstPoint();

			// compute tangents to each portion
			Vector2D vp = previous.getTangent(previous.getT1());
			Vector2D vc = current.getTangent(current.getT0());

			// compute angles
			double startAngle, endAngle;
			if(dist>0) {
				startAngle = vp.getAngle() - Math.PI/2;
				endAngle = vc.getAngle() - Math.PI/2;
			} else {
				startAngle = vp.getAngle() + Math.PI/2;
				endAngle = vc.getAngle() + Math.PI/2;
			}

			// Compute circle arc
			CircleArc2D arc = 
				new CircleArc2D(center, dist, startAngle, endAngle, 
						Angle2D.formatAngle(endAngle-startAngle)<Math.PI);
			parallel.addCurve(arc);

			// add parallel to current curve
			smoothParallel = computeParallelSmooth(current, dist);
			if(smoothParallel==null)
				return null;
			parallel.addCurve(smoothParallel);

			// prepare for next piece
			previous = current;
		}

		if(curve.isClosed()) {
			current = smoothCurves.iterator().next();

			// center of circle arc
			Point2D center = current.getFirstPoint();

			// compute tangents to each portion
			Vector2D vp = previous.getTangent(previous.getT1());
			Vector2D vc = current.getTangent(current.getT0());

			// compute angles
			double startAngle, endAngle;
			if(dist>0) {
				startAngle = vp.getAngle() - Math.PI/2;
				endAngle = vc.getAngle() - Math.PI/2;
			} else {
				startAngle = vp.getAngle() + Math.PI/2;
				endAngle = vc.getAngle() + Math.PI/2;
			}

			// Compute circle arc
			CircleArc2D arc = 
				new CircleArc2D(center, dist, startAngle, endAngle, 
						Angle2D.formatAngle(endAngle-startAngle)<Math.PI);
			parallel.addCurve(arc);
		}
		
		// set up as closed
		parallel.setClosed(curve.isClosed());

		return parallel;
		 
	}
	
	/**
	 * Return a parallel curve for some basic curves, otherwise return null.
	 */
	private final static ContinuousCurve2D computeParallelSmooth(
			SmoothCurve2D curve, double dist) {
		if (curve instanceof CirculinearElement2D)
			return ((CirculinearElement2D) curve).getParallel(dist);
		
		// Avoid to compute infinite parallel
		if(!curve.isBounded()){
			System.err.println("Try to compute parallel of an unbounded curve.");
			return null;
		}
		
		// Convert to polyline before computing parallel approximation
		Polyline2D polyline = curve.getAsPolyline(30);
		return computeParallelContinuous(polyline, dist);
	}
}
