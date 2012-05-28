/**
 * File: 	PolygonCenterVertexNumber.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 14 févr. 10
 */
package dynamic.shapes;

import math.geom2d.Angle2D;
import math.geom2d.CountMeasure2D;
import math.geom2d.Measure2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.polygon.SimplePolygon2D;
import dynamic.DynamicMeasure2D;
import dynamic.DynamicShape2D;


/**
 * @author dlegland
 *
 */
public class PolygonCenterVertexNumber2D extends DynamicShape2D {

	DynamicShape2D parent1;
	DynamicShape2D parent2;
	DynamicMeasure2D parent3;
	
	protected SimplePolygon2D polygon = new SimplePolygon2D();
	
	/**
	 * Initilize only the first point
	 * @param point
	 */
	public PolygonCenterVertexNumber2D(DynamicShape2D point1, 
			DynamicShape2D point2, DynamicMeasure2D vertexCount) {
		super();
		this.parent1 = point1;
		this.parent2 = point2;
		this.parent3 = vertexCount;

		parents.add(point1);
		parents.add(point2);
		parents.add(vertexCount);
		parents.trimToSize();

		update();
	}
	
	@Override
	public Shape2D getShape(){
		return polygon;
	}
	
	/* (non-Javadoc)
	 * @see math.geom2d.dynamic.DynamicShape2D#update()
	 */
	@Override
	public void update() {
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
		if(!parent3.isDefined()) return;

		Shape2D shape;
		
		// extract first point
		shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D center = (Point2D) shape;
		
		// extract second point
		shape = parent2.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D vertex0 = (Point2D) shape;
		
		// Extract vertex number
		Measure2D measure = parent3.getMeasure();
		if(!(measure instanceof CountMeasure2D)) return;
		int vertexNumber = ((CountMeasure2D) measure).getCount();
		
		// Do not create a polygon with less than 3 vertices
		if (vertexNumber<3)
			return;
		
		// Compute some geometric values
		double dTheta = Math.PI*2/vertexNumber;
		double theta0 = Angle2D.getHorizontalAngle(center, vertex0);
		double dist = center.getDistance(vertex0);
		
		// build the polygon vertices
		Point2D[] vertices = new Point2D[vertexNumber];
		for (int i=0; i<vertexNumber; i++){
			vertices[i] = Point2D.createPolar(center, dist, theta0+i*dTheta);
		}
		
		// creates the polygon
		this.polygon = new SimplePolygon2D(vertices);
		
		this.defined = true;
	}
}
