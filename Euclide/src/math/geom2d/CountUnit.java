/**
 * File: 	CountUnit.java
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
public class CountUnit extends Unit<AngleMeasure2D> {

    public final static CountUnit UNIT = 
        new CountUnit("unit", "", 1);
    
    double factor;
    
    public CountUnit(String name, String symbol, double factor){
        super(name, symbol);
        this.factor = factor;
    }
    
    public final static double convert(double value, CountUnit unit1,
            CountUnit unit2){
        if(unit1==unit2)
            return value;
        
        return value*unit2.factor/unit1.factor;     
    }

}
