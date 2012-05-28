/**
 * File: 	UnaryVariableOp2D.java
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
public abstract class UnaryVariableOp2D extends DynamicMeasure2D {

	DynamicMeasure2D parent;
	
	Measure2D result = new Measure2D(0);
	
	/**
	 * 
	 */
	public UnaryVariableOp2D(DynamicMeasure2D parent) {
		this.parent = parent;
		this.parents.add(parent);
		
		update();
	}

	@Override
	public Measure2D getMeasure(){
		return result;
	}
	
	protected abstract double computeResult(double val);
	
	/* (non-Javadoc)
	 * @see dynamic.DynamicObject2D#update()
	 */
	@Override
	public void update() {
		this.defined = false;
		if(!parent.isDefined()) return;

		Measure2D measure = parent.getMeasure();
		double value = measure.getValue();
		
		double res = computeResult(value);
		
		this.result = new Measure2D(res);
		
		this.defined = true;
	}
}
