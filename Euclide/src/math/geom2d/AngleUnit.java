/**
 * File: 	AngleUnit.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 11 janv. 09
 */

package math.geom2d;

import math.geom2d.Unit;

/**
 * @author dlegland
 */
public class AngleUnit extends Unit<AngleMeasure2D> {

	/** Radians are expressed between 0 and 2*PI. */
	public final static AngleUnit RADIAN = new AngleUnit("Radian", "rad",
			Math.PI/180);

	/** Degrees are expressed between 0 and 360. */
	public final static AngleUnit DEGREE = new AngleUnit("Degree", "°", 1);

	/** Turns are expressed between 0 and 1. */
	public final static AngleUnit TURN = new AngleUnit("Turn", "", 1./360);

	/** The conversion factor from degrees to this unit */
	double factor;

	public AngleUnit(String name, String symbol, double factor) {
		super(name, symbol);
		this.factor = factor;
	}

	public final static double convert(double value, AngleUnit unit1,
			AngleUnit unit2) {
		if (unit1==unit2)
			return value;

		return value*unit2.factor/unit1.factor;
	}
}
