/**
 * 
 */
package gui.panels;

import gui.EuclideGui;
import gui.EuclidePanel;
import gui.util.UnitChooser;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import math.geom2d.*;
import dynamic.DynamicMeasure2D;
import dynamic.measures.AngleWrapper2D;
import dynamic.measures.CountWrapper2D;
import dynamic.measures.MeasureWrapper2D;


/**
 * This panel is devoted to the entry of a new measure, 
 * by choosing measure type, value and unit.
 * @author dlegland
 *
 */
public class InputMeasurePanel extends EuclidePanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	
    HashMap<String, AngleUnit> angleUnits;
    HashMap<String, LengthUnit> lengthUnits;

    JLabel typeLabel 	= new JLabel("Type:");
	JLabel valueLabel 	= new JLabel("Value:");
	JLabel unitLabel 	= new JLabel("Unit:");
	
	JComboBox typeCombo 	= new JComboBox();
	JTextField valueEdit 	= new JTextField("", 8);
	UnitChooser unitCombo;
	
	DynamicMeasure2D.Type type = DynamicMeasure2D.Type.NONE;
	DynamicMeasure2D measure = null;
	
	public InputMeasurePanel(EuclideGui gui){
		super(gui);
		
        // build map of angle units
        angleUnits = new HashMap<String, AngleUnit>();
        for(AngleUnit unit : gui.getAppli().getAngleUnits())
            angleUnits.put(capitalizeFirstLetter(unit.getName()), unit);
        
//        // build map of length units
//        lengthUnits = new HashMap<String, LengthUnit>();
//        for(LengthUnit unit : gui.getAppli().getLengthUnits())
//            lengthUnits.put(capitalizeFirstLetter(unit.getName()), unit);
        
		this.createLayout();
	}
	
	private String capitalizeFirstLetter(String string){
	    return string.substring(0, 1).toUpperCase() + string.substring(1);
	}
	
	private void createLayout(){
        for(DynamicMeasure2D.Type type : DynamicMeasure2D.Type.values())
            typeCombo.addItem(type);
        
//        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        typePanel.add(typeLabel);
//        typePanel.add(typeCombo);
//        
//        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        valuePanel.add(valueLabel);
//        valuePanel.add(valueEdit);
//        
//        JPanel unitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        unitPanel.add(unitLabel);
//        unitPanel.add(unitCombo);
//        
//        JPanel mainPanel = new JPanel();
//        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
//        mainPanel.add(typePanel);
//        mainPanel.add(valuePanel);
//        mainPanel.add(unitPanel);
//        
//        this.setLayout(new BorderLayout());
//        this.add(mainPanel, BorderLayout.CENTER);
		// new unit chooser with default unit type
		unitCombo = new UnitChooser(gui, type);

		// associate labels and labeled components
		typeLabel.setLabelFor(typeCombo);
		valueLabel.setLabelFor(valueEdit);
		unitLabel.setLabelFor(unitCombo);
				
		// Setup layout
		setLayout(new GridLayout(3, 2));
		
		// add all couples of elements
		add(typeLabel);		add(typeCombo);
		add(valueLabel);	add(valueEdit);
		add(unitLabel);		add(unitCombo);

        
        typeCombo.addActionListener(this);
	}
	
	
	/**
	 * Returns the dynamic measure according to chosen type and to
	 * given value.
	 * @return the created measure
	 */
	public DynamicMeasure2D getMeasure(){
		return measure;
	}
	
	public void setType(DynamicMeasure2D.Type type){
		if (type!=this.type) {
			this.type = type;
			typeCombo.setSelectedItem(type);
			unitCombo.setUnitType(type);
			unitCombo.invalidate();
			
			switch(type) {
			case NONE:
			case LENGTH:
			case COUNTING:
				unitLabel.setEnabled(false);
				unitCombo.setEnabled(false);
				break;
				
			case ANGLE:
				unitLabel.setEnabled(true);
				unitCombo.setEnabled(true);
				break;

			}
		}
	}
	
	private void updateUnitCombo(){
		unitCombo.removeAllItems();
		switch((DynamicMeasure2D.Type) typeCombo.getSelectedItem()){
		case NONE: 
			break;
		case COUNTING:
			break;
		case ANGLE:
		    for(AngleUnit unit : gui.getAppli().getAngleUnits())
				unitCombo.addItem(capitalizeFirstLetter(unit.getName()));
		    break;
		case LENGTH:
//			for(String unitName : lengthUnits.keySet())
//				unitCombo.addItem(unitName);
			break;
		}
		unitCombo.invalidate();
	}
	
	/**
	 * updates the different widgets in the panel.
	 */
	@Override
	public void updateWidgets(){
		updateUnitCombo();
	}
	
	/**
	 * Overload this method to update current doc or view according to changes
	 * made in the panel.
	 */
	@Override
	public void applyChanges(){
	    String text = valueEdit.getText();
	    if (text.isEmpty()) return;
		double value = Double.parseDouble(text);
		
		switch((DynamicMeasure2D.Type) typeCombo.getSelectedItem()){
		case NONE: 
			measure = new MeasureWrapper2D(new Measure2D(value));
			break;
		case COUNTING:
			int count = (int) Math.round(value);
			measure = new CountWrapper2D(new CountMeasure2D(count));
			break;
		case ANGLE:
		    AngleUnit angleUnit = angleUnits.get(unitCombo.getSelectedItem());
			AngleMeasure2D angle = new AngleMeasure2D(value, angleUnit);
			measure = new AngleWrapper2D(angle);	
			break;
		case LENGTH:
//		    LengthUnit lengthUnit = lengthUnits.get(
//		            unitCombo.getSelectedItem());
//			Measure2D distance = new LengthMeasure2D(value, lengthUnit);
			Measure2D distance = new LengthMeasure2D(value);
			measure = new MeasureWrapper2D(distance);	
			break;
		default:
			measure = new MeasureWrapper2D(new Measure2D(value));	
		}			
	}
	
	public void actionPerformed(ActionEvent evt){
		if(evt.getSource()==typeCombo)
			updateUnitCombo();
	}
}
