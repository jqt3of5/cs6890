/*
 * File : Conic5Points2D.java
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
 * author : Legland
 * Created on 25 janv. 2004
 */
package dynamic.shapes;

import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.conic.Conic2D;
import math.geom2d.conic.Conic2DUtils;
import math.utils.Matrix;
import dynamic.DynamicShape2D;

/**
 * A conic going through 5 points.
 * @author Legland
 */
public class Conic5Points2D extends DynamicShape2D{
	
	DynamicShape2D parent1;
	DynamicShape2D parent2;
	DynamicShape2D parent3;
	DynamicShape2D parent4;
	DynamicShape2D parent5;
	
	Conic2D conic = null;
	
	public Conic5Points2D(
			DynamicShape2D point1, DynamicShape2D point2, 
			DynamicShape2D point3, DynamicShape2D point4,
			DynamicShape2D point5){
		super();
		this.parent1 = point1;
		this.parent2 = point2;
		this.parent3 = point3;
		this.parent4 = point4;
		this.parent5 = point5;

		parents.add(point1);
		parents.add(point2);
		parents.add(point3);
		parents.add(point4);
		parents.add(point5);
		parents.trimToSize();

		update(); 
	}
	
	@Override
	public Shape2D getShape(){
		return conic;
	}

	@Override
	public void update(){
		this.defined = false;
		
		// Check parents are defined
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
		if(!parent3.isDefined()) return;
		if(!parent4.isDefined()) return;
		if(!parent5.isDefined()) return;
				
		Shape2D shape;
		
		// the array of points
		Point2D[] points = new Point2D[5];
		
		// Extract first point
		shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		points[0] = (Point2D) shape;

		// Extract second point
		shape = parent2.getShape();
		if(!(shape instanceof Point2D)) return;
		points[1] = (Point2D) shape;
		
		// Extract third point
		shape = parent3.getShape();
		if(!(shape instanceof Point2D)) return;
		points[2] = (Point2D) shape;
		
		// Extract fourth point
		shape = parent4.getShape();
		if(!(shape instanceof Point2D)) return;
		points[3] = (Point2D) shape;
		
		// Extract fifth point
		shape = parent5.getShape();
		if(!(shape instanceof Point2D)) return;
		points[4] = (Point2D) shape;
		
		// allocate array for coordinates
		double[] x = new double[5];
		double[] y = new double[5];
		
		// extract the coordinates
		for(int i=0; i<5; i++) {
			x[i] = points[i].getX();
			y[i] = points[i].getY();
		}
		
		// Compute coefficients of the conic
		double[] coefs = findConic(x, y);
		if(coefs==null)
			return;
		
		// Reduce coefficients to create the conic
		this.conic = Conic2DUtils.reduceConic(coefs);
		if(this.conic==null)
			return;
		
		this.defined = true;		
	}
	
	public final static double[] findConic(double[] x, double[] y) {
		
		// The array for storing the coefficients
		double[] res = new double[6];
		
		// First check there is no points on the same location
		for (int i = 1; i < 5; i++) {
			for (int j = 0; j < i; j++) {
				if ((x [i] == x [j]) && (y [i] == y [j])) {
					return null;
				}
			}
		}
		
		// allocate memory for first matrix
		double[][] mat = new double[5][6];

		// Create the first matrix
		for (int i=0; i<5; i++) {
			mat[i][0] = x[i] * x[i];
			mat[i][1] = x[i] * y[i];
			mat[i][2] = y[i] * y[i];
			mat[i][3] = x[i];
			mat[i][4] = y[i];
			mat[i][5] = 1;
		}
		
		// iterations
		//for(int iter=5; iter>=0; iter--) {
			int iter = 5;
			// the matrix and the vector to invert
			double[][] 	m = new double[5][5];
			double[] 	v = new double[5];
			
			// Initialize the matrix and the vector
			for (int i=0; i<5; i++)  {
				// The matrix
				for (int j=0; j<iter-1; j++)
					m[i][j] = mat[i][j];
				for (int j=iter-1; j<5; j++)
					m[i][j] = mat[i][j];
				
				// the vector
				v[i] = -mat[i][iter];
			}

			// Call the solver
			Matrix matrix = new Matrix(m);
			double[] coefs = matrix.solve(v);
		
//		    // Should do some test to check no pathological case
//		    if ((errno == 0) && (! det.petit ()))  {
//		      for (i = 0; i < iter - 1; i++)
//		        coeff [i] = u.readcoeff (i + 1);
//		      coeff [iter - 1] = N1;
//			for (i = iter; i <= 5; i++)
//			coeff [i] = u.readcoeff (i);
//			return TRUE;
//			}
			
			// format coefficients
			for (int i=0; i<iter; i++)
				res[i] = coefs[i];
			res [iter] = 1;
			for (int i=iter+1; i<=5; i++)
				res[i] = coefs[i-1];
		//} // end iter
		
		// return the computed coefficients
		return res;
	}
}
