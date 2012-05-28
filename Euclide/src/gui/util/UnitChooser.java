/**
 * File: 	UnitChooser.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 13 janv. 09
 */
package gui.util;

import gui.EuclideGui;


import javax.swing.JComboBox;

import math.geom2d.AngleUnit;
import math.geom2d.Unit;

import dynamic.DynamicMeasure2D;


/**
 * @author dlegland
 *
 */
public class UnitChooser extends JComboBox {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private EuclideGui gui;
    
    DynamicMeasure2D.Type type;
    
    /**
     * @param arg0
     */
    public UnitChooser(EuclideGui gui, DynamicMeasure2D.Type type) {
        super();
        this.gui = gui;
        setUnitType(type);
    }

    public void setUnitType(DynamicMeasure2D.Type type) {
        this.type = type;
        
        this.removeAllItems();
        
        //if(type==DynamicMeasure2D.Type.ANGLE){
        switch (type) {
        case ANGLE:
        	for(AngleUnit angleUnit : gui.getAppli().getAngleUnits())
        		this.addItem(capitalizeFirstLetter(angleUnit.getName()));
        	break;
        	
//        if(type==DynamicMeasure2D.Type.LENGTH){
//            for(LengthUnit lengthUnit : gui.getAppli().getLengthUnits())
//                this.addItem(capitalizeFirstLetter(lengthUnit.getName()));
//        }
        
        default:
        	this.addItem("None");
        }
    }
    
    private String capitalizeFirstLetter(String string){
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
    
    public Unit<?> getSelectedUnit(){
        String text = ((String) this.getSelectedItem()).toLowerCase();
        if(type==DynamicMeasure2D.Type.ANGLE){
            for(AngleUnit angleUnit : gui.getAppli().getAngleUnits())
                if(angleUnit.getName().toLowerCase().equals(text))
                    return angleUnit;
        }
//        if(type==DynamicMeasure2D.Type.LENGTH){
//            for(LengthUnit lengthUnit : gui.getAppli().getLengthUnits())
//                if(lengthUnit.getName().toLowerCase().equals(text))
//                    return lengthUnit;
//        }
        
        return null;
    }
}
