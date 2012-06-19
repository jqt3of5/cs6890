/*
 * File : Ellipse2FocusPoint2D.java
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

import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.conic.Ellipse2D;
import dynamic.DynamicShape2D;

/**
 * Construct an ellipse from 3 points: the center, the point marking the end
 * of the major semi-axis, and a third point defining the length of the minor
 * semi-axis. Last point also specifies orientation of ellipse.
 * @author Legland
 */
public class Ellipse2FocusPoint2D extends DynamicShape2D{
	
	DynamicShape2D parent1;
	DynamicShape2D parent2;
	DynamicShape2D parent3;
	
	private Ellipse2D ellipse = new Ellipse2D();
	
	public Ellipse2FocusPoint2D(DynamicShape2D point1, DynamicShape2D point2, 
			DynamicShape2D point3){
		super();
		parent1 = point1;
		parent2 = point2;
		parent3 = point3;

		parents.add(point1);
		parents.add(point2);
		parents.add(point3);
		//parents.trimToSize();

		//update(); 
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
		
		shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D focus1 = (Point2D) shape;

		shape = parent2.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D focus2 = (Point2D) shape;
		
		shape = parent3.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D point3 = (Point2D) shape;
		
		// compute sum of distances between point3 and each focus
		double dist1 = focus1.getDistance(point3);
		double dist2 = focus2.getDistance(point3);
		double dist = dist1 + dist2;
		
		// Check ellipse exists
		double distFocus = focus1.getDistance(focus2);
		if(dist <= distFocus)
			return;
		
		// create the new ellipse
		ellipse = Ellipse2D.create(focus1, focus2, dist);
		if (ellipse == null)
			return;
		
		this.defined = true;
	}
}
