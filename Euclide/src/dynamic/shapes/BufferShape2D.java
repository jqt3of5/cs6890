/*
 * File : BufferShape2D.java
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
 * Created on 06 Sep. 2009
 */
package dynamic.shapes;

import math.geom2d.Measure2D;
import math.geom2d.Shape2D;
import math.geom2d.circulinear.CirculinearShape2D;
import math.geom2d.domain.Domain2D;
import dynamic.DynamicMeasure2D;
import dynamic.DynamicShape2D;

/**
 * Extract the vertices of a polygon as a PointSet2D.
 * @author Legland
 */
public class BufferShape2D extends DynamicShape2D{
	
    DynamicShape2D      parent1;
    DynamicMeasure2D    parent2;
    
	Domain2D buffer;
	
	public BufferShape2D(DynamicShape2D parent1, DynamicMeasure2D parent2){
		super();
		this.parent1 = parent1;
		this.parent2 = parent2;
	    
		parents.add(parent1);
		parents.add(parent2);
		//parents.trimToSize();
        
		//update(); 
	}

	@Override
	public Shape2D getShape(){
		return buffer;
	}
	
	@Override
	public void update(){
	    // check parents are defined
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;

		// extract circulinear shape from parent shape
		Shape2D shape = parent1.getShape();
		CirculinearShape2D circu = null;
		if (shape instanceof CirculinearShape2D) {
			circu = (CirculinearShape2D) shape;
		} else {
			//TODO: implement for non circulinear shapes.
		}
		
		if (circu==null)
			return;
		
		// Extract buffer size
        Measure2D measure = parent2.getMeasure();
        double d = measure.getValue();
        
		// create the shape buffer
        buffer = circu.getBuffer(d);
        
		this.defined = true;
	}
}
