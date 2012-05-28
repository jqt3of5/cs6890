/**
 * File: 	CurveUtils.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 3 août 09
 */
package util;

import math.geom2d.Vector2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.curve.SmoothCurve2D;
import math.geom2d.polygon.Polyline2D;


/**
 * @author dlegland
 *
 */
public class CurveUtils {

	/**
	 * Computes the tangent vector to the curve, either on the curve if it is
	 * smooth, or by recursively inspecting inner curve of a curve set.
	 */
	public final static Vector2D getTangentVector(Curve2D curve, double t) {
		if (curve instanceof SmoothCurve2D) {
			// For a smooth curve -> use the tangent
			return ((SmoothCurve2D) curve).getTangent(t);
		} else if (curve instanceof CurveSet2D){
			// For a curve set -> recursively inspect children curves
			CurveSet2D<?> set = (CurveSet2D<?>) curve;
			return getTangentVector(set.getChildCurve(t), 
					set.getLocalPosition(t));
		} else if (curve instanceof Polyline2D){
			// class cast
			Polyline2D poly = (Polyline2D) curve;
			
			// get indices of extremity vertices
			int ind0 = (int) Math.floor(t);
			int ind1 = ind0 + 1;
			
			// process special case of last vertex
			if(ind1==poly.getVertexNumber()) {
				if(poly.isClosed())
					ind1 = 0;
				else{
					ind1 = ind0;
					ind0 = ind0-1;
				}
			}
			
			// create the resulting vector
			return new Vector2D(poly.getVertex(ind0), poly.getVertex(ind1));
		} else {
			// Should not be other cases...
			System.err.println("unknown type of curve");
			return null;
		}
	}
}
