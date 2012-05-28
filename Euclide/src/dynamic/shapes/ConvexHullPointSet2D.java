/* file : ConvexHullPointSet2D.java
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
 * Created on 23 Feb. 2007
 *
 */
package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.point.PointSet2D;
import math.geom2d.polygon.*;
import math.geom2d.polygon.convhull.*;

import dynamic.*;

/**
 * @author dlegland
 */
public class ConvexHullPointSet2D extends DynamicShape2D {

    private DynamicShape2D parent1;
	
	Polygon2D polygon;
	
	/**
	 * Initialize from an array of points.
	 * @param points the point array creating the polygon
	 */
	public ConvexHullPointSet2D(DynamicShape2D pointSet) {
		super();
        this.parent1 = pointSet;
        
        parents.add(pointSet);
        
        update();
	}

	@Override
	public Shape2D getShape(){
		return polygon;
	}
	
	@Override
	public void update(){
		this.defined = false;
		
		// extract the point set
        Shape2D shape = parent1.getShape();
        if(!(shape instanceof PointSet2D)) return;
        PointSet2D pointSet = (PointSet2D) shape;
		
		polygon = new GrahamScan2D().convexHull(pointSet.getPoints());
		
		this.defined = true;
	}
}
