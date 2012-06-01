/*
 * File : PolygonEdge2D.java
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
 * Created on 24 jan. 2012
 */
package dynamic.shapes;

import java.util.Iterator;

import math.geom2d.Measure2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.polygon.Polygon2D;
import dynamic.DynamicMeasure2D;
import dynamic.DynamicShape2D;
import dynamic.measures.CountWrapper2D;

/**
 * @author Legland
 */
public class PolygonEdge2D extends DynamicShape2D {

	DynamicShape2D parent1;
	DynamicMeasure2D parent2;

	LineSegment2D edge = null;

	public PolygonEdge2D(DynamicShape2D polygon, int num) {
		super();
		this.parent1 = polygon;
		this.parent2 = new CountWrapper2D(num);

		parents.add(parent1);
		this.parameters.add(num);

		//update();
	}

	public PolygonEdge2D(DynamicShape2D polygon, DynamicMeasure2D index) {
		super();
		this.parent1 = polygon;
		this.parent2 = index;

		parents.add(parent1);
		parents.add(index);
		parents.trimToSize();

		//update();
	}

	/**
	 * Computes the initial index of the vertex by choosing the closest vertex
	 * to a given point.
	 * 
	 * @param parent1
	 *            a dynamic shape containing a polygon
	 * @param parent2
	 *            a dynamic shape containing a point
	 */
	public PolygonEdge2D(DynamicShape2D parent1, DynamicShape2D parent2) {
		super();
		this.parent1 = parent1;

		parents.add(parent1);

		// Extract position of parent2
		Shape2D shape = parent2.getShape();
		if (!(shape instanceof Point2D))
			return;
		Point2D point = (Point2D) shape;

		// Extract the parent polygon
		shape = parent1.getShape();
		if (!(shape instanceof Polygon2D))
			return;
		Polygon2D polygon = (Polygon2D) shape;

		// Compute initial vertex number of point on polygon
		int num = (int) Math.floor(polygon.getBoundary().project(point));
		this.parent2 = new CountWrapper2D(num);
		this.parameters.add(num);
		//update();
	}

	@Override
	public LineSegment2D getShape() {
		return this.edge;
	}

	@Override
	public void update() {
		this.defined = false;
		if (!parent1.isDefined())
			return;

		// Extract parent polygon
		Shape2D shape;
		shape = parent1.getShape();
		if (!(shape instanceof Polygon2D))
			return;
		Polygon2D polygon = (Polygon2D) shape;

		// Extract vertex index
		Measure2D measure;
		measure = parent2.getMeasure();
		int num = (int) measure.getValue();

		// check polygon has enough vertices
		if (polygon.getEdgeNumber() <= num)
			return;

		//
		Iterator<? extends LineSegment2D> iter = 
			polygon.getEdges().iterator();
		edge = iter.next();
		for (int i = 0; i < num; i++)
			edge = iter.next();

		this.defined = true;
	}
}
