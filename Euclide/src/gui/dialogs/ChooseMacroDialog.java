/* file : ChooseMacroDialog.java
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
import gui.macros.SimpleMacro;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.EuclideDoc;

/**
 * A dialog to choose a transform. The result is a transform stored in
 * current document, accessible via method getTransform().
 */ 
public class ChooseMacroDialog extends EuclideDialog
implements ActionListener {
	
	private static final long serialVersionUID = 1L;

	JButton okButton = new JButton("OK");
	JButton cancelButton = new JButton("Cancel");
	JComboBox comboBox = new JComboBox();
	JLabel description = new JLabel();
	
	/** The chosen macro */
	SimpleMacro macro = null;
	
	/** list of macros indexed by their names */
	Hashtable<String, SimpleMacro> macros = 
		new Hashtable<String, SimpleMacro>();
	
	
	public ChooseMacroDialog(EuclideGui gui){
		super(gui, "Choose a macro", true);
		
		EuclideDoc doc = gui.getAppli().getCurrentDoc();
		
		for(SimpleMacro macro : doc.getMacros()){
			this.macros.put(macro.getName(), macro);
			comboBox.addItem(macro.getName());
		}
		
		// select the first item
		if(comboBox.getItemCount()>0){
			comboBox.setSelectedIndex(0);
			macro = doc.getMacros().iterator().next();
		}
				
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		comboBox.addActionListener(this);

		JPanel mainPanel = new JPanel();
		JPanel controlPanel = new JPanel();
		JLabel label = new JLabel("Choose a macro:");
		label.setAlignmentX(Component.RIGHT_ALIGNMENT);
		description.setAlignmentX(Component.RIGHT_ALIGNMENT);
		
		controlPanel.add(okButton);
		controlPanel.add(cancelButton);
	
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.add(label);		
		mainPanel.add(Box.createRigidArea(new Dimension(0,5)));
		mainPanel.add(comboBox);
		mainPanel.add(Box.createRigidArea(new Dimension(0,5)));
		mainPanel.add(description);
		mainPanel.add(Box.createRigidArea(new Dimension(0,5)));
		mainPanel.add(controlPanel);
		
		setContentPane(mainPanel);
		pack();
	}
	
	public SimpleMacro getMacro(){
		return macro;
	}
	
	@Override
	public void setVisible(boolean b){
		if(b){
			EuclideDoc doc = gui.getAppli().getCurrentDoc();
			
			comboBox.removeAllItems();
			for(SimpleMacro macro : doc.getMacros()){
				this.macros.put(macro.getName(), macro);
				comboBox.addItem(macro.getName());
			}
			
			// select the first item
			if(comboBox.getItemCount()>0){
				comboBox.setSelectedIndex(0);
				macro = doc.getMacros().iterator().next();
			}
		}
		super.setVisible(b);
	}
	
	@Override
	public void actionPerformed(ActionEvent event){
		if(event.getSource()==comboBox){
			if(comboBox.getSelectedItem()==null) return;
			macro = macros.get(comboBox.getSelectedItem());
			System.out.println("choose transform " + macro.getName());
		}
		if(event.getSource()==okButton){
			this.setVisible(false);		
		}
		if(event.getSource()==cancelButton){
			macro=null;
			this.setVisible(false);		
		}
	}
}
