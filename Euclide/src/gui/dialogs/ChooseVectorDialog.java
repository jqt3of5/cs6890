/* file : ChooseVectorDialog.java
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
 * Created on 1 feb. 2008
 *
 */
package gui.dialogs;


import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.apache.log4j.Logger;

import dynamic.*;
import model.*;
import gui.*;


/**
 * A dialog to choose a vector. The result is a vector stored in
 * current document, accessible via method getVector().
 */ 
public class ChooseVectorDialog extends EuclideDialog implements ActionListener {
	
	private static final long serialVersionUID = 1L;

	/** Apache log4j Logger */
	private static Logger logger = Logger.getLogger("Euclide");

	JButton okButton = new JButton("OK");
	JButton cancelButton = new JButton("Cancel");
	JComboBox comboBox = new JComboBox();
	JLabel description = new JLabel();
	
	/** the current vector*/
	DynamicVector2D vector=null;
	
	/** list of names, each one referencing an instance of DynamicVector2D stored in doc*/
	Hashtable<String, DynamicVector2D> vectors = new Hashtable<String, DynamicVector2D>();
	
	
	public ChooseVectorDialog(EuclideGui gui){
		super(gui, "Choose a vector", true);
		
		EuclideDoc doc = gui.getAppli().getCurrentDoc();
		
		for(DynamicVector2D vector : doc.getVectors()){
			this.vectors.put(vector.getName(), vector);
			comboBox.addItem(vector.getName());
		}
		
		// select the first item
		if(comboBox.getItemCount()>0){
			comboBox.setSelectedIndex(0);
			vector = doc.getVectors().iterator().next();
		}
		
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		comboBox.addActionListener(this);

		JPanel mainPanel = new JPanel();
		JPanel controlPanel = new JPanel();
		JLabel label = new JLabel("Choose a vector from name:");
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
	
	@Override
	public void setVisible(boolean b){
		if(b){
			EuclideDoc doc = gui.getCurrentDoc();
			
			comboBox.removeAllItems();
			for(DynamicVector2D vector : doc.getVectors()){
				this.vectors.put(vector.getName(), vector);
				comboBox.addItem(vector.getName());
			}
			
			// select the first item
			if(comboBox.getItemCount()>0){
				comboBox.setSelectedIndex(0);
				vector = doc.getVectors().iterator().next();
			}
		}
		super.setVisible(b);
	}
	
	public DynamicVector2D getVector(){
		return vector;
	}
	
	@Override
	public void actionPerformed(ActionEvent event){
		if(event.getSource()==comboBox){
			if(comboBox.getSelectedItem()==null)
				return;
			vector = vectors.get(comboBox.getSelectedItem());
			logger.info("Choose vector: " + vector.getName());
		}
		if(event.getSource()==okButton){
			this.setVisible(false);		
		}
		if(event.getSource()==cancelButton){
			vector=null;
			this.setVisible(false);		
		}
	}
}
