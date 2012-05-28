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

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import math.geom2d.AngleMeasure2D;
import math.geom2d.AngleUnit;
import math.geom2d.CountMeasure2D;
import math.geom2d.LengthMeasure2D;
import math.geom2d.Measure2D;
import dynamic.DynamicMeasure2D;
import dynamic.DynamicMeasure2D.Type;
import dynamic.measures.Constant2D;

/**
 * A panel for editing properties of a stored constant: name, value, unit.
 * 
 * @author dlegland
 */
public class EditConstantPanel extends EuclidePanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	DynamicMeasure2D.Type type = null;
	Constant2D constant = null;

	JLabel nameLabel = new JLabel("Name:");
	JLabel typeLabel = new JLabel("Type:");
	JLabel valueLabel = new JLabel("Value:");
	JLabel unitLabel = new JLabel("Unit:");

	JTextField nameEdit = new JTextField("no name", 8);
	JComboBox typeCombo = new JComboBox();
	JTextField valueEdit = new JTextField("", 8);
	UnitChooser unitCombo;

	public EditConstantPanel(EuclideGui gui) {
		super(gui);

		createLayout();
		setType(Type.NONE);

		typeCombo.addActionListener(this);
	}

	private void createLayout() {
		// create the type combo
		for (DynamicMeasure2D.Type type : DynamicMeasure2D.Type.values())
			typeCombo.addItem(type);
		DynamicMeasure2D.Type type = DynamicMeasure2D.Type.NONE;

		// new unit chooser with default unit type
		unitCombo = new UnitChooser(gui, type);

		// associate labels and labeled components
		nameLabel.setLabelFor(nameEdit);
		typeLabel.setLabelFor(typeCombo);
		valueLabel.setLabelFor(valueEdit);
		unitLabel.setLabelFor(unitCombo);
				
		// Setup layout
		setLayout(new GridLayout(4, 2));
		
		// add all couples of elements
		add(nameLabel);		add(nameEdit);
		add(typeLabel);		add(typeCombo);
		add(valueLabel);	add(valueEdit);
		add(unitLabel);		add(unitCombo);
	}
	
	public Constant2D getConstant() {
		return constant;
	}

	public void setConstant(Constant2D constant) {
		this.constant = constant;
		this.updateWidgets();
	}

	public void setType(DynamicMeasure2D.Type type) {
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

	/**
	 * Updates the different widgets in the panel.
	 */
	@Override
	public void updateWidgets() {
		if (constant==null)
			return;
		// update name
		nameEdit.setText(constant.getName());
		
		// update value display
		double value = constant.getMeasure().getValue();
		valueEdit.setText(Double.toString(value));
		
		// update type
		setType(DynamicMeasure2D.getMeasureType(constant));
	}

	/**
	 * Overload this method to update current doc or view according to changes
	 * made in the panel.
	 */
	@Override
	public void applyChanges() {
		
		// Try to parse the constant value
		String text = valueEdit.getText();
		double value;
		try {
			value = Double.parseDouble(text);
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this.getParent(),
					"Could not understand the numeric field: " + text,
					"Parsing error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Depending on chosen type, create a specific class measure
		switch ((DynamicMeasure2D.Type) typeCombo.getSelectedItem()) {
		case NONE:
			constant.setMeasure(new Measure2D(value));
			break;
			
		case COUNTING:
			// convert value to integer, then create counting measure
			int count = (int) Math.round(value);
			CountMeasure2D countMeasure = new CountMeasure2D(count);
			constant.setMeasure(countMeasure);
			break;
			
		case ANGLE:
			// for angle, converts to the specified value
			AngleMeasure2D angle = new AngleMeasure2D(value,
					(AngleUnit) unitCombo.getSelectedUnit());
			constant.setMeasure(angle);
			break;
			
		case LENGTH:
			// Assumes the value is given in "user units"
			Measure2D distance = new LengthMeasure2D(value);
			constant.setMeasure(distance);
			break;
			
		default:
			constant.setMeasure(new Measure2D(value));
		}
		
		// update constant name
		constant.setName(nameEdit.getText());
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource()==typeCombo)
			setType((DynamicMeasure2D.Type) typeCombo.getSelectedItem());
	}
}
