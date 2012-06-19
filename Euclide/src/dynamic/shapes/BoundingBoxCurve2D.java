/*
 * File : BoundingBoxCurve2D.java
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
 * Created on 08 Apr. 2007
 */
package dynamic.shapes;

import math.geom2d.Shape2D;
import dynamic.DynamicShape2D;

/**
 * A Box2D which follows the bounding box of a given curve.
 * @author Legland
 */
public class BoundingBoxCurve2D extends DynamicShape2D{
	
	DynamicShape2D parent1;

	protected Shape2D box = null;
	
	public BoundingBoxCurve2D(DynamicShape2D curve){
		super();
		this.parent1 = curve;
		
		parents.add(curve);
		//parents.trimToSize();
		
		//update(); 
	}
	
	@Override
	public Shape2D getShape(){
		return box;
	}
	
	/* (non-Javadoc)
	 * @see math.geom2d.dynamic.DynamicShape2D#update()
	 */
	@Override
	public void update() {
		this.defined = false;
		if(!parent1.isDefined()) return;
		
		// extract shape
		Shape2D shape = parent1.getShape();
				
		// extract bounding box of shape
		this.box = shape.getBoundingBox().getBoundary();
		this.defined = true;
	}
}
