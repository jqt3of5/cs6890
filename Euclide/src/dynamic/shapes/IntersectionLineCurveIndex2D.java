/*
 * File : IntersectionLineCurveIndex2D.java
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
 * Created on 8 Apr. 2007
 */
package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.curve.*;
import math.geom2d.line.*;
import dynamic.*;

import java.util.*;

/**
 * A set of points located at the intersections of a line and an arbitrary curve.
 * @author Legland
 */
public class IntersectionLineCurveIndex2D extends DynamicShape2D{
	
	DynamicShape2D 		parent1;
	DynamicShape2D 		parent2;
	DynamicMeasure2D 	parent3;

	Point2D point = new Point2D();
	
	public IntersectionLineCurveIndex2D(DynamicShape2D line, 
			DynamicShape2D curve, DynamicMeasure2D index){
		super(); //
		this.parent1 = line;
		this.parent2 = curve;
		this.parent3 = index;

		parents.add(line);
		parents.add(curve);
		parents.add(index);
		//parents.trimToSize();

		//update();
	}
	
	@Override
	public Shape2D getShape(){
		return point;
	}
	
	@Override
	public void update(){
		defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
		if(!parent3.isDefined()) return;
		
		Shape2D shape;
		shape = parent1.getShape();
		if(!(shape instanceof LinearShape2D)) return;
		LinearShape2D line = (LinearShape2D) shape;
		
		shape = parent2.getShape();
		if(!(shape instanceof Curve2D)) return;
		Curve2D curve = (Curve2D) shape;
		
		Measure2D measure;
		measure = parent3.getMeasure();
		if(!(measure instanceof CountMeasure2D)) return;
		int index = ((CountMeasure2D) measure).getCount();

		
		// extract the intersections
		Collection<Point2D> intersections = curve.getIntersections(line);
		if (index>intersections.size() || index==0) return;
		
		int i=0;
		Iterator<Point2D> iterator = intersections.iterator();
		while(i++<index)
			point = iterator.next();

		this.defined = true;
	}
}
