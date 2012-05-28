/* file : LayersPanel.java
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
 * Created on 12 janv. 2006
 *
 */
package gui.panels;


import gui.EuclideDialog;
import gui.EuclideGui;
import gui.EuclidePanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import app.event.EuclideAppEvent;
import app.event.EuclideAppListener;

import model.EuclideLayer;
import model.EuclideSheet;
import model.event.EuclideSheetEvent;
import model.event.EuclideSheetListener;

/**
 * Layers are displayed from last to first. Then the top-most layer is
 * displayed first, and the bottom layer is displayed last.
 * @author dlegland
 */
public class LayersPanel extends EuclidePanel implements ActionListener,
		TableModelListener, ListSelectionListener, EuclideAppListener,
		EuclideSheetListener {

	private static final long serialVersionUID = 1L;
	
	EuclideSheet sheet;
	
	JTable table;
	LayersTableModel model;
	
	JScrollPane scrollPanel;
	
	JPanel middlePanel		= new JPanel();
	JButton newLayerButton 	= new JButton("New Layer");
	JButton upButton 		= new JButton("/\\");
	JButton downButton 		= new JButton("\\/");
	JButton topButton 		= new JButton("Top");
	JButton bottomButton 	= new JButton("Bottom");
	JButton deleteButton 	= new JButton("Delete");
		
	public LayersPanel(EuclideGui gui, EuclideSheet sheet){
		super(gui);
		this.sheet = sheet;
		
		this.setEuclideSheet(sheet);

		setupLayout();
	}
	
	private void setupLayout(){
				
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout());
		topPanel.add(upButton);
		topPanel.add(downButton);
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(5, 1));
		buttons.add(newLayerButton);
		buttons.add(topPanel);
		buttons.add(topButton);
		buttons.add(bottomButton);
		buttons.add(deleteButton);
		
		this.setLayout(new BorderLayout());
		middlePanel.setPreferredSize(new Dimension(350, 200));
		this.add(middlePanel, BorderLayout.CENTER);
		this.add(buttons, BorderLayout.EAST);
		
		newLayerButton.addActionListener(this);
		upButton.addActionListener(this);
		downButton.addActionListener(this);
		topButton.addActionListener(this);
		bottomButton.addActionListener(this);
		deleteButton.addActionListener(this);		
	}
	
	public void setEuclideSheet(EuclideSheet sheet){
		this.sheet = sheet;
		
		sheet.addSheetListener(this);
		
		model = new LayersTableModel(sheet);
		
		table = new JTable(model);
		model.addTableModelListener(this);
		table.getSelectionModel().addListSelectionListener(this);
		
		// embed the table in a scroll pane
		Dimension dim = table.getPreferredSize();
		dim.width = Math.max(dim.width, 300);
		dim.height = Math.max(dim.height, 100);
		scrollPanel = new JScrollPane(table);
		scrollPanel.setPreferredSize(new Dimension(350, 200));
		
		// add the component to the Panel
		middlePanel.removeAll();
		middlePanel.add(scrollPanel, BorderLayout.CENTER);
		middlePanel.invalidate();
		validate();
		repaint();
	}
		
	private void updateDisplay() {
		// update model
		model.setDocument(sheet);

		// update selected index
		int index = sheet.getLayerPosition(sheet.getCurrentLayer());
		index = sheet.getLayerNumber()-1-index;
		table.getSelectionModel().setSelectionInterval(index, index);
		
		// set up size of scroll panel
		Dimension dim = table.getPreferredSize();
		dim.width = Math.max(dim.width, 300);
		dim.height = Math.max(dim.height, 100);
		scrollPanel.getViewport().setPreferredSize(dim);
		scrollPanel.setPreferredSize(new Dimension(350, 200));
		this.doLayout();

		// validation and repaint
		table.invalidate();
		this.validate();
		table.repaint();
		
		this.gui.getCurrentView().repaint();
	}
	
	/**
	 * Return the index of the selected layer, in sheet reference.
	 * If there is no layer in the sheet, return -1.
	 */
	private int getSelectedLayerIndex() {
		// control on layer number
		int nLayer = sheet.getLayerNumber();
		if(nLayer==0)
			return -1;
		
		// get selected row
		int row = table.getSelectedRow();
		
		// get index of selected layer
		int selected = nLayer-1-row;
		
		// format between 0 and number of layers
		selected = Math.min(Math.max(selected, 0), nLayer-1);
		return selected;
	}
	
	public void actionPerformed(ActionEvent evt) {
		//System.out.println("action performed");
		
		if(evt.getSource()==newLayerButton){
			EuclideLayer layer = sheet.addNewLayer();

			sheet.setCurrentLayer(layer);
			model.setDocument(sheet);
			return;
		}		

		int pos = getSelectedLayerIndex();
		
		if(evt.getSource()==upButton){
			if(pos==-1) return;
			if(pos==sheet.getLayerNumber()-1) return;
			
			EuclideLayer layer = sheet.getLayer(pos);
			sheet.setLayerPosition(layer, pos+1);
			return;
		}
		
		if(evt.getSource()==downButton){
			if(pos==-1) return;
			if(pos==0) return;
			
			EuclideLayer layer = sheet.getLayer(pos);
			sheet.setLayerPosition(layer, pos-1);
			return;
		}
		
		if(evt.getSource()==topButton){
			if(pos==-1) return;
			if(pos==sheet.getLayerNumber()-1) return;
			
			EuclideLayer layer = sheet.getLayer(pos);
			sheet.setLayerPosition(layer, sheet.getLayerNumber()-1);
			return;
		}
		
		if(evt.getSource()==bottomButton){
			if(pos==-1) return;
			if(pos==0) return;
			
			EuclideLayer layer = sheet.getLayer(pos);
			sheet.setLayerPosition(layer, 0);
			return;
		}
		
		if(evt.getSource()==deleteButton){
			// Position of layer to delete
			if(pos==-1) return;
			
			// Extract layer to delete
			EuclideLayer layer = sheet.getLayer(pos);
			
			// default icon, custom title
			String[] options = new String[]{"Delete", "Cancel"};
			int n = JOptionPane.showOptionDialog(
			    this.gui.getCurrentFrame(),
			    "Are you sure you want to delete\nthe layer '" + 
			    	layer.getName() + "' ?",
			    "Confirm layer delete",
			    JOptionPane.YES_NO_OPTION,
			    JOptionPane.WARNING_MESSAGE, null,
			    options, options[1]);
			if(n!=0) return;
			
			table.clearSelection();
			sheet.removeLayer(layer);
		}
	}
	
	public void tableChanged(TableModelEvent e) {
		//System.out.println("table changed");
		int row = e.getFirstRow();
		int col = e.getColumn();
		
		//int row = getSelectedLayerIndex();
		int index = sheet.getLayerNumber()-1-row;
		
		boolean b;
		switch(col){
			case 0:
				System.out.println("should change current layer");
				break;
			case 1:
				System.out.println("change name of layer " + index + " to " +
					model.getValueAt(row, col).toString());
				sheet.getLayer(index).setName(model.getValueAt(row, col).toString());
				break;
			case 2:
				b = ((Boolean)model.getValueAt(row, col)).booleanValue();
				sheet.getLayer(index).setVisible(b);
				gui.getCurrentFrame().repaint();
				break;
			case 3:
				b = ((Boolean)model.getValueAt(row, col)).booleanValue();
				sheet.getLayer(index).setEditable(b);
				break;
		}
	}
	
	public void valueChanged(ListSelectionEvent e){
		//System.out.println("list event (table changed)");
		
		// get index of selected layer
		int nLayer = sheet.getLayerNumber();
		int selected = nLayer-1-table.getSelectedRow();
		
		// get index of current layer
		int current = sheet.getLayerPosition(sheet.getCurrentLayer());
		
		// if selected different from current, update table
		if(selected>=0 && selected!=current  && selected<nLayer) {
			sheet.setCurrentLayer(sheet.getLayer(selected));
			model.setDocument(sheet);
			this.updateDisplay();
		}
	}

	/* (non-Javadoc)
	 * @see model.event.EuclideSheetListener#sheetModified(model.event.EuclideSheetEvent)
	 */
	public void sheetModified(EuclideSheetEvent evt) {
		//System.out.println("sheet modified");
		updateDisplay();
	}

	public void appliModified(EuclideAppEvent evt) {
		// nothing to do
	}

	public void appliDocAdded(EuclideAppEvent evt) {
		// nothing to do
	}

	public void appliDocRemoved(EuclideAppEvent evt) {
		if (evt.getDoc() != this.sheet.getDocument())
			return;
		
		// find dialog containing the panel
		Container parent = this.getParent();
		while (!(parent instanceof EuclideDialog))
			parent = parent.getParent();
		
		// dispose the dialog
		((EuclideDialog) parent).dispose();
	}
}

class LayersTableModel extends AbstractTableModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] columnNames = {"Current", "Name", "Visible", "Editable"};
	private Object[][] data = new Object[0][0];
	private EuclideLayer[] layers = new EuclideLayer[0];
	EuclideSheet sheet;
	
	public LayersTableModel(EuclideSheet sheet){
		this.setDocument(sheet);
	}	
	
	public void setDocument(EuclideSheet newSheet){
		sheet = newSheet;
		
		if(sheet==null)
			return;
		
		int n = sheet.getLayerNumber();
		layers = new EuclideLayer[n];
		data = new Object[n][4];
		
		EuclideLayer currentLayer = sheet.getCurrentLayer();
		
		int i=0;
		for(EuclideLayer layer : sheet.getLayers()){
			int index = n-1-i;
			layers[index] = layer;
			if(layer==currentLayer)
				data[index][0] = new String("*");
			else
				data[index][0] = new String();
			data[index][1] = layer.getName();
			data[index][2] = new Boolean(layer.isVisible());
			data[index][3] = new Boolean(layer.isEditable());
			i++;		
		}		
	}
	
	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.length;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	@Override
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/*
	 * Don't need to implement this method unless your table's
	 * editable.
	 */
	@Override
	public boolean isCellEditable(int row, int col) {
		return true;
	}

	/*
	 * Don't need to implement this method unless your table's
	 * data can change.
	 */
	@Override
	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}
	
	/*
	public void tableChanged(TableModelEvent e) {
		int row = e.getFirstRow();
		int col = e.getColumn();
		
		boolean b;
		switch(col){
			case 1:
				System.out.println("change name of layer " + row + " to " +
					getValueAt(row, col).toString());
				sheet.getLayer(row).setName(getValueAt(row, col).toString());
				break;
			case 2:
				b = ((Boolean)getValueAt(row, col)).booleanValue();
				sheet.getLayer(row).setVisible(b);
				break;
			case 3:
				b = ((Boolean)getValueAt(row, col)).booleanValue();
				sheet.getLayer(row).setEditable(b);
				break;
		}
	}*/
}


class ButtonRenderer extends JButton implements TableCellRenderer {	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ButtonRenderer(String text) {
		super(text);
	}
	
	public ButtonRenderer(String text, String tooltip) {
		super(text);
		setToolTipText(tooltip);
	}

	public Component getTableCellRendererComponent(
							JTable table, Object color,
							boolean isSelected, boolean hasFocus,
							int row, int column) {
		return this;
	}
}
