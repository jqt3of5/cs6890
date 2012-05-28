/**
 * File: 	Unit.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 11 janv. 09
 */
package math.geom2d;


/**
 * @author dlegland
 *
 */
public abstract class Unit<M extends Measure2D> {
    
    String name;
    String symbol;

    protected Unit(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }
    
    /** Get the long name */
    public String getName(){
        return name;
    }
    
    /** Get the symbol associated with this unit*/
    public String getSymbol(){
        return symbol;
    }
}
