/* file : setMeasureNameDialog.java
 * 
 * Project : Euclide
 *
 * ===========================================
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY, without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. if not, write to :
 * The Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 * 
 * Created on 9 avr. 2006
 *
 */
package gui.dialogs;

import java.awt.*; 
import java.awt.event.*; 
import javax.swing.*;

import org.apache.log4j.Logger;

import dynamic.DynamicMeasure2D;

import gui.*;

/** 
 * A dialog to choose the name of a geometric measure.
 * @author dlegland
 */
public class SetMeasureNameDialog extends EuclideDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Apache log4j Logger */
	private static Logger logger = Logger.getLogger("Euclide");
		
	DynamicMeasure2D measure;
	
	JButton okButton = new JButton("OK");
	JButton cancelButton = new JButton("Cancel");
	JTextField textField = new JTextField("XXXX", 20);
	
	public SetMeasureNameDialog(EuclideGui gui, DynamicMeasure2D measure){
		super(gui, "Set Measure name", true);
		
		this.measure = measure;		
		
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);

		JPanel mainPanel = new JPanel();
		//JPanel centralPanel = new JPanel();
		JPanel controlPanel = new JPanel();
		JLabel label = new JLabel("Give a name for this measure :");
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// set the default base name for the measure
		String name = gui.getAppli().createMeasureName(measure.getMeasure());
		textField.setText(name);

		controlPanel.add(okButton);
		controlPanel.add(cancelButton);
	
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.add(label);		
		mainPanel.add(Box.createRigidArea(new Dimension(0,5)));
		mainPanel.add(textField);
		mainPanel.add(Box.createRigidArea(new Dimension(0,5)));
		mainPanel.add(controlPanel);
		
		setContentPane(mainPanel);
		pack();
	}
	
	@Override
	public void actionPerformed(ActionEvent event){
		if(event.getSource()==okButton){
			String name = textField.getText();
			measure.setName(name);
			
			// Add element to current document, with current draw settings
			gui.getCurrentDoc().addMeasure(measure);  
			logger.info("measure added: " + name);
			dispose();
		}
		if(event.getSource()==cancelButton){
			dispose();
		}
	}	
}
