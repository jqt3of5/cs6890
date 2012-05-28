/*
 * File : SingularPointsCurve2D.java
 *
 * Project : geometry
 *
 * ===========================================
 * 
 * This library is free software; you can reposribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is posributed in the hope that it will be useful, but 
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

import math.geom2d.Shape2D;
import math.geom2d.curve.*;
import math.geom2d.point.PointArray2D;
import math.geom2d.point.PointSet2D;

import dynamic.*;

/**
 * The singular points of a curve, as a PointSet2D.
 * @author Legland
 */
public class SingularPointsCurve2D extends DynamicShape2D{
	
	/** The parent curve */
	DynamicShape2D 		parent1;
	
	/** The resulting point set */
	private PointSet2D pointSet = new PointArray2D();
		
	public SingularPointsCurve2D(DynamicShape2D curve){
		super();
		this.parent1 = curve;
		
		parents.add(curve);
		
		update(); 
	}
	
	
	@Override
	public PointSet2D getShape(){
		return pointSet;
	}
	
	/* (non-Javadoc)
	 * @see math.geom2d.dynamic.DynamicShape2D#isDefined()
	 */
	@Override
	public boolean isDefined() {
		return this.defined;
	}
	
	@Override
	public void update(){
		this.defined = false;
		
		// check parents are defined
		if(!parent1.isDefined()) return;
		
		// Extract the parent shape
		Shape2D shape = parent1.getShape();
		if(!(shape instanceof Curve2D)) return;
		Curve2D curve = (Curve2D) shape;
		
		// update position of the point
		pointSet = new PointArray2D(curve.getSingularPoints());
		this.defined = true;
	}
}
