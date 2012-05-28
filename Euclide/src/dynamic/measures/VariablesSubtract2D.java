/**
 * File: 	VariablesSubtract2D.java
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
public class VariablesSubtract2D extends BinaryVariableOp2D {

	public VariablesSubtract2D(DynamicMeasure2D parent1, DynamicMeasure2D parent2) {
		super(parent1, parent2);
	}
	
	/* (non-Javadoc)
	 * @see dynamic.measures.BinaryVariableOp2D#computeResult(double, double)
	 */
	@Override
	protected double computeResult(double val1, double val2) {
		return val1+val2;
	}

}
