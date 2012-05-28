/*
 * File : Tangent2Circles2D.java
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
 * Created on 2 feb. 2006
 */
package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.conic.*;
import math.geom2d.line.*;

import dynamic.*;
import dynamic.predicates.BooleanWrapper2D;

/**
 * @author Legland
 */
public class Tangent2Circles2D extends DynamicShape2D{
	
	DynamicShape2D parent1;
	DynamicShape2D parent2;
	DynamicPredicate2D parent3=null;
	DynamicPredicate2D parent4=null;

	boolean dir1;
	boolean dir2;
	
	private StraightLine2D line = new StraightLine2D();
	
	public Tangent2Circles2D(DynamicShape2D circle1, DynamicShape2D circle2){
		this(circle1, circle2, true, true);
	}
	
	/**
	 * Specify the direction of the tangent relative to orientation of circles. This allows
	 * to differentiate the 4 different tangents we can make with 2 circles. The direction of the 
	 * tangent is from c1 to c2. Then, the first boolean is true if tangent has the same direction
	 * as the circle c1 at their meeting point. The second boolean has the same meaning for circle
	 * c2. The 4 combinations of these 2 parameters can describe the 4 different tangents.  
	 * @param c1 The first circle
	 * @param c2 The second circle
	 * @param c1Direct is true if tangent has the same direction of c1 at meeting point
	 * @param c2Direct is true if tangent has the same direction of c2 at meeting point
	 */
	public Tangent2Circles2D(DynamicShape2D c1, DynamicShape2D c2, boolean c1Direct, boolean c2Direct){
		parent1 = c1;
		parent2 = c2;
		parent3 = new BooleanWrapper2D(c1Direct);
		parent4 = new BooleanWrapper2D(c2Direct);
		
		parents.add(c1);
		parents.add(c2);
		parents.trimToSize();

		parameters.add(c1Direct);
		parameters.add(c2Direct);
		parameters.trimToSize();
		
		update(); 
	}
	
	public Tangent2Circles2D(DynamicShape2D c1, DynamicShape2D c2, 
			DynamicPredicate2D c1Direct, DynamicPredicate2D c2Direct){
		parent1 = c1;
		parent2 = c2;
		parent3 = c1Direct;
		parent4 = c2Direct;
		
		parents.add(c1);
		parents.add(c2);
		parents.add(c1Direct);
		parents.add(c2Direct);
		parents.trimToSize();

		update(); 
	}
	
	@Override
	public Shape2D getShape(){
		return line;
	}
	
	@Override
	public void update(){
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
		if(!parent3.isDefined()) return;
		if(!parent4.isDefined()) return;
			
		Shape2D shape;
		shape = parent1.getShape();
		if(!(shape instanceof Circle2D)) return;
		Circle2D circle1 = (Circle2D) shape;
		
		shape = parent2.getShape();
		if(!(shape instanceof Circle2D)) return;
		Circle2D circle2 = (Circle2D) shape;
		
		boolean dir1 = parent3.getResult();
		boolean dir2 = parent4.getResult();
		
		double r1 	= circle1.getRadius();
		double r2 	= circle2.getRadius();
		Point2D c1 	= circle1.getCenter();
		Point2D c2 	= circle2.getCenter();
		double D12 	= c1.getDistance(c2);
		double theta = Angle2D.getHorizontalAngle(c1, c2);

		// suppose both circles directly oriented (turning counterclockwise).
		// b1 and b2 are the new values of 'directed' for the circles.
//		boolean b1 = (circle1.isDirect() && dir1) || (!circle1.isDirect() && !dir1);
//		boolean b2 = (circle2.isDirect() && dir2) || (!circle2.isDirect() && !dir2);
		boolean b1 = circle1.isDirect() ^ dir1;
		boolean b2 = circle2.isDirect() ^ dir2;

		double x0, y0, dx, dy;
		double diff, alpha, d12;
		
		// There are four different cases, depending on b1 and b2
		if(b1 && b2){
			// both under
			if(r1>r2){
				diff = (r1-r2)/D12;
				alpha = (theta - Math.acos(diff) + Math.PI*2) % (Math.PI*2);
				x0 = c1.getX()+r1*Math.cos(alpha);
				y0 = c1.getY()+r1*Math.sin(alpha);
				d12 = Math.sqrt(D12*D12 - (r1-r2)*(r1-r2)); 
				dx = d12*Math.cos(alpha - Math.PI/2);
				dy = d12*Math.sin(alpha - Math.PI/2);
			}else{			
				diff = (r2-r1)/D12;
				alpha = (theta - Math.asin(diff) - Math.PI/2 + Math.PI*2) % (Math.PI*2);
				x0 = c1.getX()+r1*Math.cos(alpha);
				y0 = c1.getY()+r1*Math.sin(alpha);
				d12 = Math.sqrt(D12*D12 - (r2-r1)*(r2-r1)); 
				dx = d12*Math.cos(alpha - Math.PI/2);
				dy = d12*Math.sin(alpha - Math.PI/2);
			}
		}else if (b1 && !b2){
			//System.out.println("cas 3");
			// down and up			
			alpha = (theta - Math.acos((r1+r2)/D12) + Math.PI*2) % (Math.PI*2);
			x0 = c1.getX()+r1*Math.cos(alpha);
			y0 = c1.getY()+r1*Math.sin(alpha);
			d12 = Math.sqrt(D12*D12 - (r1+r2)*(r1+r2)); 
			dx = d12*Math.cos(alpha + Math.PI/2);
			dy = d12*Math.sin(alpha + Math.PI/2);
		}else if (!b1 && b2){
			//System.out.println("cas 4");
			// up and down			
			alpha = (theta + Math.acos((r1+r2)/D12) + Math.PI*2) % (Math.PI*2);
			x0 = c1.getX()+r1*Math.cos(alpha);
			y0 = c1.getY()+r1*Math.sin(alpha);
			d12 = Math.sqrt(D12*D12 - (r1+r2)*(r1+r2)); 
			dx = d12*Math.cos(alpha - Math.PI/2);
			dy = d12*Math.sin(alpha - Math.PI/2);												
		}else{
			// both on the top
			if(r1>r2){
				diff = (r1-r2)/D12;
				alpha = (Math.acos(diff) + theta + Math.PI*2) % (Math.PI*2);
				x0 = c1.getX()+r1*Math.cos(alpha);
				y0 = c1.getY()+r1*Math.sin(alpha);
				d12 = Math.sqrt(D12*D12 - (r1-r2)*(r1-r2)); 
				dx = d12*Math.cos(alpha - Math.PI/2);
				dy = d12*Math.sin(alpha - Math.PI/2);
			}else{			
				diff = (r2-r1)/D12;
				alpha = (Math.asin(diff) + theta + Math.PI/2 + Math.PI*2) % (Math.PI*2);
				x0 = c1.getX()+r1*Math.cos(alpha);
				y0 = c1.getY()+r1*Math.sin(alpha);
				d12 = Math.sqrt(D12*D12 - (r2-r1)*(r2-r1)); 
				dx = d12*Math.cos(alpha + Math.PI/2);
				dy = d12*Math.sin(alpha + Math.PI/2);
			}
		}
		
		// update internal line
		line = new StraightLine2D(x0, y0, dx, dy);
		this.defined = true;
	}
}
