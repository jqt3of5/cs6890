/**
 * File: 	DistanceUnit2D.java
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
 *
 */
public class LengthUnit extends Unit<LengthMeasure2D> {

    public final static LengthUnit METER = 
        new LengthUnit("meter", "m", 1);
    public final static LengthUnit KILOMETER = 
        new LengthUnit("kilometer", "km", 1000);
    public final static LengthUnit CENTIMETER = 
        new LengthUnit("centimeter", "cm", 1e-2);
    public final static LengthUnit MILLIMETER = 
        new LengthUnit("millimeter", "mm", 1e-3);
    public final static LengthUnit MICROMETER = 
        new LengthUnit("micrometer", "µm", 1e-6);

    double factor;
    
    public LengthUnit(String name, String symbol, double factor){
        super(name, symbol);
        this.factor = factor;
    }

    public final static double convert(double value, LengthUnit unit1, 
            LengthUnit unit2){
        if(unit1==unit2)
            return value;
        
        return value*unit2.factor/unit1.factor;     
    }
    
    @Override
	public String toString(){
        return "Unit: " + name;
    }
}
