/* file : ChooseTransformDialog.java
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


import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.apache.log4j.Logger;

import dynamic.DynamicTransform2D;

import model.*;
import gui.EuclideDialog;
import gui.EuclideGui;

/**
 * A dialog to choose a transform. The result is a transform stored in
 * current document, accessible via method getTransform().
 */ 
public class ChooseTransformDialog extends EuclideDialog implements ActionListener {
	
	private static final long serialVersionUID = 1L;

	/** Apache log4j Logger */
	private static Logger logger = Logger.getLogger("Euclide");

	JButton okButton = new JButton("OK");
	JButton cancelButton = new JButton("Cancel");
	JComboBox comboBox = new JComboBox();
	JLabel description = new JLabel();
	
	DynamicTransform2D transform=null;
	Hashtable<String, DynamicTransform2D> transforms = 
		new Hashtable<String, DynamicTransform2D>();
	
	
	public ChooseTransformDialog(EuclideGui gui){
		super(gui, "Choose a transform", true);
		
		EuclideDoc doc = gui.getAppli().getCurrentDoc();
		
		for(DynamicTransform2D transform : doc.getTransforms()){
			this.transforms.put(transform.getName(), transform);
			comboBox.addItem(transform.getName());
		}
		
		// select the first item
		if(comboBox.getItemCount()>0){
			comboBox.setSelectedIndex(0);
			transform = doc.getTransforms().iterator().next();
		}
				
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		comboBox.addActionListener(this);

		JPanel mainPanel = new JPanel();
		JPanel controlPanel = new JPanel();
		JLabel label = new JLabel("Choose a transform from name :");
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
	
	public DynamicTransform2D getTransform(){
		return transform;
	}
	
	@Override
	public void setVisible(boolean b){
		if(b){
			EuclideDoc doc = gui.getAppli().getCurrentDoc();
			
			comboBox.removeAllItems();
			for(DynamicTransform2D transform : doc.getTransforms()){
				this.transforms.put(transform.getName(), transform);
				comboBox.addItem(transform.getName());
			}
			
			// select the first item
			if(comboBox.getItemCount()>0){
				comboBox.setSelectedIndex(0);
				transform = doc.getTransforms().iterator().next();
			}
		}
		super.setVisible(b);
	}
	
	@Override
	public void actionPerformed(ActionEvent event){
		if(event.getSource()==comboBox){
			if(comboBox.getSelectedItem()==null) return;
			transform = transforms.get(comboBox.getSelectedItem());
			logger.info("Choose transform: " + transform.getName());
		}
		if(event.getSource()==okButton){
			this.setVisible(false);		
		}
		if(event.getSource()==cancelButton){
			transform=null;
			this.setVisible(false);		
		}
	}
}
