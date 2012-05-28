/*
 * File : Ellipse2Focus2D.java
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
 * Created on 28 dec. 2006
 */
package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.conic.*;

import dynamic.*;
import dynamic.measures.MeasureWrapper2D;

/**
 * Construct an ellipse from 3 points: the center, the point marking the end
 * of the major semi-axis, and a third point defining the length of the minor
 * semi-axis. Last point also specifies orientation of ellipse.
 * @author Legland
 */
public class Ellipse2Focus2D extends DynamicShape2D{
	
	DynamicShape2D parent1;
	DynamicShape2D parent2;
	DynamicMeasure2D parent3;
	
	private Ellipse2D ellipse = new Ellipse2D();
	
	public Ellipse2Focus2D(DynamicShape2D point1, 
			DynamicShape2D point2, double chord){
		this(point1, point2, new MeasureWrapper2D(chord));
	}
	
	public Ellipse2Focus2D(DynamicShape2D point1, DynamicShape2D point2, 
			DynamicMeasure2D chord){
		super();
		parent1 = point1;
		parent2 = point2;
		parent3 = chord;

		parents.add(point1);
		parents.add(point2);
		parents.add(chord);
		parents.trimToSize();

		update(); 
	}
	
	@Override
	public Shape2D getShape(){
		return ellipse;
	}
		
	@Override
	public void update(){
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
		if(!parent3.isDefined()) return;
				
		Shape2D shape;
		Measure2D measure;
		
		shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D focus1 = (Point2D) shape;

		shape = parent2.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D focus2 = (Point2D) shape;
		
		measure = parent3.getMeasure();
		double chordLength = measure.getValue();
		
		// Check ellipse exists
		double dist = focus1.getDistance(focus2);
		if(chordLength <= dist)
			return;

		// create the new ellipse
		ellipse = Ellipse2D.create(focus1, focus2, chordLength);
		if (ellipse == null)
			return;
		
		this.defined = true;
	}
}
