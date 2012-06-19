/**
 * File: 	BinaryVariableOp2D.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 22 nov. 09
 */
package dynamic.measures;

import math.geom2d.Measure2D;
import dynamic.DynamicMeasure2D;


/**
 * @author dlegland
 *
 */
public abstract class BinaryVariableOp2D extends DynamicMeasure2D {

	DynamicMeasure2D parent1;
	DynamicMeasure2D parent2;
	
	Measure2D result = new Measure2D(0);
	
	/**
	 * 
	 */
	public BinaryVariableOp2D(DynamicMeasure2D parent1, DynamicMeasure2D parent2) {
		this.parent1 = parent1;
		this.parent2 = parent2;
		
		this.parents.add(parent1);
		this.parents.add(parent2);
		//this.parents.trimToSize();
		
	//	update();
	}

	@Override
	public Measure2D getMeasure(){
		return result;
	}
	
	protected abstract double computeResult(double val1, double val2);
	
	/* (non-Javadoc)
	 * @see dynamic.DynamicObject2D#update()
	 */
	@Override
	public void update() {
		this.defined = false;
		if(!parent1.isDefined()) return;
		if(!parent2.isDefined()) return;

		Measure2D measure1 = parent1.getMeasure();
		double value1 = measure1.getValue();
		
		Measure2D measure2 = parent2.getMeasure();
		double value2 = measure2.getValue();
		
		double res = computeResult(value1, value2);
		
		this.result = new Measure2D(res);
		
		this.defined = true;
	}	
}
