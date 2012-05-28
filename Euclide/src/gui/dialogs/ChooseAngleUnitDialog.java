/**
 * 
 */
package gui.dialogs;

import gui.EuclideGui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.apache.log4j.Logger;

import math.geom2d.AngleUnit;

/**
 * @author dlegland
 *
 */
public class ChooseAngleUnitDialog extends JDialog implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	/** Apache log4j Logger */
	private static Logger logger = Logger.getLogger("Euclide");


	EuclideGui appli;
	
	AngleUnit unit = AngleUnit.DEGREE;
	
	JButton okButton = new JButton("OK");
	JButton cancelButton = new JButton("Cancel");
	
	String[] values = new String[]{"Radians", "Degrees", "Turns"};	
	JComboBox comboBox = new JComboBox(values);
	
	public ChooseAngleUnitDialog(EuclideGui gui){
		super(gui.getCurrentFrame(), "Choose angle unit", true);
		
		comboBox.addActionListener(this);
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JLabel label = new JLabel("Choose angle unit:");
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		comboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		panel.add(label);
		panel.add(comboBox);
		
		JPanel control = new JPanel();
		control.setLayout(new FlowLayout());
		control.add(okButton);
		control.add(cancelButton);
		
		JPanel main = new JPanel();
		main.setLayout(new BorderLayout());
		main.add(panel, BorderLayout.CENTER);
		main.add(control, BorderLayout.SOUTH);
		
		this.setContentPane(main);
		
		pack();
	}
	
	public AngleUnit getUnit(){
		return unit;
	}
	
	public void actionPerformed(ActionEvent event){
		
		if(event.getSource()==okButton){
			switch(comboBox.getSelectedIndex()){
			case 0: unit = AngleUnit.RADIAN; break;
			case 1: unit = AngleUnit.DEGREE; break;
			case 2: unit = AngleUnit.TURN; break;
			default: logger.error("Unknown unit");
			}
			this.setVisible(false);		
		}
		
		if(event.getSource()==cancelButton){
			unit = null;
			this.setVisible(false);		
		}
	}
}
