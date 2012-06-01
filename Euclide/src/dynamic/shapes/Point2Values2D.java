/* file : Point2Values2D.java
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
 * Created on 22 d�c. 2005
 *
 */
package dynamic.shapes;

import math.geom2d.*;
import dynamic.*;

/**
 * A point defined by two Cartesian coordinates, given as measures.
 * @author dlegland
 */
public class Point2Values2D extends DynamicShape2D {

	DynamicMeasure2D parent1;
	DynamicMeasure2D parent2;
	
	private Point2D point = new Point2D();

	/**
	 * @param point1
	 * @param point2
	 */
	public Point2Values2D(DynamicMeasure2D posX, DynamicMeasure2D posY) {
		this.parent1 = posX;
		this.parent2 = posY;

		parents.add(posX);
		parents.add(posY);
		parents.trimToSize();

	//	update();
	}


	@Override
	public Shape2D getShape(){
		return point;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.dynamic.DynamicShape2D#update()
	 */
	@Override
	public void update() {
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;

		Measure2D measure;
		
		measure = parent1.getMeasure();
		double posX = measure.getValue();
		
		measure = parent2.getMeasure();
		double posY = measure.getValue();
		
		this.point = new Point2D(posX, posY);
		this.defined = true;
	}

}
