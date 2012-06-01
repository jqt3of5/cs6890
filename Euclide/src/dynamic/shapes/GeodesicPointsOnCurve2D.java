/*
 * File : GeodesicPointsOnCurve2D.java
 *
 * Project : geometry
 *
 * ===========================================
 * 
 * This library is free software; you can reposribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is posributed in the hope that it will be useful, but 
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
 * Created on 24 Oct. 2009
 */
package dynamic.shapes;

import java.util.ArrayList;

import math.geom2d.CountMeasure2D;
import math.geom2d.Measure2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.circulinear.CirculinearCurve2D;
import math.geom2d.curve.ContinuousCurve2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.point.PointArray2D;
import math.geom2d.point.PointSet2D;
import dynamic.DynamicMeasure2D;
import dynamic.DynamicShape2D;

/**
 * Distributes N points on the curve, with respect to their geodesic length.
 * @author Legland
 */
public class GeodesicPointsOnCurve2D extends DynamicShape2D{
	
	/** The parent curve */
	DynamicShape2D 		parent1;
	
	/** The number of points on the curve */
	DynamicMeasure2D 	parent2;
	
	/** The resulting point set */
	private PointSet2D pointSet = new PointArray2D();
		
	public GeodesicPointsOnCurve2D(
			DynamicShape2D curve,
			DynamicMeasure2D number){
		super();
		this.parent1 = curve;
		this.parent2 = number;
		
		parents.add(curve);
		parents.add(number);
		parents.trimToSize();
		
		//update(); 
	}	
	
	@Override
	public PointSet2D getShape(){
		return pointSet;
	}
	
	/* (non-Javadoc)
	 * @see math.geom2d.dynamic.DynamicShape2D#isDefined()
	 */
	@Override
	public boolean isDefined() {
		return this.defined;
	}
	
	@Override
	public void update(){
		this.defined = false;
		
		// check parents are defined
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
		
		// Extract the parent shape
		Shape2D shape = parent1.getShape();
		if(!(shape instanceof Curve2D)) return;
		Curve2D curve = (Curve2D) shape;
		
		// Extract the parent shape
		Measure2D measure = parent2.getMeasure();
		if(!(measure instanceof CountMeasure2D)) return;
		int number = ((CountMeasure2D) measure).getCount();
		
		// avoid degenerate case of infinite curve
		if(!curve.isBounded())
			return;
		
		// Try to convert curve to circulinear
		//TODO convert non-circulinear curves
		if(!(curve instanceof CirculinearCurve2D))
			return;
		
		// class cast
		CirculinearCurve2D circu = (CirculinearCurve2D) curve;
		
		// check if curve is closed
		boolean closed = false;		
		if(curve instanceof ContinuousCurve2D) {
			if ( ((ContinuousCurve2D)curve).isClosed())
				closed = true;
		}
		
		// geodesic length between 2 points
		double incr = circu.getLength()/(closed ? number : number-1);
		
		// update position of the points
		ArrayList<Point2D> points = new ArrayList<Point2D>(number);
		for(int i=0; i<number; i++) {
			double pos = incr*(i+1);
			points.add(circu.getPoint(circu.getPosition(pos)));
		}
		
		
		
		// create the new resulting point set
		pointSet = new PointArray2D(points);
		this.defined = true;
	}
}
