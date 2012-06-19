/*
 * File : ParallelLineDistance2D.java
 *
 * Project : geometry
 *
 * ===========================================
 * 
 * This library is free software; you can redist_ribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is dist_ributed in the hope that it will be useful, but 
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
import math.geom2d.line.*;

import dynamic.*;

/**
 * @author Legland
 */
public class ParallelLineDistance2D extends DynamicShape2D{
	
	DynamicShape2D 		parent1;
	DynamicMeasure2D 	parent2;
	
	StraightLine2D line = new StraightLine2D();
	
	public ParallelLineDistance2D(DynamicShape2D line, DynamicMeasure2D distanceMeasure){
		super(); //
		this.parent1 = line;
		this.parent2 = distanceMeasure;

		parents.add(line);
		parents.add(distanceMeasure);
		//parents.trimToSize();

	//	update(); 
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

		Shape2D shape;
		Measure2D measure;
		
		shape = parent1.getShape();
		if(!(shape instanceof LinearShape2D)) return;
		LinearShape2D line0 = (LinearShape2D) shape;
		
		measure = parent2.getMeasure();
		double distance = measure.getValue();

		double[][] tab = line0.getSupportingLine().getParametric();

		double delta = Math.sqrt(tab[0][1]*tab[0][1] + tab[1][1]*tab[1][1]);
		double x0 = tab[0][0]+distance*tab[1][1]/delta;
		double y0 = tab[1][0]-distance*tab[0][1]/delta;
		double dx = tab[0][1];
		double dy = tab[1][1];
		line = new StraightLine2D(x0, y0, dx, dy);

		this.defined = true;
	}
}
