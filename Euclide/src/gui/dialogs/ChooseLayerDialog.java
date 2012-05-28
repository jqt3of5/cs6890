/* file : ChooseLayerDialog.java
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

import model.*;
import gui.*;


/**
 * A dialog to choose a vector. The result is a vector stored in
 * current document, accessible via method getVector().
 */ 
public class ChooseLayerDialog extends EuclideDialog implements ActionListener {
	
	private static final long serialVersionUID = 1L;

	/** Apache log4j Logger */
	private static Logger logger = Logger.getLogger("Euclide");

	JButton okButton = new JButton("OK");
	JButton cancelButton = new JButton("Cancel");
	JList layersList = new JList();
	JLabel description = new JLabel();
	
	/** the current layer*/
	EuclideLayer layer=null;
	
	/** the list of layers, indexed by their name */
	Hashtable<String, EuclideLayer> layers = new Hashtable<String, EuclideLayer>();	
	
	public ChooseLayerDialog(EuclideGui gui){
		super(gui, "Choose a layer", true);
		
		EuclideSheet sheet = gui.getCurrentSheet();
		if(sheet==null)
			return;
		
		initLayersList();
		
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		
		JPanel mainPanel = new JPanel();
		JPanel controlPanel = new JPanel();
		JLabel label = new JLabel("Choose a layer from name:");
		label.setAlignmentX(Component.RIGHT_ALIGNMENT);
		description.setAlignmentX(Component.RIGHT_ALIGNMENT);
		
		controlPanel.add(okButton);
		controlPanel.add(cancelButton);
	
		JScrollPane listScroller = new JScrollPane(layersList);
		listScroller.setPreferredSize(new Dimension(150, 80));
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.add(label);		
		mainPanel.add(Box.createRigidArea(new Dimension(0,5)));
		mainPanel.add(listScroller);
		mainPanel.add(Box.createRigidArea(new Dimension(0,5)));
		mainPanel.add(description);
		mainPanel.add(Box.createRigidArea(new Dimension(0,5)));
		mainPanel.add(controlPanel);
		
		setContentPane(mainPanel);
		pack();
	}
	
	private void initLayersList(){
		
		EuclideSheet sheet = gui.getCurrentSheet();
		
		DefaultListModel listModel = new DefaultListModel();
		
		if(sheet!=null)
			for(EuclideLayer layer : sheet.getLayers()){
				this.layers.put(layer.getName(), layer);
				listModel.addElement(layer.getName());
			}
	
		layersList.setModel(listModel);
		
		// select only one layer !
		layersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// select the item corresponding to current layer
		if(!listModel.isEmpty()){
			layer = sheet.getCurrentLayer();
			layersList.setSelectedIndex(sheet.getLayerPosition(layer));
		}		
	}
	
	@Override
	public void setVisible(boolean b){
		if(b){
			initLayersList();
		}
		super.setVisible(b);
	}
	
	public EuclideLayer getLayer(){
		return layer;
	}
	
	@Override
	public void actionPerformed(ActionEvent event){
		
		if(event.getSource()==okButton){
			String layerName = (String) layersList.getSelectedValue();
			if(layerName!=null){
				layer = layers.get(layerName);
				logger.info("choose layer " + layer.getName());
			}else{
				layer = null;
			}
			this.setVisible(false);		
		}
		if(event.getSource()==cancelButton){
			layer=null;
			this.setVisible(false);		
		}
	}
}
