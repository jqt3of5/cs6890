/**
 * File: 	VariableSquared2D.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 22 nov. 09
 */
package dynamic.measures;

import dynamic.DynamicMeasure2D;


/**
 * @author dlegland
 *
 */
public class VariableSquared2D extends UnaryVariableOp2D {

	public VariableSquared2D(DynamicMeasure2D parent) {
		super(parent);
	}

	/* (non-Javadoc)
	 * @see dynamic.measures.UnaryVariableOp2D#computeResult(double)
	 */
	@Override
	protected double computeResult(double val) {
		return val*val;
	}	
}
