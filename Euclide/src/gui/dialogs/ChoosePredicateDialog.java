/* file : ChoosePredicateDialog.java
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

import dynamic.DynamicPredicate2D;
import dynamic.predicates.BooleanWrapper2D;

import model.*;
import gui.*;


/**
 * A dialog to choose a predicate. The result is a predicate stored in
 * current document, accessible via method getTransform().
 */ 
public class ChoosePredicateDialog extends EuclideDialog implements ActionListener {
	
	private static final long serialVersionUID = 1L;

	/** Apache log4j Logger */
	private static Logger logger = Logger.getLogger("Euclide");

	JButton inputOkButton = new JButton("OK");
	JButton storedOkButton = new JButton("OK");
	JButton cancelButton = new JButton("Cancel");
	
	JComboBox chooseValueCombo = new JComboBox(
			new String[]{"true", "false"});
	JComboBox chooseNameCombo = new JComboBox();
	JLabel description = new JLabel();
	
	/** the current predicate*/
	DynamicPredicate2D predicate=null;
	
	/** list of names, each one referencing an instance of DynamicPredicate2D
	 *  stored in doc
	 */
	Hashtable<String, DynamicPredicate2D> predicates = 
		new Hashtable<String, DynamicPredicate2D>();
	
	
	public ChoosePredicateDialog(EuclideGui gui){
		super(gui, "Choose a predicate", true);		
		
		// Init the JComboBox for predicates name
		
		EuclideDoc doc = gui.getAppli().getCurrentDoc();
		
		for(DynamicPredicate2D predicate : doc.getPredicates()){
			this.predicates.put(predicate.getName(), predicate);
			chooseNameCombo.addItem(predicate.getName());
		}
		
		// select the first item
		if(chooseNameCombo.getItemCount()>0){
			chooseNameCombo.setSelectedIndex(0);
			predicate = doc.getPredicates().iterator().next();
		}

		// select the first item
		chooseValueCombo.setSelectedIndex(0);
		
		
		// input panel for choosing value of predicate

		JPanel inputPanel = new JPanel();
		JLabel labelInput = new JLabel("Choose the predicate value:");
		labelInput.setAlignmentX(Component.LEFT_ALIGNMENT);
		chooseValueCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
		inputOkButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));
		inputPanel.add(labelInput);		
		inputPanel.add(Box.createRigidArea(new Dimension(0,5)));
		inputPanel.add(chooseValueCombo);
		inputPanel.add(Box.createRigidArea(new Dimension(0,5)));
		inputPanel.add(inputOkButton);
		inputPanel.add(Box.createRigidArea(new Dimension(0,5)));
		
		// decorate with a nice border
		inputPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.gray),
				"Choose a constant predicate"));


		// input panel for choosing name of predicate
		
		JLabel label = new JLabel("Select an existing predicate:");
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		description.setAlignmentX(Component.LEFT_ALIGNMENT);		
		
		JPanel storedPanel = new JPanel();
		storedPanel.setLayout(new BoxLayout(storedPanel, BoxLayout.PAGE_AXIS));
		storedPanel.add(label);		
		storedPanel.add(Box.createRigidArea(new Dimension(0,5)));
		storedPanel.add(chooseNameCombo);
		storedPanel.add(Box.createRigidArea(new Dimension(0,5)));
		storedPanel.add(description);
		storedPanel.add(Box.createRigidArea(new Dimension(0,5)));
		storedPanel.add(storedOkButton);

		// decorate with a nice border
		storedPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.gray),
				"Choose a predicate from name"));
			
		JPanel controlPanel = new JPanel();
		cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		controlPanel.add(cancelButton);
	
		
		// Main panel layout		
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		inputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainPanel.add(inputPanel);
		storedPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainPanel.add(storedPanel);
		controlPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainPanel.add(controlPanel);
		
		// init Action listeners
		
		storedOkButton.addActionListener(this);
		inputOkButton.addActionListener(this);
		cancelButton.addActionListener(this);
		chooseNameCombo.addActionListener(this);
		chooseValueCombo.addActionListener(this);

		this.setContentPane(mainPanel);
		pack();
	}
	
	public DynamicPredicate2D getPredicate(){
		return predicate;
	}
	
	@Override
	public void setVisible(boolean b){
		if(b){
			EuclideDoc doc = gui.getAppli().getCurrentDoc();
			
			chooseNameCombo.removeAllItems();
			for(DynamicPredicate2D predicate : doc.getPredicates()){
				this.predicates.put(predicate.getName(), predicate);
				chooseNameCombo.addItem(predicate.getName());
			}
			
			// select the first item
			if(chooseNameCombo.getItemCount()>0){
				chooseNameCombo.setSelectedIndex(0);
				predicate = doc.getPredicates().iterator().next();
			}
		}
		super.setVisible(b);
	}
	
	@Override
	public void actionPerformed(ActionEvent event){
		if(event.getSource()==chooseNameCombo){
			if(chooseNameCombo.getSelectedItem()==null)
				return;
			predicate = predicates.get(chooseNameCombo.getSelectedItem());
			logger.info("Choose predicate: " + predicate.getName());
		}
		if(event.getSource()==inputOkButton){
			boolean res = false;
			if(chooseValueCombo.getSelectedIndex()==0) res = true;
			this.predicate = new BooleanWrapper2D(res);
			this.setVisible(false);		
		}
		if(event.getSource()==storedOkButton){
			this.setVisible(false);		
		}
		if(event.getSource()==cancelButton){
			predicate=null;
			this.setVisible(false);		
		}
	}
}
