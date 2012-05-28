/**
 * 
 */
package gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import dynamic.DynamicMeasure2D;
import dynamic.measures.Constant2D;

import math.geom2d.Measure2D;
import model.EuclideDoc;

import gui.EuclideGui;
import gui.EuclidePanel;
import gui.dialogs.EuclidePanelDialog;

/**
 * @author dlegland
 *
 */
public class ConstantsPanel extends EuclidePanel
implements ActionListener, TableModelListener, ListSelectionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	EuclideDoc doc = null;
	
	EditConstantPanel editConstantPanel;
	EuclidePanelDialog editConstantDialog;
	
	JTable table;
	ConstantsTableModel model;
	
	JPanel constantsPanel	= new JPanel();
	JButton newButton 		= new JButton("New...");
	JButton editButton 		= new JButton("Edit...");
	JButton removeButton 	= new JButton("Remove");

	public ConstantsPanel(EuclideGui gui){
		super(gui);
		
		this.doc = gui.getCurrentView().getDoc();
		editConstantPanel = new EditConstantPanel(gui);
		editConstantDialog = new EuclidePanelDialog(gui, 
				editConstantPanel, "Edit Document Constants", 
				EuclidePanelDialog.Buttons.OK_CANCEL, true);
		
		setupLayout();
		setDocument(doc);
	}
	
	private void setupLayout(){
		
		// Setup action listener for the different elements
		newButton.addActionListener(this);
		editButton.addActionListener(this);
		removeButton.addActionListener(this);		
		
		// set up layout for list of constants
		constantsPanel = new JPanel();		
		
		// Build panel for buttons
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(3, 1));
		buttons.add(newButton);
		buttons.add(editButton);
		buttons.add(removeButton);
		
		// Setup main layout
		this.setLayout(new BorderLayout());
		this.add(constantsPanel, BorderLayout.CENTER);
		this.add(buttons, BorderLayout.EAST);
	}

	/**
	 * Changes the current document of the panel. This also updates the
	 * display.
	 */
	public void setDocument(EuclideDoc doc){
		this.doc = doc;
		
		model = new ConstantsTableModel(doc);
		
		table = new JTable(model);
		model.addTableModelListener(this);
		table.getSelectionModel().addListSelectionListener(this);
		
        table.setPreferredScrollableViewportSize(new Dimension(300, 100));
        table.setFillsViewportHeight(true);
        
		// embed the table in a scroll pane
		JScrollPane scrollPane = new JScrollPane(table);
		
       //scrollPane.getViewport().setPreferredSize(new Dimension(200, 100));
		
		// add the component to the Panel
		constantsPanel.removeAll();
		constantsPanel.add(scrollPane, BorderLayout.CENTER);
		constantsPanel.invalidate();
		validate();
		repaint();
	}

	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource().equals(newButton)){
			// initialize a default value
			Constant2D constant = new Constant2D(new Measure2D(1));
			constant.setName("Constant");
			
			// open window to modify the constant
			editConstantPanel.setConstant(constant);
			editConstantDialog.setModal(true);
			
			// show dialog and wait for OK Button
			gui.showDialog(editConstantDialog);

			// Get the modified constant
			constant = editConstantPanel.getConstant();
			
			if(editConstantDialog.getCloseButton()==
				EuclidePanelDialog.CloseButton.CANCEL) return;
			
			// set up tag
			String tag = doc.getNextFreeTag("const%2d");
			constant.setTag(tag);
			
			doc.addMeasure(constant);
			
			// calling this method updates the display
			this.setDocument(doc);
						
			table.invalidate();
			this.validate();
			table.repaint();
		}
		
		if(evt.getSource().equals(editButton)){
			// find selected row
			int pos = table.getSelectedRow();
			if(pos<0) 
				return;
			
			// find selected constant
			editConstantPanel.setConstant(model.getConstant(pos));			
			gui.showDialog(editConstantDialog);
			
			this.setDocument(doc);

			doc.updateDependencies();
			gui.getCurrentView().repaint();
		}
		
		if(evt.getSource().equals(removeButton)){
			// find selected row
			int pos = table.getSelectedRow();
			if(pos<0) return;
			
			// find selected constant
			Constant2D constant = model.getConstant(pos);
			doc.removeMeasure(constant);
			
			this.setDocument(doc);
			
			doc.updateDependencies();
			gui.getCurrentView().repaint();

			table.invalidate();
			this.validate();
			table.repaint();
		}
	}
	public void tableChanged(TableModelEvent e) {
	}
	
	public void valueChanged(ListSelectionEvent e){
		table.invalidate();
		this.validate();
		table.repaint();
	}
	
}

class ConstantsTableModel extends AbstractTableModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** the parent document */
	EuclideDoc doc;
	
	/** names of the columns */
	private String[] columnNames = {"Name", "Value", "Type"};
	
	private Object[][] data = new Object[0][0];
	
	/** list of constants, used for initializing the table */
	private Constant2D[] constants = new Constant2D[0];
	
	public ConstantsTableModel(EuclideDoc doc){
		setDocument(doc);
	}	
	
	public void setDocument(EuclideDoc newDoc){
		doc = newDoc;
		
		if(doc==null)
			return;
		
		// number of rows is number of constants
		int n = doc.getConstants().size();
		
		// allocate memory
		constants = new Constant2D[n];
		data = new Object[n][3];
		
		// initialize array and table
		int i=0;
		for(Constant2D constant : doc.getConstants()){
			constants[i] = constant;
			data[i][0] = constant.getName();
			data[i][1] = Double.toString(constant.getMeasure().getValue());
			data[i][2] = DynamicMeasure2D.getMeasureType(constant);
			i++;		
		}		
	}
	
	public Constant2D getConstant(int pos){
		return constants[pos];
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
		return false;
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
}


