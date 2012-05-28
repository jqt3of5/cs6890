/* file : ChooseMacroNameDialog.java
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

import gui.EuclideDialog;
import gui.EuclideGui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ChooseMacroNameDialog extends EuclideDialog 
implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String macroName = "";
	
	JButton okButton = new JButton("OK");
	JButton cancelButton = new JButton("Cancel");
	JTextField textField = new JTextField("XXXX", 20);
	
	public ChooseMacroNameDialog(EuclideGui gui){
		super(gui, "Set Macro name", true);
		
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);

		JPanel mainPanel = new JPanel();
		JPanel controlPanel = new JPanel();
		JLabel label = new JLabel(
				gui.getText("chooseMacroNameDialog.label", 
				"Input macro name:"));
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// set the default base name for the transform
		String name = gui.getAppli().createMacroName();
		textField.setText(name);
		
		controlPanel.add(okButton);
		controlPanel.add(cancelButton);
	
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainPanel.add(label);		
		mainPanel.add(Box.createRigidArea(new Dimension(0,5)));
		mainPanel.add(textField);
		mainPanel.add(Box.createRigidArea(new Dimension(0,5)));
		mainPanel.add(controlPanel);
		
		setContentPane(mainPanel);
		pack();
	}
	
	public String getMacroName() {
		return macroName;
	}
	
	@Override
	public void actionPerformed(ActionEvent event){
		if(event.getSource()==okButton){
			macroName = textField.getText();
			this.dispose();
		}
		if(event.getSource()==cancelButton){
			this.dispose();
		}
	}	
}
