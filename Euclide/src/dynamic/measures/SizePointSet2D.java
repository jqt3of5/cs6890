/* file : SizePointSet2D.java
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
 * Created on 11 Nov. 2008
 *
 */
package dynamic.measures;

import math.geom2d.CountMeasure2D;
import math.geom2d.Measure2D;
import math.geom2d.Shape2D;
import math.geom2d.point.PointSet2D;
import dynamic.DynamicMeasure2D;
import dynamic.DynamicShape2D;

public class SizePointSet2D extends DynamicMeasure2D {

	private DynamicShape2D parent1;
	
	CountMeasure2D measure = new CountMeasure2D(0);
		
	public SizePointSet2D(DynamicShape2D pointSet){
		this.parent1 = pointSet;
		
		parents.add(pointSet);
		
		//update();
	}
	
	@Override
	public Measure2D getMeasure(){
		return measure;
	}
		
	@Override
	public void update() {
		this.defined = false;
		if(!parent1.isDefined()) return;
		
		// extract the point set
		Shape2D shape = parent1.getShape();
		if(!(shape instanceof PointSet2D)) return;
		PointSet2D pointSet = (PointSet2D) shape;
		
		// update distance measure
		this.measure.setCount(pointSet.getPointNumber());
		this.defined = true;
	}

}
