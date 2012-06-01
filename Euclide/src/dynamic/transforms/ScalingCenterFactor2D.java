/* File ScalingCenterFactor2D.java 
 *
 * Project : Euclide
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
 * Created 9 Nov. 2008
 */

package dynamic.transforms;

import math.geom2d.*;
import math.geom2d.AffineTransform2D;
import math.geom2d.transform.*;

import dynamic.DynamicMeasure2D;
import dynamic.DynamicShape2D;
import dynamic.DynamicTransform2D;

/**
 * @author dlegland
 *
 */
public class ScalingCenterFactor2D extends DynamicTransform2D {

	private DynamicShape2D 	 parent1;
	private DynamicMeasure2D parent2;
	
	AffineTransform2D trans = new AffineTransform2D();
	
	public ScalingCenterFactor2D(DynamicShape2D center, DynamicMeasure2D factor){
		super();
		this.parent1 = center;
		this.parent2 = factor;

		parents.add(center);
		parents.add(factor);
		
		//update(); 
	}

	/* (non-Javadoc)
	 * @see dynamic.DynamicTransform2D#getTransform()
	 */
	@Override
	public Transform2D getTransform() {
		return trans;
	}

	/* (non-Javadoc)
	 * @see dynamic.DynamicObject2D#update()
	 */
	@Override
	public void update() {
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;
		
		// extract the center of the scaling
		Shape2D shape = parent1.getShape();
		if(!(shape instanceof Point2D)) return;
		Point2D center = (Point2D) shape;
		
		// extract the factor of the scaling
		Measure2D measure = parent2.getMeasure();
		double factor = measure.getValue();

		// update affine transform
		this.trans = AffineTransform2D.createScaling(center, factor, factor);		
		this.defined = true;
	}

}
