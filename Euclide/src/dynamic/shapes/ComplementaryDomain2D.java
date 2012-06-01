/*
 * File : ComplementaryDomain2D.java
 *
 * Project : geometry
 *
 * ===========================================
 * 
 * This library is free software; you can distribute it and/or modify it 
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
 * Created on 13 May 2006
 */
package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.domain.*;

import dynamic.*;

/**
 * Create a Domain2D from a Boundary2D.
 * @author Legland
 */
public class ComplementaryDomain2D extends DynamicShape2D{
	
	DynamicShape2D 	parent1;
	
	private Domain2D complement = null;

	public ComplementaryDomain2D(DynamicShape2D curve){
		super();
		this.parent1 = curve;

		parents.add(curve);
		parents.trimToSize();

		//update(); 
	}	
	
	@Override
	public Shape2D getShape(){
		return complement;
	}
	
	@Override
	public void update(){
		this.defined = false;
		if(!parent1.isDefined()) return;

		Shape2D shape;
		
		shape = parent1.getShape();
		if(!(shape instanceof Domain2D)) return;
		Domain2D domain = (Domain2D) shape;
		
		complement = domain.complement();
		
		this.defined = true;
	}
}
